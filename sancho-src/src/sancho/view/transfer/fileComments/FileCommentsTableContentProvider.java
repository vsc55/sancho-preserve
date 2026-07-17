package sancho.view.transfer.fileComments;

import sancho.model.mldonkey.File;
import sancho.view.viewer.table.GTableContentProvider;

public class FileCommentsTableContentProvider extends GTableContentProvider {
   public FileCommentsTableContentProvider(FileCommentsTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      File var2 = (File)var1;
      return var2.getFileComments();
   }
}
