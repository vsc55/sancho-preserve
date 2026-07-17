package sancho.view.statistics;

import gnu.trove.TIntArrayList;
import org.eclipse.swt.widgets.Display;

public class GraphData implements Runnable {
   public static final short MAX_GRAPH_TYPES = 2;
   public static final short MAX_POINTS = 1600;
   public static final int ONE_HOUR = 3600000;
   private int numPoints;
   private int avgValue;
   private Display display;
   private String graphName;
   private int hourlyAmount;
   private int hourlyAvgValue;
   private int hourlyMaxValue;
   private long hourlySumValue;
   private int insertAt = 0;
   private boolean disposed;
   private int[] iPoints = new int[1600];
   private TIntArrayList maxList;
   private TIntArrayList avgList;
   private int maxValue;
   private String title;
   private long sumValue;

   public GraphData(Display var1, String var2, String var3) {
      this.graphName = var3;
      this.title = var2;
      this.display = var1;
      this.maxList = new TIntArrayList(0);
      this.avgList = new TIntArrayList(0);
      this.sumValue = this.hourlySumValue = 0L;
      this.avgValue = this.maxValue = 0;
      this.hourlyMaxValue = 0;
      this.hourlyAvgValue = 0;
      var1.timerExec(3600000, this);
   }

   public synchronized void addPoint(int var1) {
      if (this.iPoints != null) {
         if (this.insertAt > 1599) {
            this.insertAt = 0;
         }

         this.iPoints[this.insertAt++] = var1;
         if (var1 > this.maxValue) {
            this.maxValue = var1;
         }

         this.sumValue += (long)var1;
         this.numPoints++;
         this.avgValue = (int)(this.sumValue / (long)this.numPoints);
         if (var1 > this.hourlyMaxValue) {
            this.hourlyMaxValue = var1;
         }

         this.hourlySumValue += (long)var1;
         this.hourlyAmount++;
         this.hourlyAvgValue = (int)(this.hourlySumValue / (long)this.hourlyAmount);
      }
   }

   public synchronized void clearHistory() {
      this.sumValue = (long)(this.numPoints = 0);
      this.avgValue = this.maxValue = 0;
      this.insertAt = 0;
      this.maxList.clear();
      this.avgList.clear();
   }

   public synchronized int findMax(int var1) {
      int var2 = 20;
      int var3 = this.insertAt - 1;
      if (var1 > this.numPoints) {
         var1 = this.numPoints;
      }

      for (int var4 = 0; var4 < var1; var4++) {
         if (var3 < 0) {
            var3 = 1599;
         }

         if (this.iPoints[var3] > var2) {
            var2 = this.iPoints[var3];
         }

         var3--;
      }

      return var2;
   }

   public int getNumPoints() {
      return this.numPoints;
   }

   public int getAvg() {
      return this.avgValue;
   }

   public synchronized TIntArrayList getAvgList() {
      return this.avgList;
   }

   public synchronized int getInsertAt() {
      return this.insertAt;
   }

   public int getMax() {
      return this.maxValue;
   }

   public synchronized TIntArrayList getMaxList() {
      return this.maxList;
   }

   public String getTitle() {
      return this.title;
   }

   public String getGraphName() {
      return this.graphName;
   }

   public synchronized int getNewestPoint() {
      int var1 = this.insertAt - 1;
      if (var1 < 0) {
         var1 = 1599;
      }

      return this.iPoints[var1];
   }

   public int getPointAt(int var1) {
      return this.iPoints[var1];
   }

   public void dispose() {
      this.iPoints = null;
      this.disposed = true;
   }

   public synchronized void run() {
      if (!this.disposed) {
         this.maxList.add(this.hourlyMaxValue);
         this.avgList.add(this.hourlyAvgValue);
         this.hourlyMaxValue = this.hourlyAvgValue = 0;
         this.hourlySumValue = (long)(this.hourlyAmount = 0);
         if (this.display != null && !this.display.isDisposed() && !this.disposed) {
            this.display.timerExec(3600000, this);
         }
      }
   }
}
