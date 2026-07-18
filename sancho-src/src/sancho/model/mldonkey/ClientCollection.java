package sancho.model.mldonkey;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectProcedure;
import gnu.trove.TLongIntHashMap;
import gnu.trove.TObjectProcedure;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.EnumClientType;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.ObjectMap;

public class ClientCollection extends ACollection_Int {
   private TLongIntHashMap cleanHistoryMap = new TLongIntHashMap();
   private ObjectMap friendsWeakMap = new ObjectMap(true);
   private ObjectMap pendingWeakMap = new ObjectMap(true);
   private ObjectMap uploadersWeakMap = new ObjectMap(true);

   ClientCollection(ICore core) {
      super(core);
   }

   public void clean(MessageBuffer buffer) {
      int[] ids = buffer.getInt32List();
      synchronized (this) {
         this.cleanHistoryMap.put(System.currentTimeMillis(), ids.length);
      }

      this.retainEntries(new CleanIntMap(new TIntArrayList(ids)));
      this.cleanDeadClients();
   }

   public void cleanDeadClients() {
      this.retainEntries(new CleanDeadClients());
   }

   public static void removeAllFriends(ICore core) {
      core.send((short)17);
   }

   public void clientFile(MessageBuffer buffer) {
      int id = buffer.getInt32();
      Client client = this.getClient(id);
      if (client != null) {
         boolean hadFiles = client.hasFiles();
         client.readClientFile(buffer);
         if (!hadFiles) {
            this.updateWeak(client);
         }
      } else {
         Sancho.pDebug("Client " + id + " not found");
      }
   }

   public void dispose() {
      this.friendsWeakMap.deleteObservers();
      this.uploadersWeakMap.deleteObservers();
      this.forEachValue(new DisposeAll());
      super.dispose();
   }

   public Client getClient(int id) {
      return (Client)super.get(id);
   }

   public ObjectMap getFriendsWeakMap() {
      return this.friendsWeakMap;
   }

   public synchronized TLongIntHashMap getHistoryMap() {
      return this.cleanHistoryMap;
   }

   public ObjectMap getPendingWeakMap() {
      return this.pendingWeakMap;
   }

   public ObjectMap getUploadersWeakMap() {
      return this.uploadersWeakMap;
   }

   public void pending(MessageBuffer buffer) {
      this.processMap(this.pendingWeakMap, buffer);
   }

   public void processMap(ObjectMap weakMap, MessageBuffer buffer) {
      int[] ids = buffer.getInt32List();

      for (int i = 0; i < ids.length; i++) {
         this.core.send((short)36, Integer.valueOf(ids[i]));
         if (this.containsKey(ids[i])) {
            weakMap.addOrUpdate(this.get(ids[i]));
         }
      }

      TIntArrayList idList = new TIntArrayList(ids);
      Object[] keys = weakMap.getKeyArray();

      for (int i = 0; i < keys.length; i++) {
         Client client = (Client)keys[i];
         if (!idList.contains(client.getId())) {
            weakMap.remove(client);
            if (client.countObservers() == 0) {
               this.removeSource(client.getId(), client);
            }
         }
      }
   }

   public void read(MessageBuffer buffer) {
      int id = buffer.getInt32();
      Client client = (Client)this.get(id);
      if (client != null) {
         client.read(id, buffer);
      } else {
         client = this.core.getCollectionFactory().getClient();
         client.read(id, buffer);
         this.put(id, client);
      }

      this.updateWeak(client);
   }

   public void readUpdate(MessageBuffer buffer) {
      int id = buffer.getInt32();
      if (this.containsKey(id)) {
         this.getClient(id).readUpdate(buffer);
      }
   }

   public void removeSource(int id, Client client) {
      if (!this.friendsWeakMap.containsKey(client) && !this.uploadersWeakMap.containsKey(client) && !this.pendingWeakMap.containsKey(client)) {
         this.remove(id);
      }
   }

   public void updateAvailability(MessageBuffer buffer) {
      int fileId = buffer.getInt32();
      int clientId = buffer.getInt32();
      String avail = buffer.getString(false);
      File file = (File)this.core.getFileCollection().get(fileId);
      Client client = (Client)this.get(clientId);
      if (client != null && file != null) {
         client.putAvail(fileId, avail);
      }
   }

   public void updateUploaders(ICore core) {
      synchronized (this.uploadersWeakMap) {
         Object[] keys = this.uploadersWeakMap.getKeyArray();

         for (int i = 0; i < keys.length; i++) {
            Client client = (Client)keys[i];
            core.send((short)36, Integer.valueOf(client.getId()));
         }
      }
   }

   public void updateFriends(Client client) {
      if (client.getEnumClientType() == EnumClientType.FRIEND) {
         this.friendsWeakMap.addOrUpdate(client);
      } else {
         this.friendsWeakMap.remove(client);
      }
   }

   public void updateWeak(Client client) {
      this.updateFriends(client);
      if (this.core.getProtocol() < 23) {
         if (client.isUploader() && client.isConnected()) {
            this.uploadersWeakMap.addOrUpdate(client);
         } else {
            this.uploadersWeakMap.remove(client);
         }
      }
   }

   public void uploaders(MessageBuffer buffer) {
      this.processMap(this.uploadersWeakMap, buffer);
   }

   // Trove retainEntries filter: keep a client only while it's still observed or held by
   // one of the friends / uploaders / pending maps; drop the dead ones.
   private class CleanDeadClients implements TIntObjectProcedure {
      public boolean execute(int id, Object value) {
         Client client = (Client)value;
         return client.countObservers() != 0
            || ClientCollection.this.friendsWeakMap.containsKey(client)
            || ClientCollection.this.uploadersWeakMap.containsKey(client)
            || ClientCollection.this.pendingWeakMap.containsKey(client);
      }
   }

   // Trove forEachValue: drop every client's observers (on collection dispose).
   private static class DisposeAll implements TObjectProcedure {
      public boolean execute(Object value) {
         ((Client)value).deleteObservers();
         return true;
      }
   }
}
