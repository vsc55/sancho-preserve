package sancho.view.utility;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.core.Sancho;
import sancho.view.preferences.PreferenceLoader;

class DNDBox$1 implements DisposeListener {
   // $VF: synthetic field
   private final DNDBox this$0;

   DNDBox$1(DNDBox var1) {
      this.this$0 = var1;
   }

   public void widgetDisposed(DisposeEvent var1) {
      PreferenceConverter.setValue(PreferenceLoader.getPreferenceStore(), "dndBoxWindowBounds", this.this$0.shell.getBounds());
      if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getClientStats().deleteObserver(this.this$0);
      }
   }
}
