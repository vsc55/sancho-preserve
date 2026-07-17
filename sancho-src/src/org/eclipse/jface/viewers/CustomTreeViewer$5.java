package org.eclipse.jface.viewers;

import org.eclipse.jface.util.IOpenEventListener;
import org.eclipse.swt.events.SelectionEvent;

class CustomTreeViewer$5 implements IOpenEventListener {
   // $VF: synthetic field
   private final CustomTreeViewer this$0;

   CustomTreeViewer$5(CustomTreeViewer var1) {
      this.this$0 = var1;
   }

   public void handleOpen(SelectionEvent var1) {
      this.this$0.myHandleOpen(var1);
   }
}
