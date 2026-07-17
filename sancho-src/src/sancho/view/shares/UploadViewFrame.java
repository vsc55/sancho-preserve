package sancho.view.shares;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.TabbedViewFrame;
import sancho.view.viewer.GView;

public class UploadViewFrame extends TabbedViewFrame {
   UploadViewFrame$ShareDialog shareDialog;

   public UploadViewFrame(Composite var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4, "uploads");
      this.gView = new UploadTableView(this);
      this.createViewListener(new UploadViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("ti.u.unshare", "minus", new UploadViewFrame$1(this));
      this.addToolItem("ti.u.share", "plus", new UploadViewFrame$2(this));
      this.addToolItem("ti.u.reshare", "rotate", new UploadViewFrame$3(this));
      this.addToolSeparator();
      this.addRefine();
   }

   public void sendShareCommand(boolean var1) {
      if (this.shareDialog != null && !this.shareDialog.getDirectory().equals("") && Sancho.hasCollectionFactory()) {
         String var2 = var1 ? "share " + this.shareDialog.getPriority() : "unshare";
         var2 = var2 + " \"" + this.shareDialog.getDirectory() + "\"";
         if (var1) {
            var2 = var2 + " " + this.shareDialog.getStrategy();
         }

         Sancho.send((short)29, var2);
      }
   }

   // $VF: synthetic method
   static GView access$000(UploadViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(UploadViewFrame var0) {
      return var0.gView;
   }
}
