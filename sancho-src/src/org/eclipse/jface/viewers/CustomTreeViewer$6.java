package org.eclipse.jface.viewers;

import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;

class CustomTreeViewer$6 implements TreeListener {
   // $VF: synthetic field
   private final CustomTreeViewer this$0;

   CustomTreeViewer$6(CustomTreeViewer var1) {
      this.this$0 = var1;
   }

   public void treeExpanded(TreeEvent var1) {
      this.this$0.handleTreeExpand(var1);
   }

   public void treeCollapsed(TreeEvent var1) {
      this.this$0.handleTreeCollapse(var1);
   }
}
