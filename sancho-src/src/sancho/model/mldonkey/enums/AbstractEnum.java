package sancho.model.mldonkey.enums;

import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public abstract class AbstractEnum {
   protected int value;
   protected String name;

   protected AbstractEnum(int var1, String var2) {
      this.value = var1;
      this.name = var1 == 0 ? "" : SResources.getString(var2);
   }

   public String getName() {
      return this.name != null ? this.name : "";
   }

   public int getValue() {
      return this.value;
   }

   public byte getByteValue() {
      return (byte)this.getIntValue();
   }

   public int getIntValue() {
      return SwissArmy.log2(this.getValue());
   }
}
