package sancho.view.downloadComplete;

import java.util.Date;
import sancho.model.mldonkey.IObject_UID;
import sancho.utility.SwissArmy;

public class DownloadCompleteItem implements IObject_UID {
   long dateLong;
   String dateString;
   String hash;
   String name;
   long size;
   String sizeString;

   public long getDateLong() {
      return this.dateLong;
   }

   public String getDateString() {
      return this.dateString == null ? "" : this.dateString;
   }

   public String getMD4() {
      return this.getHash();
   }

   public String[] getUIDs() {
      return new String[]{"urn:ed2k:" + this.getHash()};
   }

   public String getHash() {
      return this.hash == null ? "" : this.hash;
   }

   public String getED2K() {
      return this.getLink();
   }

   public String getLink() {
      return "ed2k://|file|" + this.getName() + "|" + this.getSize() + "|" + this.getHash() + "|/";
   }

   public String getName() {
      return this.name == null ? "" : this.name;
   }

   public long getSize() {
      return this.size;
   }

   public String getSizeString() {
      return this.sizeString == null ? "" : this.sizeString;
   }

   public boolean parseLine(String line) {
      int spaceIndex = line.indexOf(" ");
      if (spaceIndex == -1) {
         return false;
      } else {
         String dateToken = line.substring(0, spaceIndex);

         try {
            this.dateLong = Long.parseLong(dateToken);
            this.dateString = new Date(this.dateLong).toString();
         } catch (Exception exception) {
         }

         String linkText = line.substring(spaceIndex + 1);
         if (linkText.startsWith("ed2k://|file|")) {
            int separatorIndex = linkText.indexOf("|", 13);
            if (separatorIndex < 13) {
               return false;
            } else {
               this.name = linkText.substring(13, separatorIndex);
               int startIndex = separatorIndex + 1;
               separatorIndex = linkText.indexOf("|", startIndex);
               if (separatorIndex < 0) {
                  return false;
               }

               try {
                  this.size = Long.parseLong(linkText.substring(startIndex, separatorIndex));
                  this.sizeString = SwissArmy.calcStringSize(this.size);
               } catch (NumberFormatException numberFormatException) {
                  this.size = 0L;
               }

               startIndex = separatorIndex + 1;
               separatorIndex = linkText.indexOf("|", startIndex);
               if (separatorIndex < 0) {
                  return false;
               }

               this.hash = linkText.substring(startIndex, separatorIndex).toUpperCase();
               return true;
            }
         } else {
            return false;
         }
      }
   }
}
