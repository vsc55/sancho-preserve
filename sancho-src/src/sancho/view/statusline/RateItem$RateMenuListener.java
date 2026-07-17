package sancho.view.statusline;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.core.Sancho;
import sancho.view.statusline.actions.DNDBoxAction;
import sancho.view.utility.dialogs.BandwidthDialog;

class RateItem$RateMenuListener implements IMenuListener {
   // $VF: synthetic field
   private final RateItem this$0;

   RateItem$RateMenuListener(RateItem var1) {
      this.this$0 = var1;
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (!Sancho.monitorMode) {
         var1.add(new DNDBoxAction(RateItem.access$100(this.this$0).getMainWindow()));
         new BandwidthDialog(RateItem.access$200(this.this$0).getShell(), var1);
         var1.add(new Separator());
         var1.add(new RateItem$CopyLabelsAction(this.this$0));
      }
   }
}
