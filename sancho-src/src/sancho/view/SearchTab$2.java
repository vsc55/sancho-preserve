package sancho.view;

import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Control;

class SearchTab$2 extends CTabFolder2Adapter {
   // $VF: synthetic field
   private final SearchTab this$0;

   SearchTab$2(SearchTab var1) {
      this.this$0 = var1;
   }

   public void close(CTabFolderEvent var1) {
      CTabItem var2 = (CTabItem)var1.item;
      Control var3 = var2.getControl();
      if (var3 != null && !var3.isDisposed()) {
         var3.dispose();
      }
   }
}
