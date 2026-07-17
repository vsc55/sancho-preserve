package sancho.view.transfer.clients;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.CLabel;
import sancho.model.mldonkey.File;
import sancho.utility.ObjectMap;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProvider;
import sancho.view.viewer.table.GTableContentProviderOM;

public class ClientTableContentProvider extends GTableContentProviderOM {
   private static final String RS_CLIENTS = SResources.getString("l.clients");
   private static final String RS_CONNECTED = SResources.getString("l.connected");
   private File inputElementFile;

   public ClientTableContentProvider(ClientTableView var1, CLabel var2) {
      super(var1);
      this.updateOnUpdate = true;
   }

   public Object[] getElements(Object var1) {
      if (var1 instanceof File) {
         File var2 = (File)var1;
         ObjectMap var3 = var2.getClientWeakMap();
         synchronized (var3) {
            var3.clearAllLists();
            return var3.getKeyArray();
         }
      } else {
         return GTableContentProvider.EMPTY_ARRAY;
      }
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      this.inputElementFile = null;
      if (var2 != null && var2 instanceof File) {
         File var4 = (File)var2;
         var4.getClientWeakMap().deleteObserver(this);
      }

      if (var3 != null && var3 instanceof File) {
         File var5 = (File)var3;
         this.inputElementFile = var5;
         var5.getClientWeakMap().addObserver(this);
         this.updateHeaderLabel(var5.getClientWeakMap().size());
      } else if (var3 == null) {
         this.updateHeaderLabel();
      }
   }

   public void updateHeaderLabel(int var1) {
      if (this.inputElementFile != null) {
         this.gView.getViewFrame().updateCLabelText(RS_CLIENTS + ": " + this.inputElementFile.getConnected() + " / " + var1 + " " + RS_CONNECTED);
      }
   }

   public void updateHeaderLabel() {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.gView.getViewFrame().updateCLabelText(RS_CLIENTS);
      }
   }
}
