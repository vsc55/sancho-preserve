package sancho.view.viewer.filters;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.File;
import sancho.view.viewer.GView;

public class FileExtensionViewerFilter extends AbstractViewerFilter {
   public FileExtensionViewerFilter(GView var1) {
      super(var1);
   }

   public boolean select(Viewer var1, Object var2, Object var3) {
      return var3 instanceof File ? this.isFiltered(((File)var3).getFileType()) : true;
   }
}
