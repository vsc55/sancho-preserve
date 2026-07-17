package sancho.view.rooms;

import org.eclipse.jface.viewers.CustomTableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.RoomCollection;
import sancho.utility.MyObservable;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;
import sancho.view.viewer.table.GTableContentProvider;

public class RoomsTableContentProvider extends GTableContentProvider {
   public RoomsTableContentProvider(RoomsTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      if (var1 instanceof RoomCollection) {
         synchronized (var1) {
            RoomCollection var3 = (RoomCollection)var1;
            var3.clearAllLists();
            return var3.getValues();
         }
      } else {
         return GTableContentProvider.EMPTY_ARRAY;
      }
   }

   public void setActive(boolean var1) {
      if (var1) {
         MyObservable var2;
         if ((var2 = (MyObservable)this.gView.getViewer().getInput()) != null) {
            var2.addObserver(this);
            this.needsRefresh = true;
         }
      } else {
         MyObservable var3;
         if ((var3 = (MyObservable)this.gView.getViewer().getInput()) != null) {
            var3.deleteObserver(this);
         }
      }

      super.setActive(var1);
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      if (var2 != null) {
         ((MyObservable)var2).deleteObserver(this);
      }

      if (var3 != null) {
         if (this.gView.isActive()) {
            ((MyObservable)var3).addObserver(this);
         }

         this.updateHeaderLabel();
      }
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (var1 instanceof RoomCollection) {
            RoomCollection var4 = (RoomCollection)var1;
            this.tableViewer.getTable().getDisplay().asyncExec(new RoomsTableContentProvider$1(this, var4));
         }
      }
   }

   public void updateHeaderLabel() {
      this.gView.getViewFrame().updateCLabelText(SResources.getString("t.r.availableRooms") + ": " + this.tableViewer.getTable().getItemCount());
   }

   // $VF: synthetic method
   static GView access$000(RoomsTableContentProvider var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(RoomsTableContentProvider var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static CustomTableViewer access$200(RoomsTableContentProvider var0) {
      return var0.tableViewer;
   }

   // $VF: synthetic method
   static CustomTableViewer access$300(RoomsTableContentProvider var0) {
      return var0.tableViewer;
   }

   // $VF: synthetic method
   static CustomTableViewer access$400(RoomsTableContentProvider var0) {
      return var0.tableViewer;
   }
}
