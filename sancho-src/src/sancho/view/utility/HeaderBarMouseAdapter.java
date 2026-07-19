package sancho.view.utility;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import sancho.view.MainWindow;

public class HeaderBarMouseAdapter extends MouseAdapter {
   private CLabel cLabel;
   private MenuManager menuManager;

   public HeaderBarMouseAdapter(CLabel var1, MenuManager var2) {
      this.cLabel = var1;
      this.menuManager = var2;
   }

   private boolean overImage(int var1) {
      return var1 < this.cLabel.getImage().getBounds().width;
   }

   private void addCopyItem(Menu var1) {
      var1.addMenuListener(new MenuAdapter() {
         Font oldFont = HeaderBarMouseAdapter.this.cLabel.getFont();
         Font boldFont;

         public void menuShown(MenuEvent var1) {
            Menu var2 = (Menu)var1.widget;
            FontData[] var3 = this.oldFont.getFontData();

            for (int var4 = 0; var4 < var3.length; var4++) {
               var3[var4].setStyle(1);
            }

            this.boldFont = new Font(null, var3);
            HeaderBarMouseAdapter.this.cLabel.setFont(this.boldFont);
            new MenuItem(var2, 2);
            HeaderBarMouseAdapter.this.addMenuItem(var2, "mi.copy", "copy", new SelectionAdapter() {
               public void widgetSelected(SelectionEvent var1) {
                  MainWindow.copyToClipboard(HeaderBarMouseAdapter.this.cLabel.getText());
               }
            });
         }

         public void menuHidden(MenuEvent var1) {
            Menu var2 = (Menu)var1.widget;
            var2.removeMenuListener(this);
            HeaderBarMouseAdapter.this.cLabel.setFont(this.oldFont);
            if (this.boldFont != null && !this.boldFont.isDisposed()) {
               this.boldFont.dispose();
            }
         }
      });
   }

   private void showMenu(Point var1, boolean var2) {
      Menu var3 = this.menuManager.createContextMenu(this.cLabel);
      if (var2) {
         this.addCopyItem(var3);
      }

      var3.setLocation(var1);
      var3.setVisible(true);
   }

   public void addMenuItem(Menu var1, String var2, String var3, SelectionAdapter var4) {
      MenuItem var5 = new MenuItem(var1, 0);
      var5.setText(SResources.getString(var2));
      var5.setImage(SResources.getImage(var3));
      var5.addSelectionListener(var4);
   }

   public void mouseDown(MouseEvent var1) {
      boolean var2 = false;
      if (var1.button == 1 && this.overImage(var1.x) || var1.button == 3) {
         Point var3;
         if (var1.button == 1) {
            var3 = new Point(0, this.cLabel.getBounds().height);
         } else {
            var3 = new Point(var1.x, var1.y);
            var2 = true;
         }

         this.showMenu(((CLabel)var1.widget).toDisplay(var3), var2);
      }
   }

   // Context-menu action that copies the header label's text to the clipboard.
   private class CopyAction extends Action {
      public CopyAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         MainWindow.copyToClipboard(HeaderBarMouseAdapter.this.cLabel.getText());
      }
   }
}
