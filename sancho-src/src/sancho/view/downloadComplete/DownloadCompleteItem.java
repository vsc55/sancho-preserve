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

   public boolean parseLine(String var1) {
      int var2 = var1.indexOf(" ");
      if (var2 == -1) {
         return false;
      } else {
         String var3 = var1.substring(0, var2);

         try {
            this.dateLong = Long.parseLong(var3);
            this.dateString = new Date(this.dateLong).toString();
         } catch (Exception var9) {
         }

         String var4 = var1.substring(var2 + 1);
         if (var4.startsWith("ed2k://|file|")) {
            int var5 = var4.indexOf("|", 13);
            if (var5 < 13) {
               return false;
            } else {
               this.name = var4.substring(13, var5);
               int var6 = var5 + 1;
               var5 = var4.indexOf("|", var6);

               try {
                  this.size = Long.parseLong(var4.substring(var6, var5));
                  this.sizeString = SwissArmy.calcStringSize(this.size);
               } catch (NumberFormatException var8) {
                  this.size = 0L;
               }

               var6 = var5 + 1;
               var5 = var4.indexOf("|", var6);
               this.hash = var4.substring(var6, var5).toUpperCase();
               return true;
            }
         } else {
            return false;
         }
      }
   }
}
