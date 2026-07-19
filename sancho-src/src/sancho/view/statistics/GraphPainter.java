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

   public GraphPainter(Composite parent, GraphData graph) {
      this.parent = parent;
      this.graph = graph;
      this.graphName = graph.getGraphName();
      this.updateDisplay();
   }

   private void drawBarGraph(GC gc, int width, float baseY, float scale) {
      int pointIndex = this.graph.getInsertAt() - 1;
      int remaining = 1600 > this.graph.getNumPoints() ? this.graph.getNumPoints() : 1600;
      gc.setForeground(this.graphColor1);
      int startX = 1;
      byte step = 1;
      if (this.reverse) {
         startX = width - 2;
         step = -1;
      }

      for (int x = startX; 0 <= x && x < width && remaining > 0; remaining--) {
         if (pointIndex < 0) {
            pointIndex = 1599;
         }

         float value = (float)(this.graph.getPointAt(pointIndex) / 10);
         value = baseY - value * scale;
         gc.drawLine(x, (int)baseY + 1, x, (int)value);
         pointIndex--;
         x += step;
      }
   }

   private void drawGradiantGraph(GC gc, int width, float baseY, float scale) {
      int pointIndex = this.graph.getInsertAt() - 1;
      int remaining = 1600 > this.graph.getNumPoints() ? this.graph.getNumPoints() : 1600;
      gc.setBackground(this.graphColor1);
      gc.setForeground(this.graphColor2);
      int startX = 1;
      byte step = 1;
      if (this.reverse) {
         startX = width - 2;
         step = -1;
      }

      for (int x = startX; 0 <= x && x < width && remaining > 0; remaining--) {
         if (pointIndex < 0) {
            pointIndex = 1599;
         }

         float value = (float)(this.graph.getPointAt(pointIndex) / 10);
         value = baseY - value * scale;
         gc.fillGradientRectangle(x, (int)baseY + 1, 1, (int)(value - baseY), true);
         pointIndex--;
         x += step;
      }
   }

   private void drawLineGraph(GC gc, int width, float baseY, float scale) {
      int pointIndex = this.graph.getInsertAt() - 1;
      int remaining = 1600 > this.graph.getNumPoints() ? this.graph.getNumPoints() : 1600;
      float prevY = -1.0F;
      gc.setForeground(this.graphColor1);
      gc.setLineWidth(3);
      int startX = 1;
      byte step = 1;
      byte prevStep = -1;
      if (this.reverse) {
         startX = width - 2;
         step = -1;
         prevStep = 1;
      }

      for (int x = startX; 0 <= x && x < width && remaining > 0; remaining--) {
         if (pointIndex < 0) {
            pointIndex = 1599;
         }

         float value = (float)(this.graph.getPointAt(pointIndex) / 10);
         value = baseY - value * scale;
         if (prevY == -1.0F) {
            prevY = value;
         }

         gc.drawLine(x + prevStep, (int)prevY, x, (int)value);
         prevY = value;
         pointIndex--;
         x += step;
      }

      gc.setLineWidth(1);
   }

   public void paint(GC gc, Image image) {
      GC imageGc = new GC(image);
      imageGc.setBackground(this.backgroundColor);
      imageGc.setForeground(this.backgroundColor);
      int width = image.getBounds().width;
      int height = image.getBounds().height;
      imageGc.fillRectangle(0, 0, width, height);
      int lineHeight = imageGc.getFontMetrics().getHeight() + 2;
      int baseY = height - lineHeight;
      int maxX = width - 1;
      float scale = 0.0F;
      float maxValue = (float)(this.graph.findMax(width) / 10);
      scale = ((float)baseY - 10.0F) / maxValue;
      switch (this.graphType) {
         case 1:
            this.drawBarGraph(imageGc, width, (float)baseY, scale);
            break;
         case 2:
            this.drawLineGraph(imageGc, width, (float)baseY, scale);
            break;
         default:
            this.drawGradiantGraph(imageGc, width, (float)baseY, scale);
      }

      imageGc.setForeground(this.gridColor);
      int gridIndex = 0;
      int startX = 0;
      int endX = 1 + maxX;
      int stepX = 20;
      if (this.reverse) {
         startX = width - 1;
         stepX = -stepX;
         endX = 0;
      }

      int clientHeight = this.parent.getClientArea().height;
      int fontHeight = imageGc.getFontMetrics().getHeight();
      int textY = clientHeight - fontHeight;
      int delay = this.updateDelay > 0 ? this.updateDelay : 1;

      for (int x = startX; this.reverse ? x > endX : x < endX; x += stepX) {
         imageGc.drawLine(x, 0, x, baseY + 1);
         if (gridIndex > 0 && gridIndex % 3 == 0) {
            imageGc.drawText(SwissArmy.calcTimeOfSeconds((long)(delay * gridIndex * 20)), x - 5, textY, true);
         }

         gridIndex++;
      }

      int rightX = 1 + maxX;

      for (int y = baseY + 1; y > 0; y -= 20) {
         imageGc.drawLine(1, y, rightX, y);
      }

      double newestRate = (double)this.graph.getNewestPoint() / 100.0;
      String label = newestRate + " KB/s";
      int pointY = (int)((float)baseY - (float)(this.graph.getNewestPoint() / 10) * scale);
      int lineY = pointY;
      int labelY = pointY - 6;
      if (labelY + lineHeight >= baseY) {
         labelY = baseY - lineHeight - 3;
         lineY = pointY - 6;
      }

      int labelWidth = imageGc.textExtent(label).x + 20;
      int labelHeight = imageGc.textExtent(label).y + 5;
      int labelX = 11;
      int textX = labelX + 10;
      int lineStartX = labelX;
      int lineEndX = 1;
      if (this.reverse) {
         labelX = width - labelWidth - 10;
         textX = labelX + 10;
         lineEndX = width - 1;
         lineStartX = labelX + labelWidth;
      }

      imageGc.setForeground(this.labelTextColor);
      imageGc.setBackground(this.labelBackgroundColor);
      imageGc.fillRoundRectangle(labelX, labelY, labelWidth, labelHeight, 18, 18);
      imageGc.drawRoundRectangle(labelX, labelY, labelWidth, labelHeight, 18, 18);
      imageGc.drawText(label, textX, labelY + 2);
      imageGc.setForeground(this.labelLineColor);
      imageGc.drawLine(lineStartX, lineY, lineEndX, pointY);
      gc.drawImage(image, 0, 0);
      imageGc.dispose();
   }

   public void setGraphType(int type) {
      this.graphType = type;
   }

   public void setReverse(boolean reverse) {
      this.reverse = reverse;
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
