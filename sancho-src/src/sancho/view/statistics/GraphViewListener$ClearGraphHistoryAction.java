package sancho.view.statistics;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.MessageBox;
import sancho.view.utility.SResources;

public class GraphViewListener$ClearGraphHistoryAction extends Action {
   GraphData graph;
   // $VF: synthetic field
   private final GraphViewListener this$0;

   public GraphViewListener$ClearGraphHistoryAction(GraphViewListener var1, GraphData var2) {
      super(SResources.getString("graph.clearHistory"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("clear"));
      this.graph = var2;
   }

   public void run() {
      MessageBox var1 = new MessageBox(GraphViewListener.access$000(this.this$0).getShell(), 196);
      var1.setMessage(SResources.getString("mi.areYouSure"));
      if (var1.open() == 64) {
         this.graph.clearHistory();
      }
   }
}
