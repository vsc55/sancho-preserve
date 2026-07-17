package sancho.view.preferences;

import java.io.File;
import java.io.FilenameFilter;
import sancho.utility.VersionInfo;

class RootPreferencePage$PropertiesFilter implements FilenameFilter {
   public boolean accept(File var1, String var2) {
      String var3 = var2.toLowerCase();
      return var3.startsWith(VersionInfo.getName()) && var3.endsWith(".properties");
   }
}
