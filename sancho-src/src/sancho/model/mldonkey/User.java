package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.Addr;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.Tag;
import sancho.model.mldonkey.utility.UtilityFactory;
import sancho.utility.SwissArmy;

public class User extends AObject {
   private static String S_OSB = "[";
   private static String S_CSB = "] ";
   private Addr addr;
   private int id;
   private byte[] md4;
   private String name;
   private int port;
   private int serverId;
   private Tag[] tags;

   User(ICore core) {
      super(core);
      this.addr = UtilityFactory.getAddr(core);
   }

   public void addAsFriend() {
      this.core.send((short)15, Integer.valueOf(this.getId()));
   }

   public Addr getAddr() {
      return this.addr;
   }

   public synchronized int getId() {
      return this.id;
   }

   public synchronized String getMd4() {
      return SwissArmy.calcStringOfMD4(this.md4);
   }

   public synchronized String getName() {
      return this.name != null ? this.name : "";
   }

   public synchronized int getPort() {
      return this.port;
   }

   public synchronized int getServerId() {
      return this.serverId;
   }

   public synchronized String getTagsString() {
      StringBuffer text = new StringBuffer();

      for (int i = 0; i < this.tags.length; i++) {
         text.append(S_OSB);
         text.append(this.tags[i]);
         text.append(S_CSB);
      }

      return text.toString();
   }

   public synchronized void read(int id, MessageBuffer buffer) {
      synchronized (this) {
         this.id = id;
         this.md4 = buffer.getMd4();
         this.name = buffer.getString();
         this.addr.read(false, buffer);
         this.port = buffer.getUInt16();
         this.tags = buffer.getTagList();
         this.serverId = buffer.getInt32();
      }
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getInt32(), buffer);
   }
}
