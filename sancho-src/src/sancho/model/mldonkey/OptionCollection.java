package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;
import gnu.trove.TObjectObjectProcedure;
import java.util.ArrayList;
import sancho.model.mldonkey.enums.EnumTagType;

public class OptionCollection extends ACollection_Hash {
   private static final String S_MAX_U = "max_ultrapeers";
   private static final String S_MAX_S = "max_connected_servers";

   OptionCollection(ICore core) {
      super(core);
   }

   public int getMaxConnected(Network network) {
      if (network.getEnumNetwork() == EnumNetwork.SOULSEEK) {
         return 1;
      } else {
         try {
            if (!network.hasServers() && !network.hasSupernodes()) {
               return 1;
            } else {
               String prefix = network.getEnumNetwork().getDefaultOptionPrefix();
               Option option = (Option)this.get(prefix + (network.hasSupernodes() ? S_MAX_U : S_MAX_S));
               return option == null ? 1 : Integer.parseInt(option.getValue());
            }
         } catch (Exception exception) {
            return 1;
         }
      }
   }

   public void read(MessageBuffer buffer) {
      int count = buffer.getUInt16();

      for (int i = 0; i < count; i++) {
         String name = buffer.getString();
         Option option = (Option)this.get(name);
         if (option == null) {
            option = this.core.getCollectionFactory().getOption();
         }

         option.read(name, buffer);
         this.put(name, option);
      }

      this.setChanged();
      this.notifyObservers();
   }

   public void addSectionOption(MessageBuffer buffer) {
      String section = buffer.getString();
      String description = buffer.getString();
      String name = buffer.getString();
      Option option = (Option)this.get(name);
      if (option != null) {
         option.addSectionOption(section, description, name, buffer);
      }
   }

   public void addPluginOption(MessageBuffer buffer) {
      String section = buffer.getString();
      String description = buffer.getString();
      String name = buffer.getString();
      Option option = (Option)this.get(name);
      if (option != null) {
         option.addPluginOption(section, description, name, buffer);
      }
   }

   public String[] getAllIntOptions() {
      GetAllIntOptionsProcedure collector = new GetAllIntOptionsProcedure();
      this.forEachEntry(collector);
      return collector.getOptionList();
   }

   // Trove forEachEntry: collect the names of every option holding an integer value.
   private static class GetAllIntOptionsProcedure implements TObjectObjectProcedure {
      ArrayList stringList = new ArrayList();

      public boolean execute(Object key, Object value) {
         String name = (String)key;
         Option option = (Option)value;
         if (this.isInt(option)) {
            this.stringList.add(name);
         }

         return true;
      }

      protected boolean isInt(Option option) {
         if (option.getType() == EnumTagType.INT) {
            return true;
         } else {
            try {
               Integer.parseInt(option.getValue());
               return true;
            } catch (NumberFormatException notAnInt) {
               return false;
            }
         }
      }

      public String[] getOptionList() {
         String[] names = new String[this.stringList.size()];
         this.stringList.toArray(names);
         return names;
      }
   }
}
