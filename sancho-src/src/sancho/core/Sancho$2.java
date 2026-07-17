package sancho.core;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class Sancho$2 implements Listener {
   public void handleEvent(Event var1) {
      if (Sancho.access$100() && Sancho.access$200() != null) {
         Sancho.access$200().delete();
      }
   }
}
