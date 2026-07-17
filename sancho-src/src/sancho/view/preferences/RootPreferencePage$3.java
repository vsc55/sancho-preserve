package sancho.view.preferences;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

class RootPreferencePage$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final Text val$progText;
   // $VF: synthetic field
   private final RootPreferencePage this$0;

   RootPreferencePage$3(RootPreferencePage var1, Text var2) {
      this.this$0 = var1;
      this.val$progText = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      FileDialog var2 = new FileDialog(this.val$progText.getShell(), 4);
      if (var2.open() != null) {
         String var3 = var2.getFilterPath() + System.getProperty("file.separator");
         var3 = var3 + var2.getFileName();
         this.val$progText.setText(var3);
      }
   }
}
