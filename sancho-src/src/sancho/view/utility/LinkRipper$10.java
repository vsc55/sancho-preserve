package sancho.view.utility;

class LinkRipper$10 implements Runnable {
   // $VF: synthetic field
   private final LinkRipper this$0;

   LinkRipper$10(LinkRipper var1) {
      this.this$0 = var1;
   }

   public void run() {
      this.this$0.urlGroup.setText(SResources.getString("rip.error"));
   }
}
