package sancho.view.server;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;

class ServerViewFrame$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final ServerViewFrame this$0;

   ServerViewFrame$1(ServerViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (Sancho.hasCollectionFactory()) {
         this.this$0.getCore().getServerCollection().cleanOldServers();
      }
   }
}
