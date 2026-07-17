package sancho.view.shares;

import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Text;

class UploadViewFrame$5 extends DropTargetAdapter {
   // $VF: synthetic field
   private final FileTransfer val$fileTransfer;
   // $VF: synthetic field
   private final TextTransfer val$textTransfer;
   // $VF: synthetic field
   private final Text val$text;
   // $VF: synthetic field
   private final UploadViewFrame$ShareDialog this$1;

   UploadViewFrame$5(UploadViewFrame$ShareDialog var1, FileTransfer var2, TextTransfer var3, Text var4) {
      this.this$1 = var1;
      this.val$fileTransfer = var2;
      this.val$textTransfer = var3;
      this.val$text = var4;
   }

   public void dragEnter(DropTargetEvent var1) {
      if (var1.detail == 16) {
         if ((var1.operations & 1) != 0) {
            var1.detail = 1;
         } else {
            var1.detail = 0;
         }
      }

      for (int var2 = 0; var2 < var1.dataTypes.length; var2++) {
         if (this.val$fileTransfer.isSupportedType(var1.dataTypes[var2])) {
            var1.currentDataType = var1.dataTypes[var2];
            if (var1.detail != 1) {
               var1.detail = 0;
            }
            break;
         }
      }
   }

   public void drop(DropTargetEvent var1) {
      if (this.val$textTransfer.isSupportedType(var1.currentDataType)) {
         this.val$text.append((String)var1.data);
      }

      if (this.val$fileTransfer.isSupportedType(var1.currentDataType)) {
         String[] var2 = (String[])var1.data;
         if (var2.length > 1) {
            for (int var3 = 0; var3 < var2.length; var3++) {
               this.val$text.setText(var2[var3]);
               UploadViewFrame$ShareDialog.access$302(this.this$1, var2[var3]);
               UploadViewFrame$ShareDialog.access$500(this.this$1).sendShareCommand(UploadViewFrame$ShareDialog.access$400(this.this$1));
               this.val$text.setText("");
            }
         } else if (var2.length == 1) {
            this.val$text.append(var2[0]);
         }
      }
   }
}
