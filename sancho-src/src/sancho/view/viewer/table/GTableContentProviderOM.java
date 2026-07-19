package sancho.view.viewer.table;

import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;

public abstract class GTableContentProviderOM extends GTableContentProvider {
   protected boolean updateOnUpdate;

   public GTableContentProviderOM(GView gView) {
      super(gView);
   }

   public Object[] getElements(Object input) {
      if (input instanceof ObjectMap) {
         synchronized (input) {
            ObjectMap objectMap = (ObjectMap)input;
            objectMap.clearAllLists();
            return objectMap.getKeyArray();
         }
      } else {
         return GTableContentProvider.EMPTY_ARRAY;
      }
   }

   public void setActive(boolean active) {
      if (active && this.needsRefresh) {
         Object input = this.gView.getViewer().getInput();
         if (input != null && input instanceof ObjectMap) {
            this.updateHeaderLabel(((ObjectMap)input).size());
         }
      }

      super.setActive(active);
   }

   public void setVisible(boolean visible) {
      if (visible && this.needsRefresh) {
         Object input = this.gView.getViewer().getInput();
         if (input != null && input instanceof ObjectMap) {
            this.updateHeaderLabel(((ObjectMap)input).size());
         }
      }

      super.setVisible(visible);
   }

   public void update(MyObservable observable, Object arg, int type) {
      this.onUpdate((ObjectMap)observable, arg, type);
   }

   public void onUpdate(ObjectMap objectMap, Object arg, int type) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (this.gView.isActive() && this.gView.isVisible()) {
            this.gView.getComposite().getDisplay().asyncExec(new Runnable() {
               public void run() {
                  if (GTableContentProviderOM.this.gView != null && !GTableContentProviderOM.this.gView.isDisposed()) {
                     switch (type) {
                        case 0:
                           if (objectMap.added()) {
                              GTableContentProviderOM.this.tableViewer.add(objectMap.getAndClearAdded());
                              GTableContentProviderOM.this.updateHeaderLabel(objectMap.size());
                           }
                           break;
                        case 1:
                           if (objectMap.updated()) {
                              GTableContentProviderOM.this.tableViewer.update(objectMap.getAndClearUpdated(), SResources.SA_Z);
                              if (GTableContentProviderOM.this.updateOnUpdate) {
                                 GTableContentProviderOM.this.updateHeaderLabel(objectMap.size());
                              }
                           }
                           break;
                        case 2:
                           if (objectMap.removed()) {
                              GTableContentProviderOM.this.tableViewer.remove(objectMap.getAndClearRemoved());
                              GTableContentProviderOM.this.updateHeaderLabel(objectMap.size());
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

   protected abstract void updateHeaderLabel(int count);
}
