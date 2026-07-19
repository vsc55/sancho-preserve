package sancho.view.statistics;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewFrame.ViewFrame;

public class GraphCanvas extends Composite implements PaintListener, Runnable, DisposeListener {
   public static final short MAX_GRAPH_TYPES = 2;
   private Composite composite;
   private GraphData graphData;
   private String graphName;
   private GraphPainter graphPainter;
   private boolean graphReverse;
   private int graphType;
   private Image imageBuffer;
   private boolean needNewBuffer;
   private ViewFrame viewFrame;

   public GraphCanvas(Composite parent, String title, String graphName, ViewFrame viewFrame) {
      super(parent, 262144);
      this.viewFrame = viewFrame;
      this.composite = parent;
      this.graphName = graphName;
      this.graphData = new GraphData(parent.getDisplay(), title, graphName);
      this.graphPainter = new GraphPainter(parent, this.graphData);
      this.addPaintListener(this);
      this.addDisposeListener(this);
      this.addControlListener(new ControlAdapter() {
         public void controlResized(ControlEvent event) {
            GraphCanvas.this.needNewBuffer = true;
         }
      });
      this.addMouseListener(new MouseAdapter() {
         public void mouseDoubleClick(MouseEvent event) {
            GraphCanvas.this.toggleDisplay();
         }
      });
      this.updateDisplay();
   }

   public void addPoint(float value) {
      this.graphData.addPoint((int)(value * 100.0F));
   }

   public GraphData getGraphData() {
      return this.graphData;
   }

   public void paintControl(PaintEvent event) {
      // Also build the buffer when it's still null: the code assumed controlResized
      // (the only place setting needNewBuffer) always fires before the first paint,
      // which modern SWT doesn't guarantee -> paint() would get a null Image and
      // new GC(null) would throw.
      if (this.needNewBuffer || this.imageBuffer == null) {
         Rectangle rect = this.composite.getBounds();
         if (rect.height <= 0 || rect.width <= 0) {
            return;
         }

         if (this.imageBuffer != null) {
            this.imageBuffer.dispose();
         }

         this.imageBuffer = new Image(null, this.composite.getBounds());
      }

      this.graphPainter.paint(event.gc, this.imageBuffer);
      this.needNewBuffer = false;
   }

   public void redrawInThread() {
      if (!this.isDisposed() && this.viewFrame.isVisible()) {
         this.getDisplay().asyncExec(this);
      }
   }

   public void reverse() {
      this.graphReverse = !this.graphReverse;
      this.graphPainter.setReverse(this.graphReverse);
   }

   public void run() {
      if (!this.isDisposed() && this.isVisible()) {
         this.redraw();
      }
   }

   public void toggleDisplay() {
      this.graphType++;
      if (this.graphType > 2) {
         this.graphType = 0;
      }

      this.graphPainter.setGraphType(this.graphType);
      PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
      preferenceStore.setValue("graph" + this.graphName + "Type", this.graphType);
   }

   public void updateDisplay() {
      this.graphType = PreferenceLoader.loadInt("graph" + this.graphName + "Type");
      this.graphReverse = PreferenceLoader.loadBoolean("graph" + this.graphName + "Reverse");
      this.graphPainter.updateDisplay();
      this.graphPainter.setGraphType(this.graphType);
      this.graphPainter.setReverse(this.graphReverse);
   }

   public void widgetDisposed(DisposeEvent event) {
      if (this.imageBuffer != null && !this.imageBuffer.isDisposed()) {
         this.imageBuffer.dispose();
      }

      PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
      preferenceStore.setValue("graph" + this.graphName + "Type", this.graphType);
      preferenceStore.setValue("graph" + this.graphName + "Reverse", this.graphReverse);
   }

   public void dispose() {
      this.graphData.dispose();
   }
}
