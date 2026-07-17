package sancho.core;

import sancho.view.utility.Splash;

class CoreFactory$6 implements Runnable {
   // $VF: synthetic field
   private final String val$text;
   // $VF: synthetic field
   private final String val$p;
   // $VF: synthetic field
   private final int val$i;
   // $VF: synthetic field
   private final CoreFactory this$0;

   CoreFactory$6(CoreFactory var1, String var2, String var3, int var4) {
      this.this$0 = var1;
      this.val$text = var2;
      this.val$p = var3;
      this.val$i = var4;
   }

   public void run() {
      Splash.updateText(this.val$text, this.val$p, this.val$i);
   }
}
