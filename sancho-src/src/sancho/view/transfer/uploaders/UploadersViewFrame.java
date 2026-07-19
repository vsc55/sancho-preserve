package sancho.view.transfer.uploaders;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.SashViewFrame;

public class UploadersViewFrame extends SashViewFrame {
   public UploadersViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new UploadersTableView(this);
      this.createViewListener(new UploadersViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      ToolItem toolItem = new ToolItem(this.toolBar, 0);
      toolItem.setImage(SResources.getImage("plus"));
      toolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            boolean var2 = !PreferenceLoader.loadBoolean("pollUploaders");
            PreferenceLoader.getPreferenceStore().setValue("pollUploaders", var2);
            PreferenceLoader.saveStore();
            if (UploadersViewFrame.this.getCore() != null) {
               UploadersViewFrame.this.getCore().updatePreferences();
            }

            UploadersViewFrame.this.toggleActive(toolItem, var2);
            if (var2) {
               UploadersViewFrame.this.gView.setInput();
            } else {
               UploadersViewFrame.this.gView.getViewer().setInput(null);
            }
         }
      });
      this.toggleActive(toolItem, PreferenceLoader.loadBoolean("pollUploaders"));
      new ToolItem(this.toolBar, 2);
      this.addRefine();
   }

   public void toggleActive(ToolItem var1, boolean var2) {
      var1.setImage(SResources.getImage(var2 ? "minus" : "plus"));
      var1.setToolTipText(SResources.getString(var2 ? "l.disableTable" : "l.enableTable"));
   }
}
