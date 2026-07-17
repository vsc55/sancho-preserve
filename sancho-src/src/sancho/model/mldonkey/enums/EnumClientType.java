package sancho.model.mldonkey.enums;

public class EnumClientType extends AbstractEnum {
   public static final EnumClientType SOURCE = new EnumClientType(1, "source");
   public static final EnumClientType FRIEND = new EnumClientType(2, "friend");
   public static final EnumClientType CONTACT = new EnumClientType(4, "contact");

   private EnumClientType(int var1, String var2) {
      super(var1, "e.clientType." + var2);
   }

   public static EnumClientType byteToEnum(byte var0) {
      switch (var0) {
         case 1:
            return FRIEND;
         case 2:
            return CONTACT;
         default:
            return SOURCE;
      }
   }
}
