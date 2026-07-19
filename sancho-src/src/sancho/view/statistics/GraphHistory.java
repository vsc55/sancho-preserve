package sancho.view.statistics;

import gnu.trove.TIntArrayList;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

public class GraphHistory implements PaintListener {
   private Shell shell;
   private GraphData graph;
   private TIntArrayList maxList;
   private TIntArrayList avgList;
   private Color gridColor = PreferenceLoader.loadColor("graphGridColor");
   private Color backgroundColor = PreferenceLoader.loadColor("graphBackgroundColor");
   private Color textColor = PreferenceLoader.loadColor("graphTextColor");
   private ScrollBar hScrollBar;
   private static String RS_HOUR = SResources.getString("graph.historyHour");
   private static String RS_AVG = SResources.getString("graph.historyAvg");
   private static String RS_MAX = SResources.getString("graph.historyMax");
   private static String RS_KBS = SResources.getString("graph.historyKBS");
   private static int BAR_WIDTH = 100;

   public GraphHistory(GraphData graph) {
      this.graph = graph;
      this.maxList = graph.getMaxList();
      this.avgList = graph.getAvgList();
      this.createContents();
   }

   public void createContents() {
      this.shell = new Shell(331120);
      this.hScrollBar = this.shell.getHorizontalBar();
      this.hScrollBar.setVisible(false);
      this.hScrollBar.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            GraphHistory.this.shell.redraw();
         }
      });
      this.shell.setImage(VersionInfo.getProgramIcon());
      this.shell.setText(this.graph.getTitle());
      this.shell.setLayout(new FillLayout());
      this.shell.addDisposeListener(new DisposeListener() {
         public synchronized void widgetDisposed(DisposeEvent event) {
            PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
            PreferenceConverter.setValue(preferenceStore, "graphHistoryWindowBounds", GraphHistory.this.shell.getBounds());
         }
      });
      this.shell.addControlListener(new ControlAdapter() {
         public void controlResized(ControlEvent event) {
            GraphHistory.this.adjustScrollBar();
         }
      });
      this.shell.addPaintListener(this);
      this.shell.setBounds(PreferenceLoader.loadRectangle("graphHistoryWindowBounds"));
      this.adjustScrollBar();
      this.shell.open();
   }

   public void adjustScrollBar() {
      int width = this.shell.getClientArea().width;
      int count = this.maxList.size();
      if (width >= BAR_WIDTH * count) {
         this.hScrollBar.setVisible(false);
      } else {
         this.hScrollBar.setVisible(true);
         this.hScrollBar.setMinimum(0);
         this.hScrollBar.setMaximum(count);
         this.hScrollBar.setIncrement(1);
         this.hScrollBar.setPageIncrement(1);
         this.hScrollBar.setSelection(0);
         this.hScrollBar.setThumb(width / BAR_WIDTH);
      }
   }

   public int getStart() {
      return !this.hScrollBar.isVisible() ? 0 : this.hScrollBar.getSelection();
   }

   public void paintControl(PaintEvent event) {
      if (this.shell.getClientArea().width >= 5 && this.shell.getClientArea().height >= 5) {
         int height = this.shell.getClientArea().height;
         int width = this.shell.getClientArea().width;
         Image image = new Image(this.shell.getDisplay(), this.shell.getClientArea());
         GC gc = new GC(image);
         gc.setBackground(this.backgroundColor);
         gc.fillRectangle(0, 0, this.shell.getClientArea().width, this.shell.getClientArea().height);
         gc.setForeground(this.gridColor);

         for (int x = 0; x < width; x += 20) {
            // x must be int: as a byte it overflowed past 127 (120 + 20 -> -116)
            // and the loop never terminated once the width was >= 128px, hanging the
            // UI when the graph-history window opened.
            gc.drawLine(x, 0, x, height + 1);
         }

         for (int y = height + 1; y > 0; y -= 20) {
            gc.drawLine(0, y, width + 1, y);
         }

         this.drawGraph(gc);
         event.gc.drawImage(image, 0, 0);
         gc.dispose();
         image.dispose();
      }
   }

   private void drawGraph(GC gc) {
      if (this.maxList.size() == 0) {
         gc.setForeground(this.textColor);
         gc.drawText(SResources.getString("graph.noHistory"), 0, 0);
      } else {
         int lineHeight = gc.getFontMetrics().getHeight() + 2;
         float graphHeight = (float)this.shell.getClientArea().height - (float)(lineHeight * 3);
         float max = 2.0F;

         for (int i = 0; i < this.maxList.size(); i++) {
            if ((float)(this.maxList.getQuick(i) / 10) > max) {
               max = (float)this.maxList.getQuick(i) / 10.0F;
            }
         }

         float scale = (graphHeight - 10.0F) / max;
         int x = 0;
         gc.setForeground(this.textColor);
         Color color1 = PreferenceLoader.loadColor("graph" + this.graph.getGraphName() + "Color1");
         Color color2 = PreferenceLoader.loadColor("graph" + this.graph.getGraphName() + "Color2");
         int height = this.shell.getClientArea().height;
         int start = this.getStart();
         int column = 0;

         for (int index = start; index < this.maxList.size(); column++) {
            int maxValue = this.maxList.getQuick(index);
            int avgValue = this.avgList.getQuick(index);
            x = column * BAR_WIDTH;
            float maxHeight = (float)(maxValue / 10);
            float avgHeight = (float)(avgValue / 10);
            maxHeight *= scale;
            avgHeight *= scale;
            gc.setBackground(color2);
            gc.fillRectangle(x, height + 1, BAR_WIDTH - 2, -((int)maxHeight));
            gc.setBackground(color1);
            gc.fillRectangle(x, height + 1, BAR_WIDTH - 2, -((int)avgHeight));
            gc.drawText(RS_HOUR + (index + 1), x, 0, true);
            gc.drawText(RS_AVG + (double)avgValue / 100.0 + RS_KBS, x, lineHeight, true);
            gc.drawText(RS_MAX + (double)maxValue / 100.0 + RS_KBS, x, 2 * lineHeight, true);
            index++;
         }
      }
   }
}
