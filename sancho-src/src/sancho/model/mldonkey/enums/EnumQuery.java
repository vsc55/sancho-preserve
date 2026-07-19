package sancho.model.mldonkey.enums;

public class EnumQuery extends AbstractEnum {
   public static final EnumQuery UNKNOWN = new EnumQuery(0, "");
   public static final EnumQuery AND = new EnumQuery(1, "AND");
   public static final EnumQuery OR = new EnumQuery(2, "OR");
   public static final EnumQuery ANDNOT = new EnumQuery(4, "ANDNOT");
   public static final EnumQuery MODULE = new EnumQuery(8, "MODULE");
   public static final EnumQuery KEYWORDS = new EnumQuery(16, "KEYWORDS");
   public static final EnumQuery MINSIZE = new EnumQuery(32, "MINSIZE");
   public static final EnumQuery MAXSIZE = new EnumQuery(64, "MAXSIZE");
   public static final EnumQuery FORMAT = new EnumQuery(128, "FORMAT");
   public static final EnumQuery MEDIA = new EnumQuery(256, "MEDIA");
   public static final EnumQuery MP3_ARTIST = new EnumQuery(512, "MP3_ARTIST");
   public static final EnumQuery MP3_TITLE = new EnumQuery(1024, "MP3_TITLE");
   public static final EnumQuery MP3_ALBUM = new EnumQuery(2048, "MP3_ALBUM");
   public static final EnumQuery MP3_BITRATE = new EnumQuery(4096, "MP3_BITRATE");
   public static final EnumQuery HIDDEN = new EnumQuery(8192, "HIDDEN");

   private EnumQuery(int value, String name) {
      super(value, name);
   }

   public static EnumQuery byteToEnum(byte value) {
      switch (value) {
         case 0:
            return AND;
         case 1:
            return OR;
         case 2:
            return ANDNOT;
         case 3:
            return MODULE;
         case 4:
            return KEYWORDS;
         case 5:
            return MINSIZE;
         case 6:
            return MAXSIZE;
         case 7:
            return FORMAT;
         case 8:
            return MEDIA;
         case 9:
            return MP3_ARTIST;
         case 10:
            return MP3_TITLE;
         case 11:
            return MP3_ALBUM;
         case 12:
            return MP3_BITRATE;
         case 13:
            return HIDDEN;
         default:
            return UNKNOWN;
      }
   }
}
