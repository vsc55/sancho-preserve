package sancho.model.mldonkey.enums;

public class EnumRoomState extends AbstractEnum {
   public static final EnumRoomState UNKNOWN = new EnumRoomState(0, "unknown");
   public static final EnumRoomState OPEN = new EnumRoomState(1, "open");
   public static final EnumRoomState CLOSED = new EnumRoomState(2, "closed");
   public static final EnumRoomState PAUSED = new EnumRoomState(4, "paused");

   private EnumRoomState(int value, String name) {
      super(value, "e.roomState." + name);
   }

   public static EnumRoomState intToEnum(int value) {
      switch (value) {
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
