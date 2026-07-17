package sancho.view.server;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;
import sancho.view.utility.SResources;

class ServerViewFrame$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final ServerViewFrame this$0;

   ServerViewFrame$3(ServerViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      InputDialog var2 = new InputDialog(
         ServerViewFrame.access$300(this.this$0).getShell(),
         SResources.getString("ti.s.addServerMet"),
         SResources.getString("t.srv.linkToMet"),
         SResources.getString("t.srv.linkToMetDefault"),
         new ServerViewFrame$HTTPValidator()
      );
      var2.open();
      String var3 = var2.getValue();
      if (var3 != null && Sancho.hasCollectionFactory()) {
         this.this$0.getCore().getServerCollection().addServerList(var3);
      }
   }
}
