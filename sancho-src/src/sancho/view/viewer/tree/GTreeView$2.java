package sancho.view.viewer.tree;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.TreeColumn;

class GTreeView$2 implements DisposeListener {
   // $VF: synthetic field
   private final PreferenceStore val$p;
   // $VF: synthetic field
   private final int val$arrayItem;
   // $VF: synthetic field
   private final GTreeView this$0;

   GTreeView$2(GTreeView var1, PreferenceStore var2, int var3) {
      this.this$0 = var1;
      this.val$p = var2;
      this.val$arrayItem = var3;
   }

   public synchronized void widgetDisposed(DisposeEvent var1) {
      TreeColumn var2 = (TreeColumn)var1.widget;
      if (var2.getWidth() > 0) {
         this.val$p.setValue(GTreeView.access$000(this.this$0)[this.val$arrayItem], var2.getWidth());
      }
   }
}
