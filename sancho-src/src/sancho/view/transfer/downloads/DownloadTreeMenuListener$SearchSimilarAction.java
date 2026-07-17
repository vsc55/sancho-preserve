package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.File;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$SearchSimilarAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$SearchSimilarAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.searchSimilar"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("search_small"));
   }

   public void run() {
      for (int var1 = 0; var1 < DownloadTreeMenuListener.access$1600(this.this$0).size(); var1++) {
         File var2 = (File)DownloadTreeMenuListener.access$1700(this.this$0).get(var1);
         String var3 = var2.getName();
         String var4 = var2.getExtension();
         if (var3.length() > var4.length() + 1) {
            int var5 = var3.length() - var4.length() - 1;
            var3 = var3.substring(0, var5);
         }

         var3 = SwissArmy.replaceAll(var3, "\\_", " ");
         var3 = SwissArmy.replaceAll(var3, "\\.", " ");
         var3 = SwissArmy.replaceAll(var3, "\\-", " ");
         var3 = SwissArmy.replaceAll(var3, "\\?", " ");
         var3 = SwissArmy.replaceAll(var3, "\\!", " ");
         DownloadTreeMenuListener.access$1800(this.this$0).getViewFrame().getATab().getMainWindow().autoSearch(var3);
      }
   }
}
