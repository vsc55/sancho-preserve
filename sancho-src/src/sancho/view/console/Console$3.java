package sancho.view.console;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import sancho.view.utility.WebLauncher;

class Console$3 extends MouseAdapter {
   int x;
   int y;
   // $VF: synthetic field
   private final Console this$0;

   Console$3(Console var1) {
      this.this$0 = var1;
   }

   public void mouseDown(MouseEvent var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public void mouseUp(MouseEvent var1) {
      if (var1.x == this.x) {
         if (var1.y == this.y) {
            if (var1.button == 1) {
               StyledText var2 = (StyledText)var1.widget;
               int var3 = -1;

               try {
                  var3 = var2.getOffsetAtLocation(new Point(var1.x, var1.y));
                  int var4 = var2.getLineAtOffset(var3);
                  String var5 = var2.getContent().getLine(var4);
                  int var6 = var2.getOffsetAtLine(var4);
                  StyleRange[] var7 = var2.getStyleRanges(var6, var5.length());
                  if (var7 != null) {
                     for (int var8 = 0; var8 < var7.length; var8++) {
                        if (var7[var8].underline) {
                           int var9 = var7[var8].start;
                           int var10 = var9 + var7[var8].length;
                           if (var3 >= var9 && var3 <= var10) {
                              String var11 = var2.getTextRange(var9, var7[var8].length);
                              if (!var11.toLowerCase().startsWith("http://")) {
                                 var11 = "http://" + var11;
                              }

                              WebLauncher.openLink(var11);
                           }
                        }
                     }
                  }
               } catch (IllegalArgumentException var12) {
               } catch (Exception var13) {
               }
            }
         }
      }
   }
}
