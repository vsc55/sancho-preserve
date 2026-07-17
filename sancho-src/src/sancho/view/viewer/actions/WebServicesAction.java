package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.WebLauncher;

public class WebServicesAction extends Action {
   public static final int BITZI = 1;
   public static final int FILEDONKEY = 2;
   public static final int RAZORBACK2 = 5;
   public static final int EMUGLE = 6;
   public static final String webServiceName = "webServiceName";
   public static final String webServiceURL = "webServiceURL";
   private int type;
   private String md4;
   private String ed2k;
   private long size;

   public WebServicesAction(int var1, String var2, String var3, long var4) {
      this.type = var1;
      this.md4 = var2;
      this.ed2k = var3;
      this.size = var4;
      switch (var1) {
         case 1:
            this.setText(SResources.getString("mi.web.bitzi"));
            this.setImageDescriptor(SResources.getImageDescriptor("bitzi"));
            break;
         case 2:
            this.setText(SResources.getString("mi.web.filehash"));
            this.setImageDescriptor(SResources.getImageDescriptor("filehash"));
            break;
         case 3:
         case 4:
         default:
            int var6 = Math.abs(var1);
            this.setText(PreferenceLoader.loadString("webServiceName" + var6));
            this.setImageDescriptor(SResources.getImageDescriptor("web-link"));
            break;
         case 5:
            this.setText(SResources.getString("mi.web.razorback2"));
            this.setImageDescriptor(SResources.getImageDescriptor("razorback2"));
            break;
         case 6:
            this.setText(SResources.getString("mi.web.emugle"));
            this.setImageDescriptor(SResources.getImageDescriptor("emugle"));
      }
   }

   public void run() {
      launch(this.type, this.md4, this.ed2k, this.size);
   }

   public static void launch(int var0, String var1, String var2, long var3) {
      switch (var0) {
         case 1:
            WebLauncher.openLink("http://bitzi.com/lookup/urn:ed2k:" + var1);
            break;
         case 2:
            WebLauncher.openLink("http://www.filehash.com/file/" + var1);
            break;
         case 3:
         case 4:
         default:
            int var5 = Math.abs(var0);
            String var6 = PreferenceLoader.loadString("webServiceURL" + var5);
            var6 = SwissArmy.replaceAll(var6, "%FILEMD4%", var1);
            var6 = SwissArmy.replaceAll(var6, "%FILEHASH%", var1);
            var6 = SwissArmy.replaceAll(var6, "%FILEED2K%", var2);
            var6 = SwissArmy.replaceAll(var6, "%FILESIZE%", String.valueOf(var3));
            WebLauncher.openLink(var6);
            break;
         case 5:
            WebLauncher.openLink("http://tothbenedek.hu/ed2kstats/ed2k?hash=" + var1);
            break;
         case 6:
            WebLauncher.openLink("http://emugle.com/details.php?f=" + var1);
      }
   }
}
