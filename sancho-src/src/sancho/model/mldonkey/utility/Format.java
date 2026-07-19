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

   public synchronized void read(MessageBuffer buffer) {
      this.format = EnumFormat.byteToEnum(buffer.getByte());
      if (this.format == EnumFormat.GENERIC) {
         this.extension = buffer.getString();
         this.kind = buffer.getString();
      } else if (this.format == EnumFormat.AVI) {
         this.avi_codec = buffer.getString();
         this.avi_width = buffer.getInt32();
         this.avi_height = buffer.getInt32();
         this.avi_fps = buffer.getInt32();
         this.avi_rate = buffer.getInt32();
      } else if (this.format == EnumFormat.MP3) {
         this.mp3_title = buffer.getString();
         this.mp3_artist = buffer.getString();
         this.mp3_album = buffer.getString();
         this.mp3_year = buffer.getString();
         this.mp3_comment = buffer.getString();
         this.mp3_tracknum = buffer.getInt32();
         this.mp3_genre = buffer.getInt32();
      } else if (this.format == EnumFormat.OGX) {
         Format_OGx format = UtilityFactory.getFormat_OGx();
         format.read(buffer);
      }
   }
}
