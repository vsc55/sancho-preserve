package org.eclipse.jface.viewers;

import org.eclipse.jface.util.IOpenEventListener;
import org.eclipse.swt.events.SelectionEvent;

class CustomTableViewer$4 implements IOpenEventListener {
   // $VF: synthetic field
   private final CustomTableViewer this$0;

   CustomTableViewer$4(CustomTableViewer var1) {
      this.this$0 = var1;
   }

   public void handleOpen(SelectionEvent var1) {
      this.this$0.myHandleOpen(var1);
   }
}
