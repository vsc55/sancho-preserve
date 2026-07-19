package sancho.model.mldonkey.utility;

import sancho.core.ICore;

public class UtilityFactory {
   public static Addr getAddr(ICore core) {
      int protocol = core.getProtocol();
      if (protocol >= 38) {
         return new Addr38();
      } else {
         return (Addr)(protocol >= 34 ? new Addr34() : new Addr());
      }
   }

   public static NetworkStatCollection getNetworkStatCollection() {
      return new NetworkStatCollection();
   }

   public static NetworkStat getNetworkStat(NetworkStatCollection collection) {
      return new NetworkStat(collection);
   }

   public static NetworkStatTotal getNetworkStatTotal(NetworkStatCollection collection) {
      return new NetworkStatTotal(collection);
   }

   public static ClientMessage getClientMessage(ICore core) {
      return new ClientMessage();
   }

   public static FileState getFileState(ICore core) {
      return new FileState();
   }

   public static FileComment getFileComment(ICore core) {
      return new FileComment(core);
   }

   public static Format getFormat(ICore core) {
      return new Format();
   }

   public static Format_OGx getFormat_OGx() {
      return new Format_OGx();
   }

   public static Kind getKind(ICore core) {
      return (Kind)(core.getProtocol() >= 39 ? new Kind39(core) : new Kind(core));
   }

   public static Query getQuery(ICore core) {
      return new Query(core);
   }

   public static RoomMessage getRoomMessage(ICore core) {
      return new RoomMessage();
   }

   public static SearchWaiting getSearchWaiting(ICore core) {
      return new SearchWaiting();
   }

   public static HostState getHostState(ICore core) {
      return (HostState)(core.getProtocol() >= 21 ? new HostState21() : new HostState());
   }

   public static Tag getTag(ICore core) {
      return new Tag();
   }
}
