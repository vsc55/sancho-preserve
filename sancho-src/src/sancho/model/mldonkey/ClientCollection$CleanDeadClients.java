package sancho.model.mldonkey;

import gnu.trove.TIntObjectProcedure;

class ClientCollection$CleanDeadClients implements TIntObjectProcedure {
   // $VF: synthetic field
   private final ClientCollection this$0;

   ClientCollection$CleanDeadClients(ClientCollection var1) {
      this.this$0 = var1;
   }

   public boolean execute(int var1, Object var2) {
      Client var3 = (Client)var2;
      return var3.countObservers() != 0
         || ClientCollection.access$000(this.this$0).containsKey(var3)
         || ClientCollection.access$100(this.this$0).containsKey(var3)
         || ClientCollection.access$200(this.this$0).containsKey(var3);
   }
}
