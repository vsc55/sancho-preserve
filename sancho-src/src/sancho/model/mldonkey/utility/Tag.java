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

   public void read(MessageBuffer buffer) {
      synchronized (this) {
         this.name = buffer.getString();
         this.enumType = EnumType.byteToEnum(buffer.getByte());
         if (this.enumType == EnumType.STRING) {
            this.stringValue = buffer.getString();
         } else if (this.enumType == EnumType.U_INT16) {
            this.value = buffer.getUInt16();
         } else if (this.enumType == EnumType.U_INT8) {
            this.value = buffer.getInt8();
         } else if (this.enumType == EnumType.IPADDRESS) {
            Addr addr = UtilityFactory.getAddr(Sancho.getCore());
            addr.read(false, buffer);
         } else if (this.enumType == EnumType.PAIR) {
            this.value = buffer.getInt32();
            int second = buffer.getInt32();
            this.stringValue = this.value + " " + second;
         } else {
            this.value = buffer.getInt32();
         }
      }
   }

   public synchronized String toString() {
      return this.enumType == EnumType.STRING ? this.getStringValue() : String.valueOf(this.getValue()).intern();
   }
}
