package sancho.view.utility;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

public class BSpinner {
   Spinner spinner;

   public BSpinner(Composite parent, int style) {
      this.spinner = new Spinner(parent, style);
   }

   public void setToolTipText(String text) {
      this.spinner.setToolTipText(text);
   }

   public void setEnabled(boolean enabled) {
      this.spinner.setEnabled(enabled);
   }

   public void setMinimum(int minimum) {
      this.spinner.setMinimum(minimum);
   }

   public void setMaximum(int maximum) {
      this.spinner.setMaximum(maximum);
   }

   public int getSelection() {
      return this.spinner.getSelection();
   }

   public void setSelection(int selection) {
      this.spinner.setSelection(selection);
   }

   public void setLayoutData(Object layoutData) {
      this.spinner.setLayoutData(layoutData);
   }
}
