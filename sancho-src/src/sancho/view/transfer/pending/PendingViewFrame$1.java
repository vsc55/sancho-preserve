package sancho.view.transfer.pending;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.WidgetFactory;

class PendingViewFrame$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final ToolItem val$toolItem;
   // $VF: synthetic field
   private final PendingViewFrame this$0;

   PendingViewFrame$1(PendingViewFrame var1, ToolItem var2) {
      this.this$0 = var1;
      this.val$toolItem = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      boolean var2 = !PreferenceLoader.loadBoolean("pollPending");
      PreferenceLoader.getPreferenceStore().setValue("pollPending", var2);
      PreferenceLoader.saveStore();
      if (this.this$0.getCore() != null) {
         this.this$0.getCore().updatePreferences();
      }

      this.this$0.toggleActive(this.val$toolItem, var2);
      if (var2) {
         PendingViewFrame.access$000(this.this$0).setInput();
      } else {
         PendingViewFrame.access$100(this.this$0).getViewer().setInput(null);
         WidgetFactory.setMaximizedSashFormControl(this.this$0.getParentSashForm(), 0);
      }
   }
}
