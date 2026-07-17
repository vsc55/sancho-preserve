package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$CommitAction extends Action {
   private String commitAs;
   private boolean manualInput;
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$CommitAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.commitSelected"));
      this.this$0 = var1;
      this.manualInput = false;
      this.setImageDescriptor(SResources.getImageDescriptor("commit"));
   }

   public DownloadTreeMenuListener$CommitAction(DownloadTreeMenuListener var1, String var2) {
      super(var2);
      this.this$0 = var1;
      this.manualInput = false;
      this.setImageDescriptor(SResources.getImageDescriptor("commit"));
      this.commitAs = var2;
   }

   public DownloadTreeMenuListener$CommitAction(DownloadTreeMenuListener var1, boolean var2) {
      super(SResources.getString("m.d.commitInput"));
      this.this$0 = var1;
      this.manualInput = false;
      this.setImageDescriptor(SResources.getImageDescriptor("commit_question"));
      this.manualInput = var2;
   }

   public void run() {
      if (this.commitAs == null && !this.manualInput) {
         for (int var3 = 0; var3 < DownloadTreeMenuListener.access$1200(this.this$0).size(); var3++) {
            File var4 = (File)DownloadTreeMenuListener.access$1300(this.this$0).get(var3);
            if (var4.getFileStateEnum() == EnumFileState.DOWNLOADED) {
               var4.saveFileAs(var4.getName());
            }
         }
      } else if (this.manualInput) {
         if (DownloadTreeMenuListener.access$000(this.this$0) == null) {
            return;
         }

         InputDialog var1 = new InputDialog(
            DownloadTreeMenuListener.access$1400(this.this$0).getShell(),
            SResources.getString("m.d.commitAs"),
            SResources.getString("m.d.commitAs"),
            DownloadTreeMenuListener.access$000(this.this$0).getName(),
            null
         );
         if (var1.open() == 0) {
            String var2 = var1.getValue();
            if (!var2.equals("") && DownloadTreeMenuListener.access$000(this.this$0) != null) {
               DownloadTreeMenuListener.access$000(this.this$0).saveFileAs(var2);
            }
         }
      } else if (DownloadTreeMenuListener.access$000(this.this$0) != null) {
         DownloadTreeMenuListener.access$000(this.this$0).saveFileAs(this.commitAs);
      }
   }
}
