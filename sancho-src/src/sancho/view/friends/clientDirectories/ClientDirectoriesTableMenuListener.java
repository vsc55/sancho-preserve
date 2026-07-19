package sancho.view.friends.clientDirectories;

import java.util.Map;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.Client;
import sancho.view.friends.clientFiles.ClientFilesTableView;
import sancho.view.viewer.table.GTableMenuListener;

public class ClientDirectoriesTableMenuListener extends GTableMenuListener {
   private ClientFilesTableView clientFilesTableView;
   // $VF: synthetic field
   static Class class$java$lang$String;

   public ClientDirectoriesTableMenuListener(ClientDirectoriesTableView view) {
      super(view);
   }

   public void setDirectoriesView(ClientFilesTableView view) {
      this.clientFilesTableView = view;
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(event, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
      if (this.selectedObjects.size() > 0) {
         Client client = (Client)this.gView.getViewer().getInput();
         Map resultMap = client.getClientFilesResultMap(this.selectedObjects.get(0));
         this.clientFilesTableView.setInput(resultMap);
      } else {
         this.clientFilesTableView.setInput(null);
      }
   }

   public void setFilesView(ClientFilesTableView view) {
      this.clientFilesTableView = view;
   }

   public void menuAboutToShow(IMenuManager menuManager) {
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }
}
