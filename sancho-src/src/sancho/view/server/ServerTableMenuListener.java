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

   public void doubleClick(DoubleClickEvent var1) {
      IStructuredSelection var2 = (IStructuredSelection)var1.getSelection();
      Object var3 = var2.getFirstElement();
      if (var3 instanceof Server) {
         Server var4 = (Server)var3;
         if (var4.isConnected()) {
            var4.disconnect();
         } else {
            var4.connect();
         }
      }
   }

   public ServerTableMenuListener(ServerTableView var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0 && ((Server)this.selectedObjects.get(0)).isConnected()) {
         var1.add(new DisconnectAction());
      }

      if (this.selectedObjects.size() > 0 && ((Server)this.selectedObjects.get(0)).getStateEnum() == EnumHostState.NOT_CONNECTED) {
         boolean var2 = true;
         Server var3 = (Server)this.selectedObjects.get(0);
         if (!var3.isPreferred()) {
            EnumNetwork var4 = var3.getEnumNetwork();
            if (var4 == EnumNetwork.DONKEY) {
               String var5 = var4.getDefaultOptionPrefix();
               String var6 = "connect_only_preferred_server";

               try {
                  Option var7 = (Option)this.gView.getCore().getCollectionFactory().getOptionCollection().get(var5 + var6);
                  if (var7.getValue().equalsIgnoreCase("true")) {
                     var2 = false;
                  }
               } catch (Exception var8) {
               }
            }
         }

         if (var2) {
            var1.add(new ConnectAction());
         }
      }

      if (this.selectedObjects.size() > 0) {
         var1.add(new ConnectMoreAction());
         var1.add(new GetServerUsersAction());
         var1.add(new CopyServerLink());
         var1.add(new RemoveServerAction());
         var1.add(new BlackListAction());
         if (this.gView.getCore() != null && this.gView.getCore().getProtocol() >= 32) {
            var1.add(new RenameAction());
         }

         if (this.gView.getCore() != null && this.gView.getCore().getProtocol() >= 28) {
            var1.add(new PreferredServerAction());
         }

         this.addSelectAllMenu(var1);
      }
   }

   private void removeSelectedServers() {
      for (int var1 = 0; var1 < this.selectedObjects.size(); var1++) {
         ((Server)this.selectedObjects.get(var1)).remove();
      }
   }

   private void renameSelectedServers() {
      for (int var1 = 0; var1 < this.selectedObjects.size(); var1++) {
         Server var2 = (Server)this.selectedObjects.get(var1);
         InputDialog var3 = new InputDialog(this.gView.getShell(), SResources.getString("m.d.rename"), SResources.getString("m.d.rename"), var2.getName(), null);
         if (var3.open() != 0) {
            break;
         }

         String var4 = var3.getValue();
         if (!var4.equals("")) {
            var2.rename(var4);
         }
      }
   }

   protected void onDeleteKey() {
      this.removeSelectedServers();
   }

   protected void onF2Key() {
      this.renameSelectedServers();
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$Server == null
            ? (class$sancho$model$mldonkey$Server = class$("sancho.model.mldonkey.Server"))
            : class$sancho$model$mldonkey$Server
      );
      if (this.selectedObjects.size() > 0) {
         this.serverUsersTableView.setInput((Server)this.selectedObjects.get(0));
      }
   }

   public void setServerUsersTableView(ServerUsersTableView var1) {
      this.serverUsersTableView = var1;
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   // Menu action: blacklists each selected server.
   private class BlackListAction extends Action {
      public BlackListAction() {
         super(SResources.getString("m.srv.blacklist"));
         this.setImageDescriptor(SResources.getImageDescriptor("gun"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            ((Server)selectedObjects.get(var1)).blacklist();
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
            for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
               ((Server)selectedObjects.get(var1)).connect();
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
         String var1 = "";
         String var2 = System.getProperty("line.separator");

         for (int var3 = 0; var3 < selectedObjects.size(); var3++) {
            Server var4 = (Server)selectedObjects.get(var3);
            if (var1.length() > 0) {
               var1 = var1 + var2;
            }

            var1 = var1 + var4.getLink();
         }

         MainWindow.copyToClipboard(var1);
      }
   }

   // Menu action: disconnects each selected server.
   private class DisconnectAction extends Action {
      public DisconnectAction() {
         super(SResources.getString("m.srv.disconnect"));
         this.setImageDescriptor(SResources.getImageDescriptor("menu-disconnect"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            ((Server)selectedObjects.get(var1)).disconnect();
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
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            ((Server)selectedObjects.get(var1)).getServerUsers();
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
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            ((Server)selectedObjects.get(var1)).togglePreferred();
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
