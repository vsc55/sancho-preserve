package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$CustomPriorityAction extends Action {
   private boolean relative;
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$CustomPriorityAction(DownloadTreeMenuListener var1, boolean var2) {
      super("", 2);
      this.this$0 = var1;
      this.relative = var2;
      if (var2) {
         this.setText(SResources.getString("m.d.priorityRelative"));
      } else {
         this.setText(SResources.getString("m.d.priorityAbsolute"));
      }
   }

   public void run() {
      String var1 = SResources.getString("m.d.priority")
         + " ("
         + (this.relative ? SResources.getString("m.d.priorityRelative") : SResources.getString("m.d.priorityAbsolute"))
         + ")";
      DownloadTreeMenuListener$PriorityInputDialog var2 = new DownloadTreeMenuListener$PriorityInputDialog(
         DownloadTreeMenuListener.access$3400(this.this$0).getShell(), var1, this.relative ? 0 : DownloadTreeMenuListener.access$000(this.this$0).getPriority()
      );
      if (var2.open() == 0) {
         int var3 = var2.getIntValue();
         int var4 = var2.getIncIntValue();

         for (int var5 = 0; var5 < DownloadTreeMenuListener.access$3500(this.this$0).size(); var5++) {
            File var6 = (File)DownloadTreeMenuListener.access$3600(this.this$0).get(var5);
            if (var6.getFileStateEnum() != EnumFileState.DOWNLOADED) {
               var6.sendPriority(this.relative, var3);
               int var7 = var3 += var4;
               if (var7 <= 255 && var7 >= -255) {
                  var3 = var7;
               }
            }
         }
      }
   }

   public boolean isChecked() {
      return false;
   }
}
