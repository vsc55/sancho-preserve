package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;

class ClientCollection$DisposeAll implements TObjectProcedure {
   public boolean execute(Object var1) {
      ((Client)var1).deleteObservers();
      return true;
   }
}
