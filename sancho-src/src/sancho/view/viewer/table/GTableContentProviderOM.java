package sancho.view.viewer.table;

import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
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
            this.gView.getComposite().getDisplay().asyncExec(new GTableContentProviderOM$1(this, var3, var1));
         } else {
            this.needsRefresh = true;
         }
      }
   }

   protected abstract void updateHeaderLabel(int var1);
}
