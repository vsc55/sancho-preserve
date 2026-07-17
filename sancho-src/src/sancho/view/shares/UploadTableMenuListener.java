package sancho.view.shares;

import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.core.Sancho;
import sancho.model.mldonkey.IPreview;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.SharedFile;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.view.viewer.GView;
import sancho.view.viewer.table.GTableMenuListenerClient;
import sancho.view.viewer.table.GTableView;

public class UploadTableMenuListener extends GTableMenuListenerClient {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$SharedFile;

   public UploadTableMenuListener(GTableView var1) {
      super(var1);
   }

   protected void onDeleteKey() {
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$SharedFile == null
            ? (class$sancho$model$mldonkey$SharedFile = class$("sancho.model.mldonkey.SharedFile"))
            : class$sancho$model$mldonkey$SharedFile
      );
   }

   protected void sendToStatusline(String var1) {
      if (var1 != null) {
         this.gView.getViewFrame().getATab().getMainWindow().getStatusline().setText(var1);
      }
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         SharedFile var2 = (SharedFile)this.selectedObjects.get(0);
         if (var2.getSubFileNames() != null) {
            var1.add(new UploadTableMenuListener$SharedFileDetailAction(this));
         }

         if (Sancho.hasCollectionFactory() && Sancho.getCore().getProtocol() >= 34) {
            Network var3 = Sancho.getCore().getNetworkCollection().getByEnum(EnumNetwork.BT);
            if (var3 != null) {
               var1.add(new UploadTableMenuListener$ComputeTorrentAction(this));
            }
         }

         IPreview[] var6 = new IPreview[this.selectedObjects.size()];
         int[] var4 = new int[this.selectedObjects.size()];

         for (int var5 = 0; var5 < this.selectedObjects.size(); var5++) {
            var6[var5] = (IPreview)this.selectedObjects.get(var5);
            var4[var5] = -1;
         }

         this.addPreview(var1, var6, var4);
         this.addClipboardMenu(var1);
         this.addWebServicesMenu(var1, var2.getMD4(), var2.getED2K(), var2.getSize());
         this.addSelectAllMenu(var1);
      }
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
   static List access$000(UploadTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$100(UploadTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$200(UploadTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$300(UploadTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$400(UploadTableMenuListener var0) {
      return var0.selectedObjects;
   }
}
