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
   public UploadersViewFrame(SashForm sashForm, String labelString, String imageString, AbstractTab tab) {
      super(sashForm, labelString, imageString, tab);
      this.gView = new UploadersTableView(this);
      this.createViewListener(new UploadersViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      ToolItem toolItem = new ToolItem(this.toolBar, 0);
      toolItem.setImage(SResources.getImage("plus"));
      toolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            boolean enabled = !PreferenceLoader.loadBoolean("pollUploaders");
            PreferenceLoader.getPreferenceStore().setValue("pollUploaders", enabled);
            PreferenceLoader.saveStore();
            if (UploadersViewFrame.this.getCore() != null) {
               UploadersViewFrame.this.getCore().updatePreferences();
            }

            UploadersViewFrame.this.toggleActive(toolItem, enabled);
            if (enabled) {
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

   public void toggleActive(ToolItem toolItem, boolean enabled) {
      toolItem.setImage(SResources.getImage(enabled ? "minus" : "plus"));
      toolItem.setToolTipText(SResources.getString(enabled ? "l.disableTable" : "l.enableTable"));
   }
}
