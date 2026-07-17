package sancho.view.transfer.pending;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewer.GView;

public class PendingViewFrame extends SashViewFrame {
   public PendingViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new PendingTableView(this);
      this.createViewListener(new PendingViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      ToolItem var1 = new ToolItem(this.toolBar, 0);
      var1.setImage(SResources.getImage("plus"));
      var1.addSelectionListener(new PendingViewFrame$1(this, var1));
      this.toggleActive(var1, PreferenceLoader.loadBoolean("pollPending"));
      new ToolItem(this.toolBar, 2);
      this.addRefine();
   }

   public void toggleActive(ToolItem var1, boolean var2) {
      var1.setImage(SResources.getImage(var2 ? "minus" : "plus"));
      var1.setToolTipText(SResources.getString(var2 ? "l.disableTable" : "l.enableTable"));
   }

   // $VF: synthetic method
   static GView access$000(PendingViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(PendingViewFrame var0) {
      return var0.gView;
   }
}
