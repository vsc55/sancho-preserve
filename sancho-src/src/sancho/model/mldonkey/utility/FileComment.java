package sancho.model.mldonkey.utility;

import sancho.core.ICore;

public class FileComment {
   private Addr addr;
   String comment;
   String name;
   byte rating;

   FileComment(ICore var1) {
      this.addr = UtilityFactory.getAddr(var1);
   }

   public void read(MessageBuffer var1) {
      synchronized (this) {
         this.addr.read(false, var1);
         this.name = var1.getString();
         this.rating = (byte)var1.getInt8();
         this.comment = var1.getString();
      }
   }

   public synchronized String getName() {
      return this.name != null ? this.name : "";
   }

   public synchronized String getComment() {
      return this.comment != null ? this.comment : "";
   }

   public synchronized int getRating() {
      return this.rating;
   }

   public synchronized String getRatingString() {
      return Byte.toString(this.rating);
   }

   public Addr getAddr() {
      return this.addr;
   }

   public String toString() {
      return "[" + this.getAddr().toString() + "/" + this.getName() + "] (" + this.getRatingString() + "): " + this.getComment();
   }
}
