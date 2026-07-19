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

   public GraphViewListener(SashViewFrame viewFrame, GraphCanvas graphCanvas, String showResString) {
      super(viewFrame);
      this.graphCanvas = graphCanvas;
      this.showResString = showResString;
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      menuManager.add(new HourlyGraphHistoryAction(this.graphCanvas.getGraphData()));
      menuManager.add(new ClearGraphHistoryAction(this.graphCanvas.getGraphData()));
      menuManager.add(new ReverseGraphAction(this.graphCanvas));
      this.createSashActions(menuManager, this.showResString);
   }

   // Menu action: opens the hourly graph-history window.
   private static class HourlyGraphHistoryAction extends Action {
      GraphData graph;

      public HourlyGraphHistoryAction(GraphData graph) {
         super(SResources.getString("graph.hourlyHistory"));
         this.setImageDescriptor(SResources.getImageDescriptor("graph"));
         this.graph = graph;
      }

      public void run() {
         new GraphHistory(this.graph);
      }
   }

   // Menu action: clears the graph history after user confirmation (needs the outer graph canvas' shell).
   private class ClearGraphHistoryAction extends Action {
      GraphData graph;

      public ClearGraphHistoryAction(GraphData graph) {
         super(SResources.getString("graph.clearHistory"));
         this.setImageDescriptor(SResources.getImageDescriptor("clear"));
         this.graph = graph;
      }

      public void run() {
         MessageBox messageBox = new MessageBox(GraphViewListener.this.graphCanvas.getShell(), 196);
         messageBox.setMessage(SResources.getString("mi.areYouSure"));
         if (messageBox.open() == 64) {
            this.graph.clearHistory();
         }
      }
   }

   // Menu action: reverses the graph's drawing orientation.
   private static class ReverseGraphAction extends Action {
      GraphCanvas graphCanvas;

      public ReverseGraphAction(GraphCanvas graphCanvas) {
         super(SResources.getString("graph.reverseGraph"));
         this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
         this.graphCanvas = graphCanvas;
      }

      public void run() {
         this.graphCanvas.reverse();
      }
   }
}
