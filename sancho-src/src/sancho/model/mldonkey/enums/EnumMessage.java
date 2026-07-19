package sancho.model.mldonkey.enums;

public class EnumMessage extends AbstractEnum {
   public static final EnumMessage UNKNOWN = new EnumMessage(0, "unknown");
   public static final EnumMessage SERVER = new EnumMessage(1, "server");
   public static final EnumMessage PUBLIC = new EnumMessage(2, "public");
   public static final EnumMessage PRIVATE = new EnumMessage(4, "pivate");

   private EnumMessage(int value, String name) {
      super(value, "e.message." + name);
   }

   public static final EnumMessage intToEnum(int value) {
      switch (value) {
         case 0:
            return SERVER;
         case 1:
            return PUBLIC;
         case 2:
            return PRIVATE;
         default:
            return UNKNOWN;
      }
   }
}
