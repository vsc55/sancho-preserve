package sancho.view.viewer.table;

import java.util.ArrayList;
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
   public GTableMenuListenerClient(GView var1) {
      super(var1);
   }

   protected void sendToStatusline(String var1) {
   }

   public void addPreview(IMenuManager var1, IPreview[] var2, int[] var3) {
      if (var2 != null && var2.length != 0) {
         String[] var4 = SResources.getPreviewApps(var2[0].getName());
         Program var5 = var2[0].getOSPreviewApp();
         if (var5 != null || var4 != null && var4.length > 1) {
            MyMenuManager var8 = new MyMenuManager(SResources.getString("m.d.preview"));
            var8.setImageString("preview");
            var8.add(new GTableMenuListenerClient$PreviewAction(this, var2, var3));
            var8.add(new Separator());
            if (var5 != null) {
               var8.add(new GTableMenuListenerClient$PreviewProgramAction(this, var2[0], var5, var3[0]));
               var8.add(new Separator());
            }

            if (var4 != null && var4.length > 1) {
               for (int var7 = 0; var7 < var4.length; var7++) {
                  var8.add(new GTableMenuListenerClient$PreviewAppAction(this, var2[0], var4[var7], var3[0]));
               }
            }

            var8.add(new Separator());
            var8.add(new TransferAction(this.gView.getShell(), var2, var3));
            var1.add(var8);
         } else {
            MyMenuManager var6 = new MyMenuManager(SResources.getString("m.d.preview"));
            var6.setImageString("preview");
            var6.add(new GTableMenuListenerClient$PreviewAction(this, var2, var3));
            var6.add(new Separator());
            var6.add(new TransferAction(this.gView.getShell(), var2, var3));
            var1.add(var6);
         }
      }
   }

   private void disconnectSelectedClients() {
      for (int var1 = 0; var1 < this.selectedObjects.size(); var1++) {
         Client var2 = (Client)this.selectedObjects.get(var1);
         var2.disconnect();
      }
   }

   protected void onDeleteKey() {
      this.disconnectSelectedClients();
   }

   protected Client[] createClientArray() {
      Client[] var1 = new Client[this.selectedObjects.size()];

      for (int var2 = 0; var2 < this.selectedObjects.size(); var2++) {
         Client var3 = (Client)this.selectedObjects.get(var2);
         var1[var2] = var3;
      }

      return var1;
   }

   public void addClientActions(Shell var1, File var2, IMenuManager var3, Client[] var4) {
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 0; var7 < var4.length; var7++) {
         Client var8 = var4[var7];
         if (var8.isConnected()) {
            var5.add(var8);
         } else {
            var6.add(var8);
         }
      }

      Client[] var12 = new Client[var5.size()];

      for (int var9 = 0; var9 < var5.size(); var9++) {
         var12[var9] = (Client)var5.get(var9);
      }

      Client[] var10 = new Client[var6.size()];

      for (int var11 = 0; var11 < var6.size(); var11++) {
         var10[var11] = (Client)var6.get(var11);
      }

      var3.add(new ClientDetailAction(var1, var2, var4[0]));
      if (!(this instanceof FriendsTableMenuListener)) {
         var3.add(new AddClientAsFriendAction(var4));
      }

      var3.add(new GetClientFilesAction(var4));
      if (var4[0].hasFiles()) {
         var3.add(new ClientFilesDialogAction(var1, var4[0]));
      }

      if (var12.length > 0) {
         var3.add(new DisconnectClientAction(var4));
      }

      if (var10.length > 0) {
         var3.add(new ConnectClientAction(var4));
      }
   }
}
