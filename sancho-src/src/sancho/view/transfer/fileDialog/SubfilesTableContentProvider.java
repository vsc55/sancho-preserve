package sancho.view.transfer.fileDialog;

import java.util.List;
import sancho.view.viewer.table.GTableContentProvider;

public class SubfilesTableContentProvider extends GTableContentProvider {
   public SubfilesTableContentProvider(SubfilesTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      return ((List)var1).toArray();
   }
}
