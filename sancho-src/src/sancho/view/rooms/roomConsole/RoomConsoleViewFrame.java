package sancho.view.rooms.roomConsole;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class RoomConsoleViewFrame extends SashViewFrame {
   public RoomConsoleViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4, true);
      this.createViewListener(new RoomConsoleViewListener(this));
   }
}
