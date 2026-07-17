package org.eclipse.jface.viewers;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

class CustomTableViewer$5 extends MouseAdapter {
   // $VF: synthetic field
   private final CustomTableViewer this$0;

   CustomTableViewer$5(CustomTableViewer var1) {
      this.this$0 = var1;
   }

   public void mouseDown(MouseEvent var1) {
      /* No-op: modern JFace TableViewer/TreeViewer create a default ColumnViewerEditor
      // whose activation strategy starts cell editing on click, so the 2008-era
      // tableViewerImpl.handleMouseDown() forwarding is no longer needed. */
   }
}
