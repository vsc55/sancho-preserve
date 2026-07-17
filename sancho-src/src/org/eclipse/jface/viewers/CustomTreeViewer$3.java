package org.eclipse.jface.viewers;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

class CustomTreeViewer$3 implements SelectionListener {
   // $VF: synthetic field
   private final CustomTreeViewer this$0;

   CustomTreeViewer$3(CustomTreeViewer var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.handleSelect(var1);
   }

   public void widgetDefaultSelected(SelectionEvent var1) {
      this.this$0.handleDoubleSelect(var1);
   }
}
