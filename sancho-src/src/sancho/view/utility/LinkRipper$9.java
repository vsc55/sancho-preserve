package sancho.view.utility;

class LinkRipper$9 implements Runnable {
   // $VF: synthetic field
   private final LinkRipper this$0;

   LinkRipper$9(LinkRipper var1) {
      this.this$0 = var1;
   }

   public void run() {
      this.this$0.urlGroup.setText(SResources.getString("rip.error"));
   }
}
