package sancho.view.search;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.SashViewListener;

public class SearchViewListener extends SashViewListener {
   public SearchViewListener(SearchViewFrame searchViewFrame) {
      super(searchViewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      menuManager.add(new ClearAllCombosAction());
      this.createSashActions(menuManager, "t.search.results");
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
