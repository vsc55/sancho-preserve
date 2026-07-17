package sancho.view.viewer.table;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.TableColumn;

class GTableView$3 extends ControlAdapter {
   // $VF: synthetic field
   private final PreferenceStore val$p;
   // $VF: synthetic field
   private final int val$arrayItem;
   // $VF: synthetic field
   private final GTableView this$0;

   GTableView$3(GTableView var1, PreferenceStore var2, int var3) {
      this.this$0 = var1;
      this.val$p = var2;
      this.val$arrayItem = var3;
   }

   public void controlResized(ControlEvent var1) {
      TableColumn var2 = (TableColumn)var1.widget;
      if (var2.getWidth() > 0) {
         this.val$p.setValue(GTableView.access$100(this.this$0)[this.val$arrayItem], var2.getWidth());
      }
   }
}
