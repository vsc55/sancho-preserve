package sancho.view.utility;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class CSpinner extends Composite {
   static final int BUTTON_WIDTH = 16;
   Text text;
   Button up;
   Button down;
   int minimum;
   int maximum;

   public CSpinner(Composite var1, int var2) {
      super(var1, var2);
      this.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 0, 0, false));
      this.text = new Text(this, 4);
      GridData var3 = new GridData(1808);
      var3.grabExcessHorizontalSpace = true;
      var3.grabExcessVerticalSpace = true;
      var3.verticalSpan = 2;
      this.text.setLayoutData(var3);
      Point var4 = this.text.computeSize(-1, -1);
      int var5 = var4.y / 2 + 1;
      this.up = new Button(this, 132);
      var3 = new GridData();
      var3.heightHint = var5;
      this.up.setLayoutData(var3);
      this.down = new Button(this, 1028);
      var3 = new GridData();
      var3.heightHint = var5;
      this.down.setLayoutData(var3);
      this.text.addListener(25, new Listener() {
         public void handleEvent(Event var1) {
            CSpinner.this.verify(var1);
         }
      });
      this.text.addListener(31, new Listener() {
         public void handleEvent(Event var1) {
            CSpinner.this.myTraverse(var1);
         }
      });
      this.up.addListener(13, new Listener() {
         public void handleEvent(Event var1) {
            CSpinner.this.up();
         }
      });
      this.down.addListener(13, new Listener() {
         public void handleEvent(Event var1) {
            CSpinner.this.down();
         }
      });
      this.addListener(15, new Listener() {
         public void handleEvent(Event var1) {
            CSpinner.this.focusIn();
         }
      });
      this.text.setFont(this.getFont());
      this.minimum = 0;
      this.maximum = 9;
      this.setSelection(this.minimum);
   }

   public Text getText() {
      return this.text;
   }

   public void setEnabled(boolean var1) {
      this.text.setEnabled(var1);
      super.setEnabled(var1);
   }

   void verify(Event var1) {
      try {
         Integer.parseInt(var1.text);
      } catch (NumberFormatException var3) {
         switch (var1.character) {
            case '\b':
            case '\u007f':
               break;
            case '+':
            case '-':
               if (var1.start == 0) {
                  break;
               }
            default:
               var1.doit = false;
         }
      }
   }

   protected boolean myTraverse(Event var1) {
      switch (var1.detail) {
         case 32:
            if (var1.keyCode == 16777217) {
               var1.doit = true;
               var1.detail = 0;
               this.up();
               return true;
            }

            return false;
         case 64:
            if (var1.keyCode == 16777218) {
               var1.doit = true;
               var1.detail = 0;
               this.down();
               return true;
            }

            return false;
         default:
            return false;
      }
   }

   void up() {
      this.setSelection(this.getSelection() + 1);
      this.notifyListeners(13, new Event());
   }

   void down() {
      this.setSelection(this.getSelection() - 1);
      this.notifyListeners(13, new Event());
   }

   void focusIn() {
      this.text.setFocus();
   }

   public void setFont(Font var1) {
      super.setFont(var1);
      this.text.setFont(var1);
   }

   public void setSelection(int var1) {
      if (var1 < this.minimum) {
         var1 = this.minimum;
      } else if (var1 > this.maximum) {
         var1 = this.maximum;
      }

      this.text.setText(String.valueOf(var1));
      this.text.selectAll();
      this.text.setFocus();
   }

   public int getSelection() {
      int var1;
      try {
         var1 = Integer.parseInt(this.text.getText());
      } catch (NumberFormatException var3) {
         var1 = 0;
      }

      return var1;
   }

   public void setMaximum(int var1) {
      this.checkWidget();
      this.maximum = var1;
   }

   public int getMaximum() {
      return this.maximum;
   }

   public void setMinimum(int var1) {
      this.minimum = var1;
   }

   public int getMinimum() {
      return this.minimum;
   }

   public Point computeSize(int var1, int var2, boolean var3) {
      Point var4 = super.computeSize(var1, var2, var3);
      int var5 = var4.x;
      int var6 = var4.y;
      GC var7 = new GC(this.text);
      Point var8 = var7.textExtent(String.valueOf(this.maximum));
      var7.dispose();
      int var9 = var8.x + 16 + 15;
      if (var5 < var9) {
         var5 = var9;
      }

      return new Point(var5, var6);
   }
}
