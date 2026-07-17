package sancho.view.viewer.filters;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Result;
import sancho.model.mldonkey.Room;
import sancho.model.mldonkey.Server;
import sancho.view.viewer.GView;

public class NetworkViewerFilter extends AbstractViewerFilter {
   public NetworkViewerFilter(GView var1) {
      super(var1);
   }

   public boolean select(Viewer var1, Object var2, Object var3) {
      if (var3 instanceof Server) {
         return this.isFiltered(((Server)var3).getEnumNetwork());
      } else if (var3 instanceof Result) {
         return this.isFiltered(((Result)var3).getEnumNetwork());
      } else if (var3 instanceof File) {
         return this.isFiltered(((File)var3).getEnumNetwork());
      } else if (var3 instanceof Client) {
         return this.isFiltered(((Client)var3).getEnumNetwork());
      } else {
         return var3 instanceof Room ? this.isFiltered(((Room)var3).getEnumNetwork()) : true;
      }
   }
}
