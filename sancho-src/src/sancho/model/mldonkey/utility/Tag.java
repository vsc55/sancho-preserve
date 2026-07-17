package sancho.model.mldonkey.utility;

import sancho.core.Sancho;
import sancho.model.mldonkey.enums.EnumType;

public class Tag {
   private EnumType enumType;
   private String name;
   private int value;
   private String stringValue;

   public synchronized String getName() {
      return this.name != null ? this.name : "";
   }

   public synchronized String getStringValue() {
      return this.stringValue != null ? this.stringValue : "";
   }

   public synchronized EnumType getType() {
      return this.enumType;
   }

   public synchronized int getValue() {
      return this.value;
   }

   public void read(MessageBuffer var1) {
      synchronized (this) {
         this.name = var1.getString();
         this.enumType = EnumType.byteToEnum(var1.getByte());
         if (this.enumType == EnumType.STRING) {
            this.stringValue = var1.getString();
         } else if (this.enumType == EnumType.U_INT16) {
            this.value = var1.getUInt16();
         } else if (this.enumType == EnumType.U_INT8) {
            this.value = var1.getInt8();
         } else if (this.enumType == EnumType.IPADDRESS) {
            Addr var3 = UtilityFactory.getAddr(Sancho.getCore());
            var3.read(false, var1);
         } else if (this.enumType == EnumType.PAIR) {
            this.value = var1.getInt32();
            int var6 = var1.getInt32();
            this.stringValue = this.value + " " + var6;
         } else {
            this.value = var1.getInt32();
         }
      }
   }

   public synchronized String toString() {
      return this.enumType == EnumType.STRING ? this.getStringValue() : String.valueOf(this.getValue()).intern();
   }
}
