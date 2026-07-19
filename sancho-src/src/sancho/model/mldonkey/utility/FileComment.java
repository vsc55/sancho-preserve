package sancho.model.mldonkey.utility;

import sancho.core.ICore;

public class FileComment {
   private Addr addr;
   String comment;
   String name;
   byte rating;

   FileComment(ICore core) {
      this.addr = UtilityFactory.getAddr(core);
   }

   public void read(MessageBuffer buffer) {
      synchronized (this) {
         this.addr.read(false, buffer);
         this.name = buffer.getString();
         this.rating = (byte)buffer.getInt8();
         this.comment = buffer.getString();
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
