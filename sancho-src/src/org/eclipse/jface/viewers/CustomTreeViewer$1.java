package org.eclipse.jface.viewers;

import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.widgets.TreeItem;

class CustomTreeViewer$1 implements TreeListener {
   // $VF: synthetic field
   private final CustomTreeViewer this$0;

   CustomTreeViewer$1(CustomTreeViewer var1) {
      this.this$0 = var1;
   }

   public void treeCollapsed(TreeEvent var1) {
      TreeItem var2 = (TreeItem)var1.item;
      int var3 = this.this$0.getTree().indexOf(var2);
      if (var3 >= 0) {
         this.this$0.removeExpanded(var3);
      }
   }

   public void treeExpanded(TreeEvent var1) {
      TreeItem var2 = (TreeItem)var1.item;
      int var3 = this.this$0.getTree().indexOf(var2);
      if (var3 >= 0) {
         this.this$0.addExpanded(var3);
      }
   }
}
