package sancho.view.preferences;

import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

class MLDonkeyPreferencePage$6 extends IntegerFieldEditor {
   // $VF: synthetic field
   private final MLDonkeyPreferencePage this$0;

   MLDonkeyPreferencePage$6(MLDonkeyPreferencePage var1, String var2, String var3, Composite var4) {
      super(var2, var3, var4);
      this.this$0 = var1;
   }

   protected void doFillIntoGrid(Composite var1, int var2) {
      this.getLabelControl(var1);
      Text var3 = this.getTextControl(var1);
      GridData var4 = new GridData();
      var4.horizontalSpan = var2 - 1;
      if (MLDonkeyPreferencePage.access$600() < 0) {
         GC var5 = new GC(var3);
         Point var6 = var5.textExtent("X");
         var5.dispose();
         MLDonkeyPreferencePage.access$602(var6.x);
      }

      var4.widthHint = 20 * MLDonkeyPreferencePage.access$600();
      var3.setLayoutData(var4);
   }
}
