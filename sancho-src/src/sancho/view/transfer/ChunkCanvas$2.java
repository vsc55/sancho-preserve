package sancho.view.transfer;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

class ChunkCanvas$2 extends ControlAdapter {
   // $VF: synthetic field
   private final ChunkCanvas this$0;

   ChunkCanvas$2(ChunkCanvas var1) {
      this.this$0 = var1;
   }

   public void controlResized(ControlEvent var1) {
      this.this$0.resizeImage(var1, false);
   }
}
