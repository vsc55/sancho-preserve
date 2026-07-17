package org.eclipse.jface.viewers;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

class CustomTableViewer$1 implements DisposeListener {
   // $VF: synthetic field
   private final CustomTableViewer this$0;

   CustomTableViewer$1(CustomTableViewer var1) {
      this.this$0 = var1;
   }

   public void widgetDisposed(DisposeEvent var1) {
      this.this$0.handleDispose(var1);
   }
}
