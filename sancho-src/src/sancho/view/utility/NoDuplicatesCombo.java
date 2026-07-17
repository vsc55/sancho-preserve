package sancho.view.utility;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class NoDuplicatesCombo extends Combo {
   public NoDuplicatesCombo(Composite var1, int var2) {
      super(var1, var2);
   }

   public void add(String var1, int var2) {
      if (!var1.equals("")) {
         if (this.indexOf(var1) != -1) {
            this.remove(var1);
         }

         super.add(var1, var2);
      }
   }

   protected void checkSubclass() {
   }
}
