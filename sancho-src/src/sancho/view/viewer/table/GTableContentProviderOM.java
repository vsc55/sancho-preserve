package sancho.view.viewer.table;

import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;

public abstract class GTableContentProviderOM extends GTableContentProvider {
   protected boolean updateOnUpdate;

   public GTableContentProviderOM(GView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      if (var1 instanceof ObjectMap) {
         synchronized (var1) {
            ObjectMap var3 = (ObjectMap)var1;
            var3.clearAllLists();
            return var3.getKeyArray();
         }
      } else {
         return GTableContentProvider.EMPTY_ARRAY;
      }
   }

   public void setActive(boolean var1) {
      if (var1 && this.needsRefresh) {
         Object var2 = this.gView.getViewer().getInput();
         if (var2 != null && var2 instanceof ObjectMap) {
            this.updateHeaderLabel(((ObjectMap)var2).size());
         }
      }

      super.setActive(var1);
   }

   public void setVisible(boolean var1) {
      if (var1 && this.needsRefresh) {
         Object var2 = this.gView.getViewer().getInput();
         if (var2 != null && var2 instanceof ObjectMap) {
            this.updateHeaderLabel(((ObjectMap)var2).size());
         }
      }

      super.setVisible(var1);
   }

   public void update(MyObservable var1, Object var2, int var3) {
      this.onUpdate((ObjectMap)var1, var2, var3);
   }

   public void onUpdate(ObjectMap var1, Object var2, int var3) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (this.gView.isActive() && this.gView.isVisible()) {
            this.gView.getComposite().getDisplay().asyncExec(new Runnable() {
               public void run() {
                  if (GTableContentProviderOM.this.gView != null && !GTableContentProviderOM.this.gView.isDisposed()) {
                     switch (var3) {
                        case 0:
                           if (var1.added()) {
                              GTableContentProviderOM.this.tableViewer.add(var1.getAndClearAdded());
                              GTableContentProviderOM.this.updateHeaderLabel(var1.size());
                           }
                           break;
                        case 1:
                           if (var1.updated()) {
                              GTableContentProviderOM.this.tableViewer.update(var1.getAndClearUpdated(), SResources.SA_Z);
                              if (GTableContentProviderOM.this.updateOnUpdate) {
                                 GTableContentProviderOM.this.updateHeaderLabel(var1.size());
                              }
                           }
                           break;
                        case 2:
                           if (var1.removed()) {
                              GTableContentProviderOM.this.tableViewer.remove(var1.getAndClearRemoved());
                              GTableContentProviderOM.this.updateHeaderLabel(var1.size());
                           }
                     }
                  }
               }
            });
         } else {
            this.needsRefresh = true;
         }
      }
   }

   protected abstract void updateHeaderLabel(int var1);
}
