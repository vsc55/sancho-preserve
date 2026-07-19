package sancho.view.statistics.networkStats;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.utility.NetworkStat;
import sancho.model.mldonkey.utility.NetworkStatTotal;
import sancho.view.viewer.GSorter;

public class NetworkStatsTableSorter extends GSorter {
   public NetworkStatsTableSorter(NetworkStatsTableView tableView) {
      super(tableView);
      this.setDirection(true);
   }

   public boolean sortOrder(int column) {
      return true;
   }

   protected int _compare(Viewer viewer, Object o1, Object o2, int column) {
      NetworkStat stat1 = (NetworkStat)o1;
      NetworkStat stat2 = (NetworkStat)o2;
      if (o1 instanceof NetworkStatTotal) {
         return Integer.MAX_VALUE;
      } else if (o2 instanceof NetworkStatTotal) {
         return Integer.MIN_VALUE;
      } else {
         switch (column) {
            case 0:
               return this.compareStrings(stat1.getNameLong(), stat2.getNameLong());
            case 1:
               return this.compareInts(stat1.getSeen(), stat2.getSeen());
            case 2:
               return this.compareInts(stat1.getSeenPercent(), stat2.getSeenPercent());
            case 3:
               return this.compareInts(stat1.getBanned(), stat2.getBanned());
            case 4:
               return this.compareInts(stat1.getBannedPercent(), stat2.getBannedPercent());
            case 5:
               return this.compareInts(stat1.getRequest(), stat2.getRequest());
            case 6:
               return this.compareInts(stat1.getRequestPercent(), stat2.getRequestPercent());
            case 7:
               return this.compareLongs(stat1.getUpload(), stat2.getUpload());
            case 8:
               return this.compareLongs((long)stat1.getUploadPercent(), (long)stat2.getUploadPercent());
            case 9:
               return this.comparePercents(stat1.getUploadRate(), stat2.getUploadRate());
            case 10:
               return this.compareLongs(stat1.getDownload(), stat2.getDownload());
            case 11:
               return this.compareLongs((long)stat1.getDownloadPercent(), (long)stat2.getDownloadPercent());
            case 12:
               return this.comparePercents(stat1.getDownloadRate(), stat2.getDownloadRate());
            case 13:
               return this.comparePercents(stat1.getRatio(), stat2.getRatio());
            default:
               return 0;
         }
      }
   }
}
