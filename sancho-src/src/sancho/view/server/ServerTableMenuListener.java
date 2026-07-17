package sancho.view.server;

import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.Option;
import sancho.model.mldonkey.Server;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.view.server.users.ServerUsersTableView;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;
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
         var1.add(new ServerTableMenuListener$DisconnectAction(this));
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
            var1.add(new ServerTableMenuListener$ConnectAction(this));
         }
      }

      if (this.selectedObjects.size() > 0) {
         var1.add(new ServerTableMenuListener$ConnectMoreAction(this));
         var1.add(new ServerTableMenuListener$GetServerUsersAction(this));
         var1.add(new ServerTableMenuListener$CopyServerLink(this));
         var1.add(new ServerTableMenuListener$RemoveServerAction(this));
         var1.add(new ServerTableMenuListener$BlackListAction(this));
         if (this.gView.getCore() != null && this.gView.getCore().getProtocol() >= 32) {
            var1.add(new ServerTableMenuListener$RenameAction(this));
         }

         if (this.gView.getCore() != null && this.gView.getCore().getProtocol() >= 28) {
            var1.add(new ServerTableMenuListener$PreferredServerAction(this));
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

   // $VF: synthetic method
   static List access$000(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$100(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$200(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$300(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$400(ServerTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$500(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$600(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static void access$700(ServerTableMenuListener var0) {
      var0.renameSelectedServers();
   }

   // $VF: synthetic method
   static List access$800(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$900(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1000(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1100(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1200(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1300(ServerTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static void access$1400(ServerTableMenuListener var0) {
      var0.removeSelectedServers();
   }
}
