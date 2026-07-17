package sancho.view.utility.setupWizard;

import org.eclipse.swt.widgets.Label;
import sancho.view.utility.BSpinner;

public class HostPage$SpinnerLabel {
   BSpinner spinner;
   Label label;
   // $VF: synthetic field
   private final HostPage this$0;

   public HostPage$SpinnerLabel(HostPage var1) {
      this.this$0 = var1;
   }

   public void addSpinner(BSpinner var1) {
      this.spinner = var1;
   }

   public void addLabel(Label var1) {
      this.label = var1;
   }

   public void setSelection(int var1) {
      this.spinner.setSelection(var1);
   }

   public int getSelection() {
      return this.spinner.getSelection();
   }

   public void setEnabled(boolean var1) {
      this.spinner.setEnabled(var1);
      this.label.setEnabled(var1);
   }
}
