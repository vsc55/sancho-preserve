package sancho.view.search.result;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import sancho.model.mldonkey.utility.SearchWaiting;
import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.CTabFolderViewFrame;
import sancho.view.viewer.table.GTableContentProvider;

public class ResultTableContentProvider extends GTableContentProvider {
   private static final String RS_RESULTS = SResources.getString("t.search.results");
   private String searchWaitingString = "";
   private ObjectMap objectMap;

   public ResultTableContentProvider(ResultTableView view) {
      super(view);
   }

   public Object[] getElements(Object inputElement) {
      ObjectMap objectMap = (ObjectMap)inputElement;
      synchronized (objectMap) {
         objectMap.clearAllLists();
         this.updateHeaderLabel();
         return objectMap.getKeyArray();
      }
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      if (oldInput != null) {
         ((MyObservable)oldInput).deleteObserver(this);
      }

      this.objectMap = (ObjectMap)newInput;
      if (newInput != null) {
         this.objectMap.addObserver(this);
         this.updateHeaderLabel(this.objectMap);
      }
   }

   public void dispose() {
      super.dispose();
   }

   public void update(final MyObservable observable, final Object data, final int updateType) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (this.gView.isVisible() && this.gView.isActive()) {
            Display display = this.tableViewer.getTable().getDisplay();
            display.asyncExec(new Runnable() {
               public void run() {
                  if (data == null) {
                     ObjectMap objectWeakMap = (ObjectMap)observable;
                     if (gView == null || gView.isDisposed()) {
                        return;
                     }

                     switch (updateType) {
                        case 0:
                           if (objectWeakMap.added()) {
                              tableViewer.add(objectWeakMap.getAndClearAdded());
                              updateHeaderLabel();
                           }
                           break;
                        case 1:
                           if (objectWeakMap.updated()) {
                              tableViewer.update(objectWeakMap.getAndClearUpdated(), SResources.SA_Z);
                              updateHeaderLabel();
                           }
                           break;
                        case 2:
                           if (objectWeakMap.removed()) {
                              tableViewer.remove(objectWeakMap.getAndClearRemoved());
                              updateHeaderLabel();
                           }
                     }
                  } else if (data instanceof SearchWaiting) {
                     SearchWaiting searchWaiting = (SearchWaiting)data;
                     searchWaitingString = " (" + SResources.getString("s.r.waiting") + searchWaiting.getNumWaiting() + ")";
                     updateHeaderLabel();
                  }
               }
            });
         } else {
            this.needsRefresh = true;
         }
      }
   }

   public void updateHeaderLabel() {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.updateHeaderLabel((ObjectMap)this.gView.getViewer().getInput());
      }
   }

   private void updateHeaderLabel(ObjectMap objectMap) {
      if (this.gView != null && !this.gView.isDisposed() && objectMap != null) {
         int count = objectMap.size();
         CTabFolderViewFrame viewFrame = (CTabFolderViewFrame)this.gView.getViewFrame();
         if (viewFrame.getGView() == this.gView) {
            this.gView.getViewFrame().updateCLabelText(RS_RESULTS + ": " + count + this.searchWaitingString);
         }
      }
   }
}
