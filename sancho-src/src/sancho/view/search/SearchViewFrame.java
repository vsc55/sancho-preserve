package sancho.view.search;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class SearchViewFrame extends SashViewFrame {
   ASearchTab[] searchTabs;

   public SearchViewFrame(SashForm sashForm, String name, String text, AbstractTab tab) {
      super(sashForm, name, text, tab, false);
      this.createViewListener(new SearchViewListener(this));
   }

   public void setSearchTabs(ASearchTab[] tabs) {
      this.searchTabs = tabs;
   }

   public void clearAllSavedSearchCombos() {
      for (int i = 0; i < this.searchTabs.length; i++) {
         this.searchTabs[i].clearAllSearchCombos();
      }
   }

   public void dispose() {
      this.searchTabs = null;
      super.dispose();
   }
}
