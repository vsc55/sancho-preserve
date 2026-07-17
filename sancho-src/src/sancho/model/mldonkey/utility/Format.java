package sancho.model.mldonkey.utility;

import sancho.model.mldonkey.enums.EnumFormat;

public class Format {
   private String avi_codec;
   private int avi_fps;
   private int avi_height;
   private int avi_rate;
   private int avi_width;
   private String extension;
   private EnumFormat format;
   private String kind;
   private String mp3_album;
   private String mp3_artist;
   private String mp3_comment;
   private int mp3_genre;
   private String mp3_title;
   private int mp3_tracknum;
   private String mp3_year;

   public synchronized String getAVICodec() {
      return this.avi_codec != null ? this.avi_codec : "";
   }

   public synchronized int getAVIFPS() {
      return this.avi_fps;
   }

   public synchronized int getAVIHeight() {
      return this.avi_height;
   }

   public synchronized int getAVIRate() {
      return this.avi_rate;
   }

   public synchronized int getAVIWidth() {
      return this.avi_width;
   }

   public synchronized String getExtension() {
      return this.extension != null ? this.extension : "";
   }

   public synchronized EnumFormat getFormat() {
      return this.format != null ? this.format : EnumFormat.UNKNOWN;
   }

   public synchronized String getKind() {
      return this.kind == null ? "" : this.kind;
   }

   public synchronized String getMP3Album() {
      return this.mp3_album == null ? "" : this.mp3_album;
   }

   public synchronized String getMP3Artist() {
      return this.mp3_artist == null ? "" : this.mp3_artist;
   }

   public synchronized String getMP3Comment() {
      return this.mp3_comment;
   }

   public synchronized int getMP3Genre() {
      return this.mp3_genre;
   }

   public synchronized String getMP3Title() {
      return this.mp3_title == null ? "" : this.mp3_title;
   }

   public synchronized int getMP3TrackNum() {
      return this.mp3_tracknum;
   }

   public synchronized String getMP3Year() {
      return this.mp3_year == null ? "" : this.mp3_year;
   }

   public synchronized void read(MessageBuffer var1) {
      this.format = EnumFormat.byteToEnum(var1.getByte());
      if (this.format == EnumFormat.GENERIC) {
         this.extension = var1.getString();
         this.kind = var1.getString();
      } else if (this.format == EnumFormat.AVI) {
         this.avi_codec = var1.getString();
         this.avi_width = var1.getInt32();
         this.avi_height = var1.getInt32();
         this.avi_fps = var1.getInt32();
         this.avi_rate = var1.getInt32();
      } else if (this.format == EnumFormat.MP3) {
         this.mp3_title = var1.getString();
         this.mp3_artist = var1.getString();
         this.mp3_album = var1.getString();
         this.mp3_year = var1.getString();
         this.mp3_comment = var1.getString();
         this.mp3_tracknum = var1.getInt32();
         this.mp3_genre = var1.getInt32();
      } else if (this.format == EnumFormat.OGX) {
         Format_OGx var2 = UtilityFactory.getFormat_OGx();
         var2.read(var1);
      }
   }
}
