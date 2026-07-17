package sancho.view.console;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

class Console$9 extends KeyAdapter {
   // $VF: synthetic field
   private final Console this$0;

   Console$9(Console var1) {
      this.this$0 = var1;
   }

   public void keyPressed(KeyEvent var1) {
      int var2 = this.this$0.infoDisplay.getClientArea().height / this.this$0.infoDisplay.getLineHeight();
      if ((var1.stateMask & 262144) != 0 && var1.keyCode == 102) {
         new Console$FindDialog(this.this$0).open();
      } else if ((var1.stateMask & 262144) != 0 && var1.keyCode == 108) {
         this.this$0.infoDisplay.replaceTextRange(0, this.this$0.infoDisplay.getText().length(), "");
      } else {
         switch (var1.keyCode) {
            case 13:
            case 16777296:
               this.this$0.appendInput();
               this.this$0.sendMessage();
               if (this.this$0.input.getText().length() > 0) {
                  if (this.this$0.commandHistory.contains(this.this$0.input.getText())) {
                     this.this$0.commandHistory.remove(this.this$0.input.getText());
                  }

                  this.this$0.commandHistory.add(this.this$0.input.getText());
                  this.this$0.historyMark = this.this$0.commandHistory.size() - 1;
               }

               this.this$0.input.setText("");
               break;
            case 27:
               this.this$0.input.setText("");
               break;
            case 16777217:
               if (this.this$0.commandHistory.size() > 0) {
                  if (this.this$0.historyMark < 0 || this.this$0.historyMark >= this.this$0.commandHistory.size()) {
                     this.this$0.historyMark = this.this$0.commandHistory.size() - 1;
                  }

                  this.this$0.input.setText((String)this.this$0.commandHistory.get(this.this$0.historyMark--));
                  this.this$0.input.setSelection(this.this$0.input.getText().length());
                  var1.doit = false;
               }
               break;
            case 16777218:
               if (this.this$0.commandHistory.size() > 0) {
                  if (this.this$0.historyMark >= this.this$0.commandHistory.size() || this.this$0.historyMark < 0) {
                     this.this$0.historyMark = 0;
                  }

                  this.this$0.input.setText((String)this.this$0.commandHistory.get(this.this$0.historyMark++));
                  this.this$0.input.setSelection(this.this$0.input.getText().length());
               }
               break;
            case 16777221:
               if (this.this$0.infoDisplay.getTopIndex() > var2) {
                  this.this$0.infoDisplay.setTopIndex(this.this$0.infoDisplay.getTopIndex() - var2);
               } else {
                  this.this$0.infoDisplay.setTopIndex(0);
               }
               break;
            case 16777222:
               this.this$0.infoDisplay.setTopIndex(this.this$0.infoDisplay.getTopIndex() + var2);
         }
      }
   }
}
