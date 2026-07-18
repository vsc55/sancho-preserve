package sancho.view.viewer.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

class GTableMenuListener$2 extends KeyAdapter {
   // $VF: synthetic field
   private final GTableMenuListener this$0;

   GTableMenuListener$2(GTableMenuListener var1) {
      this.this$0 = var1;
   }

   public void keyPressed(KeyEvent var1) {
      if ((var1.stateMask & SWT.MOD1) != 0 && (var1.stateMask & 131072) != 0 && var1.character == 1) {
         this.this$0.deselectAll();
      } else if (var1.stateMask == SWT.MOD1 && var1.character == 1) {
         this.this$0.selectAll();
      } else if (var1.keyCode == 127) {
         this.this$0.onDeleteKey();
      } else if (var1.keyCode == 16777227) {
         this.this$0.onF2Key();
      }
   }
}
