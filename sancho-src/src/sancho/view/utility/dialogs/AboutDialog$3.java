package sancho.view.utility.dialogs;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import sancho.utility.VersionInfo;
import sancho.view.utility.WebLauncher;

class AboutDialog$3 extends MouseAdapter {
   // $VF: synthetic field
   private final AboutDialog this$0;

   AboutDialog$3(AboutDialog var1) {
      this.this$0 = var1;
   }

   public void mouseDown(MouseEvent var1) {
      if (this.this$0.btRect.contains(var1.x, var1.y)) {
         WebLauncher.openLink(VersionInfo.getBruceHomePage());
      } else if (this.this$0.urlRect.contains(var1.x, var1.y)) {
         WebLauncher.openLink(VersionInfo.getHomePage2());
      } else if (this.this$0.roRect.contains(var1.x, var1.y)) {
         WebLauncher.openLink(VersionInfo.getHomePage2());
      }
   }

   public void mouseUp(MouseEvent var1) {
      this.this$0.close();
   }
}
