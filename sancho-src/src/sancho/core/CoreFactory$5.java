package sancho.core;

import sancho.view.utility.Splash;

class CoreFactory$5 implements Runnable {
   // $VF: synthetic field
   private final String val$text;
   // $VF: synthetic field
   private final CoreFactory this$0;

   CoreFactory$5(CoreFactory var1, String var2) {
      this.this$0 = var1;
      this.val$text = var2;
   }

   public void run() {
      Splash.updateText(this.val$text);
   }
}
