package sancho.view.preferences;

import java.util.ArrayList;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

class RootPreferencePage$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final Text val$extText;
   // $VF: synthetic field
   private final Text val$progText;
   // $VF: synthetic field
   private final ArrayList val$extList;
   // $VF: synthetic field
   private final ArrayList val$progList;
   // $VF: synthetic field
   private final List val$list;
   // $VF: synthetic field
   private final RootPreferencePage this$0;

   RootPreferencePage$4(RootPreferencePage var1, Text var2, Text var3, ArrayList var4, ArrayList var5, List var6) {
      this.this$0 = var1;
      this.val$extText = var2;
      this.val$progText = var3;
      this.val$extList = var4;
      this.val$progList = var5;
      this.val$list = var6;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (!this.val$extText.getText().equals("") && !this.val$progText.getText().equals("")) {
         String var2 = this.val$extText.getText();
         String var3 = this.val$progText.getText();
         this.val$extList.add(var2);
         this.val$progList.add(var3);
         this.this$0.refreshList(this.val$list, this.val$extList, this.val$progList);
      }
   }
}
