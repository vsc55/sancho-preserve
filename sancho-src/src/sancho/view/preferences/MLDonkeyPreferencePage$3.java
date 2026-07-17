package sancho.view.preferences;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

class MLDonkeyPreferencePage$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final MLDonkeyPreferencePage this$0;

   MLDonkeyPreferencePage$3(MLDonkeyPreferencePage var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      Button var2 = (Button)var1.widget;
      MLDonkeyPreferencePage.access$002(this.this$0, var2.getSelection());
      MLDonkeyPreferencePage.access$100(this.this$0).setRedraw(false);
      MLDonkeyPreferencePage.access$200(this.this$0).clear();
      MLDonkeyPreferencePage.access$300(this.this$0);
      Control[] var3 = MLDonkeyPreferencePage.access$400(this.this$0).getChildren();

      for (int var4 = var3.length - 1; var4 >= 0; var4--) {
         var3[var4].dispose();
      }

      this.this$0.reFilter();
      this.this$0.createFieldEditors();
      MLDonkeyPreferencePage.access$500(this.this$0).setMinSize(MLDonkeyPreferencePage.access$400(this.this$0).computeSize(-1, -1, true));
      MLDonkeyPreferencePage.access$100(this.this$0).setRedraw(true);
      MLDonkeyPreferencePage.access$400(this.this$0).layout(true);
   }
}
