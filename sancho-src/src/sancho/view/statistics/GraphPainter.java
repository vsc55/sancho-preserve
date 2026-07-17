package sancho.view.statistics;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;

public class GraphPainter {
   private static final String S_KBS = " KB/s";
   private static final int START_X = 1;
   private Color backgroundColor;
   private GraphData graph;
   private Color graphColor1;
   private Color graphColor2;
   private String graphName;
   private int graphType;
   private Color gridColor;
   private Color labelBackgroundColor;
   private Color labelLineColor;
   private Color labelTextColor;
   private final Composite parent;
   private boolean reverse;
   private Color textColor;
   private int updateDelay;

   public GraphPainter(Composite var1, GraphData var2) {
      this.parent = var1;
      this.graph = var2;
      this.graphName = var2.getGraphName();
      this.updateDisplay();
   }

   private void drawBarGraph(GC var1, int var2, float var3, float var4) {
      int var5 = this.graph.getInsertAt() - 1;
      int var6 = 1600 > this.graph.getNumPoints() ? this.graph.getNumPoints() : 1600;
      var1.setForeground(this.graphColor1);
      int var8 = 1;
      byte var9 = 1;
      if (this.reverse) {
         var8 = var2 - 2;
         var9 = -1;
      }

      for (int var10 = var8; 0 <= var10 && var10 < var2 && var6 > 0; var6--) {
         if (var5 < 0) {
            var5 = 1599;
         }

         float var7 = (float)(this.graph.getPointAt(var5) / 10);
         var7 = var3 - var7 * var4;
         var1.drawLine(var10, (int)var3 + 1, var10, (int)var7);
         var5--;
         var10 += var9;
      }
   }

   private void drawGradiantGraph(GC var1, int var2, float var3, float var4) {
      int var5 = this.graph.getInsertAt() - 1;
      int var6 = 1600 > this.graph.getNumPoints() ? this.graph.getNumPoints() : 1600;
      var1.setBackground(this.graphColor1);
      var1.setForeground(this.graphColor2);
      int var8 = 1;
      byte var9 = 1;
      if (this.reverse) {
         var8 = var2 - 2;
         var9 = -1;
      }

      for (int var10 = var8; 0 <= var10 && var10 < var2 && var6 > 0; var6--) {
         if (var5 < 0) {
            var5 = 1599;
         }

         float var7 = (float)(this.graph.getPointAt(var5) / 10);
         var7 = var3 - var7 * var4;
         var1.fillGradientRectangle(var10, (int)var3 + 1, 1, (int)(var7 - var3), true);
         var5--;
         var10 += var9;
      }
   }

   private void drawLineGraph(GC var1, int var2, float var3, float var4) {
      int var5 = this.graph.getInsertAt() - 1;
      int var6 = 1600 > this.graph.getNumPoints() ? this.graph.getNumPoints() : 1600;
      float var8 = -1.0F;
      var1.setForeground(this.graphColor1);
      var1.setLineWidth(3);
      int var9 = 1;
      byte var10 = 1;
      byte var11 = -1;
      if (this.reverse) {
         var9 = var2 - 2;
         var10 = -1;
         var11 = 1;
      }

      for (int var12 = var9; 0 <= var12 && var12 < var2 && var6 > 0; var6--) {
         if (var5 < 0) {
            var5 = 1599;
         }

         float var7 = (float)(this.graph.getPointAt(var5) / 10);
         var7 = var3 - var7 * var4;
         if (var8 == -1.0F) {
            var8 = var7;
         }

         var1.drawLine(var12 + var11, (int)var8, var12, (int)var7);
         var8 = var7;
         var5--;
         var12 += var10;
      }

      var1.setLineWidth(1);
   }

