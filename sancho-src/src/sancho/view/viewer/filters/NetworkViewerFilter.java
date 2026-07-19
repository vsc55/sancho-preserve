package sancho.view.viewer.filters;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Result;
import sancho.model.mldonkey.Room;
import sancho.model.mldonkey.Server;
import sancho.view.viewer.GView;

public class NetworkViewerFilter extends AbstractViewerFilter {
   public NetworkViewerFilter(GView gView) {
      super(gView);
   }

   public boolean select(Viewer viewer, Object parentElement, Object element) {
      if (element instanceof Server) {
         return this.isFiltered(((Server)element).getEnumNetwork());
      } else if (element instanceof Result) {
         return this.isFiltered(((Result)element).getEnumNetwork());
      } else if (element instanceof File) {
         return this.isFiltered(((File)element).getEnumNetwork());
      } else if (element instanceof Client) {
         return this.isFiltered(((Client)element).getEnumNetwork());
      } else {
         return element instanceof Room ? this.isFiltered(((Room)element).getEnumNetwork()) : true;
      }
   }
}
