package sancho.view.utility;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;

public class MaximizeSashMouseAdapter extends HeaderBarMouseAdapter {
   private SashForm sashForm;
   private Control control;

   public MaximizeSashMouseAdapter(CLabel var1, MenuManager var2, SashForm var3, Control var4) {
      super(var1, var2);
      this.sashForm = var3;
      this.control = var4;
   }

   public void mouseDoubleClick(MouseEvent var1) {
      WidgetFactory.toggleMaximizedSashFormControl(this.sashForm, this.control);
   }
}
