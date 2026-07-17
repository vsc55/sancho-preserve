package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.IPreview;
import sancho.view.utility.SResources;
import sancho.view.utility.TransferDialog;

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
      TransferAction$PreviewDownloadDialog var1 = new TransferAction$PreviewDownloadDialog(this, this.shell);
      if (var1.open() == 0) {
         String var2 = var1.getDirectory();

         for (int var3 = 0; var3 < this.iPreviewArray.length; var3++) {
            TransferDialog var4 = new TransferDialog(this.shell, this.iPreviewArray[var3], this.subFiles, var2);
            var4.open();
         }
      }
   }
}
