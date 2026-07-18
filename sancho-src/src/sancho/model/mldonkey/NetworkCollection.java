package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;
import gnu.trove.TObjectProcedure;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public class NetworkCollection extends ACollection_Int implements ICollection {
   public static int CHANGED_STATS = 1;

   NetworkCollection(ICore core) {
      super(core);
   }

   public Network getByEnum(EnumNetwork enumNetwork) {
      GetNetworkByEnum finder = new GetNetworkByEnum(enumNetwork);
      this.forEachValue(finder);
      return finder.getNetwork();
   }

   public int getEnabledAndSearchable() {
      CountEnabledAndSearchable counter = new CountEnabledAndSearchable();
      this.forEachValue(counter);
      return counter.getCount();
   }

   public EnumNetwork getNetworkEnum(int id) {
      Network network;
      return (network = (Network)this.get(id)) != null ? network.getEnumNetwork() : EnumNetwork.UNKNOWN;
   }

   public void getAllStats() {
      GetAllStats collector = new GetAllStats();
      this.forEachValue(collector);
   }

   public String getAllNetworkStats(String newLine) {
      GetNetworkStats collector = new GetNetworkStats(newLine);
      this.forEachValue(collector);
      return collector.getResultString();
   }

   public Network[] getNetworks() {
      Object[] values = this.getValues();
      Network[] networks = new Network[values.length];

      for (int i = 0; i < values.length; i++) {
         networks[i] = (Network)values[i];
      }

      return networks;
   }

   public void read(MessageBuffer buffer) {
      int id = buffer.getInt32();
      boolean changed = true;
      Network network = (Network)this.get(id);
      if (network != null) {
         boolean wasEnabled = network.isEnabled();
         int previousConnected = network.numConnectedServers();
         network.read(id, buffer);
         if (wasEnabled == network.isEnabled() && previousConnected == network.numConnectedServers()) {
            changed = false;
         }
      } else {
         network = this.core.getCollectionFactory().getNetwork();
         network.read(id, buffer);
         this.put(id, network);
         network.getStats();
      }

      if (changed) {
         this.setChanged();
         this.notifyObservers(network);
      }
   }

   public void readStats(MessageBuffer buffer) {
      int id = buffer.getInt32();
      Network network = (Network)this.get(id);
      if (network != null) {
         network.readStats(buffer);
         int changeFlags = 0;
         changeFlags |= CHANGED_STATS;
         this.setChanged();
         this.notifyObservers(network, changeFlags);
      } else {
         Sancho.pDebug("readStats failed: " + id);
      }
   }

   protected void setConnectedServers(Network network, int connectedServers) {
      if (network != null) {
         if (network.numConnectedServers() != connectedServers) {
            network.setConnectedServers(connectedServers);
            this.setChanged();
            this.notifyObservers(network);
         }
      }
   }

   // Trove forEachValue: count the networks that are both enabled and searchable.
   private static class CountEnabledAndSearchable implements TObjectProcedure {
      private int counter;

      public boolean execute(Object value) {
         Network network = (Network)value;
         if (network.isEnabled() && network.isSearchable()) {
            this.counter++;
         }

         return true;
      }

      public int getCount() {
         return this.counter;
      }
   }

   // Trove forEachValue: request stats for every enabled network.
   private static class GetAllStats implements TObjectProcedure {
      public boolean execute(Object value) {
         Network network = (Network)value;
         if (network.isEnabled()) {
            network.getStats();
         }

         return true;
      }
   }

   // Trove forEachValue: find the network matching a given EnumNetwork.
   private static class GetNetworkByEnum implements TObjectProcedure {
      EnumNetwork enumNetwork;
      Network foundNetworkInfo = null;

      public GetNetworkByEnum(EnumNetwork enumNetwork) {
         this.enumNetwork = enumNetwork;
      }

      public boolean execute(Object value) {
         Network network = (Network)value;
         if (network.equals(this.enumNetwork)) {
            this.foundNetworkInfo = network;
         }

         return true;
      }

      public Network getNetwork() {
         return this.foundNetworkInfo;
      }
   }

   // Trove forEachValue: build a human-readable stats block for every network.
   private static class GetNetworkStats implements TObjectProcedure {
      StringBuffer stringBuffer = new StringBuffer();
      String nl;

      public GetNetworkStats(String newLine) {
         this.nl = newLine;
      }

      public boolean execute(Object value) {
         Network network = (Network)value;
         this.stringBuffer.append(this.nl + SResources.getString("stats.network") + ": ");
         this.stringBuffer.append(network.getName());
         if (network.isEnabled()) {
            this.stringBuffer.append(" (" + SResources.getString("stats.enabled") + ")" + this.nl);
         } else {
            this.stringBuffer.append(" (" + SResources.getString("stats.disabled") + ")" + this.nl);
         }

         this.stringBuffer.append(SResources.getString("stats.downloaded") + ": ");
         this.stringBuffer.append(SwissArmy.calcStringSize(network.getDownloaded()) + this.nl);
         this.stringBuffer.append(SResources.getString("stats.uploaded") + ": ");
         this.stringBuffer.append(SwissArmy.calcStringSize(network.getUploaded()) + this.nl);
         return true;
      }

      public String getResultString() {
         return this.stringBuffer.toString();
      }
   }
}
