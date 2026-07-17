package sancho.core;

class CoreFactory$2 implements Runnable {
   // $VF: synthetic field
   private final CoreFactory this$0;

   CoreFactory$2(CoreFactory var1) {
      this.this$0 = var1;
   }

   public void run() {
      Sancho.getCoreConsole().getShell().close();
   }
}
