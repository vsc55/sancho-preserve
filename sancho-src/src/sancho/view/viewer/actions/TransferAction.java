package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.model.mldonkey.IPreview;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.TransferDialog;
import sancho.view.utility.WidgetFactory;

public class TransferAction extends Action {
   IPreview[] iPreviewArray;
   int[] subFiles;
   Shell shell;

   public TransferAction(Shell var1, IPreview[] var2, int[] var3) {
      super(SResources.getString("td.menuText"));
      this.setImageDescriptor(SResources.getImageDescriptor("down_arrow_green"));
      this.shell = var1;
      this.iPreviewArray = var2;
      this.subFiles = var3;
   }

   public void run() {
      PreviewDownloadDialog var1 = new PreviewDownloadDialog(this.shell);
      if (var1.open() == 0) {
         String var2 = var1.getDirectory();

         for (int var3 = 0; var3 < this.iPreviewArray.length; var3++) {
            TransferDialog var4 = new TransferDialog(this.shell, this.iPreviewArray[var3], this.subFiles, var2);
            var4.open();
         }
      }
   }

   // Dialog that prompts for the destination directory before starting downloads.
   private static class PreviewDownloadDialog extends Dialog {
      String directory;
      Text text;

      public PreviewDownloadDialog(Shell var2) {
         super(var2);
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
         var3.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               Shell var2 = PreviewDownloadDialog.this.getShell();
               DirectoryDialog var3 = new DirectoryDialog(var2, 0);
               String var4;
               if ((var4 = var3.open()) != null) {
                  PreviewDownloadDialog.this.text.setText(var4);
               }
            }
         });
         return var2;
      }

      public String getDirectory() {
         return this.directory;
      }
   }
}
