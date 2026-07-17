package org.eclipse.jface.viewers;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import sancho.view.viewer.tree.GTreeContentProvider;

class CustomTreeViewer$9 implements Listener {
   // $VF: synthetic field
   private final CustomTreeViewer this$0;

   CustomTreeViewer$9(CustomTreeViewer var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      GTreeContentProvider var2 = (GTreeContentProvider)this.this$0.getContentProvider();
      Tree var3 = (Tree)var1.widget;
      int var4 = var1.index;
      TreeItem var5 = (TreeItem)var1.item;
      TreeItem var6 = var5.getParentItem();
      Object var7;
      if (var6 != null) {
         var7 = var6.getData();
         if (var7 == null) {
            int var8 = var3.indexOf(var6);
            var7 = var2.getSFElement(var8);
         }
      } else {
         var7 = this.this$0.getInput();
      }

      var2.updateElement(var5, var7, var4);
   }
}
