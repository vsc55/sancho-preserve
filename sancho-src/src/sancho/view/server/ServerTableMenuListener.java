package sancho.view.server;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.core.Sancho;
import sancho.model.mldonkey.Option;
import sancho.model.mldonkey.Server;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.view.MainWindow;
import sancho.view.server.users.ServerUsersTableView;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableMenuListener;

public class ServerTableMenuListener extends GTableMenuListener implements IDoubleClickListener {
   private ServerUsersTableView serverUsersTableView;
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Server;

   public void doubleClick(DoubleClickEvent event) {
      IStructuredSelection selection = (IStructuredSelection)event.getSelection();
      Object element = selection.getFirstElement();
      if (element instanceof Server) {
         Server server = (Server)element;
         if (server.isConnected()) {
            server.disconnect();
         } else {
            server.connect();
         }
      }
   }

   public ServerTableMenuListener(ServerTableView tableView) {
      super(tableView);
   }

   public void menuAboutToShow(IMenuManager manager) {
      if (this.selectedObjects.size() > 0 && ((Server)this.selectedObjects.get(0)).isConnected()) {
         manager.add(new DisconnectAction());
      }

      if (this.selectedObjects.size() > 0 && ((Server)this.selectedObjects.get(0)).getStateEnum() == EnumHostState.NOT_CONNECTED) {
         boolean canConnect = true;
         Server server = (Server)this.selectedObjects.get(0);
         if (!server.isPreferred()) {
            EnumNetwork network = server.getEnumNetwork();
            if (network == EnumNetwork.DONKEY) {
               String prefix = network.getDefaultOptionPrefix();
               String optionName = "connect_only_preferred_server";

               try {
                  Option option = (Option)this.gView.getCore().getCollectionFactory().getOptionCollection().get(prefix + optionName);
                  if (option.getValue().equalsIgnoreCase("true")) {
                     canConnect = false;
                  }
               } catch (Exception exception) {
               }
            }
         }

         if (canConnect) {
            manager.add(new ConnectAction());
         }
      }

      if (this.selectedObjects.size() > 0) {
         manager.add(new ConnectMoreAction());
         manager.add(new GetServerUsersAction());
         manager.add(new CopyServerLink());
         manager.add(new RemoveServerAction());
         manager.add(new BlackListAction());
         if (this.gView.getCore() != null && this.gView.getCore().getProtocol() >= 32) {
            manager.add(new RenameAction());
         }

         if (this.gView.getCore() != null && this.gView.getCore().getProtocol() >= 28) {
            manager.add(new PreferredServerAction());
         }

         this.addSelectAllMenu(manager);
      }
   }

   private void removeSelectedServers() {
      for (int i = 0; i < this.selectedObjects.size(); i++) {
         ((Server)this.selectedObjects.get(i)).remove();
      }
   }

   private void renameSelectedServers() {
      for (int i = 0; i < this.selectedObjects.size(); i++) {
         Server server = (Server)this.selectedObjects.get(i);
         InputDialog dialog = new InputDialog(this.gView.getShell(), SResources.getString("m.d.rename"), SResources.getString("m.d.rename"), server.getName(), null);
         if (dialog.open() != 0) {
            break;
         }

         String name = dialog.getValue();
         if (!name.equals("")) {
            server.rename(name);
         }
      }
   }

   protected void onDeleteKey() {
      this.removeSelectedServers();
   }

   protected void onF2Key() {
      this.renameSelectedServers();
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$Server == null
            ? (class$sancho$model$mldonkey$Server = class$("sancho.model.mldonkey.Server"))
            : class$sancho$model$mldonkey$Server
      );
      if (this.selectedObjects.size() > 0) {
         this.serverUsersTableView.setInput((Server)this.selectedObjects.get(0));
      }
   }

   public void setServerUsersTableView(ServerUsersTableView serverUsersTableView) {
      this.serverUsersTableView = serverUsersTableView;
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }

   // Menu action: blacklists each selected server.
   private class BlackListAction extends Action {
      public BlackListAction() {
         super(SResources.getString("m.srv.blacklist"));
         this.setImageDescriptor(SResources.getImageDescriptor("gun"));
      }

      public void run() {
         for (int i = 0; i < selectedObjects.size(); i++) {
            ((Server)selectedObjects.get(i)).blacklist();
         }
      }
   }

   // Menu action: connects to each selected server.
   private class ConnectAction extends Action {
      public ConnectAction() {
         super(SResources.getString("m.srv.connect"));
         this.setImageDescriptor(SResources.getImageDescriptor("menu-connect"));
      }

      public void run() {
         if (Sancho.hasCollectionFactory()) {
            for (int i = 0; i < selectedObjects.size(); i++) {
               ((Server)selectedObjects.get(i)).connect();
            }
         }
      }
   }

   // Menu action: asks the core to connect to more servers.
   private class ConnectMoreAction extends Action {
      public ConnectMoreAction() {
         super(SResources.getString("m.srv.connectMore"));
         this.setImageDescriptor(SResources.getImageDescriptor("plus"));
      }

      public void run() {
         if (Sancho.hasCollectionFactory()) {
            gView.getCore().getServerCollection().connectMore();
         }
      }
   }

   // Menu action: copies the selected servers' links to the clipboard.
   private class CopyServerLink extends Action {
      public CopyServerLink() {
         super(SResources.getString("m.srv.copyTo"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         String text = "";
         String separator = System.getProperty("line.separator");

         for (int i = 0; i < selectedObjects.size(); i++) {
            Server server = (Server)selectedObjects.get(i);
            if (text.length() > 0) {
               text = text + separator;
            }

            text = text + server.getLink();
         }

         MainWindow.copyToClipboard(text);
      }
   }

   // Menu action: disconnects each selected server.
   private class DisconnectAction extends Action {
      public DisconnectAction() {
         super(SResources.getString("m.srv.disconnect"));
         this.setImageDescriptor(SResources.getImageDescriptor("menu-disconnect"));
      }

      public void run() {
         for (int i = 0; i < selectedObjects.size(); i++) {
            ((Server)selectedObjects.get(i)).disconnect();
         }
      }
   }

   // Menu action: requests the user list for each selected server.
   private class GetServerUsersAction extends Action {
      public GetServerUsersAction() {
         super(SResources.getString("m.srv.getServerUsers"));
         this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
      }

      public void run() {
         for (int i = 0; i < selectedObjects.size(); i++) {
            ((Server)selectedObjects.get(i)).getServerUsers();
         }
      }
   }

   // Menu action: toggles the preferred flag on each selected server.
   private class PreferredServerAction extends Action {
      public PreferredServerAction() {
         super(SResources.getString("m.srv.preferredServer"));
         this.setImageDescriptor(SResources.getImageDescriptor("heart"));
      }

      public void run() {
         for (int i = 0; i < selectedObjects.size(); i++) {
            ((Server)selectedObjects.get(i)).togglePreferred();
         }
      }
   }

   // Menu action: removes each selected server.
   private class RemoveServerAction extends Action {
      public RemoveServerAction() {
         super(SResources.getString("m.srv.removeServer"));
         this.setImageDescriptor(SResources.getImageDescriptor("minus"));
      }

      public void run() {
         removeSelectedServers();
      }
   }

   // Menu action: renames each selected server via an input dialog.
   private class RenameAction extends Action {
      public RenameAction() {
         super(SResources.getString("m.srv.rename"));
         this.setImageDescriptor(SResources.getImageDescriptor("commit_question"));
      }

      public void run() {
         renameSelectedServers();
      }
   }
}
