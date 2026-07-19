package sancho.view.viewFrame;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.MaximizeSashMouseAdapter;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewer.GView;

public class CTabFolderViewFrame extends SashViewFrame {
   protected CTabFolder cTabFolder = this.createCTabFolder();

   public CTabFolderViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.cTabFolder.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            CTabFolderViewFrame.this.onCTabFolderSelection();
         }
      });
   }

   protected CTabFolder createCTabFolder() {
      return WidgetFactory.createCTabFolder(this.childComposite, 0);
   }

   public void createViewListener(CTabFolderViewListener var1) {
      this.setupViewListener(var1);
      this.cLabel.addMouseListener(new MaximizeSashMouseAdapter(this.cLabel, this.menuManager, this.getParentSashForm(), this.getControl()));
   }

   public void onCTabFolderSelection() {
      this.updateRefine();
   }

   public CTabFolder getCTabFolder() {
      return this.cTabFolder;
   }

   public GView getGView() {
      return this.cTabFolder != null
            && !this.cTabFolder.isDisposed()
            && this.cTabFolder.getSelection() != null
            && !this.cTabFolder.getSelection().isDisposed()
            && this.cTabFolder.getSelection().getData("gView") != null
         ? (GView)this.cTabFolder.getSelection().getData("gView")
         : null;
   }

   public void updateDisplay() {
      super.updateDisplay();

      for (int var1 = 0; var1 < this.cTabFolder.getItems().length; var1++) {
         CTabItem var2 = this.cTabFolder.getItems()[var1];
         if (var2.getData("gView") != null) {
            GView var3 = (GView)var2.getData("gView");
            if (var3 != null) {
               var3.updateDisplay();
            }
         }
      }
   }

   public void dispose() {
      if (this.cTabFolder != null) {
         this.cTabFolder.dispose();
         this.cTabFolder = null;
      }

      super.dispose();
   }

   public void updateRefine() {
      if (this.refineText != null) {
         if (this.getGView() != null) {
            this.refineText.setText(this.getGView().getRefineString());
            this.refineText.setEnabled(true);
            this.clearRefineToolItem.setEnabled(true);
         } else {
            this.refineText.setText("");
            this.refineText.setEnabled(false);
            this.clearRefineToolItem.setEnabled(false);
         }
      }
   }

   public void onDisconnect() {
      for (int var1 = 0; var1 < this.cTabFolder.getItems().length; var1++) {
         CTabItem var2 = this.cTabFolder.getItems()[var1];
         if (var2.getData("gView") != null) {
            GView var3 = (GView)var2.getData("gView");
            var3.unsetInput();
         }
      }

      super.onDisconnect();
   }

   public void onConnect() {
      for (int var1 = 0; var1 < this.cTabFolder.getItems().length; var1++) {
         CTabItem var2 = this.cTabFolder.getItems()[var1];
         if (var2.getData("gView") != null) {
            GView var3 = (GView)var2.getData("gView");
            var3.setInput();
         }
      }
   }
}
