package org.eclipse.jface.viewers;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import sancho.view.viewer.table.GTableContentProvider;

class CustomTableViewer$6 implements Listener {
   // $VF: synthetic field
   private final CustomTableViewer this$0;

   CustomTableViewer$6(CustomTableViewer var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      GTableContentProvider var2 = (GTableContentProvider)this.this$0.getContentProvider();
      int var3 = var1.index;
      TableItem var4 = (TableItem)var1.item;
      var2.updateElement(var4, var3);
   }
}
