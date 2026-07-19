package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableMenuListener;

public class SelectAllAction extends Action {
   GTableMenuListener ml;
   boolean select;

   public SelectAllAction(GTableMenuListener menuListener, boolean select) {
      this.ml = menuListener;
      this.select = select;
      if (select) {
         this.setText(SResources.getString("mi.selectAll") + "\tCtrl+A");
      } else {
         this.setText(SResources.getString("mi.deselectAll") + "\tCtrl+Shift+A");
      }
   }

   public void run() {
      if (this.select) {
         this.ml.selectAll();
      } else {
         this.ml.deselectAll();
      }
   }
}
