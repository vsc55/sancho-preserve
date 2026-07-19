package sancho.view.transfer.fileComments;

import sancho.model.mldonkey.File;
import sancho.view.viewer.table.GTableContentProvider;

public class FileCommentsTableContentProvider extends GTableContentProvider {
   public FileCommentsTableContentProvider(FileCommentsTableView view) {
      super(view);
   }

   public Object[] getElements(Object input) {
      File file = (File)input;
      return file.getFileComments();
   }
}
