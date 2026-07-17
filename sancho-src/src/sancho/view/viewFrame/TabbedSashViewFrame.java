package sancho.view.viewFrame;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.MaximizeSashMouseAdapter;

public class TabbedSashViewFrame extends TabbedViewFrame {
   protected SashForm parentSashForm;

   public TabbedSashViewFrame(SashForm var1, String var2, String var3, AbstractTab var4, String var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   public TabbedSashViewFrame(SashForm var1, String var2, String var3, AbstractTab var4, String var5, boolean var6) {
      super(var1, var2, var3, var4, var5, var6);
      this.parentSashForm = var1;
   }

   public void createViewListener(TabbedSashViewListener var1) {
      this.setupViewListener(var1);
      this.cLabel.addMouseListener(new MaximizeSashMouseAdapter(this.cLabel, this.menuManager, this.getParentSashForm(), this.getControl()));
   }

   public SashForm getParentSashForm() {
      return this.parentSashForm;
   }
}
