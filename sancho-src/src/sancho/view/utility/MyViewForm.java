package sancho.view.utility;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;

public class MyViewForm extends Composite {
   public int marginWidth = 0;
   public int marginHeight = 0;
   public int horizontalSpacing = 0;
   public int verticalSpacing = 1;
   public static RGB borderInsideRGB = new RGB(132, 130, 132);
   public static RGB borderMiddleRGB = new RGB(143, 141, 138);
   public static RGB borderOutsideRGB = new RGB(171, 168, 165);
   private Control topLeft;
   private Control topCenter;
   private Control topRight;
   private Control content;
   private boolean separateTopCenter = false;
   private boolean showBorder = false;
   private int borderTop = 0;
   private int borderBottom = 0;
   private int borderLeft = 0;
   private int borderRight = 0;
   private Color borderColor1 = new Color(this.getDisplay(), borderInsideRGB);
   private Color borderColor2 = new Color(this.getDisplay(), borderMiddleRGB);
   private Color borderColor3 = new Color(this.getDisplay(), borderOutsideRGB);
   private Rectangle oldArea;
   private static final int OFFSCREEN = -200;

   public MyViewForm(Composite parent, int style) {
      super(parent, checkStyle(style));
      this.setBorderVisible((style & 2048) != 0);
      Listener listener = new Listener() {
         public void handleEvent(Event event) {
            switch (event.type) {
               case 9:
                  MyViewForm.this.onPaint(event.gc);
               case 10:
               default:
                  break;
               case 11:
                  MyViewForm.this.onResize();
                  break;
               case 12:
                  MyViewForm.this.onDispose();
            }
         }
      };
      int[] eventTypes = new int[]{12, 9, 11};

      for (int i = 0; i < eventTypes.length; i++) {
         this.addListener(eventTypes[i], listener);
      }
   }

   public static Color changeColor(RGB rgb, int delta) {
      int red = modifyIntColor(rgb.red, delta);
      int green = modifyIntColor(rgb.green, delta);
      int blue = modifyIntColor(rgb.blue, delta);
      return new Color(null, red, green, blue);
   }

   public static Color changeColor(RGB rgb, int delta, int fallback) {
      int red = modifyIntColor(rgb.red, delta, fallback);
      int green = modifyIntColor(rgb.green, delta, fallback);
      int blue = modifyIntColor(rgb.blue, delta, fallback);
      return new Color(null, red, green, blue);
   }

   public static int modifyIntColor(int value, int delta) {
      return modifyIntColor(value, delta, value);
   }

   public static int modifyIntColor(int value, int delta, int fallback) {
      return value + delta >= 0 && value + delta <= 255 ? value + delta : fallback;
   }

   static int checkStyle(int style) {
      int mask = 109051904;
      return style & mask | 1048576;
   }

   public Point computeSize(int wHint, int hHint, boolean changed) {
      this.checkWidget();
      Point leftSize = new Point(0, 0);
      if (this.topLeft != null) {
         leftSize = this.topLeft.computeSize(-1, -1);
      }

      Point centerSize = new Point(0, 0);
      if (this.topCenter != null) {
         centerSize = this.topCenter.computeSize(-1, -1);
      }

      Point rightSize = new Point(0, 0);
      if (this.topRight != null) {
         rightSize = this.topRight.computeSize(-1, -1);
      }

      Point size = new Point(0, 0);
      if (this.separateTopCenter || wHint != -1 && leftSize.x + centerSize.x + rightSize.x > wHint) {
         size.x = leftSize.x + rightSize.x;
         if (leftSize.x > 0 && rightSize.x > 0) {
            size.x = size.x + this.horizontalSpacing;
         }

         size.x = Math.max(centerSize.x, size.x);
         size.y = Math.max(leftSize.y, rightSize.y);
         if (this.topCenter != null) {
            size.y = size.y + centerSize.y;
            if (this.topLeft != null || this.topRight != null) {
               size.y = size.y + this.verticalSpacing;
            }
         }
      } else {
         size.x = leftSize.x + centerSize.x + rightSize.x;
         int count = -1;
         if (leftSize.x > 0) {
            count++;
         }

         if (centerSize.x > 0) {
            count++;
         }

         if (rightSize.x > 0) {
            count++;
         }

         if (count > 0) {
            size.x = size.x + count * this.horizontalSpacing;
         }

         size.y = Math.max(leftSize.y, Math.max(centerSize.y, rightSize.y));
      }

      if (this.content != null) {
         new Point(0, 0);
         Point contentSize = this.content.computeSize(-1, -1);
         size.x = Math.max(size.x, contentSize.x);
         size.y = size.y + contentSize.y;
         if (size.y > contentSize.y) {
            size.y = size.y + this.verticalSpacing;
         }
      }

      size.x = size.x + 2 * this.marginWidth;
      size.y = size.y + 2 * this.marginHeight;
      if (wHint != -1) {
         size.x = wHint;
      }

      if (hHint != -1) {
         size.y = hHint;
      }

      Rectangle trim = this.computeTrim(0, 0, size.x, size.y);
      return new Point(trim.width, trim.height);
   }