   public void paint(GC var1, Image var2) {
      GC var3 = new GC(var2);
      var3.setBackground(this.backgroundColor);
      var3.setForeground(this.backgroundColor);
      int var4 = var2.getBounds().width;
      int var5 = var2.getBounds().height;
      var3.fillRectangle(0, 0, var4, var5);
      int var6 = var3.getFontMetrics().getHeight() + 2;
      int var7 = var5 - var6;
      int var8 = var4 - 1;
      float var9 = 0.0F;
      float var10 = (float)(this.graph.findMax(var4) / 10);
      var9 = ((float)var7 - 10.0F) / var10;
      switch (this.graphType) {
         case 1:
            this.drawBarGraph(var3, var4, (float)var7, var9);
            break;
         case 2:
            this.drawLineGraph(var3, var4, (float)var7, var9);
            break;
         default:
            this.drawGradiantGraph(var3, var4, (float)var7, var9);
      }

      var3.setForeground(this.gridColor);
      int var11 = 0;
      int var12 = 0;
      int var13 = 1 + var8;
      int var14 = 20;
      if (this.reverse) {
         var12 = var4 - 1;
         var14 = -var14;
         var13 = 0;
      }

      int var15 = this.parent.getClientArea().height;
      int var16 = var3.getFontMetrics().getHeight();
      int var17 = var15 - var16;
      int var18 = this.updateDelay > 0 ? this.updateDelay : 1;

      for (int var19 = var12; this.reverse ? var19 > var13 : var19 < var13; var19 += var14) {
         var3.drawLine(var19, 0, var19, var7 + 1);
         if (var11 > 0 && var11 % 3 == 0) {
            var3.drawText(SwissArmy.calcTimeOfSeconds((long)(var18 * var11 * 20)), var19 - 5, var17, true);
         }

         var11++;
      }

      int var20 = 1 + var8;

      for (int var21 = var7 + 1; var21 > 0; var21 -= 20) {
         var3.drawLine(1, var21, var20, var21);
      }

      double var22 = (double)this.graph.getNewestPoint() / 100.0;
      String var24 = var22 + " KB/s";
      int var25 = (int)((float)var7 - (float)(this.graph.getNewestPoint() / 10) * var9);
      int var26 = var25;
      int var27 = var25 - 6;
      if (var27 + var6 >= var7) {
         var27 = var7 - var6 - 3;
         var26 = var25 - 6;
      }

      int var28 = var3.textExtent(var24).x + 20;
      int var29 = var3.textExtent(var24).y + 5;
      int var30 = 11;
      int var31 = var30 + 10;
      int var32 = var30;
      int var33 = 1;
      if (this.reverse) {
         var30 = var4 - var28 - 10;
         var31 = var30 + 10;
         var33 = var4 - 1;
         var32 = var30 + var28;
      }

      var3.setForeground(this.labelTextColor);
      var3.setBackground(this.labelBackgroundColor);
      var3.fillRoundRectangle(var30, var27, var28, var29, 18, 18);
      var3.drawRoundRectangle(var30, var27, var28, var29, 18, 18);
      var3.drawText(var24, var31, var27 + 2);
      var3.setForeground(this.labelLineColor);
      var3.drawLine(var32, var26, var33, var25);
      var1.drawImage(var2, 0, 0);
      var3.dispose();
   }

   public void setGraphType(int var1) {
      this.graphType = var1;
   }

   public void setReverse(boolean var1) {
      this.reverse = var1;
   }

   public void updateDisplay() {
      this.graphColor1 = PreferenceLoader.loadColor("graph" + this.graphName + "Color1");
      this.graphColor2 = PreferenceLoader.loadColor("graph" + this.graphName + "Color2");
      this.backgroundColor = PreferenceLoader.loadColor("graphBackgroundColor");
      this.gridColor = PreferenceLoader.loadColor("graphGridColor");
      this.textColor = PreferenceLoader.loadColor("graphTextColor");
      this.labelBackgroundColor = PreferenceLoader.loadColor("graphLabelBackgroundColor");
      this.labelTextColor = PreferenceLoader.loadColor("graphLabelTextColor");
      this.labelLineColor = PreferenceLoader.loadColor("graphLabelLineColor");
      this.updateDelay = PreferenceLoader.loadInt("graphUpdateDelay");
   }
}
