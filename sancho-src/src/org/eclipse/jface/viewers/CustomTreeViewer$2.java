package org.eclipse.jface.viewers;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

class CustomTreeViewer$2 implements DisposeListener {
   // $VF: synthetic field
   private final CustomTreeViewer this$0;

   CustomTreeViewer$2(CustomTreeViewer var1) {
      this.this$0 = var1;
   }

   public void widgetDisposed(DisposeEvent var1) {
      this.this$0.handleDispose(var1);
   }
}
