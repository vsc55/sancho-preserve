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

   public SearchTab_Simple(ResultViewFrame viewFrame, SearchTab searchTab) {
      super(viewFrame, searchTab);
   }

   protected void createSearchTip(Composite composite) {
      Group group = new Group(composite, 72);
      group.setText(SResources.getString("s.searchTip"));
      GridData gridData = new GridData(4, 128, true, false, 2, 1);
      group.setLayoutData(gridData);
      group.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 0, 0, false));
      Label label = new Label(group, 64);
      label.setText(SResources.getString("s.searchTipText"));
      gridData = new GridData(4, 128, true, false, 1, 1);
      label.setLayoutData(gridData);
   }

   public Control createTab(CTabFolder tabFolder) {
      Composite composite = this.createMainComposite(tabFolder);
      this.searchCombo = this.createSavedSearchCombo(composite, "s.searchFor", "simpleSearchFor");
      this.createNetworkCombo(composite);
      this.fileTypeCombo = this.createFileType(composite);
      this.searchTypeCombo = this.createSearchTypeCombo(composite);
      this.createSearchTip(composite);
      return composite;
   }

   public String getText() {
      return SResources.getString("s.tab.simple");
   }

   public void performSearch() {
      if (!this.searchCombo.getText().equals("")) {
         String searchText = this.searchCombo.getText();
         this.searchCombo.add(searchText, 0);
         if (Sancho.hasCollectionFactory()) {
            SearchQuery query = this.viewFrame.getCore().getCollectionFactory().getSearchQuery();
            query.setSearchString(searchText);
            this.addNetwork(query);
            this.parseFileType(this.fileTypeCombo, query);
            this.parseSearchType(this.searchTypeCombo, query);
            query.send();
            new ResultTab(this.viewFrame, this.searchTab.getCTabFolder(), this.searchTab, query.getSearchId(), searchText);
            this.searchCombo.setText("");
         }
      }
   }
}
