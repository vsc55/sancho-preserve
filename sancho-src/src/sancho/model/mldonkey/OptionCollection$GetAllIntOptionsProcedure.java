package sancho.model.mldonkey;

import gnu.trove.TObjectObjectProcedure;
import java.util.ArrayList;
import sancho.model.mldonkey.enums.EnumTagType;

class OptionCollection$GetAllIntOptionsProcedure implements TObjectObjectProcedure {
   ArrayList stringList = new ArrayList();

   public boolean execute(Object var1, Object var2) {
      String var3 = (String)var1;
      Option var4 = (Option)var2;
      if (this.isInt(var4)) {
         this.stringList.add(var3);
      }

      return true;
   }

   protected boolean isInt(Option var1) {
      if (var1.getType() == EnumTagType.INT) {
         return true;
      } else {
         try {
            Integer.parseInt(var1.getValue());
            return true;
         } catch (NumberFormatException var3) {
            return false;
         }
      }
   }

   public String[] getOptionList() {
      String[] var1 = new String[this.stringList.size()];
      this.stringList.toArray(var1);
      return var1;
   }
}
