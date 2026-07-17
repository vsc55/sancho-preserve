package sancho.view.server;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.TabbedSashViewFrame;
import sancho.view.viewer.GView;

public class ServerViewFrame extends TabbedSashViewFrame {
   public ServerViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4, "server");
      this.gView = new ServerTableView(this);
      this.createViewListener(new ServerViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("ti.s.cleanOld", "minus", new ServerViewFrame$1(this));
      this.addToolItem("ti.s.addServer", "plus", new ServerViewFrame$2(this));
      this.addToolItem("ti.s.addServerMet", "plus-globe", new ServerViewFrame$3(this));
      this.addToolSeparator();
      this.addRefine();
   }

   // $VF: synthetic method
   static GView access$000(ServerViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(ServerViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$200(ServerViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$300(ServerViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$400(ServerViewFrame var0) {
      return var0.gView;
   }
}
