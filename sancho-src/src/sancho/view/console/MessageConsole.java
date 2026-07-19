package sancho.view.console;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;

public class MessageConsole extends Console {
   private static final SimpleDateFormat sdFormatter = new SimpleDateFormat("[HH:mm:ss] ");
   protected int clientId;

   public MessageConsole(Composite composite, int style, int clientId) {
      super(composite, style);
      this.clientId = clientId;
   }

   public void prefixAppend() {
      this.infoDisplay.append(this.getTimeStamp() + "> ");
   }

   public String getTimeStamp() {
      return sdFormatter.format(new Date());
   }

   public void sendMessage() {
      if (Sancho.hasCollectionFactory()) {
         Object[] args = new Object[]{Integer.valueOf(this.clientId), this.input.getText()};
         Sancho.send((short)43, args);
      }
   }
}
