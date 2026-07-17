package sancho.view.search;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import sancho.core.Sancho;
import sancho.model.mldonkey.utility.SearchQuery;
import sancho.view.SearchTab;
import sancho.view.search.result.ResultTab;
import sancho.view.search.result.ResultViewFrame;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class SearchTab_Simple extends ASearchTab {
   Combo fileTypeCombo;

   public SearchTab_Simple(ResultViewFrame var1, SearchTab var2) {
      super(var1, var2);
   }

   protected void createSearchTip(Composite var1) {
      Group var2 = new Group(var1, 72);
      var2.setText(SResources.getString("s.searchTip"));
      GridData var3 = new GridData(4, 128, true, false, 2, 1);
      var2.setLayoutData(var3);
      var2.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 0, 0, false));
      Label var4 = new Label(var2, 64);
      var4.setText(SResources.getString("s.searchTipText"));
      var3 = new GridData(4, 128, true, false, 1, 1);
      var4.setLayoutData(var3);
   }

   public Control createTab(CTabFolder var1) {
      Composite var2 = this.createMainComposite(var1);
      this.searchCombo = this.createSavedSearchCombo(var2, "s.searchFor", "simpleSearchFor");
      this.createNetworkCombo(var2);
      this.fileTypeCombo = this.createFileType(var2);
      this.searchTypeCombo = this.createSearchTypeCombo(var2);
      this.createSearchTip(var2);
      return var2;
   }

   public String getText() {
      return SResources.getString("s.tab.simple");
   }

   public void performSearch() {
      if (!this.searchCombo.getText().equals("")) {
         String var1 = this.searchCombo.getText();
         this.searchCombo.add(var1, 0);
         if (Sancho.hasCollectionFactory()) {
            SearchQuery var2 = this.viewFrame.getCore().getCollectionFactory().getSearchQuery();
            var2.setSearchString(var1);
            this.addNetwork(var2);
            this.parseFileType(this.fileTypeCombo, var2);
            this.parseSearchType(this.searchTypeCombo, var2);
            var2.send();
            new ResultTab(this.viewFrame, this.searchTab.getCTabFolder(), this.searchTab, var2.getSearchId(), var1);
            this.searchCombo.setText("");
         }
      }
   }
}
