package sancho.model.mldonkey.utility;

import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumClientMode;
import sancho.utility.SwissArmy;

public class Kind {
   protected int port;
   private byte[] hash;
   protected Addr addr;

   Kind(ICore core) {
      this.addr = UtilityFactory.getAddr(core);
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

   public synchronized EnumClientMode read(MessageBuffer buffer) {
      EnumClientMode clientMode = EnumClientMode.byteToEnum(buffer.getByte());
      if (clientMode == EnumClientMode.DIRECT) {
         this.addr.read(false, buffer);
         this.port = buffer.getUInt16();
      } else {
         this.addr.read(true, buffer);
         this.hash = buffer.getMd4();
         this.readAddr(buffer);
      }

      return clientMode;
   }

   public void readAddr(MessageBuffer buffer) {
   }
}
