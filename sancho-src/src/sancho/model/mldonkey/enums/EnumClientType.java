package sancho.model.mldonkey.enums;

public class EnumClientType extends AbstractEnum {
   public static final EnumClientType SOURCE = new EnumClientType(1, "source");
   public static final EnumClientType FRIEND = new EnumClientType(2, "friend");
   public static final EnumClientType CONTACT = new EnumClientType(4, "contact");

   private EnumClientType(int value, String name) {
      super(value, "e.clientType." + name);
   }

   public static EnumClientType byteToEnum(byte value) {
      switch (value) {
         case 1:
            return FRIEND;
         case 2:
            return CONTACT;
         default:
            return SOURCE;
      }
   }
}
