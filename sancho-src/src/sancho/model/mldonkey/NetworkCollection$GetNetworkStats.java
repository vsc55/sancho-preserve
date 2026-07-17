package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

class NetworkCollection$GetNetworkStats implements TObjectProcedure {
   StringBuffer stringBuffer = new StringBuffer();
   String nl;

   public NetworkCollection$GetNetworkStats(String var1) {
      this.nl = var1;
   }

   public boolean execute(Object var1) {
      Network var2 = (Network)var1;
      this.stringBuffer.append(this.nl + SResources.getString("stats.network") + ": ");
      this.stringBuffer.append(var2.getName());
      if (var2.isEnabled()) {
         this.stringBuffer.append(" (" + SResources.getString("stats.enabled") + ")" + this.nl);
      } else {
         this.stringBuffer.append(" (" + SResources.getString("stats.disabled") + ")" + this.nl);
      }

      this.stringBuffer.append(SResources.getString("stats.downloaded") + ": ");
      this.stringBuffer.append(SwissArmy.calcStringSize(var2.getDownloaded()) + this.nl);
      this.stringBuffer.append(SResources.getString("stats.uploaded") + ": ");
      this.stringBuffer.append(SwissArmy.calcStringSize(var2.getUploaded()) + this.nl);
      return true;
   }

   public String getResultString() {
      return this.stringBuffer.toString();
   }
}
