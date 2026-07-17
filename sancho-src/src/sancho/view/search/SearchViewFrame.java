package sancho.view.search;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class SearchViewFrame extends SashViewFrame {
   ASearchTab[] searchTabs;

   public SearchViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4, false);
      this.createViewListener(new SearchViewListener(this));
   }

   public void setSearchTabs(ASearchTab[] var1) {
      this.searchTabs = var1;
   }

   public void clearAllSavedSearchCombos() {
      for (int var1 = 0; var1 < this.searchTabs.length; var1++) {
         this.searchTabs[var1].clearAllSearchCombos();
      }
   }

   public void dispose() {
      this.searchTabs = null;
      super.dispose();
   }
}
