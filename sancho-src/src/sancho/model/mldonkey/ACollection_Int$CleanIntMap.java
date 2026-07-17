package sancho.model.mldonkey;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectProcedure;

class ACollection_Int$CleanIntMap implements TIntObjectProcedure {
   TIntArrayList retainIntList;

   public ACollection_Int$CleanIntMap(TIntArrayList var1) {
      this.retainIntList = var1;
   }

   public boolean execute(int var1, Object var2) {
      return this.retainIntList.contains(var1);
   }
}
