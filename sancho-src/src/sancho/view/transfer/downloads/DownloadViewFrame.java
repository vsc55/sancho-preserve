package sancho.view.transfer.downloads;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.TabbedSashViewFrame;
import sancho.view.viewer.GView;

public class DownloadViewFrame extends TabbedSashViewFrame {
   public DownloadViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4, "downloads");
      this.gView = new DownloadTreeView(this);
      this.createViewListener(new DownloadViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      if (!PreferenceLoader.loadString("explorerExecutable").equals("")) {
         this.addToolItem("ti.d.fileExplorer", "file-explorer", new DownloadViewFrame$1(this));
      }

      this.addToolItem("ti.d.commitAll", "commit", new DownloadViewFrame$2(this));
      this.addToolItem("ti.d.toggleClients", "split-table", new DownloadViewFrame$3(this));
      this.addToolItem("ti.d.collapseAll", "collapseall", new DownloadViewFrame$4(this));
      this.addToolItem("ti.d.expandAll", "expandall", new DownloadViewFrame$5(this));
      new ToolItem(this.toolBar, 2);
      this.addRefine();
   }

   public Control getControl() {
      return super.getParentSashForm();
   }

   public CTabFolder getCTabFolder() {
      return this.cTabFolder;
   }

   public SashForm getParentSashForm() {
      return this.getParentSashForm(true);
   }

   public SashForm getParentSashForm(boolean var1) {
      return var1 ? (SashForm)super.getParentSashForm().getParent() : super.getParentSashForm();
   }

   // $VF: synthetic method
   static GView access$000(DownloadViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(DownloadViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$200(DownloadViewFrame var0) {
      return var0.gView;
   }
}
