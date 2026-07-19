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

   public CSpinner(Composite parent, int style) {
      super(parent, style);
      this.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 0, 0, false));
      this.text = new Text(this, 4);
      GridData gridData = new GridData(1808);
      gridData.grabExcessHorizontalSpace = true;
      gridData.grabExcessVerticalSpace = true;
      gridData.verticalSpan = 2;
      this.text.setLayoutData(gridData);
      Point size = this.text.computeSize(-1, -1);
      int buttonHeight = size.y / 2 + 1;
      this.up = new Button(this, 132);
      gridData = new GridData();
      gridData.heightHint = buttonHeight;
      this.up.setLayoutData(gridData);
      this.down = new Button(this, 1028);
      gridData = new GridData();
      gridData.heightHint = buttonHeight;
      this.down.setLayoutData(gridData);
      this.text.addListener(25, new Listener() {
         public void handleEvent(Event event) {
            CSpinner.this.verify(event);
         }
      });
      this.text.addListener(31, new Listener() {
         public void handleEvent(Event event) {
            CSpinner.this.myTraverse(event);
         }
      });
      this.up.addListener(13, new Listener() {
         public void handleEvent(Event event) {
            CSpinner.this.up();
         }
      });
      this.down.addListener(13, new Listener() {
         public void handleEvent(Event event) {
            CSpinner.this.down();
         }
      });
      this.addListener(15, new Listener() {
         public void handleEvent(Event event) {
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

   public void setEnabled(boolean enabled) {
      this.text.setEnabled(enabled);
      super.setEnabled(enabled);
   }

   void verify(Event event) {
      try {
         Integer.parseInt(event.text);
      } catch (NumberFormatException notANumber) {
         switch (event.character) {
            case '\b':
            case '\u007f':
               break;
            case '+':
            case '-':
               if (event.start == 0) {
                  break;
               }
            default:
               event.doit = false;
         }
      }
   }

   protected boolean myTraverse(Event event) {
      switch (event.detail) {
         case 32:
            if (event.keyCode == 16777217) {
               event.doit = true;
               event.detail = 0;
               this.up();
               return true;
            }

            return false;
         case 64:
            if (event.keyCode == 16777218) {
               event.doit = true;
               event.detail = 0;
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

   public void setFont(Font font) {
      super.setFont(font);
      this.text.setFont(font);
   }

   public void setSelection(int selection) {
      if (selection < this.minimum) {
         selection = this.minimum;
      } else if (selection > this.maximum) {
         selection = this.maximum;
      }

      this.text.setText(String.valueOf(selection));
      this.text.selectAll();
      this.text.setFocus();
   }

   public int getSelection() {
      int selection;
      try {
         selection = Integer.parseInt(this.text.getText());
      } catch (NumberFormatException notANumber) {
         selection = 0;
      }

      return selection;
   }

   public void setMaximum(int maximum) {
      this.checkWidget();
      this.maximum = maximum;
   }

   public int getMaximum() {
      return this.maximum;
   }

   public void setMinimum(int minimum) {
      this.minimum = minimum;
   }

   public int getMinimum() {
      return this.minimum;
   }

   public Point computeSize(int wHint, int hHint, boolean changed) {
      Point size = super.computeSize(wHint, hHint, changed);
      int width = size.x;
      int height = size.y;
      GC gc = new GC(this.text);
      Point extent = gc.textExtent(String.valueOf(this.maximum));
      gc.dispose();
      int minWidth = extent.x + 16 + 15;
      if (width < minWidth) {
         width = minWidth;
      }

      return new Point(width, height);
   }
}
