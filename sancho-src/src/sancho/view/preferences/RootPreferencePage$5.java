package sancho.view.preferences;

import java.util.ArrayList;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;

class RootPreferencePage$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final List val$list;
   // $VF: synthetic field
   private final ArrayList val$extList;
   // $VF: synthetic field
   private final ArrayList val$progList;
   // $VF: synthetic field
   private final RootPreferencePage this$0;

   RootPreferencePage$5(RootPreferencePage var1, List var2, ArrayList var3, ArrayList var4) {
      this.this$0 = var1;
      this.val$list = var2;
      this.val$extList = var3;
      this.val$progList = var4;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (this.val$list.getSelectionIndex() != -1) {
         int var2 = this.val$list.getSelectionIndex();
         this.val$extList.remove(var2);
         this.val$progList.remove(var2);
         this.this$0.refreshList(this.val$list, this.val$extList, this.val$progList);
      }
   }
}
