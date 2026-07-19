package sancho.view.utility;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class NoDuplicatesCombo extends Combo {
   public NoDuplicatesCombo(Composite composite, int style) {
      super(composite, style);
   }

   public void add(String text, int index) {
      if (!text.equals("")) {
         if (this.indexOf(text) != -1) {
            this.remove(text);
         }

         super.add(text, index);
      }
   }

   protected void checkSubclass() {
   }
}
