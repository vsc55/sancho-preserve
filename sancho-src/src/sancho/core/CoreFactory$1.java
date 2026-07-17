package sancho.core;

import sancho.view.utility.Splash;

class CoreFactory$1 implements Runnable {
   // $VF: synthetic field
   private final String val$text;
   // $VF: synthetic field
   private final String val$message;
   // $VF: synthetic field
   private final CoreFactory this$0;

   CoreFactory$1(CoreFactory var1, String var2, String var3) {
      this.this$0 = var1;
      this.val$text = var2;
      this.val$message = var3;
   }

   public void run() {
      Splash.setVisible(false);
      CoreFactory.access$002(this.this$0, CoreFactory.openQuestion(null, this.val$text, this.val$message));
   }
}
