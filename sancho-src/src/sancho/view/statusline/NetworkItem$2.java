package sancho.view.statusline;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.core.Sancho;

class NetworkItem$2 implements DisposeListener {
   // $VF: synthetic field
   private final NetworkItem this$0;

   NetworkItem$2(NetworkItem var1) {
      this.this$0 = var1;
   }

   public void widgetDisposed(DisposeEvent var1) {
      if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getNetworkCollection().deleteObserver(this.this$0);
      }
   }
}
