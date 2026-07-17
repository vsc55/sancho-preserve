package sancho.model.mldonkey;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;

public class File18 extends File {
   private Map availMap;

   File18(ICore var1) {
      super(var1);
   }

   protected void readAvailability(MessageBuffer var1) {
      int var2 = var1.getUInt16();
      if (this.availMap == null) {
         this.availMap = new HashMap(var2);
      } else {
         this.availMap.clear();
      }

      boolean var3 = false;
      String var4 = this.getAvail();

      for (int var5 = 0; var5 < var2; var5++) {
         int var6 = var1.getInt32();
         Network var7 = (Network)this.core.getNetworkCollection().get(var6);
         if (var7 == null) {
            String var8 = var1.getString();
            Sancho.pDebug("readAvail: " + var8);
         } else if (var7.getEnumNetwork() == EnumNetwork.MULTINET) {
            this.avail = var1.getString();
            var3 = true;
         } else {
            String var9 = var1.getString();
            if (!var3) {
               this.avail = var9;
            }

            this.availMap.put(var7, var9);
         }
      }

      if (var4 != null && !var4.equals(this.getAvail())) {
         this.addChangedBits(1024);
      }

      this.setRelativeAvail();
   }

   public boolean hasAvails() {
      return true;
   }

   public synchronized String getAvails(Network var1) {
      return (String)this.availMap.get(var1);
   }

   public synchronized Network[] getAllAvailNetworks() {
      if (this.availMap == null) {
         return new Network[0];
      } else {
         Network[] var1 = new Network[this.availMap.size()];
         int var2 = 0;

         for (Iterator var3 = this.availMap.keySet().iterator(); var3.hasNext(); var2++) {
            var1[var2] = (Network)var3.next();
         }

         return var1;
      }
   }
}
