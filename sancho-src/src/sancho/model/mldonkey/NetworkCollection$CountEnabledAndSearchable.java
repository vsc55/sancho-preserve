package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;

class NetworkCollection$CountEnabledAndSearchable implements TObjectProcedure {
   private int counter;

   public boolean execute(Object var1) {
      Network var2 = (Network)var1;
      if (var2.isEnabled() && var2.isSearchable()) {
         this.counter++;
      }

      return true;
   }

   public int getCount() {
      return this.counter;
   }
}
