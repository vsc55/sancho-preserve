package sancho.view.viewer.actions;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.SResources;

public class FlipSashAction extends AbstractSashAction {
   public FlipSashAction(SashForm var1) {
      super(var1);
      this.setText(SResources.getString("mi.flipSash"));
      this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
   }

   public void run() {
      this.sashForm.setOrientation(this.sashForm.getOrientation() == 256 ? 512 : 256);
   }
}
