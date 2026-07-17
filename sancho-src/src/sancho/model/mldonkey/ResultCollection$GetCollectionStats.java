package sancho.model.mldonkey;

import gnu.trove.TIntObjectProcedure;
import sancho.utility.ObjectMap;

class ResultCollection$GetCollectionStats implements TIntObjectProcedure {
   private StringBuffer sb = new StringBuffer();

   public ResultCollection$GetCollectionStats() {
   }

   public boolean execute(int var1, Object var2) {
      ObjectMap var3 = (ObjectMap)var2;
      this.sb.append("[");
      this.sb.append(var1);
      this.sb.append("|");
      this.sb.append(var3.size());
      this.sb.append("]");
      return true;
   }

   public String getStats() {
      return this.sb.toString().intern();
   }
}
