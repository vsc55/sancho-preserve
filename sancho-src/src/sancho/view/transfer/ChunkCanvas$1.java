package sancho.view.transfer;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import sancho.view.utility.dialogs.ChunkColorDialog;

class ChunkCanvas$1 extends MouseAdapter {
   // $VF: synthetic field
   private final Composite val$parent;
   // $VF: synthetic field
   private final ChunkCanvas this$0;

   ChunkCanvas$1(ChunkCanvas var1, Composite var2) {
      this.this$0 = var1;
      this.val$parent = var2;
   }

   public void mouseDoubleClick(MouseEvent var1) {
      ChunkColorDialog var2 = new ChunkColorDialog(this.val$parent.getShell());
      if (var2.open() == 0) {
         ChunkCanvas.refreshAll();
      }
   }
}
