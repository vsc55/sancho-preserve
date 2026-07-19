package sancho.view.server;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.ServerCollection;
import sancho.utility.MyObservable;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProvider;

public class ServerTableContentProvider extends GTableContentProvider {
   public ServerTableContentProvider(ServerTableView tableView) {
      super(tableView);
   }

   public Object[] getElements(Object input) {
      synchronized (input) {
         ServerCollection serverCollection = (ServerCollection)input;
         serverCollection.clearAllLists();
         Object[] servers = serverCollection.getServers();
         this.updateLabel(serverCollection);
         return servers;
      }
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      if (oldInput != null) {
         ((MyObservable)oldInput).deleteObserver(this);
      }

      if (newInput != null && this.gView.isActive()) {
         ((MyObservable)newInput).addObserver(this);
         this.updateLabel((ServerCollection)newInput);
      }
   }

   public void setActive(boolean active) {
      ServerCollection serverCollection = (ServerCollection)this.gView.getViewer().getInput();
      if (serverCollection != null) {
         if (active) {
            this.needsRefresh = true;
            serverCollection.addObserver(this);
         } else {
            serverCollection.deleteObserver(this);
         }
      }

      super.setActive(active);
   }

   public void update(final MyObservable observable, Object arg, int type) {
      if (this.gView.getViewer() != null && !this.gView.getComposite().isDisposed()) {
         if (this.gView.isActive() && this.gView.isVisible()) {
            if (observable instanceof ServerCollection) {
               this.tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
                  public void run() {
                     if (gView != null && !gView.isDisposed()) {
                        ServerCollection serverCollection = (ServerCollection)observable;
                        boolean changed = false;
                        if (serverCollection.removed()) {
                           tableViewer.remove(serverCollection.getAndClearRemoved());
                           changed = true;
                        }

                        if (serverCollection.added()) {
                           tableViewer.add(serverCollection.getAndClearAdded());
                           changed = true;
                        }

                        if (serverCollection.updated()) {
                           boolean updated = tableViewer.updateOrRefresh(serverCollection.getAndClearUpdated(), SResources.SA_Z);
                           changed = !updated;
                        }

                        if (changed) {
                           updateLabel(serverCollection);
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

   protected void updateLabel(ServerCollection serverCollection) {
      this.gView.getViewFrame().updateCLabelText(serverCollection.getHeaderString());
   }
}
