package sancho.view.search.result;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.view.utility.SResources;

class ResultTab$1 implements DisposeListener {
   // $VF: synthetic field
   private final ResultTab this$0;

   ResultTab$1(ResultTab var1) {
      this.this$0 = var1;
   }

   public void widgetDisposed(DisposeEvent var1) {
      ResultTab.access$000(this.this$0).onCTabFolderSelection();
      if (ResultTab.access$100(this.this$0).getItemCount() == 0) {
         ResultTab.access$000(this.this$0).updateCLabelText(SResources.getString("t.search.results"));
      }
   }
}
