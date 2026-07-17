package sancho.view.viewFrame;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.view.preferences.PreferenceLoader;

class TabbedViewFrame$4 implements DisposeListener {
   // $VF: synthetic field
   private final TabbedViewFrame this$0;

   TabbedViewFrame$4(TabbedViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetDisposed(DisposeEvent var1) {
      CTabItem var2 = (CTabItem)var1.widget;
      PreferenceStore var3 = PreferenceLoader.getPreferenceStore();
      var3.setValue(this.this$0.tabPrefString + "Tabs", this.this$0.cTabFolder.getItemCount());
      int var4 = this.this$0.cTabFolder.indexOf(var2);
      var3.setValue(this.this$0.tabPrefString + "Tab_" + var4 + "_Name", var2.getText());
      if (this.this$0.cTabFolder.getSelection() == var2) {
         var3.setValue(this.this$0.tabPrefString + "Tab_" + var4 + "_Filters", this.this$0.gView.filtersToString());
      } else {
         var3.setValue(this.this$0.tabPrefString + "Tab_" + var4 + "_Filters", (String)var2.getData("filterString"));
      }
   }
}
