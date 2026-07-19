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

   public ResultTableContentProvider(ResultTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      ObjectMap var2 = (ObjectMap)var1;
      synchronized (var2) {
         var2.clearAllLists();
         this.updateHeaderLabel();
         return var2.getKeyArray();
      }
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      if (var2 != null) {
         ((MyObservable)var2).deleteObserver(this);
      }

      this.objectMap = (ObjectMap)var3;
      if (var3 != null) {
         this.objectMap.addObserver(this);
         this.updateHeaderLabel(this.objectMap);
      }
   }

   public void dispose() {
      super.dispose();
   }

   public void update(final MyObservable var1, final Object var2, final int var3) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (this.gView.isVisible() && this.gView.isActive()) {
            Display var4 = this.tableViewer.getTable().getDisplay();
            var4.asyncExec(new Runnable() {
               public void run() {
                  if (var2 == null) {
                     ObjectMap objectWeakMap = (ObjectMap)var1;
                     if (gView == null || gView.isDisposed()) {
                        return;
                     }

                     switch (var3) {
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
                  } else if (var2 instanceof SearchWaiting) {
                     SearchWaiting searchWaiting = (SearchWaiting)var2;
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

   private void updateHeaderLabel(ObjectMap var1) {
      if (this.gView != null && !this.gView.isDisposed() && var1 != null) {
         int var2 = var1.size();
         CTabFolderViewFrame var3 = (CTabFolderViewFrame)this.gView.getViewFrame();
         if (var3.getGView() == this.gView) {
            this.gView.getViewFrame().updateCLabelText(RS_RESULTS + ": " + var2 + this.searchWaitingString);
         }
      }
   }
}
