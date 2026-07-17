package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Client;
import sancho.view.utility.SResources;

public class GetClientFilesAction extends Action {
   Client[] clientArray;

   public GetClientFilesAction(Client[] var1) {
      super(SResources.getString("mi.getClientFiles"));
      this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
      this.clientArray = var1;
   }

   public void run() {
      for (int var1 = 0; var1 < this.clientArray.length; var1++) {
         if (this.clientArray[var1] != null) {
            this.clientArray[var1].requestClientFiles();
         }
      }
   }
}
