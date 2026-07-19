package sancho.view.search;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.SashViewListener;

public class SearchViewListener extends SashViewListener {
   public SearchViewListener(SearchViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new ClearAllCombosAction());
      this.createSashActions(var1, "t.search.results");
   }

   // Menu action that clears all saved search combo entries.
   private class ClearAllCombosAction extends Action {
      public ClearAllCombosAction() {
         super(SResources.getString("mi.clearCombos"));
         this.setImageDescriptor(SResources.getImageDescriptor("clear"));
      }

      public void run() {
         ((SearchViewFrame)viewFrame).clearAllSavedSearchCombos();
      }
   }
}
