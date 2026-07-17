package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;

class NetworkCollection$GetAllStats implements TObjectProcedure {
   public boolean execute(Object var1) {
      Network var2 = (Network)var1;
      if (var2.isEnabled()) {
         var2.getStats();
      }

      return true;
   }
}
