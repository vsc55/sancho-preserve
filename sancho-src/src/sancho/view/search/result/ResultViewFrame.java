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

   public ResultViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
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
      boolean var1 = PreferenceLoader.loadBoolean("resultsCTabFolderTabsOnTop");
      CTabFolder var2 = WidgetFactory.createCTabFolder(this.childComposite, 64 | (var1 ? 128 : 1024));
      WidgetFactory.addCTabFolderMenu(var2, "resultsCTabFolder");
      return var2;
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
         public void widgetSelected(SelectionEvent var1) {
            CTabItem[] var2 = ResultViewFrame.this.cTabFolder.getItems();

            for (int var3 = 0; var3 < var2.length; var3++) {
               CTabItem var4 = var2[var3];
               Control var5 = var4.getControl();
               if (var5 != null && !var5.isDisposed()) {
                  var5.dispose();
               }

               var4.dispose();
            }
         }
      });
   }

   public void addExtendSearch() {
      this.extendSearchToolItem = new ToolItem(this.toolBar, 0);
      this.extendSearchToolItem.setImage(SResources.getImage("plus"));
      this.extendSearchToolItem.setToolTipText(SResources.getString("ti.extendSearch"));
      this.extendSearchToolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
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
         public void widgetSelected(SelectionEvent var1) {
            CTabItem var2 = ResultViewFrame.this.cTabFolder.getSelection();
            if (var2 != null) {
               ResultTab var3 = (ResultTab)var2.getData();
               if (var3 != null) {
                  if (var3.isPaused()) {
                     ResultViewFrame.this.togglePauseContinue(false);
                     var3.unPause();
                  } else {
                     var3.pause();
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

   public void togglePauseContinue(boolean var1) {
      this.pauseContinueToolItem.setImage(SResources.getImage(var1 ? "forward" : "pause"));
      this.pauseContinueToolItem.setToolTipText(SResources.getString(var1 ? "ti.continueSearch" : "ti.pauseSearch"));
   }

   public void updatePauseContinue() {
      if (this.getGView() != null) {
         CTabItem var1 = this.cTabFolder.getSelection();
         if (var1 == null) {
            return;
         }

         ResultTab var2 = (ResultTab)var1.getData();
         if (var2 == null) {
            return;
         }

         this.togglePauseContinue(var2.isPaused());
         this.pauseContinueToolItem.setEnabled(true);
      } else {
         this.togglePauseContinue(false);
         this.pauseContinueToolItem.setEnabled(false);
      }
   }
}
