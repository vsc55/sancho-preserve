package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

public class DownloadViewListener$DisplayChunkGraphsAction extends Action {
   protected DownloadTreeView gView;
   // $VF: synthetic field
   private final DownloadViewListener this$0;

   public DownloadViewListener$DisplayChunkGraphsAction(DownloadViewListener var1, DownloadTreeView var2) {
      super(SResources.getString("p.r.downloads.displayChunkGraphs"));
      this.this$0 = var1;
      if (this.isChecked()) {
         this.setImageDescriptor(SResources.getImageDescriptor("checkmark"));
      }

      this.gView = var2;
   }

   public boolean isChecked() {
      return PreferenceLoader.loadBoolean("displayChunkGraphs");
   }

   public void run() {
      boolean var1 = this.isChecked();
      PreferenceLoader.getPreferenceStore().setValue("displayChunkGraphs", !var1);
      PreferenceLoader.saveStore();
      this.gView.setChunkGraphs();
   }
}
