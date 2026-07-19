package sancho.view.viewFrame;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.MaximizeSashMouseAdapter;

public class SashViewFrame extends ViewFrame {
   protected SashForm parentSashForm;

   public SashViewFrame(SashForm sashForm, String prefString, String imageString, AbstractTab aTab) {
      this(sashForm, prefString, imageString, aTab, false);
   }

   public SashViewFrame(SashForm sashForm, String prefString, String imageString, AbstractTab aTab, boolean flat) {
      super(sashForm, prefString, imageString, aTab, flat);
      this.parentSashForm = sashForm;
   }

   public void createViewListener(SashViewListener viewListener) {
      this.setupViewListener(viewListener);
      this.cLabel.addMouseListener(new MaximizeSashMouseAdapter(this.cLabel, this.menuManager, this.getParentSashForm(), this.getControl()));
   }

   public SashForm getParentSashForm() {
      return this.parentSashForm;
   }
}
