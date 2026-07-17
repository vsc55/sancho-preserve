package sancho.view.utility;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.core.Sancho;
import sancho.view.statusline.actions.DNDBoxAction;
import sancho.view.statusline.actions.PreferencesAction;
import sancho.view.utility.dialogs.BandwidthDialog;

class DNDBox$DNDBoxMenuListener implements IMenuListener {
   // $VF: synthetic field
   private final DNDBox this$0;

   DNDBox$DNDBoxMenuListener(DNDBox var1) {
      this.this$0 = var1;
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (Sancho.monitorMode) {
         var1.add(new DNDBox$ExitAction(this.this$0.mainWindow.getShell()));
      } else {
         var1.add(new DNDBox$HideRestoreAction(this.this$0.mainWindow.getShell()));
         if (this.this$0.mainWindow.getShell().isVisible()) {
            var1.add(new DNDBoxAction(this.this$0.mainWindow));
         }

         var1.add(new Separator());
         var1.add(new PreferencesAction(this.this$0.mainWindow));
         new BandwidthDialog(this.this$0.shell, var1);
      }
   }
}
