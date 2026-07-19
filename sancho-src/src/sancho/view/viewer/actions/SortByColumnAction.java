package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.view.viewer.GView;

public class SortByColumnAction extends Action {
   GView gView;
   int column;

   public SortByColumnAction(GView gView, int column) {
      super(gView.getColumnText(column), 2);
      this.gView = gView;
      this.column = column;
   }

   public void run() {
      this.gView.sortByColumn(this.column);
   }

   public boolean isChecked() {
      return this.gView.getSortColumn() == this.column;
   }
}
