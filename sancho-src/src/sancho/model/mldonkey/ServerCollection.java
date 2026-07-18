package sancho.model.mldonkey;

import gnu.trove.TIntArrayList;
import gnu.trove.TLongIntHashMap;
import java.net.InetAddress;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import gnu.trove.TIntObjectProcedure;
import gnu.trove.TObjectProcedure;

public class ServerCollection extends ACollection_Int2 {
   private static final String RS_SERVERS = SResources.getString("tab.servers");
   private static final String RS_CONNECTED = SResources.getString("l.connected");
   private TLongIntHashMap cleanHistoryMap = new TLongIntHashMap();
   private boolean displayNodes;
   protected int numConnected;
   protected long donkeyNumServers;
   protected long donkeyNumUsers;
   protected long donkeyNumFiles;

   ServerCollection(ICore core) {
      super(core);
      this.updatePreferences();
   }

   public long getDonkeyNumServers() {
      return this.donkeyNumServers;
   }

   public String getDonkeyNumServersString() {
      return SwissArmy.calcStringSizeGrouped(this.donkeyNumServers);
   }

   public String getDonkeyNumFilesString() {
      return SwissArmy.calcStringSizeGrouped(this.donkeyNumFiles);
   }

   public String getDonkeyNumUsersString() {
      return SwissArmy.calcStringSizeGrouped(this.donkeyNumUsers);
   }

   public void addServer(Network network, InetAddress address, short port) {
      if (address != null && network != null) {
         Object[] payload = new Object[]{Integer.valueOf(network.getId()), address.getAddress(), Short.valueOf(port)};
         this.core.send((short)54, payload);
      }
   }

   public void addServerList(String url) {
      String kind = url.toLowerCase().endsWith(".met") ? "server.met" : "ocl";
      this.core.send((short)29, "urladd " + kind + " " + url);
   }

   public void clean(MessageBuffer buffer) {
      int[] ids = buffer.getInt32List();
      synchronized (this) {
         this.cleanHistoryMap.put(System.currentTimeMillis(), ids.length);
      }

      this.retainEntries(new CleanIntMap(new TIntArrayList(ids)));
      this.setChanged();
      this.notifyObservers();
   }

   public void cleanOldServers() {
      this.core.send((short)2);
   }

   public void connectMore() {
      this.core.send((short)1);
   }

   public int getConnected() {
      return this.numConnected;
   }

   public int getConnected(EnumNetwork enumNetwork) {
      CountNetworkConnected counter = new CountNetworkConnected(enumNetwork);
      this.forEachValue(counter);
      return counter.getCount();
   }

   public synchronized TLongIntHashMap getHistoryMap() {
      return this.cleanHistoryMap;
   }

   public Object[] getServers() {
      return this.getValues();
   }

   private void put(int id, Server server) {
      if (server != null) {
         super.put(id, server);
         this.addToAdded(server);
         if (server.isConnected()) {
            this.setConnected(1);
         }
      }
   }

   public void read(MessageBuffer buffer) {
      int id = buffer.getInt32();
      int networkId = buffer.getInt32();
      Network network = (Network)this.core.getNetworkCollection().get(networkId);
      if (network != null) {
         if (this.displayNodes || network.hasServers()) {
            Server server = (Server)this.get(id);
            if (server != null) {
               server.read(id, networkId, buffer);
               if (server.getStateEnum() != EnumHostState.REMOVE_HOST) {
                  this.addToUpdated(server);
               }
            } else {
               server = this.core.getCollectionFactory().getServer();
               server.read(id, networkId, buffer);
               if (server.getStateEnum() != EnumHostState.REMOVE_HOST) {
                  this.put(server.getId(), server);
               }
            }

            this.setChanged();
            this.notifyObservers();
         }
      }
   }

   public void readUpdate(MessageBuffer buffer) {
      Server server = (Server)this.get(buffer.getInt32());
      if (server != null) {
         server.readUpdate(buffer);
         if (server.getStateEnum() != EnumHostState.REMOVE_HOST) {
            this.addToUpdated(server);
            this.setChanged();
            this.notifyObservers();
         }
      }
   }

