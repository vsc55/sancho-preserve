package sancho.view.friends;

import org.eclipse.jface.viewers.Viewer;
import sancho.utility.MyObservable;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProviderOM;

public class FriendsTableContentProvider extends GTableContentProviderOM {
   private static final String RS_FRIENDS = SResources.getString("l.friends");

   public FriendsTableContentProvider(FriendsTableView view) {
      super(view);
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      if (oldInput != null) {
         ((MyObservable)oldInput).deleteObserver(this);
      }

      if (newInput != null) {
         ((MyObservable)newInput).addObserver(this);
         this.updateHeaderLabel();
      }
   }

   public void updateHeaderLabel(int count) {
      this.gView.getViewFrame().updateCLabelText(RS_FRIENDS + ": " + count);
   }

   public void updateHeaderLabel() {
      this.gView.getViewFrame().updateCLabelText(RS_FRIENDS + ": " + this.tableViewer.getTable().getItemCount());
   }
}
