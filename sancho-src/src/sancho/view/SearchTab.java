package sancho.view;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import sancho.view.search.ASearchTab;
import sancho.view.search.SearchTab_Advanced;
import sancho.view.search.SearchTab_Audio;
import sancho.view.search.SearchTab_Simple;
import sancho.view.search.SearchViewFrame;
import sancho.view.search.result.ResultTableContentProvider;
import sancho.view.search.result.ResultViewFrame;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewer.GView;

public class SearchTab extends AbstractTab {
   private Composite composite;
   private CTabFolder resultsCTabFolder;
   private ResultViewFrame resultViewFrame;
   private Button searchButton;
   private CTabFolder searchCTabFolder;
   private ASearchTab[] searchTabs;

   public SearchTab(MainWindow var1, String var2) {
      super(var1, var2);
   }

   protected Button createButton(Composite var1, String var2, SelectionAdapter var3) {
      Button var4 = new Button(this.composite, 8);
      var4.setText(SResources.getString(var2));
      var4.addSelectionListener(var3);
      return var4;
   }

   protected void createContents(Composite var1) {
      String var2 = "searchSash";
      SashForm var3 = WidgetFactory.createSashForm(var1, var2);
      this.createViewFrames(var3);
      WidgetFactory.loadSashForm(var3, var2);
   }

   public void onConnect() {
      super.onConnect();

      for (int var1 = 0; var1 < this.searchTabs.length; var1++) {
         this.searchTabs[var1].onConnect();
      }
   }

   public void dispose() {
      if (this.searchTabs != null) {
         for (int var1 = 0; var1 < this.searchTabs.length; var1++) {
            this.searchTabs[var1].dispose();
         }
      }

      super.dispose();
   }

   public void autoSearch(String var1) {
      this.searchTabs[0].autoSearch(var1);
   }

   private void createLeftSash(Composite var1) {
      var1.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      this.searchCTabFolder = WidgetFactory.createCTabFolder(var1);
      this.searchCTabFolder.setLayoutData(new GridData(768));
      this.searchTabs = new ASearchTab[]{
         new SearchTab_Simple(this.resultViewFrame, this), new SearchTab_Advanced(this.resultViewFrame, this), new SearchTab_Audio(this.resultViewFrame, this)
      };

      for (int var4 = 0; var4 < this.searchTabs.length; var4++) {
         CTabItem var3 = new CTabItem(this.searchCTabFolder, 0);
         var3.setText(this.searchTabs[var4].getText());
         Control var2 = this.searchTabs[var4].createTab(this.searchCTabFolder);
         if (var4 == 0) {
            var3.setControl(var2);
         }

         var3.setData("myControl", var2);
         var3.setData(this.searchTabs[var4]);
      }

      this.searchCTabFolder.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            CTabItem var2 = (CTabItem)var1.item;
            var2.setControl((Control)var2.getData("myControl"));

            for (int var3 = 0; var3 < SearchTab.this.searchCTabFolder.getItems().length; var3++) {
               if (SearchTab.this.searchCTabFolder.getItems()[var3] != var2) {
                  SearchTab.this.searchCTabFolder.getItems()[var3].setControl(null);
               }
            }

            SearchTab.this.searchCTabFolder.getParent().layout();
         }
      });
      this.searchCTabFolder.setSelection(0);
      Composite var5 = new Composite(var1, 0);
      var5.setLayout(WidgetFactory.createGridLayout(1, 2, 0, 0, 0, false));
      var5.setLayoutData(new GridData(768));
      Label var6 = new Label(var5, 258);
      var6.setLayoutData(new GridData(768));
      Composite var7 = new Composite(var5, 0);
      var7.setLayout(WidgetFactory.createGridLayout(1, 7, 5, 0, 0, false));
      var7.setLayoutData(new GridData(768));
      this.createSearchButton(var7);
   }

   private void createRightSash(CTabFolder var1) {
      this.resultsCTabFolder = var1;
      this.resultsCTabFolder.setData(this);
      this.resultsCTabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
         public void close(CTabFolderEvent var1) {
            CTabItem var2 = (CTabItem)var1.item;
            Control var3 = var2.getControl();
            if (var3 != null && !var3.isDisposed()) {
               var3.dispose();
            }
         }
      });
      this.resultsCTabFolder.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            CTabItem var2 = (CTabItem)var1.item;
            GView var3 = (GView)var2.getData("gView");
            if (var3 == null) {
               SearchTab.this.resultViewFrame.updateCLabelText(SResources.getString("t.search.results"));
            } else {
               ResultTableContentProvider var4 = (ResultTableContentProvider)var3.getContentProvider();
               var4.updateHeaderLabel();
            }
         }
      });
   }

   private void createSearchButton(Composite var1) {
      this.composite = new Composite(var1, 0);
      GridData var2 = new GridData(768);
      var2.heightHint = 40;
      var2.horizontalSpan = 2;
      this.composite.setLayoutData(var2);
      this.composite.setLayout(new FillLayout());
      this.searchButton = this.createButton(this.composite, "s.search", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            SearchTab.this.getSearch().performSearch();
         }
      });
   }

   protected void createViewFrames(SashForm var1) {
      SearchViewFrame var2 = new SearchViewFrame(var1, "tab.search", "tab.search.buttonSmall", this);
      this.addViewFrame(var2);
      this.resultViewFrame = new ResultViewFrame(var1, "t.search.results", "tab.search.buttonSmall", this);
      this.addViewFrame(this.resultViewFrame);
      this.createLeftSash(var2.getChildComposite());
      this.createRightSash(this.resultViewFrame.getCTabFolder());
      var2.setSearchTabs(this.searchTabs);
   }

   public CTabFolder getCTabFolder() {
      return this.resultsCTabFolder;
   }

   private ASearchTab getSearch() {
      CTabItem var1 = this.searchCTabFolder.getSelection();
      return (ASearchTab)var1.getData();
   }

   public boolean isButtonEnabled() {
      return this.searchButton != null && !this.searchButton.isDisposed() ? this.searchButton.isEnabled() : false;
   }

   public void setActive() {
      super.setActive();
      this.getSearch().setFocus();
   }

   public void setButtonEnabled(boolean var1) {
      if (this.searchButton != null && !this.searchButton.isDisposed()) {
         this.searchButton.setEnabled(var1);
      }
   }
}
