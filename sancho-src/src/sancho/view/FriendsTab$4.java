package sancho.view;

import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.utility.ClientMessage;

class FriendsTab$4 implements Runnable {
   // $VF: synthetic field
   private final Object val$arg1;
   // $VF: synthetic field
   private final Client val$client;
   // $VF: synthetic field
   private final FriendsTab this$0;

   FriendsTab$4(FriendsTab var1, Object var2, Client var3) {
      this.this$0 = var1;
      this.val$arg1 = var2;
      this.val$client = var3;
   }

   public void run() {
      this.this$0.messageFromClient((ClientMessage)this.val$arg1, this.val$client);
   }
}
