package sancho.view.utility;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import sancho.utility.VersionInfo;

public class BSpinner {
   boolean emulated = VersionInfo.isWin95();
   Spinner spinner;
   CSpinner cspinner;

   public BSpinner(Composite var1, int var2) {
      this.emulated = false;
      if (this.emulated) {
         this.cspinner = new CSpinner(var1, var2);
      } else {
         this.spinner = new Spinner(var1, var2);
      }
   }

   public void setToolTipText(String var1) {
      if (!this.emulated) {
         this.spinner.setToolTipText(var1);
      }
   }

   public void setEnabled(boolean var1) {
      if (this.emulated) {
         this.cspinner.setEnabled(var1);
      } else {
         this.spinner.setEnabled(var1);
      }
   }

   public void setMinimum(int var1) {
      if (this.emulated) {
         this.cspinner.setMinimum(var1);
      } else {
         this.spinner.setMinimum(var1);
      }
   }

   public void setMaximum(int var1) {
      if (this.emulated) {
         this.cspinner.setMaximum(var1);
      } else {
         this.spinner.setMaximum(var1);
      }
   }

   public int getSelection() {
      return this.emulated ? this.cspinner.getSelection() : this.spinner.getSelection();
   }

   public void setSelection(int var1) {
      if (this.emulated) {
         this.cspinner.setSelection(var1);
      } else {
         this.spinner.setSelection(var1);
      }
   }

   public void setLayoutData(Object var1) {
      if (this.emulated) {
         this.cspinner.setLayoutData(var1);
      } else {
         this.spinner.setLayoutData(var1);
      }
   }
}
