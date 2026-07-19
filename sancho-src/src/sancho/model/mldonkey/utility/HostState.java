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

   protected static int readRank(byte stateByte) {
      switch (stateByte) {
         case 4:
            return -1;
         case 10:
            return -2;
         default:
            return 0;
      }
   }

   public synchronized EnumHostState read(MessageBuffer buffer) {
      byte stateByte = buffer.getByte();
      EnumHostState hostState = EnumHostState.byteToEnum(stateByte);
      if (hostState != EnumHostState.CONNECTED_AND_QUEUED && hostState != EnumHostState.NOT_CONNECTED_WAS_QUEUED) {
         this.rank = readRank(stateByte);
      } else {
         this.rank = buffer.getInt32();
      }

      return hostState;
   }
}
