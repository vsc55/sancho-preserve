package sancho.view.statistics;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.view.preferences.PreferenceLoader;

class GraphHistory$2 implements DisposeListener {
   // $VF: synthetic field
   private final GraphHistory this$0;

   GraphHistory$2(GraphHistory var1) {
      this.this$0 = var1;
   }

   public synchronized void widgetDisposed(DisposeEvent var1) {
      PreferenceStore var2 = PreferenceLoader.getPreferenceStore();
      PreferenceConverter.setValue(var2, "graphHistoryWindowBounds", GraphHistory.access$000(this.this$0).getBounds());
   }
}
