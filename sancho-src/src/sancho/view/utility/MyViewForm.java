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
import org.eclipse.swt.widgets.Layout;

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

   public MyViewForm(Composite var1, int var2) {
      super(var1, checkStyle(var2));
      this.setBorderVisible((var2 & 2048) != 0);
      MyViewForm$1 var3 = new MyViewForm$1(this);
      int[] var4 = new int[]{12, 9, 11};

      for (int var5 = 0; var5 < var4.length; var5++) {
         this.addListener(var4[var5], var3);
      }
   }

   public static Color changeColor(RGB var0, int var1) {
      int var2 = modifyIntColor(var0.red, var1);
      int var3 = modifyIntColor(var0.green, var1);
      int var4 = modifyIntColor(var0.blue, var1);
      return new Color(null, var2, var3, var4);
   }

   public static Color changeColor(RGB var0, int var1, int var2) {
      int var3 = modifyIntColor(var0.red, var1, var2);
      int var4 = modifyIntColor(var0.green, var1, var2);
      int var5 = modifyIntColor(var0.blue, var1, var2);
      return new Color(null, var3, var4, var5);
   }

   public static int modifyIntColor(int var0, int var1) {
      return modifyIntColor(var0, var1, var0);
   }

   public static int modifyIntColor(int var0, int var1, int var2) {
      return var0 + var1 >= 0 && var0 + var1 <= 255 ? var0 + var1 : var2;
   }

   static int checkStyle(int var0) {
      int var1 = 109051904;
      return var0 & var1 | 1048576;
   }

   public Point computeSize(int var1, int var2, boolean var3) {
      this.checkWidget();
      Point var4 = new Point(0, 0);
      if (this.topLeft != null) {
         var4 = this.topLeft.computeSize(-1, -1);
      }

      Point var5 = new Point(0, 0);
      if (this.topCenter != null) {
         var5 = this.topCenter.computeSize(-1, -1);
      }

      Point var6 = new Point(0, 0);
      if (this.topRight != null) {
         var6 = this.topRight.computeSize(-1, -1);
      }

      Point var7 = new Point(0, 0);
      if (this.separateTopCenter || var1 != -1 && var4.x + var5.x + var6.x > var1) {
         var7.x = var4.x + var6.x;
         if (var4.x > 0 && var6.x > 0) {
            var7.x = var7.x + this.horizontalSpacing;
         }

         var7.x = Math.max(var5.x, var7.x);
         var7.y = Math.max(var4.y, var6.y);
         if (this.topCenter != null) {
            var7.y = var7.y + var5.y;
            if (this.topLeft != null || this.topRight != null) {
               var7.y = var7.y + this.verticalSpacing;
            }
         }
      } else {
         var7.x = var4.x + var5.x + var6.x;
         int var8 = -1;
         if (var4.x > 0) {
            var8++;
         }

         if (var5.x > 0) {
            var8++;
         }

         if (var6.x > 0) {
            var8++;
         }

         if (var8 > 0) {
            var7.x = var7.x + var8 * this.horizontalSpacing;
         }

         var7.y = Math.max(var4.y, Math.max(var5.y, var6.y));
      }

      if (this.content != null) {
         new Point(0, 0);
         Point var9 = this.content.computeSize(-1, -1);
         var7.x = Math.max(var7.x, var9.x);
         var7.y = var7.y + var9.y;
         if (var7.y > var9.y) {
            var7.y = var7.y + this.verticalSpacing;
         }
      }

      var7.x = var7.x + 2 * this.marginWidth;
      var7.y = var7.y + 2 * this.marginHeight;
      if (var1 != -1) {
         var7.x = var1;
      }

      if (var2 != -1) {
         var7.y = var2;
      }

      Rectangle var10 = this.computeTrim(0, 0, var7.x, var7.y);
      return new Point(var10.width, var10.height);
   }

   public Rectangle computeTrim(int var1, int var2, int var3, int var4) {
      this.checkWidget();
      int var5 = var1 - this.borderLeft;
      int var6 = var2 - this.borderTop;
      int var7 = var3 + this.borderLeft + this.borderRight;
      int var8 = var4 + this.borderTop + this.borderBottom;
      return new Rectangle(var5, var6, var7, var8);
   }

   public Rectangle getClientArea() {
      this.checkWidget();
      Rectangle var1 = super.getClientArea();
      var1.x = var1.x + this.borderLeft;
      var1.y = var1.y + this.borderTop;
      var1.width = var1.width - (this.borderLeft + this.borderRight);
      var1.height = var1.height - (this.borderTop + this.borderBottom);
      return var1;
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

   public void layout(boolean var1) {
      this.checkWidget();
      Rectangle var2 = this.getClientArea();
      Point var3 = new Point(0, 0);
      if (this.topLeft != null && !this.topLeft.isDisposed()) {
         var3 = this.topLeft.computeSize(-1, -1);
      }

      Point var4 = new Point(0, 0);
      if (this.topCenter != null && !this.topCenter.isDisposed()) {
         var4 = this.topCenter.computeSize(-1, -1);
      }

      Point var5 = new Point(0, 0);
      if (this.topRight != null && !this.topRight.isDisposed()) {
         var5 = this.topRight.computeSize(-1, -1);
      }

      int var6 = var3.x + var4.x + var5.x + 2 * this.marginWidth;
      int var7 = -1;
      if (var3.x > 0) {
         var7++;
      }

      if (var4.x > 0) {
         var7++;
      }

      if (var5.x > 0) {
         var7++;
      }

      if (var7 > 0) {
         var6 += var7 * this.horizontalSpacing;
      }

      int var8 = var2.x + var2.width - this.marginWidth;
      int var9 = var2.y + this.marginHeight;
      boolean var10 = false;
      if (!this.separateTopCenter && var6 <= var2.width) {
         int var19 = Math.max(var5.y, Math.max(var4.y, var3.y));
         if (this.topRight != null && !this.topRight.isDisposed()) {
            var10 = true;
            var8 -= var5.x;
            this.topRight.setBounds(var8, var9, var5.x, var19);
            var8 -= this.horizontalSpacing;
         }

         if (this.topCenter != null && !this.topCenter.isDisposed()) {
            var10 = true;
            var8 -= var4.x;
            this.topCenter.setBounds(var8, var9, var4.x, var19);
            var8 -= this.horizontalSpacing;
         }

         if (this.topLeft != null && !this.topLeft.isDisposed()) {
            var10 = true;
            var3 = this.topLeft.computeSize(var8 - var2.x - this.marginWidth, var19);
            this.topLeft.setBounds(var2.x + this.marginWidth, var9, var3.x, var19);
         }

         if (var10) {
            var9 += var19 + this.verticalSpacing;
         }
      } else {
         int var11 = Math.max(var5.y, var3.y);
         if (this.topRight != null && !this.topRight.isDisposed()) {
            var10 = true;
            var8 -= var5.x;
            this.topRight.setBounds(var8, var9, var5.x, var11);
            var8 -= this.horizontalSpacing;
         }

         if (this.topLeft != null && !this.topLeft.isDisposed()) {
            var10 = true;
            var3 = this.topLeft.computeSize(var8 - var2.x - this.marginWidth, -1);
            this.topLeft.setBounds(var2.x + this.marginWidth, var9, var3.x, var11);
         }

         if (var10) {
            var9 += var11 + this.verticalSpacing;
         }

         if (this.topCenter != null && !this.topCenter.isDisposed()) {
            var10 = true;
            var4 = this.topCenter.computeSize(var2.width - 2 * this.marginWidth, -1);
            this.topCenter.setBounds(var2.x + var2.width - this.marginWidth - var4.x, var9, var4.x, var4.y);
            var9 += var4.y + this.verticalSpacing;
         }
      }

      if (this.content != null && !this.content.isDisposed()) {
         this.content.setBounds(var2.x + this.marginWidth, var9, var2.width - 2 * this.marginWidth, var2.y + var2.height - var9 - this.marginHeight);
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

   void onPaint(GC var1) {
      Color var2 = var1.getForeground();
      Point var3 = this.getSize();
      if (this.showBorder) {
         if ((this.getStyle() & 8388608) != 0) {
            var1.setForeground(this.borderColor1);
            var1.drawRectangle(0, 0, var3.x - 1, var3.y - 1);
         } else {
            var1.setForeground(this.borderColor1);
            var1.drawRectangle(0, 0, var3.x - 3, var3.y - 3);
            var1.setForeground(this.borderColor1);
            var1.setBackground(this.getDisplay().getSystemColor(1));
            var1.fillRectangle(0, 0, var3.x - 3, var3.y - 3);
            var1.setBackground(this.getDisplay().getSystemColor(18));
            var1.fillRectangle(1, 1, var3.x - 3, var3.y - 3);
            var1.setForeground(this.borderColor2);
            var1.drawLine(1, var3.y - 2, var3.x - 1, var3.y - 2);
            var1.drawLine(var3.x - 2, 1, var3.x - 2, var3.y - 1);
            var1.setForeground(this.borderColor3);
            var1.drawLine(2, var3.y - 1, var3.x - 2, var3.y - 1);
            var1.drawLine(var3.x - 1, 2, var3.x - 1, var3.y - 2);
         }
      }

      var1.setForeground(var2);
   }

   void onResize() {
      this.layout();
      Rectangle var1 = super.getClientArea();
      if (this.oldArea != null && this.oldArea.width != 0 && this.oldArea.height != 0) {
         int var2 = 0;
         if (this.oldArea.width < var1.width) {
            var2 = var1.width - this.oldArea.width + this.borderRight;
         } else if (this.oldArea.width > var1.width) {
            var2 = this.borderRight;
         }

         this.redraw(var1.x + var1.width - var2, var1.y, var2, var1.height, false);
         int var3 = 0;
         if (this.oldArea.height < var1.height) {
            var3 = var1.height - this.oldArea.height + this.borderBottom;
         }

         if (this.oldArea.height > var1.height) {
            var3 = this.borderBottom;
         }

         this.redraw(var1.x, var1.y + var1.height - var3, var1.width, var3, false);
      } else {
         this.redraw();
      }

      this.oldArea = var1;
   }

   public void setContent(Control var1) {
      this.checkWidget();
      if (var1 != null && var1.getParent() != this) {
         SWT.error(5);
      }

      if (this.content != null && !this.content.isDisposed()) {
         this.content.setBounds(-200, -200, 0, 0);
      }

      this.content = var1;
      this.layout();
   }

   public void setFont(Font var1) {
      super.setFont(var1);
      if (this.topLeft != null && !this.topLeft.isDisposed()) {
         this.topLeft.setFont(var1);
      }

      if (this.topCenter != null && !this.topCenter.isDisposed()) {
         this.topCenter.setFont(var1);
      }

      if (this.topRight != null && !this.topRight.isDisposed()) {
         this.topRight.setFont(var1);
      }

      this.layout();
   }

   public void setLayout(Layout var1) {
      this.checkWidget();
   }

   public void setTopCenter(Control var1) {
      this.checkWidget();
      if (var1 != null && var1.getParent() != this) {
         SWT.error(5);
      }

      if (this.topCenter != null && !this.topCenter.isDisposed()) {
         this.topCenter.setBounds(-200, -200, 0, 0);
      }

      this.topCenter = var1;
      this.layout();
   }

   public void setTopLeft(Control var1) {
      this.checkWidget();
      if (var1 != null && var1.getParent() != this) {
         SWT.error(5);
      }

      if (this.topLeft != null && !this.topLeft.isDisposed()) {
         this.topLeft.setBounds(-200, -200, 0, 0);
      }

      this.topLeft = var1;
      this.layout();
   }

   public void setTopRight(Control var1) {
      this.checkWidget();
      if (var1 != null && var1.getParent() != this) {
         SWT.error(5);
      }

      if (this.topRight != null && !this.topRight.isDisposed()) {
         this.topRight.setBounds(-200, -200, 0, 0);
      }

      this.topRight = var1;
      this.layout();
   }

   public void setBorderVisible(boolean var1) {
      this.checkWidget();
      if (this.showBorder != var1) {
         this.showBorder = var1;
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

   public void setTopCenterSeparate(boolean var1) {
      this.checkWidget();
      this.separateTopCenter = var1;
      this.layout();
   }
}
