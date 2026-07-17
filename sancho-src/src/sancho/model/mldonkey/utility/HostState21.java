package sancho.model.mldonkey.utility;

import sancho.model.mldonkey.enums.EnumHostState;

public class HostState21 extends HostState {
   protected int fileNum = -1;

   public synchronized EnumHostState read(MessageBuffer var1) {
      EnumHostState var2 = super.read(var1);
      if (var2 == EnumHostState.CONNECTED_DOWNLOADING) {
         this.fileNum = var1.getInt32();
      }

      return var2;
   }

   public synchronized int getFileNum() {
      return this.fileNum;
   }
}
