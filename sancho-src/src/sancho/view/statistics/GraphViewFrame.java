package sancho.view.statistics;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.SashViewFrame;

public class GraphViewFrame extends SashViewFrame {
   GraphCanvas graphCanvas;

   public GraphViewFrame(SashForm var1, String var2, String var3, AbstractTab var4, String var5) {
      super(var1, var2, var3, var4);
      this.graphCanvas = new GraphCanvas(this.childComposite, SResources.getString(var2), var5, this);
      this.createViewListener(new GraphViewListener(this, this.graphCanvas, var2.equals("graph.uploads") ? "graph.downloads" : "graph.uploads"));
   }

   public GraphCanvas getGraphCanvas() {
      return this.graphCanvas;
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.graphCanvas.updateDisplay();
   }

   public void dispose() {
      this.graphCanvas.dispose();
      super.dispose();
   }
}
