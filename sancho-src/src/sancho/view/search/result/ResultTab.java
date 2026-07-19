package sancho.view.search.result;

import org.eclipse.jface.viewers.CustomTableViewer;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import sancho.core.Sancho;
import sancho.model.mldonkey.ResultCollection;
import sancho.model.mldonkey.utility.SearchWaiting;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.utility.ObjectMap;
import sancho.utility.SwissArmy;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewer.GView;

public class ResultTab implements MyObserver, Runnable, DisposeListener {
   private boolean hasTable;
   private CTabFolder cTabFolder;
   private CTabItem cTabItem;
   private GView gView;
   private boolean paused;
   private int searchId;
   private Composite searchingComposite;
   private Label searchingLabel;
   private String searchString;
   private AbstractTab searchTab;
   private ResultViewFrame viewFrame;

   public ResultTab(ResultViewFrame viewFrame, CTabFolder cTabFolder, AbstractTab searchTab, int searchId, String searchString) {
      this.searchString = searchString;
      this.cTabFolder = cTabFolder;
      this.searchId = searchId;
      this.searchTab = searchTab;
      this.viewFrame = viewFrame;
      this.createContent();
      if (Sancho.hasCollectionFactory()) {
         viewFrame.getCore().getResultCollection().addObserver(this);
      }

      viewFrame.onCTabFolderSelection();
   }

   private void createContent() {
      this.cTabItem = new CTabItem(this.cTabFolder, 8388608);
      this.viewFrame.updateCLabelText(SResources.getString("t.search.results"));
      this.cTabItem.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent event) {
            ResultTab.this.viewFrame.onCTabFolderSelection();
            if (ResultTab.this.cTabFolder.getItemCount() == 0) {
               ResultTab.this.viewFrame.updateCLabelText(SResources.getString("t.search.results"));
            }
         }
      });
      this.cTabItem.addDisposeListener(this);
      this.cTabItem.setText(SwissArmy.stringNoAccel(this.searchString));
      this.cTabItem.setToolTipText(SResources.getString("s.r.searchingFor") + this.searchString);
      this.cTabItem.setImage(SResources.getImage("search_small"));
      this.cTabItem.setData(this);
      this.searchingComposite = new Composite(this.cTabFolder, 0);
      this.searchingComposite.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      this.searchingLabel = new Label(this.searchingComposite, 0);
      this.searchingLabel.setText(SResources.getString("s.r.searching"));
      this.searchingLabel.setLayoutData(new GridData(2));
      this.cTabItem.setControl(this.searchingComposite);
      this.cTabFolder.setSelection(this.cTabItem);
   }

   private void createTable() {
      this.cTabItem.setImage(SResources.getImage(this.searchId < 0 ? "jigle" : "search_complete"));
      this.gView = new ResultTableView(this.viewFrame, this.cTabItem, this.searchTab);
      this.gView.setActive(true);
      this.gView.setVisible(true);
      this.cTabItem.setControl(((CustomTableViewer)this.gView.getViewer()).getTable());
   }

   public boolean isPaused() {
      return this.paused;
   }

   public void pause() {
      this.paused = true;
      if (Sancho.hasCollectionFactory()) {
         ObjectMap objectMap = (ObjectMap)this.viewFrame.getCore().getResultCollection().get(this.searchId);
         if (objectMap != null) {
            objectMap.deleteObservers();
         }
      }
   }

   public void run() {
      if (!this.cTabItem.isDisposed() && !this.paused && Sancho.hasCollectionFactory()) {
         if (this.searchingLabel != null && !this.searchingLabel.isDisposed()) {
            this.searchingLabel.dispose();
            this.searchingComposite.dispose();
         }

         this.createTable();
         if (Sancho.hasCollectionFactory()) {
            this.gView.getViewer().setInput(this.viewFrame.getCore().getResultCollection().get(this.searchId));
         }
      }
   }

   public void unPause() {
      this.paused = false;
      if (Sancho.hasCollectionFactory() && this.gView != null && !this.gView.isDisposed()) {
         this.gView.getViewer().setInput(this.viewFrame.getCore().getResultCollection().get(this.searchId));
      }
   }

   public void update(MyObservable observable, Object arg, int id) {
      if (this.cTabItem != null && !this.cTabItem.isDisposed() && !this.isPaused()) {
         if (arg instanceof SearchWaiting) {
            SearchWaiting searchWaiting = (SearchWaiting)arg;
            if (searchWaiting.getId() == this.searchId && this.searchingLabel != null && !this.searchingLabel.isDisposed()) {
               this.searchingLabel.getDisplay().asyncExec(new Runnable() {
               public void run() {
                  if (ResultTab.this.searchingLabel != null && !ResultTab.this.searchingLabel.isDisposed()) {
                     ResultTab.this.searchingLabel.setText(SResources.getString("s.r.searchesWaiting") + searchWaiting.getNumWaiting());
                     ResultTab.this.searchingLabel.getParent().layout();
                  }
               }
            });
            }
         }

         if (!this.hasTable && this.gView == null && ((ResultCollection)observable).containsKey(this.searchId)) {
            if (Sancho.hasCollectionFactory() && this.viewFrame.getCore().getResultCollection() != null) {
               this.viewFrame.getCore().getResultCollection().deleteObserver(this);
            }

            this.hasTable = true;
            this.cTabFolder.getDisplay().asyncExec(this);
         }
      }
   }

   public void widgetDisposed(DisposeEvent event) {
      if (Sancho.hasCollectionFactory()) {
         this.viewFrame.getCore().getResultCollection().deleteObserver(this);
         this.viewFrame.getCore().getResultCollection().closeSearch(this.searchId);
      }
   }
}
