package sancho.view.viewer.table;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.TableColumn;

class GTableView$2 implements DisposeListener {
   // $VF: synthetic field
   private final PreferenceStore val$p;
   // $VF: synthetic field
   private final int val$arrayItem;
   // $VF: synthetic field
   private final GTableView this$0;

   GTableView$2(GTableView var1, PreferenceStore var2, int var3) {
      this.this$0 = var1;
      this.val$p = var2;
      this.val$arrayItem = var3;
   }

   public synchronized void widgetDisposed(DisposeEvent var1) {
      TableColumn var2 = (TableColumn)var1.widget;
      if (var2.getWidth() > 0) {
         this.val$p.setValue(GTableView.access$000(this.this$0)[this.val$arrayItem], var2.getWidth());
      }
   }
}
