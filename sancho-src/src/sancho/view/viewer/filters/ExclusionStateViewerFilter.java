package sancho.view.viewer.filters;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Result;
import sancho.model.mldonkey.Server;
import sancho.view.viewer.GView;

public class ExclusionStateViewerFilter extends AbstractViewerFilter {
   public ExclusionStateViewerFilter(GView var1) {
      super(var1);
   }

   public boolean select(Viewer var1, Object var2, Object var3) {
      if (var3 instanceof Server) {
         return !this.isFiltered(((Server)var3).getStateEnum());
      } else if (var3 instanceof File) {
         return !this.isFiltered(((File)var3).getFileStateEnum());
      } else if (var3 instanceof Client) {
         return !this.isFiltered(((Client)var3).getStateEnum());
      } else {
         return var3 instanceof Result ? !this.isFiltered(((Result)var3).getRating()) : true;
      }
   }

   public boolean isFilterProperty(Object var1, String var2) {
      return var1 instanceof File ? ((File)var1).hasChangedBit(128) : true;
   }
}
