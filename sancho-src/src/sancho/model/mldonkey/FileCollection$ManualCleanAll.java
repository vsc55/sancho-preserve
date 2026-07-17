package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;

class FileCollection$ManualCleanAll implements TObjectProcedure {
   public boolean execute(Object var1) {
      ((File)var1).manualClean();
      return true;
   }
}
