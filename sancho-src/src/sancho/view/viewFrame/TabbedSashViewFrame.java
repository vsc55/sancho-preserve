package sancho.view.viewFrame;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.MaximizeSashMouseAdapter;

public class TabbedSashViewFrame extends TabbedViewFrame {
   protected SashForm parentSashForm;

   public TabbedSashViewFrame(SashForm sashForm, String prefString, String imageString, AbstractTab aTab, String tabPrefString) {
      this(sashForm, prefString, imageString, aTab, tabPrefString, false);
   }

   public TabbedSashViewFrame(SashForm sashForm, String prefString, String imageString, AbstractTab aTab, String tabPrefString, boolean flat) {
      super(sashForm, prefString, imageString, aTab, tabPrefString, flat);
      this.parentSashForm = sashForm;
   }

   public void createViewListener(TabbedSashViewListener viewListener) {
      this.setupViewListener(viewListener);
      this.cLabel.addMouseListener(new MaximizeSashMouseAdapter(this.cLabel, this.menuManager, this.getParentSashForm(), this.getControl()));
   }

   public SashForm getParentSashForm() {
      return this.parentSashForm;
   }
}
