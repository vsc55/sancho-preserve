package sancho.model.mldonkey.utility;

public class NetworkStatTotal extends NetworkStat {
   NetworkStatTotal(NetworkStatCollection collection) {
      super(collection);
   }

   public String getNameLong() {
      return "Total";
   }

   public String getNameShort() {
      return "Ttl";
   }

   public void read(int seen, int banned, int request, long download, long upload) {
      synchronized (this) {
         this.seen = seen;
         this.banned = banned;
         this.filerequest = request;
         this.download = download;
         this.upload = upload;
      }
   }
}
