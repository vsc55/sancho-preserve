package sancho.view.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import sancho.core.Sancho;
import sancho.view.utility.SResources;

class ServerViewFrame$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final ServerViewFrame this$0;

   ServerViewFrame$2(ServerViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (Sancho.hasCollectionFactory()) {
         ServerViewFrame$AddServerByIPDialog var2 = new ServerViewFrame$AddServerByIPDialog(this.this$0, ServerViewFrame.access$000(this.this$0).getShell());
         if (var2.open() == 0) {
            String var3 = var2.getName();
            short var4 = (short)var2.getPort();
            InetAddress var5 = null;

            try {
               var5 = InetAddress.getByName(var3);
            } catch (UnknownHostException var8) {
               MessageBox var7 = new MessageBox(ServerViewFrame.access$100(this.this$0).getShell(), 40);
               var7.setText(SResources.getString("l.lookupError"));
               var7.setMessage(SResources.getString("l.resolveError"));
               var7.open();
            }

            if (Sancho.hasCollectionFactory() && var5 != null) {
               ServerViewFrame.access$200(this.this$0).getCore().getServerCollection().addServer(var2.getNetwork(), var5, var4);
            }
         }
      }
   }
}
