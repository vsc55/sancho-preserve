package sancho.view.statusline.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import sancho.view.utility.SResources;
import sancho.view.utility.dialogs.BandwidthDialog;

public class RateBandwidthDialogAction extends Action {
   Shell shell;

   public RateBandwidthDialogAction(Shell shell) {
      super(SResources.getString("l.bandwidthSettings"));
      this.setImageDescriptor(SResources.getImageDescriptor("down_arrow_green"));
      this.shell = shell;
   }

   public void run() {
      new BandwidthDialog(this.shell).open();
   }
}
