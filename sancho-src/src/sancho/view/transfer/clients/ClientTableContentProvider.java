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

   public ClientTableContentProvider(ClientTableView view, CLabel label) {
      super(view);
      this.updateOnUpdate = true;
   }

   public Object[] getElements(Object element) {
      if (element instanceof File) {
         File file = (File)element;
         ObjectMap clientMap = file.getClientWeakMap();
         synchronized (clientMap) {
            clientMap.clearAllLists();
            return clientMap.getKeyArray();
         }
      } else {
         return GTableContentProvider.EMPTY_ARRAY;
      }
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      this.inputElementFile = null;
      if (oldInput != null && oldInput instanceof File) {
         File oldFile = (File)oldInput;
         oldFile.getClientWeakMap().deleteObserver(this);
      }

      if (newInput != null && newInput instanceof File) {
         File newFile = (File)newInput;
         this.inputElementFile = newFile;
         newFile.getClientWeakMap().addObserver(this);
         this.updateHeaderLabel(newFile.getClientWeakMap().size());
      } else if (newInput == null) {
         this.updateHeaderLabel();
      }
   }

   public void updateHeaderLabel(int count) {
      if (this.inputElementFile != null) {
         this.gView.getViewFrame().updateCLabelText(RS_CLIENTS + ": " + this.inputElementFile.getConnected() + " / " + count + " " + RS_CONNECTED);
      }
   }

   public void updateHeaderLabel() {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.gView.getViewFrame().updateCLabelText(RS_CLIENTS);
      }
   }
}
