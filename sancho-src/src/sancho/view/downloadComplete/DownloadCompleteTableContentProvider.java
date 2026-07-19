package sancho.view.downloadComplete;

import java.util.List;
import sancho.view.viewer.table.GTableContentProvider;

public class DownloadCompleteTableContentProvider extends GTableContentProvider {
   public DownloadCompleteTableContentProvider(DownloadCompleteTableView view) {
      super(view);
   }

   public Object[] getElements(Object inputElement) {
      return ((List)inputElement).toArray();
   }
}
