package sancho.model.mldonkey.utility;

public class NetworkStatTotal extends NetworkStat {
   NetworkStatTotal(NetworkStatCollection var1) {
      super(var1);
   }

   public String getNameLong() {
      return "Total";
   }

   public String getNameShort() {
      return "Ttl";
   }

   public void read(int var1, int var2, int var3, long var4, long var6) {
      synchronized (this) {
         this.seen = var1;
         this.banned = var2;
         this.filerequest = var3;
         this.download = var4;
         this.upload = var6;
      }
   }
}
