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

   public GraphData(Display display, String title, String graphName) {
      this.graphName = graphName;
      this.title = title;
      this.display = display;
      this.maxList = new TIntArrayList(0);
      this.avgList = new TIntArrayList(0);
      this.sumValue = this.hourlySumValue = 0L;
      this.avgValue = this.maxValue = 0;
      this.hourlyMaxValue = 0;
      this.hourlyAvgValue = 0;
      display.timerExec(3600000, this);
   }

   public synchronized void addPoint(int value) {
      if (this.iPoints != null) {
         if (this.insertAt > 1599) {
            this.insertAt = 0;
         }

         this.iPoints[this.insertAt++] = value;
         if (value > this.maxValue) {
            this.maxValue = value;
         }

         this.sumValue += (long)value;
         this.numPoints++;
         this.avgValue = (int)(this.sumValue / (long)this.numPoints);
         if (value > this.hourlyMaxValue) {
            this.hourlyMaxValue = value;
         }

         this.hourlySumValue += (long)value;
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

   public synchronized int findMax(int count) {
      int max = 20;
      int index = this.insertAt - 1;
      if (count > this.numPoints) {
         count = this.numPoints;
      }

      for (int i = 0; i < count; i++) {
         if (index < 0) {
            index = 1599;
         }

         if (this.iPoints[index] > max) {
            max = this.iPoints[index];
         }

         index--;
      }

      return max;
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
      int index = this.insertAt - 1;
      if (index < 0) {
         index = 1599;
      }

      return this.iPoints[index];
   }

   public int getPointAt(int index) {
      return this.iPoints[index];
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
