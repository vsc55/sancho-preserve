package sancho.view.viewer.filters;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.File;
import sancho.view.viewer.GView;

public class FileExtensionViewerFilter extends AbstractViewerFilter {
   public FileExtensionViewerFilter(GView gView) {
      super(gView);
   }

   public boolean select(Viewer viewer, Object parentElement, Object element) {
      return element instanceof File ? this.isFiltered(((File)element).getFileType()) : true;
   }
}
