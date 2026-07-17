package sancho.view.transfer.uploaders;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewer.GView;

public class UploadersViewFrame extends SashViewFrame {
   public UploadersViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new UploadersTableView(this);
      this.createViewListener(new UploadersViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      ToolItem var1 = new ToolItem(this.toolBar, 0);
      var1.setImage(SResources.getImage("plus"));
      var1.addSelectionListener(new UploadersViewFrame$1(this, var1));
      this.toggleActive(var1, PreferenceLoader.loadBoolean("pollUploaders"));
      new ToolItem(this.toolBar, 2);
      this.addRefine();
   }

   public void toggleActive(ToolItem var1, boolean var2) {
      var1.setImage(SResources.getImage(var2 ? "minus" : "plus"));
      var1.setToolTipText(SResources.getString(var2 ? "l.disableTable" : "l.enableTable"));
   }

   // $VF: synthetic method
   static GView access$000(UploadersViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(UploadersViewFrame var0) {
      return var0.gView;
   }
}
