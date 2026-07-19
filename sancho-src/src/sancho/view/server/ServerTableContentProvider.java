package sancho.view.server;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.ServerCollection;
import sancho.utility.MyObservable;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProvider;

public class ServerTableContentProvider extends GTableContentProvider {
   public ServerTableContentProvider(ServerTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      synchronized (var1) {
         ServerCollection var3 = (ServerCollection)var1;
         var3.clearAllLists();
         Object[] var4 = var3.getServers();
         this.updateLabel(var3);
         return var4;
      }
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      if (var2 != null) {
         ((MyObservable)var2).deleteObserver(this);
      }

      if (var3 != null && this.gView.isActive()) {
         ((MyObservable)var3).addObserver(this);
         this.updateLabel((ServerCollection)var3);
      }
   }

   public void setActive(boolean var1) {
      ServerCollection var2 = (ServerCollection)this.gView.getViewer().getInput();
      if (var2 != null) {
         if (var1) {
            this.needsRefresh = true;
            var2.addObserver(this);
         } else {
            var2.deleteObserver(this);
         }
      }

      super.setActive(var1);
   }

   public void update(final MyObservable var1, Object var2, int var3) {
      if (this.gView.getViewer() != null && !this.gView.getComposite().isDisposed()) {
         if (this.gView.isActive() && this.gView.isVisible()) {
            if (var1 instanceof ServerCollection) {
               this.tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
                  public void run() {
                     if (gView != null && !gView.isDisposed()) {
                        ServerCollection serverCollection = (ServerCollection)var1;
                        boolean var2 = false;
                        if (serverCollection.removed()) {
                           tableViewer.remove(serverCollection.getAndClearRemoved());
                           var2 = true;
                        }

                        if (serverCollection.added()) {
                           tableViewer.add(serverCollection.getAndClearAdded());
                           var2 = true;
                        }

                        if (serverCollection.updated()) {
                           boolean var3 = tableViewer.updateOrRefresh(serverCollection.getAndClearUpdated(), SResources.SA_Z);
                           var2 = !var3;
                        }

                        if (var2) {
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

   protected void updateLabel(ServerCollection var1) {
      this.gView.getViewFrame().updateCLabelText(var1.getHeaderString());
   }
}
