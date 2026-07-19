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
   public DownloadViewFrame(SashForm sashForm, String prefString, String labelText, AbstractTab tab) {
      super(sashForm, prefString, labelText, tab, "downloads");
      this.gView = new DownloadTreeView(this);
      this.createViewListener(new DownloadViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      if (!PreferenceLoader.loadString("explorerExecutable").equals("")) {
         this.addToolItem("ti.d.fileExplorer", "file-explorer", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               String executable = PreferenceLoader.loadString("explorerExecutable");
               String openFolder = PreferenceLoader.loadString("explorerOpenFolder");
               if (!executable.equals("")) {
                  String[] args = new String[]{executable, openFolder};
                  SwissArmy.execInThread(args, null);
               }
            }
         });
      }

      this.addToolItem("ti.d.commitAll", "commit", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            if (Sancho.hasCollectionFactory()) {
               DownloadViewFrame.this.getCore().getFileCollection().commitAll();
            }
         }
      });
      this.addToolItem("ti.d.toggleClients", "split-table", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            ((DownloadTreeView)DownloadViewFrame.this.gView).toggleClientsTable();
         }
      });
      this.addToolItem("ti.d.collapseAll", "collapseall", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            ((CustomTreeViewer)DownloadViewFrame.this.gView.getViewer()).collapseAll();
         }
      });
      this.addToolItem("ti.d.expandAll", "expandall", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
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

   public SashForm getParentSashForm(boolean outer) {
      return outer ? (SashForm)super.getParentSashForm().getParent() : super.getParentSashForm();
   }
}
