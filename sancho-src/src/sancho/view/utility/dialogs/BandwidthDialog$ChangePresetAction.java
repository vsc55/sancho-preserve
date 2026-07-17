package sancho.view.utility.dialogs;

import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;
import org.eclipse.jface.action.Action;
import sancho.core.Sancho;
import sancho.model.mldonkey.Option;

class BandwidthDialog$ChangePresetAction extends Action {
   private TObjectIntHashMap hm;
   // $VF: synthetic field
   private final BandwidthDialog this$0;

   public BandwidthDialog$ChangePresetAction(BandwidthDialog var1, String var2, TObjectIntHashMap var3) {
      super(var2);
      this.this$0 = var1;
      this.hm = var3;
   }

   public void run() {
      if (Sancho.hasCollectionFactory()) {
         TObjectIntIterator var1 = this.hm.iterator();

         while (var1.hasNext()) {
            var1.advance();
            Option var2 = (Option)var1.key();
            int var3 = var1.value();
            var2.setValue(String.valueOf(var3));
         }
      }
   }
}
