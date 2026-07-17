package org.eclipse.jface.viewers;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class CustomTreeViewer$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final CustomTreeViewer this$0;

   CustomTreeViewer$4(CustomTreeViewer var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.handlePostSelect(var1);
   }
}
