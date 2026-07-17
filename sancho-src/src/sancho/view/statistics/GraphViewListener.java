package sancho.view.statistics;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class GraphViewListener extends SashViewListener {
   private GraphCanvas graphCanvas;
   private String showResString;

   public GraphViewListener(SashViewFrame var1, GraphCanvas var2, String var3) {
      super(var1);
      this.graphCanvas = var2;
      this.showResString = var3;
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new GraphViewListener$HourlyGraphHistoryAction(this.graphCanvas.getGraphData()));
      var1.add(new GraphViewListener$ClearGraphHistoryAction(this, this.graphCanvas.getGraphData()));
      var1.add(new GraphViewListener$ReverseGraphAction(this.graphCanvas));
      this.createSashActions(var1, this.showResString);
   }

   // $VF: synthetic method
   static GraphCanvas access$000(GraphViewListener var0) {
      return var0.graphCanvas;
   }
}
