package sancho.view.statistics;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class GraphViewListener$HourlyGraphHistoryAction extends Action {
   GraphData graph;

   public GraphViewListener$HourlyGraphHistoryAction(GraphData var1) {
      super(SResources.getString("graph.hourlyHistory"));
      this.setImageDescriptor(SResources.getImageDescriptor("graph"));
      this.graph = var1;
   }

   public void run() {
      new GraphHistory(this.graph);
   }
}
