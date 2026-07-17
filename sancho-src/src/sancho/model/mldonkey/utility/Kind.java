package sancho.model.mldonkey.utility;

import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumClientMode;
import sancho.utility.SwissArmy;

public class Kind {
   protected int port;
   private byte[] hash;
   protected Addr addr;

   Kind(ICore var1) {
      this.addr = UtilityFactory.getAddr(var1);
   }

   public synchronized String getHash() {
      return this.hash == null ? "" : SwissArmy.calcStringOfMD4(this.hash);
   }

   public Addr getAddr() {
      return this.addr;
   }

   public synchronized int getPort() {
      return this.port;
   }

   public synchronized EnumClientMode read(MessageBuffer var1) {
      EnumClientMode var2 = EnumClientMode.byteToEnum(var1.getByte());
      if (var2 == EnumClientMode.DIRECT) {
         this.addr.read(false, var1);
         this.port = var1.getUInt16();
      } else {
         this.addr.read(true, var1);
         this.hash = var1.getMd4();
         this.readAddr(var1);
      }

      return var2;
   }

   public void readAddr(MessageBuffer var1) {
   }
}
