package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.core.Sancho;
import sancho.view.utility.SResources;

public class RefreshUploadsAction extends Action {
   public RefreshUploadsAction() {
      super(SResources.getString("mi.refresh"));
      this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
   }

   public void run() {
      Sancho.send((short)49);
   }
}
