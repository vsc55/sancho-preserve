package sancho.view.friends;

import org.eclipse.jface.viewers.Viewer;
import sancho.utility.MyObservable;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProviderOM;

public class FriendsTableContentProvider extends GTableContentProviderOM {
   private static final String RS_FRIENDS = SResources.getString("l.friends");

   public FriendsTableContentProvider(FriendsTableView var1) {
      super(var1);
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      if (var2 != null) {
         ((MyObservable)var2).deleteObserver(this);
      }

      if (var3 != null) {
         ((MyObservable)var3).addObserver(this);
         this.updateHeaderLabel();
      }
   }

   public void updateHeaderLabel(int var1) {
      this.gView.getViewFrame().updateCLabelText(RS_FRIENDS + ": " + var1);
   }

   public void updateHeaderLabel() {
      this.gView.getViewFrame().updateCLabelText(RS_FRIENDS + ": " + this.tableViewer.getTable().getItemCount());
   }
}
