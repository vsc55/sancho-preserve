package sancho.model.mldonkey.enums;

import java.util.TreeMap;

public class EnumExtension extends AbstractEnum {
   public static final EnumExtension UNKNOWN = new EnumExtension(0, "");
   public static final EnumExtension AUDIO = new EnumExtension(1, "audio");
   public static final EnumExtension VIDEO = new EnumExtension(2, "video");
   public static final EnumExtension ARCHIVE = new EnumExtension(4, "archive");
   public static final EnumExtension CDIMAGE = new EnumExtension(8, "cdimage");
   public static final EnumExtension IMAGE = new EnumExtension(16, "image");
   public static final EnumExtension DOCUMENT = new EnumExtension(32, "document");
   public static final EnumExtension PROGRAM = new EnumExtension(64, "program");
   public static final EnumExtension EMULECOLLECTION = new EnumExtension(128, "emulecollection");
   protected static final TreeMap EXT_MAP = new TreeMap();

   private EnumExtension(int var1, String var2) {
      super(var1, "e.extension." + var2);
   }

   private static synchronized void ADD_TO_MAP(EnumExtension var0, String[] var1) {
      for (int var2 = 0; var2 < var1.length; var2++) {
         EXT_MAP.put(var1[var2], var0);
      }
   }

   public static synchronized EnumExtension GET_EXT(String var0) {
      EnumExtension var1 = (EnumExtension)EXT_MAP.get(var0);
      if (var1 == null) {
         int var2 = var0.length();
         if (var2 == 3) {
            String var3 = var0.toLowerCase();
            if (var3.startsWith("r")) {
               String var4 = var3.substring(1);

               try {
                  Integer.parseInt(var4);
                  var1 = ARCHIVE;
               } catch (Exception var6) {
               }
            }
         }
      }

      return var1;
   }

   static {
      ADD_TO_MAP(
         AUDIO,
         new String[]{
            "669",
            "aac",
            "aif",
            "aiff",
            "amf",
            "ams",
            "ape",
            "au",
            "dbm",
            "dmf",
            "dsm",
            "far",
            "flac",
            "it",
            "m4a",
            "mdl",
            "med",
            "mid",
            "midi",
            "mka",
            "mod",
            "mol",
            "mp1",
            "mp2",
            "mp3",
            "mpa",
            "mpc",
            "mpp",
            "mtm",
            "nst",
            "ogg",
            "okt",
            "psm",
            "ptm",
            "ra",
            "rmi",
            "s3m",
            "stm",
            "ult",
            "umx",
            "wav",
            "wma",
            "wow",
            "xm"
         }
      );
      ADD_TO_MAP(
         VIDEO,
         new String[]{
            "3g2",
            "3gp",
            "3gp2",
            "3gpp",
            "asf",
            "avi",
            "divx",
            "m1v",
            "m2v",
            "mkv",
            "mov",
            "mp1v",
            "mp2v",
            "mp4",
            "mpe",
            "mpeg",
            "mpg",
            "mps",
            "mpv",
            "mpv1",
            "mpv2",
            "ogm",
            "qt",
            "ram",
            "rm",
            "rmvb",
            "rv",
            "rv9",
            "swf",
            "ts",
            "vivo",
            "vob",
            "wmv",
            "xvid"
         }
      );
      ADD_TO_MAP(
         DOCUMENT, new String[]{"chm", "css", "diz", "doc", "dot", "hlp", "htm", "html", "nfo", "pdf", "pps", "ppt", "ps", "rtf", "txt", "wri", "xls", "xml"}
      );
      ADD_TO_MAP(
         ARCHIVE,
         new String[]{
            "7z", "ace", "alz", "arj", "bz2", "cab", "cbz", "cbr", "gz", "hqx", "lha", "lzh", "msi", "rar", "sea", "sit", "tar", "tgz", "uc2", "z", "zip"
         }
      );
      ADD_TO_MAP(CDIMAGE, new String[]{"bin", "bwa", "bwi", "bws", "bwt", "ccd", "cue", "dmg", "dmz", "img", "iso", "mdf", "mds", "nrg", "sub", "toast"});
      ADD_TO_MAP(
         IMAGE,
         new String[]{"bmp", "dcx", "emf", "gif", "ico", "jpeg", "jpg", "pct", "pcx", "pic", "pict", "png", "psd", "psp", "tga", "tif", "tiff", "wmf", "xif"}
      );
      ADD_TO_MAP(PROGRAM, new String[]{"bat", "cmd", "com", "exe"});
      ADD_TO_MAP(EMULECOLLECTION, new String[]{"emulecollection"});
   }
}
