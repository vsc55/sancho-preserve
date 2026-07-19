package sancho.view.search.result;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolItem;
import sancho.core.Sancho;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.CTabFolderViewFrame;

public class ResultViewFrame extends CTabFolderViewFrame {
   ToolItem pauseContinueToolItem;
   ToolItem extendSearchToolItem;
   ToolItem closeAllTabsToolItem;

   public ResultViewFrame(SashForm sashForm, String name, String text, AbstractTab tab) {
      super(sashForm, name, text, tab);
      this.createViewListener(new ResultViewListener(this));
      this.createViewToolBar();
   }

   public void onCTabFolderSelection() {
      super.onCTabFolderSelection();
      this.updatePauseContinue();
      this.updateExtendSearch();
      this.closeAllTabsToolItem.setEnabled(this.cTabFolder.getItemCount() > 0);
   }

   protected CTabFolder createCTabFolder() {
      boolean tabsOnTop = PreferenceLoader.loadBoolean("resultsCTabFolderTabsOnTop");
      CTabFolder cTabFolder = WidgetFactory.createCTabFolder(this.childComposite, 64 | (tabsOnTop ? 128 : 1024));
      WidgetFactory.addCTabFolderMenu(cTabFolder, "resultsCTabFolder");
      return cTabFolder;
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addCloseAllTabs();
      this.addExtendSearch();
      this.addPauseContinue();
      this.addRefine();
   }

   public void addCloseAllTabs() {
      this.closeAllTabsToolItem = new ToolItem(this.toolBar, 0);
      this.closeAllTabsToolItem.setImage(SResources.getImage("x"));
      this.closeAllTabsToolItem.setToolTipText(SResources.getString("ti.f.closeAllTabs"));
      this.closeAllTabsToolItem.setEnabled(false);
      this.closeAllTabsToolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            CTabItem[] items = ResultViewFrame.this.cTabFolder.getItems();

            for (int i = 0; i < items.length; i++) {
               CTabItem item = items[i];
               Control control = item.getControl();
               if (control != null && !control.isDisposed()) {
                  control.dispose();
               }

               item.dispose();
            }
         }
      });
   }

   public void addExtendSearch() {
      this.extendSearchToolItem = new ToolItem(this.toolBar, 0);
      this.extendSearchToolItem.setImage(SResources.getImage("plus"));
      this.extendSearchToolItem.setToolTipText(SResources.getString("ti.extendSearch"));
      this.extendSearchToolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            Sancho.send((short)4);
         }
      });
      this.updateExtendSearch();
   }

   public void updateExtendSearch() {
      this.extendSearchToolItem.setEnabled(this.cTabFolder.getItemCount() > 0);
   }

   public void addPauseContinue() {
      this.pauseContinueToolItem = new ToolItem(this.toolBar, 0);
      this.togglePauseContinue(false);
      this.pauseContinueToolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            CTabItem cTabItem = ResultViewFrame.this.cTabFolder.getSelection();
            if (cTabItem != null) {
               ResultTab resultTab = (ResultTab)cTabItem.getData();
               if (resultTab != null) {
                  if (resultTab.isPaused()) {
                     ResultViewFrame.this.togglePauseContinue(false);
                     resultTab.unPause();
                  } else {
                     resultTab.pause();
                     ResultViewFrame.this.togglePauseContinue(true);
                  }
               }
            }
         }
      });
      new ToolItem(this.toolBar, 2);
      if (this.getGView() == null) {
         this.pauseContinueToolItem.setEnabled(false);
      }
   }

   public void togglePauseContinue(boolean paused) {
      this.pauseContinueToolItem.setImage(SResources.getImage(paused ? "forward" : "pause"));
      this.pauseContinueToolItem.setToolTipText(SResources.getString(paused ? "ti.continueSearch" : "ti.pauseSearch"));
   }

   public void updatePauseContinue() {
      if (this.getGView() != null) {
         CTabItem cTabItem = this.cTabFolder.getSelection();
         if (cTabItem == null) {
            return;
         }

         ResultTab resultTab = (ResultTab)cTabItem.getData();
         if (resultTab == null) {
            return;
         }

         this.togglePauseContinue(resultTab.isPaused());
         this.pauseContinueToolItem.setEnabled(true);
      } else {
         this.togglePauseContinue(false);
         this.pauseContinueToolItem.setEnabled(false);
      }
   }
}
