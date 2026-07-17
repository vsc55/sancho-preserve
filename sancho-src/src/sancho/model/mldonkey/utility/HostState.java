package sancho.model.mldonkey.utility;

import sancho.model.mldonkey.enums.EnumHostState;

public class HostState {
   protected int rank;

   public synchronized int getRank() {
      return this.rank;
   }

   public int getFileNum() {
      return -1;
   }

   protected static int readRank(byte var0) {
      switch (var0) {
         case 4:
            return -1;
         case 10:
            return -2;
         default:
            return 0;
      }
   }

   public synchronized EnumHostState read(MessageBuffer var1) {
      byte var2 = var1.getByte();
      EnumHostState var3 = EnumHostState.byteToEnum(var2);
      if (var3 != EnumHostState.CONNECTED_AND_QUEUED && var3 != EnumHostState.NOT_CONNECTED_WAS_QUEUED) {
         this.rank = readRank(var2);
      } else {
         this.rank = var1.getInt32();
      }

      return var3;
   }
}
