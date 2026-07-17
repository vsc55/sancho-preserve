package org.eclipse.jface.viewers;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

class CustomTableViewer$2 implements SelectionListener {
   // $VF: synthetic field
   private final CustomTableViewer this$0;

   CustomTableViewer$2(CustomTableViewer var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.handleSelect(var1);
   }

   public void widgetDefaultSelected(SelectionEvent var1) {
      this.this$0.handleDoubleSelect(var1);
   }
}
