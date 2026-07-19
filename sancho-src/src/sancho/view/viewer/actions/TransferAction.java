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

   public TransferAction(Shell shell, IPreview[] previews, int[] subFiles) {
      super(SResources.getString("td.menuText"));
      this.setImageDescriptor(SResources.getImageDescriptor("down_arrow_green"));
      this.shell = shell;
      this.iPreviewArray = previews;
      this.subFiles = subFiles;
   }

   public void run() {
      PreviewDownloadDialog dialog = new PreviewDownloadDialog(this.shell);
      if (dialog.open() == 0) {
         String directory = dialog.getDirectory();

         for (int i = 0; i < this.iPreviewArray.length; i++) {
            TransferDialog transferDialog = new TransferDialog(this.shell, this.iPreviewArray[i], this.subFiles, directory);
            transferDialog.open();
         }
      }
   }

   // Dialog that prompts for the destination directory before starting downloads.
   private static class PreviewDownloadDialog extends Dialog {
      String directory;
      Text text;

      public PreviewDownloadDialog(Shell shell) {
         super(shell);
      }

      protected void configureShell(Shell shell) {
         super.configureShell(shell);
         shell.setImage(VersionInfo.getProgramIcon());
         shell.setText(SResources.getString("td.title"));
      }

      protected void buttonPressed(int buttonId) {
         this.directory = this.text.getText();
         super.buttonPressed(buttonId);
      }

      protected Control createDialogArea(Composite parent) {
         Composite composite = (Composite)super.createDialogArea(parent);
         composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
         this.text = new Text(composite, 2052);
         this.text.setLayoutData(new GridData(768));
         this.text.setText(PreferenceLoader.loadString("previewDownloadDirectory"));
         Button button = new Button(composite, 0);
         button.setText(SResources.getString("b.browse"));
         button.setLayoutData(new GridData());
         button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               Shell shell = PreviewDownloadDialog.this.getShell();
               DirectoryDialog directoryDialog = new DirectoryDialog(shell, 0);
               String directory;
               if ((directory = directoryDialog.open()) != null) {
                  PreviewDownloadDialog.this.text.setText(directory);
               }
            }
         });
         return composite;
      }

      public String getDirectory() {
         return this.directory;
      }
   }
}
