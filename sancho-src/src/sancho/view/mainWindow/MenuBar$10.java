package sancho.view.mainWindow;

import java.util.List;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;

class MenuBar$10 extends MenuAdapter {
   // $VF: synthetic field
   private final Menu val$viewMenu;
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$10(MenuBar var1, Menu var2) {
      this.this$0 = var1;
      this.val$viewMenu = var2;
   }

   public void menuShown(MenuEvent var1) {
      MenuItem[] var2 = this.val$viewMenu.getItems();

      for (int var3 = 0; var3 < var2.length; var3++) {
         var2[var3].dispose();
      }

      List var4 = MenuBar.access$100(this.this$0).getTabs();

      for (int var5 = 0; var5 < var4.size(); var5++) {
         AbstractTab var6 = (AbstractTab)var4.get(var5);
         MenuBar.access$302(this.this$0, new MenuItem(this.val$viewMenu, 8));
         MenuBar.access$300(this.this$0).setText(var6.getToolButton().getText());
         MenuBar.access$300(this.this$0).setImage(var6.getToolButton().getSmallInActiveImage());
         MenuBar.access$300(this.this$0).addListener(13, new MenuBar$11(this, var6));
      }

      MenuBar.access$302(this.this$0, new MenuItem(this.val$viewMenu, 2));
      this.this$0.createMenuItem(this.val$viewMenu, "&" + SResources.getString("menu.view.tabSelector"), "preferences", new MenuBar$12(this));
   }

   // $VF: synthetic method
   static MenuBar access$400(MenuBar$10 var0) {
      return var0.this$0;
   }
}
