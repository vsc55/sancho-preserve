package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;

class FileCollection$DisposeAll implements TObjectProcedure {
   public boolean execute(Object var1) {
      ((File)var1).dispose();
      return true;
   }
}
