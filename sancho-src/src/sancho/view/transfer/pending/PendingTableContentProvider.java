package sancho.view.transfer.pending;

import org.eclipse.jface.viewers.Viewer;
import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProviderOM;

public class PendingTableContentProvider extends GTableContentProviderOM {
   public static final String RS_PENDING = SResources.getString("l.pending");
   public static final String RS_DISABLED = SResources.getString("l.disabled");

   public PendingTableContentProvider(PendingTableView view) {
      super(view);
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      if (oldInput != null) {
         ((MyObservable)oldInput).deleteObserver(this);
      }

      if (newInput != null) {
         ObjectMap objectMap = (ObjectMap)newInput;
         objectMap.addObserver(this);
         if (!PreferenceLoader.loadBoolean("pollPending")) {
            this.updateHeaderLabel();
         } else {
            this.updateHeaderLabel(objectMap.size());
         }
      } else {
         this.updateHeaderLabel();
      }
   }

   protected void updateHeaderLabel(int count) {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.gView.getViewFrame().updateCLabelText(RS_PENDING + ": " + count);
      }
   }

   protected void updateHeaderLabel() {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.gView.getViewFrame().updateCLabelText(RS_PENDING + ": " + RS_DISABLED);
      }
   }
}