   public Rectangle computeTrim(int x, int y, int width, int height) {
      this.checkWidget();
      int trimX = x - this.borderLeft;
      int trimY = y - this.borderTop;
      int trimWidth = width + this.borderLeft + this.borderRight;
      int trimHeight = height + this.borderTop + this.borderBottom;
      return new Rectangle(trimX, trimY, trimWidth, trimHeight);
   }

   public Rectangle getClientArea() {
      this.checkWidget();
      Rectangle rect = super.getClientArea();
      rect.x = rect.x + this.borderLeft;
      rect.y = rect.y + this.borderTop;
      rect.width = rect.width - (this.borderLeft + this.borderRight);
      rect.height = rect.height - (this.borderTop + this.borderBottom);
      return rect;
   }

   public Control getContent() {
      return this.content;
   }

   public Control getTopCenter() {
      return this.topCenter;
   }

   public Control getTopLeft() {
      return this.topLeft;
   }

   public Control getTopRight() {
      return this.topRight;
   }

   public void layout(boolean changed) {
      this.checkWidget();
      Rectangle clientArea = this.getClientArea();
      Point leftSize = new Point(0, 0);
      if (this.topLeft != null && !this.topLeft.isDisposed()) {
         leftSize = this.topLeft.computeSize(-1, -1);
      }

      Point centerSize = new Point(0, 0);
      if (this.topCenter != null && !this.topCenter.isDisposed()) {
         centerSize = this.topCenter.computeSize(-1, -1);
      }

      Point rightSize = new Point(0, 0);
      if (this.topRight != null && !this.topRight.isDisposed()) {
         rightSize = this.topRight.computeSize(-1, -1);
      }

      int totalWidth = leftSize.x + centerSize.x + rightSize.x + 2 * this.marginWidth;
      int count = -1;
      if (leftSize.x > 0) {
         count++;
      }

      if (centerSize.x > 0) {
         count++;
      }

      if (rightSize.x > 0) {
         count++;
      }

      if (count > 0) {
         totalWidth += count * this.horizontalSpacing;
      }

      int x = clientArea.x + clientArea.width - this.marginWidth;
      int y = clientArea.y + this.marginHeight;
      boolean placed = false;
      if (!this.separateTopCenter && totalWidth <= clientArea.width) {
         int rowHeight = Math.max(rightSize.y, Math.max(centerSize.y, leftSize.y));
         if (this.topRight != null && !this.topRight.isDisposed()) {
            placed = true;
            x -= rightSize.x;
            this.topRight.setBounds(x, y, rightSize.x, rowHeight);
            x -= this.horizontalSpacing;
         }

         if (this.topCenter != null && !this.topCenter.isDisposed()) {
            placed = true;
            x -= centerSize.x;
            this.topCenter.setBounds(x, y, centerSize.x, rowHeight);
            x -= this.horizontalSpacing;
         }

         if (this.topLeft != null && !this.topLeft.isDisposed()) {
            placed = true;
            leftSize = this.topLeft.computeSize(x - clientArea.x - this.marginWidth, rowHeight);
            this.topLeft.setBounds(clientArea.x + this.marginWidth, y, leftSize.x, rowHeight);
         }

         if (placed) {
            y += rowHeight + this.verticalSpacing;
         }
      } else {
         int rowHeight = Math.max(rightSize.y, leftSize.y);
         if (this.topRight != null && !this.topRight.isDisposed()) {
            placed = true;
            x -= rightSize.x;
            this.topRight.setBounds(x, y, rightSize.x, rowHeight);
            x -= this.horizontalSpacing;
         }

         if (this.topLeft != null && !this.topLeft.isDisposed()) {
            placed = true;
            leftSize = this.topLeft.computeSize(x - clientArea.x - this.marginWidth, -1);
            this.topLeft.setBounds(clientArea.x + this.marginWidth, y, leftSize.x, rowHeight);
         }

         if (placed) {
            y += rowHeight + this.verticalSpacing;
         }

         if (this.topCenter != null && !this.topCenter.isDisposed()) {
            placed = true;
            centerSize = this.topCenter.computeSize(clientArea.width - 2 * this.marginWidth, -1);
            this.topCenter.setBounds(clientArea.x + clientArea.width - this.marginWidth - centerSize.x, y, centerSize.x, centerSize.y);
            y += centerSize.y + this.verticalSpacing;
         }
      }

      if (this.content != null && !this.content.isDisposed()) {
         this.content.setBounds(clientArea.x + this.marginWidth, y, clientArea.width - 2 * this.marginWidth, clientArea.y + clientArea.height - y - this.marginHeight);
      }
   }

   void onDispose() {
      if (this.borderColor1 != null) {
         this.borderColor1.dispose();
      }

      this.borderColor1 = null;
      if (this.borderColor2 != null) {
         this.borderColor2.dispose();
      }

      this.borderColor2 = null;
      if (this.borderColor3 != null) {
         this.borderColor3.dispose();
      }

      this.borderColor3 = null;
      this.topLeft = null;
      this.topCenter = null;
      this.topRight = null;
      this.content = null;
      this.oldArea = null;
   }

