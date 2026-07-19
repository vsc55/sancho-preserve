package sancho.model.mldonkey.enums;

public class EnumFormat extends AbstractEnum {
   public static final EnumFormat UNKNOWN = new EnumFormat(1, "unknown");
   public static final EnumFormat GENERIC = new EnumFormat(2, "generic");
   public static final EnumFormat AVI = new EnumFormat(4, "avi");
   public static final EnumFormat MP3 = new EnumFormat(8, "mp3");
   public static final EnumFormat OGX = new EnumFormat(16, "ogx");

   private EnumFormat(int value, String name) {
      super(value, "e.format." + name);
   }

   public static EnumFormat byteToEnum(byte value) {
      switch (value) {
         case 1:
            return GENERIC;
         case 2:
            return AVI;
         case 3:
            return MP3;
         case 4:
            return OGX;
         default:
            return UNKNOWN;
      }
   }
}
