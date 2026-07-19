package sancho.model.mldonkey;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.StringTokenizer;
import org.eclipse.swt.graphics.Image;
import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumExtension;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.enums.EnumRating;
import sancho.model.mldonkey.enums.EnumType;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.Tag;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public class Result extends AObject implements IObject_UID {
   private static final String RS_MD4 = SResources.getString("r.tt.md4");
   private static final String RS_NETWORK = SResources.getString("r.tt.network");
   private static final String RS_FORMAT = SResources.getString("r.tt.format");
   private static final String RS_SIZE = SResources.getString("r.tt.size");
   private static final String RS_ADOWNLOADED = SResources.getString("r.tt.alreadyDownloaded");
   private static final String TAG_LENGTH = "length";
   private static final String TAG_TIME = "time";
   private static final String TAG_SECONDS = "seconds";
   private static final String TAG_CODEC = "codec";
   private static final String TAG_RESOLUTION = "resolution";
   private static final String TAG_SAMPLE_RATE = "sampleRate";
   private static final String TAG_BITRATE = "bitrate";
   private static final String TAG_QUALITY = "quality";
   private static final String TAG_AVAILABILITY = "availability";
   private static final String TAG_COMPLETESOURCES = "completesources";
   private static final String S_NEWLINE = "\n";
   protected static Pattern fakeRE;
   protected static Pattern pornographyFilterRE;
   protected static Pattern profanityFilterRE;
   protected boolean downloaded;
   protected boolean containsFake;
   protected boolean containsPornography;
   protected boolean containsProfanity;
   protected EnumExtension extensionEnum;
   protected String comment;
   protected String format;
   protected int id;
   protected byte[] md4;
   protected String[] names;
   protected EnumNetwork networkEnum;
   protected EnumRating rating;
   protected long size;
   protected int tag_availability;
   protected int tag_bitrate;
   protected int tag_completesources = -1;
   protected String tag_codec;
   protected String tag_length;
   protected Tag[] tagList;
   protected String type;

   Result(ICore core) {
      super(core);
   }

   public synchronized boolean downloaded() {
      return this.downloaded;
   }

   public synchronized boolean containsFake() {
      return this.containsFake;
   }

   public synchronized boolean containsPornography() {
      return this.containsPornography;
   }

   public synchronized boolean containsProfanity() {
      return this.containsProfanity;
   }

   public boolean equals(Object object) {
      return object instanceof Result && this.getId() == ((Result)object).getId();
   }

   public synchronized int getAvail() {
      return this.tag_availability;
   }

   public synchronized int getBitrateTag() {
      return this.tag_bitrate;
   }

   public synchronized EnumNetwork getEnumNetwork() {
      return this.networkEnum;
   }

   public synchronized String getNetworkName() {
      return this.networkEnum.getName();
   }

   public synchronized Image getNetworkImage() {
      return this.networkEnum.getImage();
   }

   public synchronized String getBitrateTagString() {
      return this.tag_bitrate > 0 ? String.valueOf(this.tag_bitrate).intern() : "";
   }

   public synchronized String getCodecTag() {
      return this.tag_codec != null ? this.tag_codec : "";
   }

   public synchronized String getComment() {
      return this.comment != null ? this.comment : "";
   }

   public synchronized int getCompleteSources() {
      return this.tag_completesources;
   }

   public String getCompleteSourcesString() {
      int completeSources = this.getCompleteSources();
      return completeSources != -1 ? String.valueOf(completeSources).intern() : "";
   }

   public String getED2K() {
      return this.getName() == null || this.getEnumNetwork() != EnumNetwork.DONKEY && this.getEnumNetwork() != EnumNetwork.UNKNOWN
         ? ""
         : "ed2k://|file|" + this.getName() + "|" + this.getSize() + "|" + this.getMD4() + "|/";
   }

   public synchronized String getFormat() {
      return this.format != null ? this.format : "";
   }

   protected void calcFormat() {
      if (this.format == null || this.format.equals("")) {
         int dotIndex = this.getName().lastIndexOf(".");
         if (dotIndex != -1) {
            this.format = this.getName().substring(dotIndex + 1).toLowerCase().intern();
         }
      }
   }

   public synchronized int getId() {
      return this.id;
   }

   public synchronized String[] getUIDs() {
      return null;
   }

   public synchronized String getLengthTag() {
      return this.tag_length != null ? this.tag_length : "";
   }

   public synchronized String getMD4() {
      return SwissArmy.calcStringOfMD4(this.md4);
   }

   public Image getFileTypeImage() {
      return SResources.getFileTypeImage(this.getFormat());
   }

   public String getName() {
      String[] names = this.getNames();
      if (names.length < 1) {
         return "";
      } else {
         String name = names[0];
         EnumNetwork network = this.getEnumNetwork();
         if (network == EnumNetwork.GNUT || network == EnumNetwork.GNUT2) {
            String nullChar = String.valueOf('\u0000');
            name = SwissArmy.replaceAll(name, nullChar, "");
         }

         return name;
      }
   }

   public synchronized String[] getNames() {
      return this.names != null ? this.names : new String[0];
   }

   public synchronized EnumRating getRating() {
      return this.rating;
   }

   public synchronized String getRatingString() {
      if (this.core.getResultCollection().verboseNumbers) {
         StringBuffer buffer = new StringBuffer();
         buffer.append(this.rating.getName());
         buffer.append("(");
         buffer.append(this.getAvail());
         buffer.append(")");
         return buffer.toString().intern();
      } else {
         return String.valueOf(this.getAvail());
      }
   }

   public synchronized long getSize() {
      return this.size;
   }

   public synchronized String getSizeString() {
      return SwissArmy.calcStringSize(this.getSize()).intern();
   }

   public synchronized Tag[] getTagList() {
      if (this.tagList != null && this.tagList.length != 0) {
         Tag[] tags = new Tag[this.tagList.length];
         System.arraycopy(this.tagList, 0, tags, 0, this.tagList.length);
         return tags;
      } else {
         return new Tag[0];
      }
   }

   public String getToolTip() {
      return this.getName() + "\n" + this.getToolTipContent();
   }

   public String getToolTipContent() {
      StringBuffer buffer = new StringBuffer();
      String[] uids = this.getUIDs();
      if (uids != null) {
         for (int i = 0; i < uids.length; i++) {
            buffer.append(uids[i]);
            buffer.append("\n");
         }
      } else if (this.getEnumNetwork() == EnumNetwork.DONKEY) {
         buffer.append(RS_MD4);
         buffer.append(this.getMD4().toUpperCase());
         buffer.append("\n");
      }

      buffer.append(RS_NETWORK);
      buffer.append(this.getEnumNetwork().getName());
      buffer.append("\n");
      if (!this.getFormat().equals("")) {
         buffer.append(RS_FORMAT);
         buffer.append(this.getFormat());
         buffer.append("\n");
      }

      buffer.append(RS_SIZE);
      buffer.append(this.getSizeString());
      Tag[] tags = this.getTagList();

      for (int i = 0; i < tags.length; i++) {
         Tag tag = tags[i];
         buffer.append("\n");
         buffer.append(this.localizeTagName(tag.getName()));
         buffer.append(": ");
         if (tag.getType() == EnumType.STRING) {
            buffer.append(tag.getStringValue());
         } else {
            buffer.append(tag.getValue());
         }
      }

      if (this.downloaded()) {
         buffer.append("\n");
         buffer.append(RS_ADOWNLOADED);
      }

      return buffer.toString();
   }

   protected String localizeTagName(String tagName) {
      String resourceKey = this.lookupTagName(tagName);
      return resourceKey == null ? tagName : SResources.getString(resourceKey);
   }

   protected String lookupTagName(String tagName) {
      if (tagName.equalsIgnoreCase("length")) {
         return "s.r.tag.length";
      } else if (tagName.equalsIgnoreCase("time")) {
         return "s.r.tag.time";
      } else if (tagName.equalsIgnoreCase("seconds")) {
         return "s.r.tag.seconds";
      } else if (tagName.equalsIgnoreCase("codec")) {
         return "s.r.tag.codec";
      } else if (tagName.equalsIgnoreCase("resolution")) {
         return "s.r.tag.resolution";
      } else if (tagName.equalsIgnoreCase("sampleRate")) {
         return "s.r.tag.samplerate";
      } else if (tagName.equalsIgnoreCase("bitrate")) {
         return "s.r.tag.bitrate";
      } else if (tagName.equalsIgnoreCase("quality")) {
         return "s.r.tag.quality";
      } else if (tagName.equalsIgnoreCase("availability")) {
         return "s.r.tag.availability";
      } else {
         return tagName.equalsIgnoreCase("completesources") ? "s.r.tag.completesources" : null;
      }
   }

   public synchronized String getType() {
      if (this.type != null && !this.type.equals("")) {
         return this.type;
      } else {
         return this.extensionEnum != null ? this.extensionEnum.getName() : EnumExtension.UNKNOWN.getName();
      }
   }

   public int hashCode() {
      return this.getId();
   }

   public boolean isDownloading() {
      return this.core.getFileCollection().containsHash(this.getMD4());
   }

   protected void parseTags() {
      this.tag_codec = "";
      this.tag_length = "";

      for (int i = 0; i < this.tagList.length; i++) {
         String tagName = this.tagList[i].getName();
         if (tagName.equals("length") || tagName.equals("time") || tagName.equals("seconds")) {
            this.tag_length = this.tagList[i].getStringValue();
         } else if (tagName.equals("codec") || tagName.equals("resolution") || tagName.equals("sampleRate")) {
            this.tag_codec = this.tagList[i].getStringValue();
         } else if (tagName.equals("bitrate")) {
            this.tag_bitrate = this.tagList[i].getValue();
         } else if (tagName.equals("completesources")) {
            this.tag_completesources = this.tagList[i].getValue();
         } else if (tagName.equals("quality")) {
            StringTokenizer tokenizer = new StringTokenizer(this.tagList[i].getStringValue());
            if (tokenizer.hasMoreTokens()) {
               try {
                  this.tag_bitrate = Integer.parseInt(tokenizer.nextToken());
               } catch (Exception exception) {
               }
            }
         } else if (tagName.equals("availability")) {
            this.tag_availability = this.tagList[i].getValue();
         }

         if (this.tag_availability == 0) {
            this.tag_availability = 1;
         }
      }
   }

   protected void readUIDs(MessageBuffer buffer) {
      this.md4 = buffer.getMd4();
   }

   public void read(int id, MessageBuffer buffer) {
      synchronized (this) {
         this.id = id;
         this.networkEnum = this.readNetworkEnum(buffer);
         this.names = buffer.getStringList();
         this.readUIDs(buffer);
         this.size = this.readSize(buffer);
         this.format = buffer.getString();
         this.calcFormat();
         this.type = buffer.getString();
         this.tagList = buffer.getTagList();
         this.comment = buffer.getString();
         this.downloaded = buffer.getBool();
         this.regexFilters();
         this.parseTags();
         this.setRating();
         this.calcFileType();
      }
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getInt32(), buffer);
   }

   protected long readSize(MessageBuffer buffer) {
      return (long)buffer.getInt32() & 4294967295L;
   }

   protected void regexFilters() {
      if (this.core.getResultCollection().filterPornography || this.core.getResultCollection().filterProfanity) {
         for (int i = 0; i < this.names.length; i++) {
            if (profanityFilterRE != null && profanityFilterRE.matcher(this.names[i]).find()) {
               this.containsProfanity = true;
               if (this.containsPornography) {
                  break;
               }
            }

            if (pornographyFilterRE != null && pornographyFilterRE.matcher(this.names[i]).find()) {
               this.containsPornography = true;
               if (this.containsProfanity) {
                  break;
               }
            }

            if (!this.containsFake) {
               this.containsFake = SwissArmy.containsFake(this.names[i]);
            }
         }
      }

      if (!this.containsFake) {
         this.containsFake = SwissArmy.containsFake(this.comment);
      }
   }

   protected void calcFileType() {
      EnumExtension extension = EnumExtension.GET_EXT(this.getFormat());
      this.extensionEnum = extension != null ? extension : EnumExtension.UNKNOWN;
   }

   protected EnumNetwork readNetworkEnum(MessageBuffer buffer) {
      return this.core.getNetworkCollection().getNetworkEnum(buffer.getInt32());
   }

   protected void setRating() {
      this.rating = this.containsFake ? EnumRating.FAKE : EnumRating.intToEnum(this.getAvail());
   }

   static {
      try {
         profanityFilterRE = Pattern.compile("fuck|shit", Pattern.CASE_INSENSITIVE);
      } catch (PatternSyntaxException patternSyntaxException) {
         profanityFilterRE = null;
      }

      try {
         pornographyFilterRE = Pattern.compile(
            "fuck|shit|porn|pr0n|pussy|xxx|sex|erotic|anal|lolita|sluts|fetish|naked|incest|bondage|masturbat|blow.*job|barely.*legal", Pattern.CASE_INSENSITIVE
         );
      } catch (PatternSyntaxException patternSyntaxException) {
         pornographyFilterRE = null;
      }

      try {
         fakeRE = Pattern.compile("fake", Pattern.CASE_INSENSITIVE);
      } catch (PatternSyntaxException patternSyntaxException) {
         fakeRE = null;
      }
   }
}
