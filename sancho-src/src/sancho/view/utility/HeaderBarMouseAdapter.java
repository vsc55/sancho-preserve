package sancho.view.utility;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

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
      HeaderBarMouseAdapter$1 var2 = new HeaderBarMouseAdapter$1(this);
      var1.addMenuListener(var2);
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

   // $VF: synthetic method
   static CLabel access$000(HeaderBarMouseAdapter var0) {
      return var0.cLabel;
   }
}
