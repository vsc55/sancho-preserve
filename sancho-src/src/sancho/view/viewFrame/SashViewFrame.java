package sancho.view.viewFrame;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.MaximizeSashMouseAdapter;

public class SashViewFrame extends ViewFrame {
   protected SashForm parentSashForm;

   public SashViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      this(var1, var2, var3, var4, false);
   }

   public SashViewFrame(SashForm var1, String var2, String var3, AbstractTab var4, boolean var5) {
      super(var1, var2, var3, var4, var5);
      this.parentSashForm = var1;
   }

   public void createViewListener(SashViewListener var1) {
      this.setupViewListener(var1);
      this.cLabel.addMouseListener(new MaximizeSashMouseAdapter(this.cLabel, this.menuManager, this.getParentSashForm(), this.getControl()));
   }

   public SashForm getParentSashForm() {
      return this.parentSashForm;
   }
}
