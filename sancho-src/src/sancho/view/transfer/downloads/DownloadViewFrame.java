package sancho.view.transfer.downloads;

import org.eclipse.jface.viewers.CustomTreeViewer;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolItem;
import sancho.core.Sancho;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.TabbedSashViewFrame;

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
         this.addToolItem("ti.d.fileExplorer", "file-explorer", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               String var2 = PreferenceLoader.loadString("explorerExecutable");
               String var3 = PreferenceLoader.loadString("explorerOpenFolder");
               if (!var2.equals("")) {
                  String[] var4 = new String[]{var2, var3};
                  SwissArmy.execInThread(var4, null);
               }
            }
         });
      }

      this.addToolItem("ti.d.commitAll", "commit", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            if (Sancho.hasCollectionFactory()) {
               DownloadViewFrame.this.getCore().getFileCollection().commitAll();
            }
         }
      });
      this.addToolItem("ti.d.toggleClients", "split-table", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            ((DownloadTreeView)DownloadViewFrame.this.gView).toggleClientsTable();
         }
      });
      this.addToolItem("ti.d.collapseAll", "collapseall", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            ((CustomTreeViewer)DownloadViewFrame.this.gView.getViewer()).collapseAll();
         }
      });
      this.addToolItem("ti.d.expandAll", "expandall", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            ((CustomTreeViewer)DownloadViewFrame.this.gView.getViewer()).expandAll();
         }
      });
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
}
