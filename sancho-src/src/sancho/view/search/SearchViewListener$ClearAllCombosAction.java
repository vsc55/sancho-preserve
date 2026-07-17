package sancho.view.search;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class SearchViewListener$ClearAllCombosAction extends Action {
   // $VF: synthetic field
   private final SearchViewListener this$0;

   public SearchViewListener$ClearAllCombosAction(SearchViewListener var1) {
      super(SResources.getString("mi.clearCombos"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("clear"));
   }

   public void run() {
      ((SearchViewFrame)SearchViewListener.access$000(this.this$0)).clearAllSavedSearchCombos();
   }
}
