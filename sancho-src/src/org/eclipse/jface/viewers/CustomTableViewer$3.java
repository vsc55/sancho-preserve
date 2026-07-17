package org.eclipse.jface.viewers;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class CustomTableViewer$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final CustomTableViewer this$0;

   CustomTableViewer$3(CustomTableViewer var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.handlePostSelect(var1);
   }
}
