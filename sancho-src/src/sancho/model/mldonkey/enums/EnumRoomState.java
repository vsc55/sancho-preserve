package sancho.model.mldonkey.enums;

public class EnumRoomState extends AbstractEnum {
   public static final EnumRoomState UNKNOWN = new EnumRoomState(0, "unknown");
   public static final EnumRoomState OPEN = new EnumRoomState(1, "open");
   public static final EnumRoomState CLOSED = new EnumRoomState(2, "closed");
   public static final EnumRoomState PAUSED = new EnumRoomState(4, "paused");

   private EnumRoomState(int var1, String var2) {
      super(var1, "e.roomState." + var2);
   }

   public static EnumRoomState intToEnum(int var0) {
      switch (var0) {
         case 0:
            return OPEN;
         case 1:
            return CLOSED;
         case 2:
            return PAUSED;
         default:
            return UNKNOWN;
      }
   }
}
