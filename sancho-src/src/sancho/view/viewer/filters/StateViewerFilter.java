package sancho.view.viewer.filters;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Result;
import sancho.model.mldonkey.Room;
import sancho.model.mldonkey.Server;
import sancho.view.viewer.GView;

public class StateViewerFilter extends AbstractViewerFilter {
   public StateViewerFilter(GView gView) {
      super(gView);
   }

   public boolean select(Viewer viewer, Object parentElement, Object element) {
      if (element instanceof Server) {
         return this.isFiltered(((Server)element).getStateEnum());
      } else if (element instanceof File) {
         return this.isFiltered(((File)element).getFileStateEnum());
      } else if (element instanceof Client) {
         return this.isFiltered(((Client)element).getStateEnum());
      } else if (element instanceof Result) {
         return this.isFiltered(((Result)element).getRating());
      } else {
         return element instanceof Room ? this.isFiltered(((Room)element).getRoomState()) : true;
      }
   }
}
