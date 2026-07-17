package sancho.view.transfer.downloads;

import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import sancho.utility.SwissArmy;
import sancho.view.transfer.UniformResourceLocator;

class DownloadTreeMenuListener$2 extends DropTargetAdapter {
   // $VF: synthetic field
   private final TextTransfer val$textTransfer;
   // $VF: synthetic field
   private final UniformResourceLocator val$uRL;
   // $VF: synthetic field
   private final FileTransfer val$fileTransfer;
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   DownloadTreeMenuListener$2(DownloadTreeMenuListener var1, TextTransfer var2, UniformResourceLocator var3, FileTransfer var4) {
      this.this$0 = var1;
      this.val$textTransfer = var2;
      this.val$uRL = var3;
      this.val$fileTransfer = var4;
   }

   public void dragEnter(DropTargetEvent var1) {
      if (var1.detail == 16) {
         if ((var1.operations & 4) != 0) {
            var1.detail = 4;
         } else if ((var1.operations & 1) != 0) {
            var1.detail = 1;
         } else if ((var1.operations & 2) != 0) {
            var1.detail = 2;
         } else {
            var1.detail = 0;
         }
      }
   }

   public void drop(DropTargetEvent var1) {
      if (var1.data != null && !DownloadTreeMenuListener.access$100(this.this$0)) {
         if (this.val$textTransfer.isSupportedType(var1.currentDataType) || this.val$uRL.isSupportedType(var1.currentDataType)) {
            SwissArmy.sendLink(DownloadTreeMenuListener.access$200(this.this$0).getCore(), (String)var1.data);
         }

         if (this.val$fileTransfer.isSupportedType(var1.currentDataType)) {
            String[] var2 = (String[])var1.data;

            for (int var3 = 0; var3 < var2.length; var3++) {
               SwissArmy.sendLink(DownloadTreeMenuListener.access$300(this.this$0).getCore(), var2[var3]);
            }
         }
      }
   }
}
