package sancho.view.search;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import sancho.core.Sancho;
import sancho.model.mldonkey.utility.SearchQuery;
import sancho.view.SearchTab;
import sancho.view.search.result.ResultTab;
import sancho.view.search.result.ResultViewFrame;
import sancho.view.utility.SResources;

public class SearchTab_Advanced extends ASearchTab2 {
   Combo fileType;

   public SearchTab_Advanced(ResultViewFrame var1, SearchTab var2) {
      super(var1, var2);
   }

   public Control createTab(CTabFolder var1) {
      Composite var2 = this.createMainComposite(var1);
      this.searchCombo = this.createSavedSearchCombo(var2, "s.searchFor", "advancedSearchFor");
      this.createNetworkCombo(var2);
      this.fileType = this.createFileType(var2);
      this.searchTypeCombo = this.createSearchTypeCombo(var2);
      String[] var3 = new String[]{"", "exe", "bin", "img", "gif", "jpg"};
      this.formatCombo = this.createCombo(var2, 0, "s.format", var3);
      this.createSeparator(var2);
      var3 = new String[]{"", "3", "5", "10", "25", "50"};
      this.minAvailCombo = this.createCombo(var2, 0, "s.minAvail", var3);
      this.minCombo = this.createMinMaxCombo(var2, 0, "s.minSize");
      this.maxCombo = this.createMinMaxCombo(var2, 0, "s.maxSize");
      var3 = new String[]{"", "50", "100", "200", "400"};
      this.resultCombo = this.createIntegerCombo(var2, 0, "s.maxResults", var3);
      var2.setData(this);
      return var2;
   }

   public String getText() {
      return SResources.getString("s.tab.advanced");
   }

   public void performSearch() {
      String var1 = this.searchCombo.getText();
      this.searchCombo.add(var1, 0);
      if (!var1.equals("") && Sancho.hasCollectionFactory()) {
         SearchQuery var2 = this.viewFrame.getCore().getCollectionFactory().getSearchQuery();
         var2.setSearchString(var1);
         this.parseFileType(this.fileType, var2);
         this.parseSearchType(this.searchTypeCombo, var2);
         this.addMinSizeToQuery(var2, this.minCombo);
         this.addMaxSizeToQuery(var2, this.maxCombo);
         this.addMaxResultsToQuery(var2, this.resultCombo);
         if (!this.formatCombo.getText().equals("")) {
            var2.setFormat(this.formatCombo.getText());
         }

         this.addNetwork(var2);
         var2.send();
         int var3 = var2.getSearchId();
         this.viewFrame.getCore().getResultCollection().setMinAvail(var3, this.parseMinAvail());
         new ResultTab(this.viewFrame, this.searchTab.getCTabFolder(), this.searchTab, var3, var1);
         this.searchCombo.setText("");
      }
   }
}
