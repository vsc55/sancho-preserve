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

   public WebServicesAction(int type, String md4, String ed2k, long size) {
      this.type = type;
      this.md4 = md4;
      this.ed2k = ed2k;
      this.size = size;
      switch (type) {
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
            int serviceNumber = Math.abs(type);
            this.setText(PreferenceLoader.loadString("webServiceName" + serviceNumber));
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

   public static void launch(int type, String md4, String ed2k, long size) {
      switch (type) {
         case 1:
            WebLauncher.openLink("http://bitzi.com/lookup/urn:ed2k:" + md4);
            break;
         case 2:
            WebLauncher.openLink("http://www.filehash.com/file/" + md4);
            break;
         case 3:
         case 4:
         default:
            int serviceNumber = Math.abs(type);
            String url = PreferenceLoader.loadString("webServiceURL" + serviceNumber);
            url = SwissArmy.replaceAll(url, "%FILEMD4%", md4);
            url = SwissArmy.replaceAll(url, "%FILEHASH%", md4);
            url = SwissArmy.replaceAll(url, "%FILEED2K%", ed2k);
            url = SwissArmy.replaceAll(url, "%FILESIZE%", String.valueOf(size));
            WebLauncher.openLink(url);
            break;
         case 5:
            WebLauncher.openLink("http://tothbenedek.hu/ed2kstats/ed2k?hash=" + md4);
            break;
         case 6:
            WebLauncher.openLink("http://emugle.com/details.php?f=" + md4);
      }
   }
}
