package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;

public class OptionCollection extends ACollection_Hash {
   private static final String S_MAX_U = "max_ultrapeers";
   private static final String S_MAX_S = "max_connected_servers";

   OptionCollection(ICore var1) {
      super(var1);
   }

   public int getMaxConnected(Network var1) {
      if (var1.getEnumNetwork() == EnumNetwork.SOULSEEK) {
         return 1;
      } else {
         try {
            if (!var1.hasServers() && !var1.hasSupernodes()) {
               return 1;
            } else {
               String var2 = var1.getEnumNetwork().getDefaultOptionPrefix();
               Option var3 = (Option)this.get(var2 + (var1.hasSupernodes() ? "max_ultrapeers" : "max_connected_servers"));
               return var3 == null ? 1 : Integer.parseInt(var3.getValue());
            }
         } catch (Exception var4) {
            return 1;
         }
      }
   }

   public void read(MessageBuffer var1) {
      int var2 = var1.getUInt16();

      for (int var5 = 0; var5 < var2; var5++) {
         String var3 = var1.getString();
         Option var4 = (Option)this.get(var3);
         if (var4 == null) {
            var4 = this.core.getCollectionFactory().getOption();
         }

         var4.read(var3, var1);
         this.put(var3, var4);
      }

      this.setChanged();
      this.notifyObservers();
   }

   public void addSectionOption(MessageBuffer var1) {
      String var2 = var1.getString();
      String var3 = var1.getString();
      String var4 = var1.getString();
      Option var5 = (Option)this.get(var4);
      if (var5 != null) {
         var5.addSectionOption(var2, var3, var4, var1);
      }
   }

   public void addPluginOption(MessageBuffer var1) {
      String var2 = var1.getString();
      String var3 = var1.getString();
      String var4 = var1.getString();
      Option var5 = (Option)this.get(var4);
      if (var5 != null) {
         var5.addPluginOption(var2, var3, var4, var1);
      }
   }

   public String[] getAllIntOptions() {
      OptionCollection$GetAllIntOptionsProcedure var1 = new OptionCollection$GetAllIntOptionsProcedure();
      this.forEachEntry(var1);
      return var1.getOptionList();
   }
}
