package sancho.view.viewer.actions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

class TransferAction$PreviewDownloadDialog extends Dialog {
   String directory;
   Text text;
   // $VF: synthetic field
   private final TransferAction this$0;

   public TransferAction$PreviewDownloadDialog(TransferAction var1, Shell var2) {
      super(var2);
      this.this$0 = var1;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
      var1.setText(SResources.getString("td.title"));
   }

   protected void buttonPressed(int var1) {
      this.directory = this.text.getText();
      super.buttonPressed(var1);
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      this.text = new Text(var2, 2052);
      this.text.setLayoutData(new GridData(768));
      this.text.setText(PreferenceLoader.loadString("previewDownloadDirectory"));
      Button var3 = new Button(var2, 0);
      var3.setText(SResources.getString("b.browse"));
      var3.setLayoutData(new GridData());
      var3.addSelectionListener(new TransferAction$1(this));
      return var2;
   }

   public String getDirectory() {
      return this.directory;
   }
}
