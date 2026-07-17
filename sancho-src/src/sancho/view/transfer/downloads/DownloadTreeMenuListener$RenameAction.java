package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$RenameAction extends Action {
   private String renameAs;
   private boolean manualInput;
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$RenameAction(DownloadTreeMenuListener var1, String var2) {
      super(var2);
      this.this$0 = var1;
      this.manualInput = false;
      this.setImageDescriptor(DownloadTreeMenuListener.access$000(var1).getFileTypeImageDescriptor());
      this.renameAs = var2;
   }

   public DownloadTreeMenuListener$RenameAction(DownloadTreeMenuListener var1, boolean var2) {
      super(SResources.getString("m.d.commitInput"));
      this.this$0 = var1;
      this.manualInput = false;
      this.setImageDescriptor(SResources.getImageDescriptor("commit_question"));
      this.manualInput = var2;
   }

   public void run() {
      if (this.manualInput) {
         DownloadTreeMenuListener.access$1500(this.this$0);
      } else if (DownloadTreeMenuListener.access$000(this.this$0) != null) {
         DownloadTreeMenuListener.access$000(this.this$0).rename(this.renameAs);
      }
   }
}
