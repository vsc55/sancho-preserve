package sancho.view.viewFrame.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedViewFrame;

public class AddTabAction extends Action {
   TabbedViewFrame viewFrame;

   public AddTabAction(TabbedViewFrame viewFrame) {
      super(SResources.getString("mi.d.addTab"));
      this.setImageDescriptor(SResources.getImageDescriptor("plus"));
      this.viewFrame = viewFrame;
   }

   public void run() {
      InputDialog dialog = new InputDialog(
         this.viewFrame.getCTabFolder().getShell(),
         SResources.getString("ti.d.tabName"),
         SResources.getString("ti.d.tabName"),
         SResources.getString("My new tab!"),
         null
      );
      if (dialog.open() != 1) {
         String name = dialog.getValue();
         this.viewFrame.createItem(name);
      }
   }
}
