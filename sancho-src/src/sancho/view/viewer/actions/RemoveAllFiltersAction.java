package sancho.view.viewer.actions;

import org.eclipse.jface.viewers.ViewerFilter;
import sancho.view.search.result.ResultTableView;
import sancho.view.server.ServerTableView;
import sancho.view.shares.UploadTableView;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;
import sancho.view.viewer.filters.AbstractViewerFilter;

public class RemoveAllFiltersAction extends AbstractFilterAction {
   public RemoveAllFiltersAction(GView var1) {
      super(SResources.getString("mi.noFilters"), 0, var1, null);
   }

   public boolean isChecked() {
      return this.gView != null && this.gView.getFilters().length == 0;
   }

   public AbstractViewerFilter createNewFilter() {
      return null;
   }

   public void run() {
      if (!(this.gView instanceof ResultTableView) && !(this.gView instanceof ServerTableView) && !(this.gView instanceof UploadTableView)) {
         this.gView.resetFilters();
      } else {
         ViewerFilter[] var1 = this.gView.getFilters();

         for (int var2 = 0; var2 < var1.length; var2++) {
            if (var1[var2] instanceof AbstractViewerFilter) {
               this.gView.removeFilter(var1[var2]);
            }
         }
      }
   }
}
