package sancho.model.mldonkey.enums;

public class EnumFileState extends AbstractEnum {
   public static final EnumFileState UNKNOWN = new EnumFileState(0, "unknown");
   public static final EnumFileState DOWNLOADING = new EnumFileState(1, "downloading");
   public static final EnumFileState PAUSED = new EnumFileState(2, "paused");
   public static final EnumFileState DOWNLOADED = new EnumFileState(4, "downloaded");
   public static final EnumFileState SHARED = new EnumFileState(8, "shared");
   public static final EnumFileState CANCELLED = new EnumFileState(16, "cancelled");
   public static final EnumFileState NEW = new EnumFileState(32, "new");
   public static final EnumFileState ABORTED = new EnumFileState(64, "aborted");
   public static final EnumFileState QUEUED = new EnumFileState(128, "queued");

   private EnumFileState(int var1, String var2) {
      super(var1, "e.fileState." + var2);
   }

   public static EnumFileState byteToEnum(byte var0) {
      switch (var0) {
         case 0:
            return DOWNLOADING;
         case 1:
            return PAUSED;
         case 2:
            return DOWNLOADED;
         case 3:
            return SHARED;
         case 4:
            return CANCELLED;
         case 5:
            return NEW;
         case 6:
            return ABORTED;
         case 7:
            return QUEUED;
         default:
            return UNKNOWN;
      }
   }
}
