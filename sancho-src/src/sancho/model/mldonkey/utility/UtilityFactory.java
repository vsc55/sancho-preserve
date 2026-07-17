package sancho.model.mldonkey.utility;

import sancho.core.ICore;

public class UtilityFactory {
   public static Addr getAddr(ICore var0) {
      int var1 = var0.getProtocol();
      if (var1 >= 38) {
         return new Addr38();
      } else {
         return (Addr)(var1 >= 34 ? new Addr34() : new Addr());
      }
   }

   public static NetworkStatCollection getNetworkStatCollection() {
      return new NetworkStatCollection();
   }

   public static NetworkStat getNetworkStat(NetworkStatCollection var0) {
      return new NetworkStat(var0);
   }

   public static NetworkStatTotal getNetworkStatTotal(NetworkStatCollection var0) {
      return new NetworkStatTotal(var0);
   }

   public static ClientMessage getClientMessage(ICore var0) {
      return new ClientMessage();
   }

   public static FileState getFileState(ICore var0) {
      return new FileState();
   }

   public static FileComment getFileComment(ICore var0) {
      return new FileComment(var0);
   }

   public static Format getFormat(ICore var0) {
      return new Format();
   }

   public static Format_OGx getFormat_OGx() {
      return new Format_OGx();
   }

   public static Kind getKind(ICore var0) {
      return (Kind)(var0.getProtocol() >= 39 ? new Kind39(var0) : new Kind(var0));
   }

   public static Query getQuery(ICore var0) {
      return new Query(var0);
   }

   public static RoomMessage getRoomMessage(ICore var0) {
      return new RoomMessage();
   }

   public static SearchWaiting getSearchWaiting(ICore var0) {
      return new SearchWaiting();
   }

   public static HostState getHostState(ICore var0) {
      return (HostState)(var0.getProtocol() >= 21 ? new HostState21() : new HostState());
   }

   public static Tag getTag(ICore var0) {
      return new Tag();
   }
}
