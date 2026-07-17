package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;

class SharedFileCollection$CalculateTotalSize implements TObjectProcedure {
   long total;

   public boolean execute(Object var1) {
      SharedFile var2 = (SharedFile)var1;
      this.total = this.total + var2.getSize();
      return true;
   }

   public long getTotal() {
      return this.total;
   }
}
