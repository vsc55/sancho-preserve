package sancho.view.rooms;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.RoomCollection;
import sancho.utility.MyObservable;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProvider;

public class RoomsTableContentProvider extends GTableContentProvider {
   public RoomsTableContentProvider(RoomsTableView view) {
      super(view);
   }

   public Object[] getElements(Object input) {
      if (input instanceof RoomCollection) {
         synchronized (input) {
            RoomCollection roomCollection = (RoomCollection)input;
            roomCollection.clearAllLists();
            return roomCollection.getValues();
         }
      } else {
         return GTableContentProvider.EMPTY_ARRAY;
      }
   }

   public void setActive(boolean active) {
      if (active) {
         MyObservable observable;
         if ((observable = (MyObservable)this.gView.getViewer().getInput()) != null) {
            observable.addObserver(this);
            this.needsRefresh = true;
         }
      } else {
         MyObservable observable;
         if ((observable = (MyObservable)this.gView.getViewer().getInput()) != null) {
            observable.deleteObserver(this);
         }
      }

      super.setActive(active);
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      if (oldInput != null) {
         ((MyObservable)oldInput).deleteObserver(this);
      }

      if (newInput != null) {
         if (this.gView.isActive()) {
            ((MyObservable)newInput).addObserver(this);
         }

         this.updateHeaderLabel();
      }
   }

   public void update(MyObservable observable, Object arg, int id) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (observable instanceof RoomCollection) {
            final RoomCollection roomCollection = (RoomCollection)observable;
            this.tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
               public void run() {
                  if (gView != null && !gView.isDisposed()) {
                     if (roomCollection.removed()) {
                        tableViewer.remove(roomCollection.getAndClearRemoved());
                        updateHeaderLabel();
                     }

                     if (roomCollection.added()) {
                        tableViewer.add(roomCollection.getAndClearAdded());
                        updateHeaderLabel();
                     }

                     if (roomCollection.updated()) {
                        tableViewer.update(roomCollection.getAndClearUpdated(), SResources.SA_Z);
                     }
                  }
               }
            });
         }
      }
   }

   public void updateHeaderLabel() {
      this.gView.getViewFrame().updateCLabelText(SResources.getString("t.r.availableRooms") + ": " + this.tableViewer.getTable().getItemCount());
   }
}
