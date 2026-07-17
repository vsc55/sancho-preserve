package sancho.model.mldonkey.enums;

import sancho.core.Sancho;

public class EnumType extends AbstractEnum {
   public static final EnumType UNKNOWN = new EnumType(0, "UNKNOWN");
   public static final EnumType UNSIGNED_INT = new EnumType(1, "UNSIGNED_INT");
   public static final EnumType SIGNED_INT = new EnumType(2, "SIGNED_INT");
   public static final EnumType STRING = new EnumType(4, "STRING");
   public static final EnumType IPADDRESS = new EnumType(8, "IPADDRESS");
   public static final EnumType U_INT16 = new EnumType(16, "UINT16");
   public static final EnumType U_INT8 = new EnumType(32, "UINT8");
   public static final EnumType PAIR = new EnumType(64, "PAIR");

   private EnumType(int var1, String var2) {
      super(var1, var2);
   }

   public static EnumType byteToEnum(byte var0) {
      switch (var0) {
         case 0:
            return UNSIGNED_INT;
         case 1:
            return SIGNED_INT;
         case 2:
            return STRING;
         case 3:
            return IPADDRESS;
         case 4:
            return U_INT16;
         case 5:
            return U_INT8;
         case 6:
            return PAIR;
         default:
            Sancho.pDebug("ET: Unknown Type");
            return UNKNOWN;
      }
   }
}
