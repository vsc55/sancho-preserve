package sancho.view.shares;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.SharedFile;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.view.utility.SResources;

public class UploadTableMenuListener$ComputeTorrentAction extends Action {
   // $VF: synthetic field
   private final UploadTableMenuListener this$0;

   public UploadTableMenuListener$ComputeTorrentAction(UploadTableMenuListener var1) {
      super(SResources.getString("l.computeTorrent"));
      this.this$0 = var1;
      this.setImageDescriptor(EnumNetwork.BT.getImageDescriptor());
   }

   public void run() {
      for (int var1 = 0; var1 < UploadTableMenuListener.access$200(this.this$0).size(); var1++) {
         UploadTableMenuListener$ComputeTorrentDialog var2 = new UploadTableMenuListener$ComputeTorrentDialog(
            UploadTableMenuListener.access$300(this.this$0).getShell()
         );
         if (var2.open() == 0) {
            SharedFile var3 = (SharedFile)UploadTableMenuListener.access$400(this.this$0).get(var1);
            var3.computeTorrent(var2.getTracker(), var2.getComment());
         }
      }
   }
}
