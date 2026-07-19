package sancho.view.friends.clientDirectories;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.view.friends.clientFiles.ClientFilesTableView;
import sancho.view.viewer.table.GTableContentProvider;

public class ClientDirectoriesTableContentProvider extends GTableContentProvider {
   ClientFilesTableView clientFilesTableView;

   public ClientDirectoriesTableContentProvider(ClientDirectoriesTableView view) {
      super(view);
   }

   public Object[] getElements(Object inputElement) {
      if (inputElement instanceof Client) {
         Client client = (Client)inputElement;
         if (client.hasFiles()) {
            return client.getFileDirectories();
         }
      }

      return GTableContentProvider.EMPTY_ARRAY;
   }

   public void setFilesView(ClientFilesTableView view) {
      this.clientFilesTableView = view;
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      if (this.clientFilesTableView != null) {
         this.clientFilesTableView.setInput(null);
      }

      if (newInput != null) {
         Client client = (Client)newInput;
         if (client.hasFiles()) {
            this.clientFilesTableView.setInput(client.getFirstResultMap());
         }
      }

      super.inputChanged(viewer, oldInput, newInput);
   }
}
