package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;
import sancho.view.utility.BSpinner;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewer.GView;

public class SetMinDynamicColumnWidthAction extends Action {
   GView gView;

   public SetMinDynamicColumnWidthAction(GView var1) {
      super(SResources.getString("mi.setMinWidth"));
      this.gView = var1;
   }

   public void run() {
      MinWidthDialog var1 = new MinWidthDialog(
         this.gView.getShell(), SResources.getString("mi.setMinWidth"), this.gView.getMinDynamicColumnWidth()
      );
      if (var1.open() == 0) {
         this.gView.setMinDynamicColumnWidth(var1.getIntValue());
      }
   }

   // Dialog with a spinner and scale for choosing the minimum dynamic column width.
   private static class MinWidthDialog extends Dialog {
      int initialValue;
      int intValue;
      String title;
      BSpinner spinner;

      public MinWidthDialog(Shell var1, String var2, int var3) {
         super(var1);
         this.initialValue = var3;
         this.title = var2;
      }

      protected void configureShell(Shell var1) {
         super.configureShell(var1);
         var1.setImage(VersionInfo.getProgramIcon());
         var1.setText(this.title);
      }

      protected Control createDialogArea(Composite var1) {
         Composite var2 = (Composite)super.createDialogArea(var1);
         var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         this.spinner = new BSpinner(var2, 2048);
         this.spinner.setMaximum(1000);
         this.spinner.setSelection(this.initialValue);
         Scale var3 = new Scale(var2, 256);
         var3.setLayoutData(new GridData(768));
         var3.setMinimum(0);
         var3.setMaximum(1000);
         var3.setSelection(this.initialValue);
         var3.setIncrement(1);
         var3.setPageIncrement(5);
         var3.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               int var2 = var3.getSelection();
               MinWidthDialog.this.spinner.setSelection(var2);
            }
         });
         return var2;
      }

      protected void buttonPressed(int var1) {
         this.intValue = this.spinner.getSelection();
         super.buttonPressed(var1);
      }

      public int getIntValue() {
         return this.intValue;
      }
   }
}
