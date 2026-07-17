package sancho.view;

import sancho.model.mldonkey.utility.ClientMessage;

class FriendsTab$4 implements Runnable {
   // $VF: synthetic field
   private final Object val$arg1;
   // $VF: synthetic field
   private final FriendsTab this$0;

   FriendsTab$4(FriendsTab var1, Object var2) {
      this.this$0 = var1;
      this.val$arg1 = var2;
   }

   public void run() {
      this.this$0.messageFromClient((ClientMessage)this.val$arg1);
   }
}
