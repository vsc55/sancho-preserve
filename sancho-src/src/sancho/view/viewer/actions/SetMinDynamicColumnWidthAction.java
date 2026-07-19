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

   public SetMinDynamicColumnWidthAction(GView gView) {
      super(SResources.getString("mi.setMinWidth"));
      this.gView = gView;
   }

   public void run() {
      MinWidthDialog dialog = new MinWidthDialog(
         this.gView.getShell(), SResources.getString("mi.setMinWidth"), this.gView.getMinDynamicColumnWidth()
      );
      if (dialog.open() == 0) {
         this.gView.setMinDynamicColumnWidth(dialog.getIntValue());
      }
   }

   // Dialog with a spinner and scale for choosing the minimum dynamic column width.
   private static class MinWidthDialog extends Dialog {
      int initialValue;
      int intValue;
      String title;
      BSpinner spinner;

      public MinWidthDialog(Shell shell, String title, int initialValue) {
         super(shell);
         this.initialValue = initialValue;
         this.title = title;
      }

      protected void configureShell(Shell shell) {
         super.configureShell(shell);
         shell.setImage(VersionInfo.getProgramIcon());
         shell.setText(this.title);
      }

      protected Control createDialogArea(Composite parent) {
         Composite composite = (Composite)super.createDialogArea(parent);
         composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         this.spinner = new BSpinner(composite, 2048);
         this.spinner.setMaximum(1000);
         this.spinner.setSelection(this.initialValue);
         Scale scale = new Scale(composite, 256);
         scale.setLayoutData(new GridData(768));
         scale.setMinimum(0);
         scale.setMaximum(1000);
         scale.setSelection(this.initialValue);
         scale.setIncrement(1);
         scale.setPageIncrement(5);
         scale.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               int value = scale.getSelection();
               MinWidthDialog.this.spinner.setSelection(value);
            }
         });
         return composite;
      }

      protected void buttonPressed(int buttonId) {
         this.intValue = this.spinner.getSelection();
         super.buttonPressed(buttonId);
      }

      public int getIntValue() {
         return this.intValue;
      }
   }
}
