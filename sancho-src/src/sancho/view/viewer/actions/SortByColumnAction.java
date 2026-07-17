package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.view.viewer.GView;

public class SortByColumnAction extends Action {
   GView gView;
   int column;

   public SortByColumnAction(GView var1, int var2) {
      super(var1.getColumnText(var2), 2);
      this.gView = var1;
      this.column = var2;
   }

   public void run() {
      this.gView.sortByColumn(this.column);
   }

   public boolean isChecked() {
      return this.gView.getSortColumn() == this.column;
   }
}
