package sancho.view.viewer.actions;

import org.eclipse.jface.viewers.ViewerFilter;
import sancho.view.search.result.ResultTableView;
import sancho.view.server.ServerTableView;
import sancho.view.shares.UploadTableView;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;
import sancho.view.viewer.filters.AbstractViewerFilter;

public class RemoveAllFiltersAction extends AbstractFilterAction {
   public RemoveAllFiltersAction(GView gView) {
      super(SResources.getString("mi.noFilters"), 0, gView, null);
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
         ViewerFilter[] filters = this.gView.getFilters();

         for (int i = 0; i < filters.length; i++) {
            if (filters[i] instanceof AbstractViewerFilter) {
               this.gView.removeFilter(filters[i]);
            }
         }
      }
   }
}
