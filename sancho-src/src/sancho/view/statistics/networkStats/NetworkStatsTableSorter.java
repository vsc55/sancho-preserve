package sancho.view.statistics.networkStats;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.utility.NetworkStat;
import sancho.model.mldonkey.utility.NetworkStatTotal;
import sancho.view.viewer.GSorter;

public class NetworkStatsTableSorter extends GSorter {
   public NetworkStatsTableSorter(NetworkStatsTableView var1) {
      super(var1);
      this.setDirection(true);
   }

   public boolean sortOrder(int var1) {
      return true;
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      NetworkStat var5 = (NetworkStat)var2;
      NetworkStat var6 = (NetworkStat)var3;
      if (var2 instanceof NetworkStatTotal) {
         return Integer.MAX_VALUE;
      } else if (var3 instanceof NetworkStatTotal) {
         return Integer.MIN_VALUE;
      } else {
         switch (var4) {
            case 0:
               return this.compareStrings(var5.getNameLong(), var6.getNameLong());
            case 1:
               return this.compareInts(var5.getSeen(), var6.getSeen());
            case 2:
               return this.compareInts(var5.getSeenPercent(), var6.getSeenPercent());
            case 3:
               return this.compareInts(var5.getBanned(), var6.getBanned());
            case 4:
               return this.compareInts(var5.getBannedPercent(), var6.getBannedPercent());
            case 5:
               return this.compareInts(var5.getRequest(), var6.getRequest());
            case 6:
               return this.compareInts(var5.getRequestPercent(), var6.getRequestPercent());
            case 7:
               return this.compareLongs(var5.getUpload(), var6.getUpload());
            case 8:
               return this.compareLongs((long)var5.getUploadPercent(), (long)var6.getUploadPercent());
            case 9:
               return this.comparePercents(var5.getUploadRate(), var6.getUploadRate());
            case 10:
               return this.compareLongs(var5.getDownload(), var6.getDownload());
            case 11:
               return this.compareLongs((long)var5.getDownloadPercent(), (long)var6.getDownloadPercent());
            case 12:
               return this.comparePercents(var5.getDownloadRate(), var6.getDownloadRate());
            case 13:
               return this.comparePercents(var5.getRatio(), var6.getRatio());
            default:
               return 0;
         }
      }
   }
}
