package sancho.view.search;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewListener;
import sancho.view.viewFrame.ViewFrame;

public class SearchViewListener extends SashViewListener {
   public SearchViewListener(SearchViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new SearchViewListener$ClearAllCombosAction(this));
      this.createSashActions(var1, "t.search.results");
   }

   // $VF: synthetic method
   static ViewFrame access$000(SearchViewListener var0) {
      return var0.viewFrame;
   }
}
