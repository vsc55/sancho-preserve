package sancho.view.friends.clientDirectories;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.view.friends.clientFiles.ClientFilesTableView;
import sancho.view.viewer.table.GTableContentProvider;

public class ClientDirectoriesTableContentProvider extends GTableContentProvider {
   ClientFilesTableView clientFilesTableView;

   public ClientDirectoriesTableContentProvider(ClientDirectoriesTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      if (var1 instanceof Client) {
         Client var2 = (Client)var1;
         if (var2.hasFiles()) {
            return var2.getFileDirectories();
         }
      }

      return GTableContentProvider.EMPTY_ARRAY;
   }

   public void setFilesView(ClientFilesTableView var1) {
      this.clientFilesTableView = var1;
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      if (this.clientFilesTableView != null) {
         this.clientFilesTableView.setInput(null);
      }

      if (var3 != null) {
         Client var4 = (Client)var3;
         if (var4.hasFiles()) {
            this.clientFilesTableView.setInput(var4.getFirstResultMap());
         }
      }

      super.inputChanged(var1, var2, var3);
   }
}
