package sancho.view.statistics;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class GraphViewListener$ReverseGraphAction extends Action {
   GraphCanvas graphCanvas;

   public GraphViewListener$ReverseGraphAction(GraphCanvas var1) {
      super(SResources.getString("graph.reverseGraph"));
      this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
      this.graphCanvas = var1;
   }

   public void run() {
      this.graphCanvas.reverse();
   }
}
