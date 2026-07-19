package sancho.view.statistics;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.MessageBox;
import sancho.view.utility.SResources;
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
      var1.add(new HourlyGraphHistoryAction(this.graphCanvas.getGraphData()));
      var1.add(new ClearGraphHistoryAction(this.graphCanvas.getGraphData()));
      var1.add(new ReverseGraphAction(this.graphCanvas));
      this.createSashActions(var1, this.showResString);
   }

   // Menu action: opens the hourly graph-history window.
   private static class HourlyGraphHistoryAction extends Action {
      GraphData graph;

      public HourlyGraphHistoryAction(GraphData var1) {
         super(SResources.getString("graph.hourlyHistory"));
         this.setImageDescriptor(SResources.getImageDescriptor("graph"));
         this.graph = var1;
      }

      public void run() {
         new GraphHistory(this.graph);
      }
   }

   // Menu action: clears the graph history after user confirmation (needs the outer graph canvas' shell).
   private class ClearGraphHistoryAction extends Action {
      GraphData graph;

      public ClearGraphHistoryAction(GraphData var1) {
         super(SResources.getString("graph.clearHistory"));
         this.setImageDescriptor(SResources.getImageDescriptor("clear"));
         this.graph = var1;
      }

      public void run() {
         MessageBox var1 = new MessageBox(GraphViewListener.this.graphCanvas.getShell(), 196);
         var1.setMessage(SResources.getString("mi.areYouSure"));
         if (var1.open() == 64) {
            this.graph.clearHistory();
         }
      }
   }

   // Menu action: reverses the graph's drawing orientation.
   private static class ReverseGraphAction extends Action {
      GraphCanvas graphCanvas;

      public ReverseGraphAction(GraphCanvas var1) {
         super(SResources.getString("graph.reverseGraph"));
         this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
         this.graphCanvas = var1;
      }

      public void run() {
         this.graphCanvas.reverse();
      }
   }
}
