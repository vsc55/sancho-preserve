package sancho.view.viewer;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

public class CControlEditor {
   public int horizontalAlignment = 16777216;
   public boolean grabHorizontal = false;
   public int minimumWidth = 0;
   public int verticalAlignment = 16777216;
   public boolean grabVertical = false;
   public int minimumHeight = 0;
   Composite parent;
   Control editor;
   private Listener scrollbarListener;

   public CControlEditor(Composite var1) {
      this.parent = var1;
      this.scrollbarListener = new Listener() {
         public void handleEvent(Event var1) {
         }
      };
      ScrollBar var2 = var1.getHorizontalBar();
      if (var2 != null) {
         var2.addListener(13, this.scrollbarListener);
      }

      ScrollBar var3 = var1.getVerticalBar();
      if (var3 != null) {
         var3.addListener(13, this.scrollbarListener);
      }
   }

   Rectangle computeBounds() {
      Rectangle var1 = this.parent.getClientArea();
      Rectangle var2 = new Rectangle(var1.x, var1.y, this.minimumWidth, this.minimumHeight);
      if (this.grabHorizontal) {
         var2.width = Math.max(var1.width, this.minimumWidth);
      }

      if (this.grabVertical) {
         var2.height = Math.max(var1.height, this.minimumHeight);
      }

      switch (this.horizontalAlignment) {
         case 16384:
            break;
         case 131072:
            var2.x = var2.x + (var1.width - var2.width);
            break;
         default:
            var2.x = var2.x + (var1.width - var2.width) / 2;
      }

      switch (this.verticalAlignment) {
         case 128:
            break;
         case 1024:
            var2.y = var2.y + (var1.height - var2.height);
            break;
         default:
            var2.y = var2.y + (var1.height - var2.height) / 2;
      }

      return var2;
   }

   public void dispose() {
      if (!this.parent.isDisposed()) {
         ScrollBar var1 = this.parent.getHorizontalBar();
         if (var1 != null) {
            var1.removeListener(13, this.scrollbarListener);
         }

         ScrollBar var2 = this.parent.getVerticalBar();
         if (var2 != null) {
            var2.removeListener(13, this.scrollbarListener);
         }
      }

      this.parent = null;
      this.editor = null;
      this.scrollbarListener = null;
   }

   public Control getEditor() {
      return this.editor;
   }

   public void layout() {
      this.resize();
   }

   void resize() {
      if (this.editor == null || this.editor.isDisposed()) {
         ;
      }
   }

   void scroll(Event var1) {
      if (this.editor != null && !this.editor.isDisposed()) {
         this.resize();
      }
   }

   public void setEditor(Control var1) {
      if (var1 == null) {
         this.editor = null;
      } else {
         this.editor = var1;
         this.resize();
         if (!var1.isDisposed()) {
            var1.setVisible(true);
         }
      }
   }
}
