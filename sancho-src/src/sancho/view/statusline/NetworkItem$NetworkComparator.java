package sancho.view.statusline;

import java.util.Comparator;
import sancho.model.mldonkey.Network;

class NetworkItem$NetworkComparator implements Comparator {
   public int compare(Object var1, Object var2) {
      Network var3 = (Network)var1;
      Network var4 = (Network)var2;
      if (var3.getName().equalsIgnoreCase("g2") && var4.getName().equalsIgnoreCase("gnutella")) {
         return 1;
      } else {
         return var4.getName().equalsIgnoreCase("g2") && var3.getName().equalsIgnoreCase("gnutella") ? -1 : var3.getName().compareToIgnoreCase(var4.getName());
      }
   }
}
