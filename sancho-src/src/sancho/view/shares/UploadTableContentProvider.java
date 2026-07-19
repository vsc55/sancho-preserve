package sancho.view.shares;

import org.eclipse.jface.viewers.Viewer;
import sancho.core.Sancho;
import sancho.model.mldonkey.ClientStats;
import sancho.model.mldonkey.SharedFileCollection;
import sancho.utility.MyObservable;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProvider;

public class UploadTableContentProvider extends GTableContentProvider {
   private static final String S_UPLOADS = SResources.getString("l.uploads");
   private long lastTimeStamp;

   public UploadTableContentProvider(UploadTableView view) {
      super(view);
   }

   public void setActive(boolean active) {
      SharedFileCollection collection = (SharedFileCollection)this.gView.getViewer().getInput();
      if (collection != null) {
         if (active) {
            collection.addObserver(this);
            this.needsRefresh = true;
         } else {
            collection.deleteObserver(this);
         }
      }

      super.setActive(active);
   }

   public Object[] getElements(Object input) {
      synchronized (input) {
         SharedFileCollection collection = (SharedFileCollection)input;
         collection.clearAllLists();
         return collection.getValues();
      }
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      SharedFileCollection oldCollection = (SharedFileCollection)oldInput;
      SharedFileCollection newCollection = (SharedFileCollection)newInput;
      if (oldCollection != null) {
         oldCollection.deleteObserver(this);
      }

      if (newCollection != null) {
         if (this.gView.isActive()) {
            newCollection.addObserver(this);
         }

         if (Sancho.hasCollectionFactory()) {
            newCollection.getCore().getClientStats().addObserver(this);
         }
      }
   }

   public void update(MyObservable observable, Object data, int type) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (this.gView.isActive() && this.gView.isVisible()) {
            if (observable instanceof ClientStats) {
               ClientStats stats = (ClientStats)observable;
               if (System.currentTimeMillis() > this.lastTimeStamp + 5000L) {
                  this.lastTimeStamp = System.currentTimeMillis();
                  this.gView
                     .getViewFrame()
                     .updateCLabelTextInGuiThread(
                        S_UPLOADS
                           + ": "
                           + stats.getNumSharedFiles()
                           + " ("
                           + SwissArmy.calcStringSize(stats.getUploadCounter())
                           + "/"
                           + stats.getCore().getSharedFileCollection().getTotalSizeString()
                           + ")"
                     );
               }
            } else if (observable instanceof SharedFileCollection) {
               final SharedFileCollection collection = (SharedFileCollection)observable;
               this.tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
                  public void run() {
                     if (gView != null && !gView.isDisposed()) {
                        if (collection.removed()) {
                           tableViewer.remove(collection.getAndClearRemoved());
                        }

                        if (collection.added()) {
                           tableViewer.add(collection.getAndClearAdded());
                        }

                        if (collection.updated()) {
                           tableViewer.update(collection.getAndClearUpdated(), SResources.SA_Z);
                        }
                     }
                  }
               });
            }
         } else {
            this.needsRefresh = true;
         }
      }
   }
}
