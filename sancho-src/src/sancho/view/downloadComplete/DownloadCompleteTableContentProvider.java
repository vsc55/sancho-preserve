package sancho.view.downloadComplete;

import java.util.List;
import sancho.view.viewer.table.GTableContentProvider;

public class DownloadCompleteTableContentProvider extends GTableContentProvider {
   public DownloadCompleteTableContentProvider(DownloadCompleteTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      return ((List)var1).toArray();
   }
}
