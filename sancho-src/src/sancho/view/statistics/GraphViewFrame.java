package sancho.view.statistics;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.SashViewFrame;

public class GraphViewFrame extends SashViewFrame {
   GraphCanvas graphCanvas;

   public GraphViewFrame(SashForm sashForm, String name, String tooltip, AbstractTab tab, String graphName) {
      super(sashForm, name, tooltip, tab);
      this.graphCanvas = new GraphCanvas(this.childComposite, SResources.getString(name), graphName, this);
      this.createViewListener(new GraphViewListener(this, this.graphCanvas, name.equals("graph.uploads") ? "graph.downloads" : "graph.uploads"));
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
