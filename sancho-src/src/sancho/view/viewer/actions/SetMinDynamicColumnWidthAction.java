package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;

public class SetMinDynamicColumnWidthAction extends Action {
   GView gView;

   public SetMinDynamicColumnWidthAction(GView var1) {
      super(SResources.getString("mi.setMinWidth"));
      this.gView = var1;
   }

   public void run() {
      SetMinDynamicColumnWidthAction$MinWidthDialog var1 = new SetMinDynamicColumnWidthAction$MinWidthDialog(
         this.gView.getShell(), SResources.getString("mi.setMinWidth"), this.gView.getMinDynamicColumnWidth()
      );
      if (var1.open() == 0) {
         this.gView.setMinDynamicColumnWidth(var1.getIntValue());
      }
   }
}
