package org.eclipse.jface.viewers;

class CustomTableViewer$7 implements Runnable {
   // $VF: synthetic field
   private final CustomTableViewer this$0;

   CustomTableViewer$7(CustomTableViewer var1) {
      this.this$0 = var1;
   }

   public void run() {
      this.this$0.myInternalVirtualRefreshAll();
   }
}
