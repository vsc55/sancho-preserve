package sancho.view.transfer.downloads;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;

class DownloadViewFrame$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final DownloadViewFrame this$0;

   DownloadViewFrame$1(DownloadViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      String var2 = PreferenceLoader.loadString("explorerExecutable");
      String var3 = PreferenceLoader.loadString("explorerOpenFolder");
      if (!var2.equals("")) {
         String[] var4 = new String[]{var2, var3};
         SwissArmy.execInThread(var4, null);
      }
   }
}
