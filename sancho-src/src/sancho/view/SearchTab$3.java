package sancho.view;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.search.result.ResultTableContentProvider;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;

class SearchTab$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final SearchTab this$0;

   SearchTab$3(SearchTab var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      CTabItem var2 = (CTabItem)var1.item;
      GView var3 = (GView)var2.getData("gView");
      if (var3 == null) {
         SearchTab.access$100(this.this$0).updateCLabelText(SResources.getString("t.search.results"));
      } else {
         ResultTableContentProvider var4 = (ResultTableContentProvider)var3.getContentProvider();
         var4.updateHeaderLabel();
      }
   }
}
