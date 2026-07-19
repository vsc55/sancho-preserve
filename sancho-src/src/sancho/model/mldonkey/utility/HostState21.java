package sancho.model.mldonkey.utility;

import sancho.model.mldonkey.enums.EnumHostState;

public class HostState21 extends HostState {
   protected int fileNum = -1;

   public synchronized EnumHostState read(MessageBuffer buffer) {
      EnumHostState hostState = super.read(buffer);
      if (hostState == EnumHostState.CONNECTED_DOWNLOADING) {
         this.fileNum = buffer.getInt32();
      }

      return hostState;
   }

   public synchronized int getFileNum() {
      return this.fileNum;
   }
}
