package sancho.view.console;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;

public class RoomConsole extends Console {
   protected int roomNumber;

   public RoomConsole(Composite composite, int style, int roomNumber) {
      super(composite, style);
      this.roomNumber = roomNumber;
   }

   public void sendMessage() {
      if (Sancho.hasCollectionFactory()) {
         Object[] args = new Object[]{Integer.valueOf(this.roomNumber), Byte.valueOf((byte)1), Integer.valueOf(0), this.input.getText()};
         Sancho.send((short)39, args);
      }
   }
}
