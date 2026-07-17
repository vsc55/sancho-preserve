package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;
import java.util.ArrayList;
import java.util.List;

class FileCollection$GetAllInteresting implements TObjectProcedure {
   List arrayList = new ArrayList();

   public FileCollection$GetAllInteresting() {
   }

   public Object[] getArray() {
      return this.arrayList.toArray();
   }

   public boolean execute(Object var1) {
      File var2 = (File)var1;
      if (var2.isInteresting()) {
         this.arrayList.add(var2);
      }

      return true;
   }
}
