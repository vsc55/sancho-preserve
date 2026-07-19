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

   public StatisticTab(MainWindow mainWindow, String name) {
      super(mainWindow, name);
      this.onConnect();
      this.updateDisplay();
   }

   protected void createContents(Composite parent) {
      this.cTabFolder = new CTabFolder(parent, 8388608);
      CTabItem tabItem = new CTabItem(this.cTabFolder, 0);
      tabItem.setText(RS_GLOBAL);
      tabItem.setImage(SResources.getImage("tab.statistics.buttonSmall"));
      this.cTabFolder.setSelection(tabItem);
      Composite composite = new Composite(this.cTabFolder, 0);
      composite.setLayout(new FillLayout());
      tabItem.setControl(composite);
      String sashName = "statisticsSash";
      SashForm sashForm = WidgetFactory.createSashForm(composite, sashName);
      this.createInfo(sashForm);
      this.createGraphSash(sashForm);
      WidgetFactory.loadSashForm(sashForm, sashName);
   }

   public void onDisconnect() {
      super.onDisconnect();
      CTabItem[] tabItems = this.cTabFolder.getItems();
      if (tabItems != null) {
         for (int i = 0; i < tabItems.length; i++) {
            if (tabItems[i].isDisposed()) {
               return;
            }

            if (!tabItems[i].getText().equals(RS_GLOBAL)) {
               this.disposeCTab(tabItems[i]);
            }
         }
      }
   }

   public void disposeCTab(CTabItem tabItem) {
      ArrayList viewFrames = (ArrayList)tabItem.getData("VFList");
      if (viewFrames != null) {
         for (int i = 0; i < viewFrames.size(); i++) {
            NetworkStatsViewFrame viewFrame = (NetworkStatsViewFrame)viewFrames.get(i);
            if (viewFrame != null) {
               viewFrame.dispose();
            }

            this.removeViewFrame(viewFrame);
         }
      }

      tabItem.dispose();
   }

   protected void updateNetworkTab(Network network) {
      CTabItem[] tabItems = this.cTabFolder.getItems();
      if (tabItems != null) {
         CTabItem tabItem = null;

         for (int i = 0; i < tabItems.length; i++) {
            if (tabItems[i].isDisposed()) {
               return;
            }

            if (tabItems[i].getText().equals(network.getName())) {
               tabItem = tabItems[i];
            }
         }

         if (tabItem == null) {
            this.createNetworkTab(network);
         } else {
            NetworkStatCollection[] statCollections = network.getNetworkStatCollection();
            if (statCollections != null) {
               for (int j = 0; j < statCollections.length; j++) {
                  ArrayList viewFrames = (ArrayList)tabItem.getData("VFList");
                  int size = viewFrames.size();
                  if (tabItem.getData(statCollections[j].getName()) == null || size != statCollections.length) {
                     this.disposeCTab(tabItem);
                     this.createNetworkTab(network);
                     return;
                  }
               }

               for (int k = 0; k < statCollections.length; k++) {
                  NetworkStatsViewFrame viewFrame = (NetworkStatsViewFrame)tabItem.getData(statCollections[k].getName());
                  if (viewFrame != null) {
                     viewFrame.refreshInThread();
                  }
               }
            }
         }
      }
   }

   public void createNetworkTab(Network network) {
      NetworkStatCollection[] statCollections = network.getNetworkStatCollection();
      if (statCollections != null) {
         CTabItem tabItem = new CTabItem(this.cTabFolder, 0);
         tabItem.setText(network.getName());
         tabItem.setImage(network.getEnumNetwork().getImage());
         ArrayList viewFrames = new ArrayList();
         tabItem.setData("VFList", viewFrames);
         Composite composite = new Composite(this.cTabFolder, 0);
         composite.setLayout(new FillLayout());
         tabItem.setControl(composite);
         int index = 0;
         String sashName = "statistics." + network.getName();
         SashForm sashForm = WidgetFactory.createSashForm(composite, sashName);
         SashForm currentSashForm = sashForm;
         String subSashName = null;
         boolean multiColumn = statCollections.length > 2;
         boolean pairComplete = false;

         for (int i = 0; i < statCollections.length; i++) {
            if (multiColumn) {
               if (i % 2 == 0) {
                  subSashName = "statistics." + network.getName() + "." + index;
                  Composite composite2 = new Composite(sashForm, 0);
                  composite2.setLayout(new FillLayout());
                  currentSashForm = WidgetFactory.createSashForm(composite2, subSashName);
               } else {
                  pairComplete = true;
               }
            }

            NetworkStatsViewFrame viewFrame = new NetworkStatsViewFrame(
               currentSashForm,
               statCollections[i].getName() + ": " + SwissArmy.calcStringOfSecondsFull((long)statCollections[i].getUptime()),
               network.getEnumNetwork().getImageString(),
               this,
               network,
               statCollections[i]
            );
            viewFrame.setVisible(true);
            viewFrame.setActive(true);
            this.addViewFrame(viewFrame);
            viewFrames.add(viewFrame);
            tabItem.setData(statCollections[i].getName(), viewFrame);
            if (pairComplete) {
               index++;
               WidgetFactory.loadSashForm(currentSashForm, subSashName);
               pairComplete = false;
            }
         }

         WidgetFactory.loadSashForm(sashForm, sashName);
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

   private void createInfo(SashForm sashForm) {
      this.networksViewFrame = new NetworksViewFrame(sashForm, "tab.statistics", "tab.statistics.buttonSmall", this);
      this.addViewFrame(this.networksViewFrame);
   }

   private void createGraphSash(Composite parent) {
      String sashName = "graphSash";
      SashForm sashForm = WidgetFactory.createSashForm(parent, sashName);
      this.createDownloadsGraph(sashForm);
      this.createUploadsGraph(sashForm);
      WidgetFactory.loadSashForm(sashForm, sashName);
   }

   private void createDownloadsGraph(SashForm sashForm) {
      GraphViewFrame graphViewFrame = this.createGraph(sashForm, RS_DOWNLOADS, "Downloads");
      this.addViewFrame(graphViewFrame);
      this.downloadsGraphCanvas = graphViewFrame.getGraphCanvas();
      this.downloadsHeaderCLabel = graphViewFrame.getCLabel();
   }

   private void createUploadsGraph(SashForm sashForm) {
      GraphViewFrame graphViewFrame = this.createGraph(sashForm, RS_UPLOADS, "Uploads");
      this.addViewFrame(graphViewFrame);
      this.uploadsGraphCanvas = graphViewFrame.getGraphCanvas();
      this.uploadsHeaderCLabel = graphViewFrame.getCLabel();
   }

   private GraphViewFrame createGraph(SashForm sashForm, String text, String name) {
      return new GraphViewFrame(sashForm, text, "tab.statistics.buttonSmall", this, name);
   }

   public void updateNetworkCollection(Object source, int changeFlags) {
      if ((changeFlags & NetworkCollection.CHANGED_STATS) != 0) {
         Network network = (Network)source;
         this.cTabFolder.getDisplay().asyncExec(new Runnable() {
            public void run() {
               StatisticTab.this.updateNetworkTab(network);
            }
         });
      }
   }

   public void update(MyObservable observable, Object data, int changeFlags) {
      if (observable instanceof NetworkCollection) {
         this.updateNetworkCollection(data, changeFlags);
      } else if (observable instanceof ClientStats) {
         long now;
         if ((now = System.currentTimeMillis()) - (long)(this.updateDelay * 1000) >= this.lastTimeStamp) {
            ClientStats stats = (ClientStats)observable;
            if (stats != null) {
               this.uploadsGraphCanvas.addPoint(stats.getTcpUploadRate());
               this.downloadsGraphCanvas.addPoint(stats.getTcpDownloadRate());
               if (this.isActive()) {
                  this.uploadsGraphCanvas.redrawInThread();
                  this.downloadsGraphCanvas.redrawInThread();
                  this.updateHeaderLabels(stats);
               }

               this.lastTimeStamp = System.currentTimeMillis();
               if (this.lastTextTimeStamp + 30000L < now) {
                  this.networksViewFrame.refreshInThread();
                  this.lastTextTimeStamp = now;
               }
            }
         }
      }
   }

   public void updateHeaderLabels(ClientStats stats) {
      if (this.uploadsHeaderCLabel != null && !this.uploadsHeaderCLabel.isDisposed()) {
         this.uploadsHeaderCLabel.getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (StatisticTab.this.uploadsHeaderCLabel != null && !StatisticTab.this.uploadsHeaderCLabel.isDisposed()) {
                  StatisticTab.this.uploadsHeaderCLabel
                     .setText(this.writeLabel(StatisticTab.this.uploadsGraphCanvas.getGraphData(), RS_UPLOADS, stats.getUploadCounter()));
                  StatisticTab.this.downloadsHeaderCLabel
                     .setText(this.writeLabel(StatisticTab.this.downloadsGraphCanvas.getGraphData(), RS_DOWNLOADS, stats.getDownloadCounter()));
               }
            }

            public String writeLabel(GraphData graphData, String text, long counter) {
               StringBuffer buffer = new StringBuffer(32);
               buffer.append(text);
               buffer.append(": ");
               buffer.append(SwissArmy.calcStringSize(counter));
               buffer.append(" ");
               buffer.append(RS_TOTAL);
               buffer.append(", ");
               buffer.append((double)graphData.getAvg() / 100.0);
               buffer.append(" ");
               buffer.append(RS_AVG);
               buffer.append(", ");
               buffer.append((double)graphData.getMax() / 100.0);
               buffer.append(" ");
               buffer.append(RS_MAX);
               if (StatisticTab.this.updateDelay > 0) {
                  buffer.append(" ");
                  buffer.append(" (");
                  buffer.append(StatisticTab.this.updateDelay);
                  buffer.append("s ");
                  buffer.append(RS_DELAY);
                  buffer.append(")");
               }

               return buffer.toString();
            }
         });
      }
   }

   public void setInActive(boolean inactive) {
      super.setInActive();
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.updateDelay = PreferenceLoader.loadInt("graphUpdateDelay");
      this.lastTimeStamp = 0L;
   }
}
