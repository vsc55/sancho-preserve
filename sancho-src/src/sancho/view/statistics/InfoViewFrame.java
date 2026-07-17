package sancho.view.statistics;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Text;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class InfoViewFrame extends SashViewFrame {
   Text myText = new Text(this.getChildComposite(), 778);

   public InfoViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.createViewListener(new InfoViewListener(this));
   }

   public Text getText() {
      return this.myText;
   }
}
