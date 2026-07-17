package sancho.view.transfer.uploaders;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.preferences.PreferenceLoader;

class UploadersViewFrame$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final ToolItem val$toolItem;
   // $VF: synthetic field
   private final UploadersViewFrame this$0;

   UploadersViewFrame$1(UploadersViewFrame var1, ToolItem var2) {
      this.this$0 = var1;
      this.val$toolItem = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      boolean var2 = !PreferenceLoader.loadBoolean("pollUploaders");
      PreferenceLoader.getPreferenceStore().setValue("pollUploaders", var2);
      PreferenceLoader.saveStore();
      if (this.this$0.getCore() != null) {
         this.this$0.getCore().updatePreferences();
      }

      this.this$0.toggleActive(this.val$toolItem, var2);
      if (var2) {
         UploadersViewFrame.access$000(this.this$0).setInput();
      } else {
         UploadersViewFrame.access$100(this.this$0).getViewer().setInput(null);
      }
   }
}
