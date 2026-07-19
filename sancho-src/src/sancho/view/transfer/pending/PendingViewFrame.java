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
   public PendingViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new PendingTableView(this);
      this.createViewListener(new PendingViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      ToolItem toolItem = new ToolItem(this.toolBar, 0);
      toolItem.setImage(SResources.getImage("plus"));
      toolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            boolean var2 = !PreferenceLoader.loadBoolean("pollPending");
            PreferenceLoader.getPreferenceStore().setValue("pollPending", var2);
            PreferenceLoader.saveStore();
            if (PendingViewFrame.this.getCore() != null) {
               PendingViewFrame.this.getCore().updatePreferences();
            }

            PendingViewFrame.this.toggleActive(toolItem, var2);
            if (var2) {
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

   public void toggleActive(ToolItem var1, boolean var2) {
      var1.setImage(SResources.getImage(var2 ? "minus" : "plus"));
      var1.setToolTipText(SResources.getString(var2 ? "l.disableTable" : "l.enableTable"));
   }
}
