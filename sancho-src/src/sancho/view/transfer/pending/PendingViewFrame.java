package sancho.view.transfer.pending;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.SashViewFrame;

public class PendingViewFrame extends SashViewFrame {
   public PendingViewFrame(SashForm sashForm, String labelString, String imageString, AbstractTab tab) {
      super(sashForm, labelString, imageString, tab);
      this.gView = new PendingTableView(this);
      this.createViewListener(new PendingViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      ToolItem toolItem = new ToolItem(this.toolBar, 0);
      toolItem.setImage(SResources.getImage("plus"));
      toolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            boolean enabled = !PreferenceLoader.loadBoolean("pollPending");
            PreferenceLoader.getPreferenceStore().setValue("pollPending", enabled);
            PreferenceLoader.saveStore();
            if (PendingViewFrame.this.getCore() != null) {
               PendingViewFrame.this.getCore().updatePreferences();
            }

            PendingViewFrame.this.toggleActive(toolItem, enabled);
            if (enabled) {
               PendingViewFrame.this.gView.setInput();
            } else {
               PendingViewFrame.this.gView.getViewer().setInput(null);
               WidgetFactory.setMaximizedSashFormControl(PendingViewFrame.this.getParentSashForm(), 0);
            }
         }
      });
      this.toggleActive(toolItem, PreferenceLoader.loadBoolean("pollPending"));
      new ToolItem(this.toolBar, 2);
      this.addRefine();
   }

   public void toggleActive(ToolItem toolItem, boolean enabled) {
      toolItem.setImage(SResources.getImage(enabled ? "minus" : "plus"));
      toolItem.setToolTipText(SResources.getString(enabled ? "l.disableTable" : "l.enableTable"));
   }
}
