package sancho.view.preferences;

import java.util.Comparator;
import sancho.model.mldonkey.Option;

class MLDonkeyPreferencePage$OptionsComparator implements Comparator {
   public int compare(Object var1, Object var2) {
      Option var3 = (Option)var1;
      Option var4 = (Option)var2;
      return var3.getName().compareToIgnoreCase(var4.getName());
   }
}
