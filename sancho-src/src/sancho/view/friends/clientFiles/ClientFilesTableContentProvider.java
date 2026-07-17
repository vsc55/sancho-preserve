package sancho.view.friends.clientFiles;

import java.util.Map;
import org.eclipse.jface.viewers.Viewer;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProvider;

public class ClientFilesTableContentProvider extends GTableContentProvider {
   public static final String RS_CLIENT_FILES = SResources.getString("l.clientFiles");

   public ClientFilesTableContentProvider(ClientFilesTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      if (var1 instanceof Map) {
         Map var2 = (Map)var1;
         this.updateHeaderLabel(var2.size());
         return var2.keySet().toArray();
      } else {
         return GTableContentProvider.EMPTY_ARRAY;
      }
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      this.updateHeaderLabel();
   }

   public void updateHeaderLabel(int var1) {
      this.gView.getViewFrame().updateCLabelText(RS_CLIENT_FILES + ": " + var1);
   }

   public void updateHeaderLabel() {
      this.gView.getViewFrame().updateCLabelText(RS_CLIENT_FILES);
   }
}
