package sancho.view.transfer.fileDialog;

import java.util.List;
import sancho.view.viewer.table.GTableContentProvider;

public class SubfilesTableContentProvider extends GTableContentProvider {
   public SubfilesTableContentProvider(SubfilesTableView view) {
      super(view);
   }

   public Object[] getElements(Object input) {
      return ((List)input).toArray();
   }
}
