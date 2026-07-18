package sancho.view.utility;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

public class BSpinner {
   Spinner spinner;

   public BSpinner(Composite var1, int var2) {
      this.spinner = new Spinner(var1, var2);
   }

   public void setToolTipText(String var1) {
      this.spinner.setToolTipText(var1);
   }

   public void setEnabled(boolean var1) {
      this.spinner.setEnabled(var1);
   }

   public void setMinimum(int var1) {
      this.spinner.setMinimum(var1);
   }

   public void setMaximum(int var1) {
      this.spinner.setMaximum(var1);
   }

   public int getSelection() {
      return this.spinner.getSelection();
   }

   public void setSelection(int var1) {
      this.spinner.setSelection(var1);
   }

   public void setLayoutData(Object var1) {
      this.spinner.setLayoutData(var1);
   }
}
