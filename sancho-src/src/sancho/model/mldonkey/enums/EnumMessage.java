package sancho.model.mldonkey.enums;

public class EnumMessage extends AbstractEnum {
   public static final EnumMessage UNKNOWN = new EnumMessage(0, "unknown");
   public static final EnumMessage SERVER = new EnumMessage(1, "server");
   public static final EnumMessage PUBLIC = new EnumMessage(2, "public");
   public static final EnumMessage PRIVATE = new EnumMessage(4, "pivate");

   private EnumMessage(int var1, String var2) {
      super(var1, "e.message." + var2);
   }

   public static final EnumMessage intToEnum(int var0) {
      switch (var0) {
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
