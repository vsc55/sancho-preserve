package sancho.model.mldonkey.utility;

import sancho.model.mldonkey.enums.EnumFileState;

public class FileState {
   private EnumFileState state;
   private String reason;

   public synchronized String getReason() {
      return this.reason != null ? this.reason : "";
   }

   public synchronized EnumFileState getState() {
      return this.state;
   }

   public void read(MessageBuffer var1) {
      synchronized (this) {
         this.state = EnumFileState.byteToEnum(var1.getByte());
         if (this.state == EnumFileState.ABORTED) {
            this.reason = var1.getString();
         }
      }
   }
}
