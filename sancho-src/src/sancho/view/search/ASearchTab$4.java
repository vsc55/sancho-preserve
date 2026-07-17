package sancho.view.search;

import sancho.core.Sancho;

class ASearchTab$4 implements Runnable {
   // $VF: synthetic field
   private final ASearchTab this$0;

   ASearchTab$4(ASearchTab var1) {
      this.this$0 = var1;
   }

   public void run() {
      if (this.this$0.networkCombo != null && !this.this$0.networkCombo.isDisposed()) {
         if (Sancho.hasCollectionFactory() && this.this$0.viewFrame.getCore().getNetworkCollection().getEnabledAndSearchable() != 0) {
            this.this$0.syncNetworkCombo(false);
         } else {
            if (this.this$0.searchCombo.isEnabled()) {
               this.this$0.searchCombo.setText(ASearchTab.S_NO_NETWORK);
               this.this$0.searchCombo.setEnabled(false);
            }

            if (this.this$0.networkCombo.isEnabled()) {
               this.this$0.networkCombo.removeAll();
               this.this$0.networkCombo.setText(ASearchTab.S_NO_NETWORK);
               this.this$0.networkCombo.setEnabled(false);
            }

            if (this.this$0.searchTab.isButtonEnabled()) {
               this.this$0.searchTab.setButtonEnabled(false);
            }
         }
      }
   }
}
