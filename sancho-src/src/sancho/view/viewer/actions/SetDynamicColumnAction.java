package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.view.viewer.GView;

public class SetDynamicColumnAction extends Action {
   GView gView;
   int column;

   public SetDynamicColumnAction(GView gView, int column) {
      super(gView.getColumnText(column), 2);
      this.gView = gView;
      this.column = column;
   }

   public void run() {
      this.gView.setDynamicColumn(this.column);
   }

   public boolean isChecked() {
      return this.gView.getDynamicColumn() == this.column;
   }
}