   void onPaint(GC gc) {
      Color foreground = gc.getForeground();
      Point size = this.getSize();
      if (this.showBorder) {
         if ((this.getStyle() & 8388608) != 0) {
            gc.setForeground(this.borderColor1);
            gc.drawRectangle(0, 0, size.x - 1, size.y - 1);
         } else {
            gc.setForeground(this.borderColor1);
            gc.drawRectangle(0, 0, size.x - 3, size.y - 3);
            gc.setForeground(this.borderColor1);
            gc.setBackground(this.getDisplay().getSystemColor(1));
            gc.fillRectangle(0, 0, size.x - 3, size.y - 3);
            gc.setBackground(this.getDisplay().getSystemColor(18));
            gc.fillRectangle(1, 1, size.x - 3, size.y - 3);
            gc.setForeground(this.borderColor2);
            gc.drawLine(1, size.y - 2, size.x - 1, size.y - 2);
            gc.drawLine(size.x - 2, 1, size.x - 2, size.y - 1);
            gc.setForeground(this.borderColor3);
            gc.drawLine(2, size.y - 1, size.x - 2, size.y - 1);
            gc.drawLine(size.x - 1, 2, size.x - 1, size.y - 2);
         }
      }

      gc.setForeground(foreground);
   }

   void onResize() {
      this.layout();
      Rectangle clientArea = super.getClientArea();
      if (this.oldArea != null && this.oldArea.width != 0 && this.oldArea.height != 0) {
         int widthDelta = 0;
         if (this.oldArea.width < clientArea.width) {
            widthDelta = clientArea.width - this.oldArea.width + this.borderRight;
         } else if (this.oldArea.width > clientArea.width) {
            widthDelta = this.borderRight;
         }

         this.redraw(clientArea.x + clientArea.width - widthDelta, clientArea.y, widthDelta, clientArea.height, false);
         int heightDelta = 0;
         if (this.oldArea.height < clientArea.height) {
            heightDelta = clientArea.height - this.oldArea.height + this.borderBottom;
         }

         if (this.oldArea.height > clientArea.height) {
            heightDelta = this.borderBottom;
         }

         this.redraw(clientArea.x, clientArea.y + clientArea.height - heightDelta, clientArea.width, heightDelta, false);
      } else {
         this.redraw();
      }

      this.oldArea = clientArea;
   }

   public void setContent(Control control) {
      this.checkWidget();
      if (control != null && control.getParent() != this) {
         SWT.error(5);
      }

      if (this.content != null && !this.content.isDisposed()) {
         this.content.setBounds(-200, -200, 0, 0);
      }

      this.content = control;
      this.layout();
   }

   public void setFont(Font font) {
      super.setFont(font);
      if (this.topLeft != null && !this.topLeft.isDisposed()) {
         this.topLeft.setFont(font);
      }

      if (this.topCenter != null && !this.topCenter.isDisposed()) {
         this.topCenter.setFont(font);
      }

      if (this.topRight != null && !this.topRight.isDisposed()) {
         this.topRight.setFont(font);
      }

      this.layout();
   }

   public void setLayout(Layout layout) {
      this.checkWidget();
   }

   public void setTopCenter(Control control) {
      this.checkWidget();
      if (control != null && control.getParent() != this) {
         SWT.error(5);
      }

      if (this.topCenter != null && !this.topCenter.isDisposed()) {
         this.topCenter.setBounds(-200, -200, 0, 0);
      }

      this.topCenter = control;
      this.layout();
   }

   public void setTopLeft(Control control) {
      this.checkWidget();
      if (control != null && control.getParent() != this) {
         SWT.error(5);
      }

      if (this.topLeft != null && !this.topLeft.isDisposed()) {
         this.topLeft.setBounds(-200, -200, 0, 0);
      }

      this.topLeft = control;
      this.layout();
   }

   public void setTopRight(Control control) {
      this.checkWidget();
      if (control != null && control.getParent() != this) {
         SWT.error(5);
      }

      if (this.topRight != null && !this.topRight.isDisposed()) {
         this.topRight.setBounds(-200, -200, 0, 0);
      }

      this.topRight = control;
      this.layout();
   }

   public void setBorderVisible(boolean visible) {
      this.checkWidget();
      if (this.showBorder != visible) {
         this.showBorder = visible;
         if (this.showBorder) {
            if ((this.getStyle() & 8388608) != 0) {
               this.borderLeft = this.borderTop = this.borderRight = this.borderBottom = 1;
            } else {
               this.borderLeft = this.borderTop = 1;
               this.borderRight = this.borderBottom = 3;
            }
         } else {
            this.borderBottom = this.borderTop = this.borderLeft = this.borderRight = 0;
         }

         this.layout();
         this.redraw();
      }
   }

   public void setTopCenterSeparate(boolean separate) {
      this.checkWidget();
      this.separateTopCenter = separate;
      this.layout();
   }
}
