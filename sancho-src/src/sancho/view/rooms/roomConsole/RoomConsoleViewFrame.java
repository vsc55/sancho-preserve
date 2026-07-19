package sancho.view.rooms.roomConsole;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class RoomConsoleViewFrame extends SashViewFrame {
   public RoomConsoleViewFrame(SashForm sashForm, String prefString, String imageString, AbstractTab tab) {
      super(sashForm, prefString, imageString, tab, true);
      this.createViewListener(new RoomConsoleViewListener(this));
   }
}
