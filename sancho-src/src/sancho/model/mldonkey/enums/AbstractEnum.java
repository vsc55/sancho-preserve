package sancho.model.mldonkey.enums;

import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public abstract class AbstractEnum {
   protected int value;
   protected String name;

   protected AbstractEnum(int value, String resourceKey) {
      this.value = value;
      this.name = value == 0 ? "" : SResources.getString(resourceKey);
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
