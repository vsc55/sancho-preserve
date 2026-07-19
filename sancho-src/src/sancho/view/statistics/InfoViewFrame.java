package sancho.view.statistics;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Text;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class InfoViewFrame extends SashViewFrame {
   Text myText = new Text(this.getChildComposite(), 778);

   public InfoViewFrame(SashForm sashForm, String name, String tooltip, AbstractTab tab) {
      super(sashForm, name, tooltip, tab);
      this.createViewListener(new InfoViewListener(this));
   }

   public Text getText() {
      return this.myText;
   }
}
