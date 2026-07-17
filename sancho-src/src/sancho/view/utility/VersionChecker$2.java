package sancho.view.utility;

import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;

class VersionChecker$2 implements Runnable {
   // $VF: synthetic field
   private final VersionChecker$1 this$1;

   VersionChecker$2(VersionChecker$1 var1) {
      this.this$1 = var1;
   }

   public void run() {
      if (VersionChecker$1.access$000(this.this$1).shell != null && !VersionChecker$1.access$000(this.this$1).shell.isDisposed()) {
         if (VersionChecker$1.access$000(this.this$1).statusLine != null) {
            VersionChecker$1.access$000(this.this$1)
               .statusLine
               .setText(
                  "["
                     + VersionInfo.getName()
                     + "] "
                     + SResources.getString("l.current")
                     + VersionInfo.getVersion()
                     + " / "
                     + SResources.getString("l.latest")
                     + VersionChecker$1.access$000(this.this$1).newVersion
               );
         }

         if (!VersionChecker$1.access$000(this.this$1).newVersion.equals(VersionInfo.getVersion()) && PreferenceLoader.loadBoolean("versionCheckPopup")) {
            new VersionChecker$VersionDialog(VersionChecker$1.access$000(this.this$1).shell, VersionChecker$1.access$000(this.this$1).newVersion).open();
         }
      }
   }
}
