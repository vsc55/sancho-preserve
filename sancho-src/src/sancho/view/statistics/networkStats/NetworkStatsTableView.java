package sancho.view.statistics.networkStats;

import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.utility.NetworkStatCollection;
import sancho.utility.SwissArmy;
import sancho.view.utility.Base64;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class NetworkStatsTableView extends GTableView {
   public static final int NAME = 0;
   public static final int SEEN = 1;
   public static final int SEENP = 2;
   public static final int BANNED = 3;
   public static final int BANNEDP = 4;
   public static final int REQUESTS = 5;
   public static final int REQUESTSP = 6;
   public static final int UPLOAD = 7;
   public static final int UPLOADP = 8;
   public static final int UPLOADR = 9;
   public static final int DOWNLOAD = 10;
   public static final int DOWNLOADP = 11;
   public static final int DOWNLOADR = 12;
   public static final int RATIO = 13;
   NetworkStatCollection networkStatCollection;

   public NetworkStatsTableView(ViewFrame viewFrame, Network network, NetworkStatCollection collection) {
      super(viewFrame);
      this.networkStatCollection = collection;
      String name = network.getName() + " " + collection.getName();
      this.preferenceString = "networkStats" + Base64.encode(name.getBytes());
      this.columnLabels = new String[]{
         "networkStats.name",
         "networkStats.seen",
         "networkStats.seen%",
         "networkStats.banned",
         "networkStats.banned%",
         "networkStats.requests",
         "networkStats.requests%",
         "networkStats.upload",
         "networkStats.upload%",
         "networkStats.uploadR",
         "networkStats.download",
         "networkStats.download%",
         "networkStats.downloadR",
         "networkStats.ratio"
      };
      this.columnAlignment = new int[]{16384, 131072, 131072, 131072, 131072, 131072, 131072, 131072, 131072, 131072, 131072, 131072, 131072, 16384};
      this.columnDefaultWidths = new int[]{100, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75};
      this.gSorter = new NetworkStatsTableSorter(this);
      this.tableContentProvider = new NetworkStatsTableContentProvider(this);
      this.tableLabelProvider = new NetworkStatsTableLabelProvider(this);
      this.tableMenuListener = new NetworkStatsTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((NetworkStatsTableMenuListener)this.tableMenuListener);
      this.addMenuListener();
   }

   public void setInput() {
      this.sViewer.setInput(this.networkStatCollection);
   }

   public void updateHeader() {
      this.getViewFrame()
         .updateCLabelText(this.networkStatCollection.getName() + ": " + SwissArmy.calcStringOfSecondsFull((long)this.networkStatCollection.getUptime()));
   }
}
