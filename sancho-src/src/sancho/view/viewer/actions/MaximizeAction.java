package sancho.view.viewer.actions;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class MaximizeAction extends AbstractSashAction {
   private Control control;

   public MaximizeAction(SashForm var1, Control var2, String var3) {
      super(var1);
      this.control = var2;
      if (this.sashForm.getMaximizedControl() == null) {
         this.setText(SResources.getString("mi.maximize"));
         this.setImageDescriptor(SResources.getImageDescriptor("maximize"));
      } else {
         this.setText(SResources.getString("mi.show") + " " + SResources.getString(var3));
         this.setImageDescriptor(SResources.getImageDescriptor("restore"));
      }
   }

   public void run() {
      WidgetFactory.toggleMaximizedSashFormControl(this.sashForm, this.control);
   }
}
