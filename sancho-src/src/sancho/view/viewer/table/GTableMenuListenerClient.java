package sancho.view.viewer.table;

import java.util.ArrayList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.IPreview;
import sancho.view.friends.FriendsTableMenuListener;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;
import sancho.view.viewer.actions.AddClientAsFriendAction;
import sancho.view.viewer.actions.ClientDetailAction;
import sancho.view.viewer.actions.ClientFilesDialogAction;
import sancho.view.viewer.actions.ConnectClientAction;
import sancho.view.viewer.actions.DisconnectClientAction;
import sancho.view.viewer.actions.GetClientFilesAction;
import sancho.view.viewer.actions.TransferAction;

public abstract class GTableMenuListenerClient extends GTableMenuListener {
   public GTableMenuListenerClient(GView gView) {
      super(gView);
   }

   protected void sendToStatusline(String text) {
   }

   public void addPreview(IMenuManager menuManager, IPreview[] previews, int[] subFiles) {
      if (previews != null && previews.length != 0) {
         String[] previewApps = SResources.getPreviewApps(previews[0].getName());
         Program program = previews[0].getOSPreviewApp();
         if (program != null || previewApps != null && previewApps.length > 1) {
            MyMenuManager menu = new MyMenuManager(SResources.getString("m.d.preview"));
            menu.setImageString("preview");
            menu.add(new PreviewAction(previews, subFiles));
            menu.add(new Separator());
            if (program != null) {
               menu.add(new PreviewProgramAction(previews[0], program, subFiles[0]));
               menu.add(new Separator());
            }

            if (previewApps != null && previewApps.length > 1) {
               for (int i = 0; i < previewApps.length; i++) {
                  menu.add(new PreviewAppAction(previews[0], previewApps[i], subFiles[0]));
               }
            }

            menu.add(new Separator());
            menu.add(new TransferAction(this.gView.getShell(), previews, subFiles));
            menuManager.add(menu);
         } else {
            MyMenuManager menu = new MyMenuManager(SResources.getString("m.d.preview"));
            menu.setImageString("preview");
            menu.add(new PreviewAction(previews, subFiles));
            menu.add(new Separator());
            menu.add(new TransferAction(this.gView.getShell(), previews, subFiles));
            menuManager.add(menu);
         }
      }
   }

   private void disconnectSelectedClients() {
      for (int i = 0; i < this.selectedObjects.size(); i++) {
         Client client = (Client)this.selectedObjects.get(i);
         client.disconnect();
      }
   }

   protected void onDeleteKey() {
      this.disconnectSelectedClients();
   }

   protected Client[] createClientArray() {
      Client[] clients = new Client[this.selectedObjects.size()];

      for (int i = 0; i < this.selectedObjects.size(); i++) {
         Client client = (Client)this.selectedObjects.get(i);
         clients[i] = client;
      }

      return clients;
   }

   public void addClientActions(Shell shell, File file, IMenuManager menuManager, Client[] clients) {
      ArrayList connectedClients = new ArrayList();
      ArrayList disconnectedClients = new ArrayList();

      for (int i = 0; i < clients.length; i++) {
         Client client = clients[i];
         if (client.isConnected()) {
            connectedClients.add(client);
         } else {
            disconnectedClients.add(client);
         }
      }

      Client[] connected = new Client[connectedClients.size()];

      for (int j = 0; j < connectedClients.size(); j++) {
         connected[j] = (Client)connectedClients.get(j);
      }

      Client[] disconnected = new Client[disconnectedClients.size()];

      for (int k = 0; k < disconnectedClients.size(); k++) {
         disconnected[k] = (Client)disconnectedClients.get(k);
      }

      menuManager.add(new ClientDetailAction(shell, file, clients[0]));
      if (!(this instanceof FriendsTableMenuListener)) {
         menuManager.add(new AddClientAsFriendAction(clients));
      }

      menuManager.add(new GetClientFilesAction(clients));
      if (clients[0].hasFiles()) {
         menuManager.add(new ClientFilesDialogAction(shell, clients[0]));
      }

      if (connected.length > 0) {
         menuManager.add(new DisconnectClientAction(clients));
      }

      if (disconnected.length > 0) {
         menuManager.add(new ConnectClientAction(clients));
      }
   }

   // Menu action that previews every selected sub-file with its default preview command.
   private class PreviewAction extends Action {
      int[] subFileArray;
      IPreview[] iPreviewArray;

      public PreviewAction(IPreview[] previews, int[] subFiles) {
         super(SResources.getString("m.d.preview"));
         this.setImageDescriptor(SResources.getImageDescriptor("preview"));
         this.iPreviewArray = previews;
         this.subFileArray = subFiles;
      }

      public void run() {
         for (int i = 0; i < this.iPreviewArray.length; i++) {
            for (int j = 0; j < this.subFileArray.length; j++) {
               sendToStatusline(this.iPreviewArray[i].preview(this.subFileArray[j]));
            }
         }
      }
   }

   // Menu action that previews a sub-file with a specific external application.
   private class PreviewAppAction extends Action {
      IPreview iPreview;
      String app;
      int subFileNum;

      public PreviewAppAction(IPreview iPreview, String app, int subFileNum) {
         super(new java.io.File(app).getName());
         this.setImageDescriptor(SResources.getImageDescriptor("preview"));
         this.iPreview = iPreview;
         this.app = app;
         this.subFileNum = subFileNum;
      }

      public void run() {
         sendToStatusline(this.iPreview.preview(this.app, this.subFileNum));
      }
   }

   // Menu action that previews a sub-file with the OS-registered program.
   private class PreviewProgramAction extends Action {
      IPreview iPreview;
      Program program;
      int subFileNum;

      public PreviewProgramAction(IPreview iPreview, Program program, int subFileNum) {
         super(program.getName());
         this.setImageDescriptor(iPreview.getFileTypeImageDescriptor());
         this.iPreview = iPreview;
         this.program = program;
         this.subFileNum = subFileNum;
      }

      public void run() {
         sendToStatusline(this.iPreview.preview(this.program, this.subFileNum));
      }
   }
}
