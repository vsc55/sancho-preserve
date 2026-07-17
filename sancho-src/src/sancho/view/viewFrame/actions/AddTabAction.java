package sancho.view.viewFrame.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedViewFrame;

public class AddTabAction extends Action {
   TabbedViewFrame viewFrame;

   public AddTabAction(TabbedViewFrame var1) {
      super(SResources.getString("mi.d.addTab"));
      this.setImageDescriptor(SResources.getImageDescriptor("plus"));
      this.viewFrame = var1;
   }

   public void run() {
      InputDialog var1 = new InputDialog(
         this.viewFrame.getCTabFolder().getShell(),
         SResources.getString("ti.d.tabName"),
         SResources.getString("ti.d.tabName"),
         SResources.getString("My new tab!"),
         null
      );
      if (var1.open() != 1) {
         String var2 = var1.getValue();
         this.viewFrame.createItem(var2);
      }
   }
}
