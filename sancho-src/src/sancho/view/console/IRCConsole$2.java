package sancho.view.console;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

class IRCConsole$2 implements Runnable {
   // $VF: synthetic field
   private final String[] val$stringArray;
   // $VF: synthetic field
   private final int[] val$colorCodes;
   // $VF: synthetic field
   private final IRCConsole this$0;

   IRCConsole$2(IRCConsole var1, String[] var2, int[] var3) {
      this.this$0 = var1;
      this.val$stringArray = var2;
      this.val$colorCodes = var3;
   }

   public void run() {
      IRCConsole.access$000(this.this$0).setLength(0);

      for (int var1 = 0; var1 < this.val$stringArray.length; var1++) {
         IRCConsole.access$000(this.this$0).append(this.val$stringArray[var1]);
      }

      this.this$0.appendNewLine(IRCConsole.access$000(this.this$0).toString());
      int var2 = this.this$0.infoDisplay.getCharCount() - IRCConsole.access$000(this.this$0).length() - this.this$0.getLineDelimiter().length();
      Object var3 = null;

      for (int var4 = 0; var4 < this.val$colorCodes.length; var4++) {
         var3 = this.this$0.intToColor(this.val$colorCodes[var4]);
         if (var3 != null) {
            this.this$0
               .infoDisplay
               .setStyleRange(new StyleRange(var2, this.val$stringArray[var4].length(), (Color)var3, this.this$0.infoDisplay.getBackground()));
         }

         var2 += this.val$stringArray[var4].length();
      }
   }
}