   public synchronized String getHeaderString() {
      StringBuffer text = new StringBuffer(64);
      text.append(RS_SERVERS);
      text.append(": ");
      text.append(this.getConnected());
      text.append(" / ");
      text.append(this.size());
      text.append(" ");
      text.append(RS_CONNECTED);
      this.updateDonkeyTotals();
      if (this.getDonkeyNumServers() > 0L) {
         text.append(" ");
         text.append(" ");
         text.append(" ");
         text.append(" ");
         text.append("(");
         text.append(EnumNetwork.DONKEY.getName());
         text.append(": ");
         text.append(this.getDonkeyNumServersString());
         text.append("/");
         text.append(this.getDonkeyNumUsersString());
         text.append("/");
         text.append(this.getDonkeyNumFilesString());
         text.append(")");
      }

      return text.toString();
   }

   public synchronized void updateDonkeyTotals() {
      CountDonkeyTotals counter = new CountDonkeyTotals();
      this.forEachValue(counter);
      this.donkeyNumFiles = counter.getNumFiles();
      this.donkeyNumUsers = counter.getNumUsers();
      this.donkeyNumServers = counter.getNumServers();
   }

   public Object remove(int id) {
      Server server = (Server)super.get(id);
      if (server != null && server.isConnected()) {
         this.setConnected(-1);
      }

      return super.remove(id);
   }

   public void remove(Server server) {
      super.remove(server.getId());
      this.addToRemoved(server);
      this.setChanged();
      this.notifyObservers();
   }

   public void removeAll(EnumNetwork enumNetwork) {
      this.retainEntries(new RemoveNetworkServers(enumNetwork));
      this.setChanged();
      this.notifyObservers();
   }

   public void serverUser(MessageBuffer buffer) {
      int id = buffer.getInt32();
      Server server = (Server)this.get(id);
      if (server != null) {
         server.serverUser(buffer);
         this.addToUpdated(server);
      } else if (Sancho.debug) {
         Sancho.pDebug("su-nf:" + id);
      }
   }

   public void setConnected(int delta) {
      this.numConnected += delta;
   }

   public void updatePreferences() {
      this.displayNodes = PreferenceLoader.loadBoolean("displayNodes");
   }

   // Trove forEachValue: total servers/users/files across all eDonkey servers.
   private static class CountDonkeyTotals implements TObjectProcedure {
      private long numFiles;
      private long numUsers;
      private long numServers;

      public boolean execute(Object value) {
         Server server = (Server)value;
         if (server.getEnumNetwork() == EnumNetwork.DONKEY) {
            this.numServers++;
            this.numUsers = this.numUsers + server.getNumUsers();
            this.numFiles = this.numFiles + server.getNumFiles();
         }

         return true;
      }

      public long getNumServers() {
         return this.numServers;
      }

      public long getNumFiles() {
         return this.numFiles;
      }

      public long getNumUsers() {
         return this.numUsers;
      }
   }

   // Trove forEachValue: count connected servers on a given network.
   private static class CountNetworkConnected implements TObjectProcedure {
      private int counter;
      private EnumNetwork networkEnum;

      public CountNetworkConnected(EnumNetwork networkEnum) {
         this.networkEnum = networkEnum;
      }

      public boolean execute(Object value) {
         Server server = (Server)value;
         if (server.isConnected() && server.getEnumNetwork() == this.networkEnum) {
            this.counter++;
         }

         return true;
      }

      public int getCount() {
         return this.counter;
      }
   }

   // Trove retainEntries filter: drop every server belonging to a given network.
   private static class RemoveNetworkServers implements TIntObjectProcedure {
      private EnumNetwork enumNetwork;

      public RemoveNetworkServers(EnumNetwork enumNetwork) {
         this.enumNetwork = enumNetwork;
      }

      public boolean execute(int id, Object value) {
         Server server = (Server)value;
         return this.enumNetwork != server.getEnumNetwork();
      }
   }
}
