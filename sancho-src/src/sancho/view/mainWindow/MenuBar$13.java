package sancho.view.mainWindow;

import java.io.File;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import sancho.utility.VersionInfo;
import sancho.view.downloadComplete.DownloadCompleteShell;
import sancho.view.utility.SResources;

class MenuBar$13 implements Listener {
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$13(MenuBar var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      File var2 = new File(VersionInfo.getDownloadLogFile());
      if (!var2.exists()) {
         MessageBox var3 = new MessageBox(MenuBar.access$200(this.this$0), 34);
         var3.setMessage(SResources.getString("l.noCompleteDownloads") + "\n" + VersionInfo.getDownloadLogFile());
         var3.open();
      } else {
         new DownloadCompleteShell(MenuBar.access$100(this.this$0).getShell()).open();
      }
   }
}
