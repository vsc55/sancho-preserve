package sancho.view.transfer.downloads;

import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

class DownloadTreeMenuListener$1 extends DragSourceAdapter {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   DownloadTreeMenuListener$1(DownloadTreeMenuListener var1) {
      this.this$0 = var1;
   }

   public void dragStart(DragSourceEvent var1) {
      if (DownloadTreeMenuListener.access$000(this.this$0) == null) {
         var1.doit = false;
      } else {
         var1.doit = true;
         DownloadTreeMenuListener.access$102(this.this$0, true);
      }
   }

   public void dragSetData(DragSourceEvent var1) {
      var1.data = DownloadTreeMenuListener.access$000(this.this$0).getED2K();
   }

   public void dragFinished(DragSourceEvent var1) {
      DownloadTreeMenuListener.access$102(this.this$0, false);
   }
}
