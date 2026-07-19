package sancho.view.utility;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;

public class MaximizeSashMouseAdapter extends HeaderBarMouseAdapter {
   private SashForm sashForm;
   private Control control;

   public MaximizeSashMouseAdapter(CLabel cLabel, MenuManager menuManager, SashForm sashForm, Control control) {
      super(cLabel, menuManager);
      this.sashForm = sashForm;
      this.control = control;
   }

   public void mouseDoubleClick(MouseEvent event) {
      WidgetFactory.toggleMaximizedSashFormControl(this.sashForm, this.control);
   }
}
