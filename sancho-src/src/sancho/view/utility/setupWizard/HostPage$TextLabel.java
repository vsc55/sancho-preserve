package sancho.view.utility.setupWizard;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class HostPage$TextLabel {
   Text text;
   Label label;
   // $VF: synthetic field
   private final HostPage this$0;

   public HostPage$TextLabel(HostPage var1) {
      this.this$0 = var1;
   }

   public void setText(String var1) {
      this.text.setText(var1);
   }

   public void addText(Text var1) {
      this.text = var1;
   }

   public void addLabel(Label var1) {
      this.label = var1;
   }

   public String getText() {
      return this.text.getText();
   }

   public void setEchoChar(char var1) {
      this.text.setEchoChar(var1);
   }

   public void setEnabled(boolean var1) {
      this.text.setEnabled(var1);
      this.label.setEnabled(var1);
   }
}
