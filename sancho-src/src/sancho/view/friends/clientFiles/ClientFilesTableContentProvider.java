package sancho.view.friends.clientFiles;

import java.util.Map;
import org.eclipse.jface.viewers.Viewer;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProvider;

public class ClientFilesTableContentProvider extends GTableContentProvider {
   public static final String RS_CLIENT_FILES = SResources.getString("l.clientFiles");

   public ClientFilesTableContentProvider(ClientFilesTableView view) {
      super(view);
   }

   public Object[] getElements(Object input) {
      if (input instanceof Map) {
         Map map = (Map)input;
         this.updateHeaderLabel(map.size());
         return map.keySet().toArray();
      } else {
         return GTableContentProvider.EMPTY_ARRAY;
      }
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      this.updateHeaderLabel();
   }

   public void updateHeaderLabel(int count) {
      this.gView.getViewFrame().updateCLabelText(RS_CLIENT_FILES + ": " + count);
   }

   public void updateHeaderLabel() {
      this.gView.getViewFrame().updateCLabelText(RS_CLIENT_FILES);
   }
}
