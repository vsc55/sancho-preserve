package sancho.view;

import java.util.ArrayList;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.ClientStats;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.NetworkCollection;
import sancho.model.mldonkey.utility.NetworkStatCollection;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.statistics.GraphCanvas;
import sancho.view.statistics.GraphData;
import sancho.view.statistics.GraphViewFrame;
import sancho.view.statistics.networkStats.NetworkStatsViewFrame;
import sancho.view.statistics.networks.NetworksViewFrame;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class StatisticTab extends AbstractTab implements MyObserver {
   private static final String RS_UPLOADS = SResources.getString("graph.uploads");
   private static final String RS_DOWNLOADS = SResources.getString("graph.downloads");
   private static final String RS_AVG = SResources.getString("graph.avg");
   private static final String RS_MAX = SResources.getString("graph.max");
   private static final String RS_TOTAL = SResources.getString("l.total");
   private static final String RS_DELAY = SResources.getString("l.delay");
   private static final String RS_GLOBAL = SResources.getString("l.global");
   private static final String VF_LIST = "VFList";
   private static final String S_S = "s ";
   private NetworksViewFrame networksViewFrame;
   private GraphCanvas uploadsGraphCanvas;
   private GraphCanvas downloadsGraphCanvas;
   private CLabel uploadsHeaderCLabel;
   private CLabel downloadsHeaderCLabel;
   private int updateDelay;
   private long lastTimeStamp;
   private long lastTextTimeStamp;
   private CTabFolder cTabFolder;

   public StatisticTab(MainWindow var1, String var2) {
      super(var1, var2);
      this.onConnect();
      this.updateDisplay();
   }

   protected void createContents(Composite var1) {
      this.cTabFolder = new CTabFolder(var1, 8388608);
      CTabItem var2 = new CTabItem(this.cTabFolder, 0);
      var2.setText(RS_GLOBAL);
      var2.setImage(SResources.getImage("tab.statistics.buttonSmall"));
      this.cTabFolder.setSelection(var2);
      Composite var3 = new Composite(this.cTabFolder, 0);
      var3.setLayout(new FillLayout());
      var2.setControl(var3);
      String var4 = "statisticsSash";
      SashForm var5 = WidgetFactory.createSashForm(var3, var4);
      this.createInfo(var5);
      this.createGraphSash(var5);
      WidgetFactory.loadSashForm(var5, var4);
   }

   public void onDisconnect() {
      super.onDisconnect();
      CTabItem[] var1 = this.cTabFolder.getItems();
      if (var1 != null) {
         for (int var2 = 0; var2 < var1.length; var2++) {
            if (var1[var2].isDisposed()) {
               return;
            }

            if (!var1[var2].getText().equals(RS_GLOBAL)) {
               this.disposeCTab(var1[var2]);
            }
         }
      }
   }

   public void disposeCTab(CTabItem var1) {
      ArrayList var2 = (ArrayList)var1.getData("VFList");
      if (var2 != null) {
         for (int var3 = 0; var3 < var2.size(); var3++) {
            NetworkStatsViewFrame var4 = (NetworkStatsViewFrame)var2.get(var3);
            if (var4 != null) {
               var4.dispose();
            }

            this.removeViewFrame(var4);
         }
      }

      var1.dispose();
   }

   protected void updateNetworkTab(Network var1) {
      CTabItem[] var2 = this.cTabFolder.getItems();
      if (var2 != null) {
         CTabItem var3 = null;

         for (int var4 = 0; var4 < var2.length; var4++) {
            if (var2[var4].isDisposed()) {
               return;
            }

            if (var2[var4].getText().equals(var1.getName())) {
               var3 = var2[var4];
            }
         }

         if (var3 == null) {
            this.createNetworkTab(var1);
         } else {
            NetworkStatCollection[] var5 = var1.getNetworkStatCollection();
            if (var5 != null) {
               for (int var6 = 0; var6 < var5.length; var6++) {
                  ArrayList var7 = (ArrayList)var3.getData("VFList");
                  int var8 = var7.size();
                  if (var3.getData(var5[var6].getName()) == null || var8 != var5.length) {
                     this.disposeCTab(var3);
                     this.createNetworkTab(var1);
                     return;
                  }
               }

               for (int var9 = 0; var9 < var5.length; var9++) {
                  NetworkStatsViewFrame var10 = (NetworkStatsViewFrame)var3.getData(var5[var9].getName());
                  if (var10 != null) {
                     var10.refreshInThread();
                  }
               }
            }
         }
      }
   }

   public void createNetworkTab(Network var1) {
      NetworkStatCollection[] var2 = var1.getNetworkStatCollection();
      if (var2 != null) {
         CTabItem var3 = new CTabItem(this.cTabFolder, 0);
         var3.setText(var1.getName());
         var3.setImage(var1.getEnumNetwork().getImage());
         ArrayList var4 = new ArrayList();
         var3.setData("VFList", var4);
         Composite var5 = new Composite(this.cTabFolder, 0);
         var5.setLayout(new FillLayout());
         var3.setControl(var5);
         int var6 = 0;
         String var7 = "statistics." + var1.getName();
         SashForm var8 = WidgetFactory.createSashForm(var5, var7);
         SashForm var9 = var8;
         String var10 = null;
         boolean var11 = var2.length > 2;
         boolean var12 = false;

         for (int var13 = 0; var13 < var2.length; var13++) {
            if (var11) {
               if (var13 % 2 == 0) {
                  var10 = "statistics." + var1.getName() + "." + var6;
                  Composite var14 = new Composite(var8, 0);
                  var14.setLayout(new FillLayout());
                  var9 = WidgetFactory.createSashForm(var14, var10);
               } else {
                  var12 = true;
               }
            }

            NetworkStatsViewFrame var15 = new NetworkStatsViewFrame(
               var9,
               var2[var13].getName() + ": " + SwissArmy.calcStringOfSecondsFull((long)var2[var13].getUptime()),
               var1.getEnumNetwork().getImageString(),
               this,
               var1,
               var2[var13]
            );
            var15.setVisible(true);
            var15.setActive(true);
            this.addViewFrame(var15);
            var4.add(var15);
            var3.setData(var2[var13].getName(), var15);
            if (var12) {
               var6++;
               WidgetFactory.loadSashForm(var9, var10);
               var12 = false;
            }
         }

         WidgetFactory.loadSashForm(var8, var7);
      }
   }

   public void onConnect() {
      super.onConnect();
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getClientStats().addObserver(this);
         this.getCore().getNetworkCollection().addObserver(this);
         this.getCore().getNetworkCollection().getAllStats();
      }
   }

   public void dispose() {
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getClientStats().deleteObserver(this);
         this.getCore().getNetworkCollection().deleteObserver(this);
      }

      super.dispose();
   }

   private void createInfo(SashForm var1) {
      this.networksViewFrame = new NetworksViewFrame(var1, "tab.statistics", "tab.statistics.buttonSmall", this);
      this.addViewFrame(this.networksViewFrame);
   }

   private void createGraphSash(Composite var1) {
      String var2 = "graphSash";
      SashForm var3 = WidgetFactory.createSashForm(var1, var2);
      this.createDownloadsGraph(var3);
      this.createUploadsGraph(var3);
      WidgetFactory.loadSashForm(var3, var2);
   }

   private void createDownloadsGraph(SashForm var1) {
      GraphViewFrame var2 = this.createGraph(var1, RS_DOWNLOADS, "Downloads");
      this.addViewFrame(var2);
      this.downloadsGraphCanvas = var2.getGraphCanvas();
      this.downloadsHeaderCLabel = var2.getCLabel();
   }

   private void createUploadsGraph(SashForm var1) {
      GraphViewFrame var2 = this.createGraph(var1, RS_UPLOADS, "Uploads");
      this.addViewFrame(var2);
      this.uploadsGraphCanvas = var2.getGraphCanvas();
      this.uploadsHeaderCLabel = var2.getCLabel();
   }

   private GraphViewFrame createGraph(SashForm var1, String var2, String var3) {
      return new GraphViewFrame(var1, var2, "tab.statistics.buttonSmall", this, var3);
   }

   public void updateNetworkCollection(Object var1, int var2) {
      if ((var2 & NetworkCollection.CHANGED_STATS) != 0) {
         Network var3 = (Network)var1;
         this.cTabFolder.getDisplay().asyncExec(new Runnable() {
            public void run() {
               StatisticTab.this.updateNetworkTab(var3);
            }
         });
      }
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (var1 instanceof NetworkCollection) {
         this.updateNetworkCollection(var2, var3);
      } else if (var1 instanceof ClientStats) {
         long var4;
         if ((var4 = System.currentTimeMillis()) - (long)(this.updateDelay * 1000) >= this.lastTimeStamp) {
            ClientStats var6 = (ClientStats)var1;
            if (var6 != null) {
               this.uploadsGraphCanvas.addPoint(var6.getTcpUploadRate());
               this.downloadsGraphCanvas.addPoint(var6.getTcpDownloadRate());
               if (this.isActive()) {
                  this.uploadsGraphCanvas.redrawInThread();
                  this.downloadsGraphCanvas.redrawInThread();
                  this.updateHeaderLabels(var6);
               }

               this.lastTimeStamp = System.currentTimeMillis();
               if (this.lastTextTimeStamp + 30000L < var4) {
                  this.networksViewFrame.refreshInThread();
                  this.lastTextTimeStamp = var4;
               }
            }
         }
      }
   }

   public void updateHeaderLabels(ClientStats var1) {
      if (this.uploadsHeaderCLabel != null && !this.uploadsHeaderCLabel.isDisposed()) {
         this.uploadsHeaderCLabel.getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (StatisticTab.this.uploadsHeaderCLabel != null && !StatisticTab.this.uploadsHeaderCLabel.isDisposed()) {
                  StatisticTab.this.uploadsHeaderCLabel
                     .setText(this.writeLabel(StatisticTab.this.uploadsGraphCanvas.getGraphData(), RS_UPLOADS, var1.getUploadCounter()));
                  StatisticTab.this.downloadsHeaderCLabel
                     .setText(this.writeLabel(StatisticTab.this.downloadsGraphCanvas.getGraphData(), RS_DOWNLOADS, var1.getDownloadCounter()));
               }
            }

            public String writeLabel(GraphData var1, String var2, long var3) {
               StringBuffer var5 = new StringBuffer(32);
               var5.append(var2);
               var5.append(": ");
               var5.append(SwissArmy.calcStringSize(var3));
               var5.append(" ");
               var5.append(RS_TOTAL);
               var5.append(", ");
               var5.append((double)var1.getAvg() / 100.0);
               var5.append(" ");
               var5.append(RS_AVG);
               var5.append(", ");
               var5.append((double)var1.getMax() / 100.0);
               var5.append(" ");
               var5.append(RS_MAX);
               if (StatisticTab.this.updateDelay > 0) {
                  var5.append(" ");
                  var5.append(" (");
                  var5.append(StatisticTab.this.updateDelay);
                  var5.append("s ");
                  var5.append(RS_DELAY);
                  var5.append(")");
               }

               return var5.toString();
            }
         });
      }
   }

   public void setInActive(boolean var1) {
      super.setInActive();
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.updateDelay = PreferenceLoader.loadInt("graphUpdateDelay");
      this.lastTimeStamp = 0L;
   }
}
