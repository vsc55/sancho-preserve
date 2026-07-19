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

   public HeaderBarMouseAdapter(CLabel cLabel, MenuManager menuManager) {
      this.cLabel = cLabel;
      this.menuManager = menuManager;
   }

   private boolean overImage(int x) {
      return x < this.cLabel.getImage().getBounds().width;
   }

   private void addCopyItem(Menu menu) {
      menu.addMenuListener(new MenuAdapter() {
         Font oldFont = HeaderBarMouseAdapter.this.cLabel.getFont();
         Font boldFont;

         public void menuShown(MenuEvent event) {
            Menu menu = (Menu)event.widget;
            FontData[] fontData = this.oldFont.getFontData();

            for (int i = 0; i < fontData.length; i++) {
               fontData[i].setStyle(1);
            }

            this.boldFont = new Font(null, fontData);
            HeaderBarMouseAdapter.this.cLabel.setFont(this.boldFont);
            new MenuItem(menu, 2);
            HeaderBarMouseAdapter.this.addMenuItem(menu, "mi.copy", "copy", new SelectionAdapter() {
               public void widgetSelected(SelectionEvent event) {
                  MainWindow.copyToClipboard(HeaderBarMouseAdapter.this.cLabel.getText());
               }
            });
         }

         public void menuHidden(MenuEvent event) {
            Menu menu = (Menu)event.widget;
            menu.removeMenuListener(this);
            HeaderBarMouseAdapter.this.cLabel.setFont(this.oldFont);
            if (this.boldFont != null && !this.boldFont.isDisposed()) {
               this.boldFont.dispose();
            }
         }
      });
   }

   private void showMenu(Point point, boolean addCopy) {
      Menu menu = this.menuManager.createContextMenu(this.cLabel);
      if (addCopy) {
         this.addCopyItem(menu);
      }

      menu.setLocation(point);
      menu.setVisible(true);
   }

   public void addMenuItem(Menu menu, String textKey, String imageKey, SelectionAdapter listener) {
      MenuItem menuItem = new MenuItem(menu, 0);
      menuItem.setText(SResources.getString(textKey));
      menuItem.setImage(SResources.getImage(imageKey));
      menuItem.addSelectionListener(listener);
   }

   public void mouseDown(MouseEvent event) {
      boolean addCopy = false;
      if (event.button == 1 && this.overImage(event.x) || event.button == 3) {
         Point point;
         if (event.button == 1) {
            point = new Point(0, this.cLabel.getBounds().height);
         } else {
            point = new Point(event.x, event.y);
            addCopy = true;
         }

         this.showMenu(((CLabel)event.widget).toDisplay(point), addCopy);
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
