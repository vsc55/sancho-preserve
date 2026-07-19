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

   public UploadTableContentProvider(UploadTableView var1) {
      super(var1);
   }

   public void setActive(boolean var1) {
      SharedFileCollection var2 = (SharedFileCollection)this.gView.getViewer().getInput();
      if (var2 != null) {
         if (var1) {
            var2.addObserver(this);
            this.needsRefresh = true;
         } else {
            var2.deleteObserver(this);
         }
      }

      super.setActive(var1);
   }

   public Object[] getElements(Object var1) {
      synchronized (var1) {
         SharedFileCollection var3 = (SharedFileCollection)var1;
         var3.clearAllLists();
         return var3.getValues();
      }
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      SharedFileCollection var4 = (SharedFileCollection)var2;
      SharedFileCollection var5 = (SharedFileCollection)var3;
      if (var4 != null) {
         var4.deleteObserver(this);
      }

      if (var5 != null) {
         if (this.gView.isActive()) {
            var5.addObserver(this);
         }

         if (Sancho.hasCollectionFactory()) {
            var5.getCore().getClientStats().addObserver(this);
         }
      }
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (this.gView.isActive() && this.gView.isVisible()) {
            if (var1 instanceof ClientStats) {
               ClientStats var4 = (ClientStats)var1;
               if (System.currentTimeMillis() > this.lastTimeStamp + 5000L) {
                  this.lastTimeStamp = System.currentTimeMillis();
                  this.gView
                     .getViewFrame()
                     .updateCLabelTextInGuiThread(
                        S_UPLOADS
                           + ": "
                           + var4.getNumSharedFiles()
                           + " ("
                           + SwissArmy.calcStringSize(var4.getUploadCounter())
                           + "/"
                           + var4.getCore().getSharedFileCollection().getTotalSizeString()
                           + ")"
                     );
               }
            } else if (var1 instanceof SharedFileCollection) {
               final SharedFileCollection var5 = (SharedFileCollection)var1;
               this.tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
                  public void run() {
                     if (gView != null && !gView.isDisposed()) {
                        if (var5.removed()) {
                           tableViewer.remove(var5.getAndClearRemoved());
                        }

                        if (var5.added()) {
                           tableViewer.add(var5.getAndClearAdded());
                        }

                        if (var5.updated()) {
                           tableViewer.update(var5.getAndClearUpdated(), SResources.SA_Z);
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
