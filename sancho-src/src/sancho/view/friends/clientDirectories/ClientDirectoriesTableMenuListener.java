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

   public ClientDirectoriesTableMenuListener(ClientDirectoriesTableView var1) {
      super(var1);
   }

   public void setDirectoriesView(ClientFilesTableView var1) {
      this.clientFilesTableView = var1;
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(var1, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
      if (this.selectedObjects.size() > 0) {
         Client var2 = (Client)this.gView.getViewer().getInput();
         Map var3 = var2.getClientFilesResultMap(this.selectedObjects.get(0));
         this.clientFilesTableView.setInput(var3);
      } else {
         this.clientFilesTableView.setInput(null);
      }
   }

   public void setFilesView(ClientFilesTableView var1) {
      this.clientFilesTableView = var1;
   }

   public void menuAboutToShow(IMenuManager var1) {
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
