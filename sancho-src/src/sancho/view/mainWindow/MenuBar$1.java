package sancho.view.mainWindow;

import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import sancho.core.Sancho;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

class MenuBar$1 extends MenuAdapter {
   // $VF: synthetic field
   private final Menu val$fileMenu;
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$1(MenuBar var1, Menu var2) {
      this.this$0 = var1;
      this.val$fileMenu = var2;
   }

   public void menuShown(MenuEvent var1) {
      MenuItem[] var2 = this.val$fileMenu.getItems();

      for (int var3 = 0; var3 < var2.length; var3++) {
         var2[var3].dispose();
      }

      if (Sancho.getCoreFactory().isAutoReconnecting()) {
         this.this$0.createMenuItem(this.val$fileMenu, "&" + SResources.getString("menu.file.stopAutoReconnect"), "nuke", new MenuBar$2(this));
      }

      if (Sancho.getCoreFactory().isConnected()) {
         this.this$0.createMenuItem(this.val$fileMenu, "&" + SResources.getString("menu.file.inputLink"), "tab.transfers.buttonSmall", new MenuBar$3(this));
         this.this$0
            .createMenuItem(
               this.val$fileMenu, "&" + SResources.getString("menu.file.loadTorrents") + "\tCTRL+T", "tab.transfers.buttonSmall", new MenuBar$4(this)
            );
      }

      if (Sancho.getCoreFactory().isConnected() && Sancho.getCoreConsole() == null) {
         this.this$0.createMenuItem(this.val$fileMenu, "&" + SResources.getString("menu.file.killCore"), "nuke", new MenuBar$5(this));
      }

      if (Sancho.hasCollectionFactory()) {
         this.this$0.createMenuItem(this.val$fileMenu, "&" + SResources.getString("menu.file.disconnect"), "menu-disconnect", new MenuBar$6(this));
      } else {
         MenuBar.access$302(this.this$0, new MenuItem(this.val$fileMenu, 64));
         MenuBar.access$300(this.this$0).setText(SResources.getString("menu.file.connect"));
         MenuBar.access$300(this.this$0).setImage(SResources.getImage("menu-connect"));
         Menu var4 = new Menu(MenuBar.access$300(this.this$0));
         MenuBar.access$300(this.this$0).setMenu(var4);
         this.this$0.createMenuItem(var4, "&" + SResources.getString("menu.file.reconnect"), "menu-connect", new MenuBar$7(this));
         MenuBar.access$302(this.this$0, new MenuItem(var4, 2));

         for (int var5 = 0; var5 <= 0 || PreferenceLoader.contains("hm_" + var5 + "_hostname"); var5++) {
            String var6 = var5 == 0 ? SResources.getString("l.default") + ": " : "";
            String var7 = PreferenceLoader.loadString("hm_" + var5 + "_description");
            if (var7.equals("")) {
               var7 = PreferenceLoader.loadString("hm_" + var5 + "_hostname") + ":" + PreferenceLoader.loadString("hm_" + var5 + "_port");
            }

            var6 = var6 + var7;
            this.this$0.createMenuItem(var4, var6, "menu-connect", new MenuBar$8(this, var5));
         }
      }

      MenuBar.access$302(this.this$0, new MenuItem(this.val$fileMenu, 2));
      String var9 = SResources.getString("menu.file.exit");
      if (PreferenceLoader.loadBoolean("killCoreOnExit") && Sancho.getCoreFactory().isConnected()) {
         var9 = SResources.getString("menu.file.exitAndKill");
      }

      this.this$0.createMenuItem(this.val$fileMenu, var9, "x", new MenuBar$9(this));
   }

   // $VF: synthetic method
   static MenuBar access$000(MenuBar$1 var0) {
      return var0.this$0;
   }
}
