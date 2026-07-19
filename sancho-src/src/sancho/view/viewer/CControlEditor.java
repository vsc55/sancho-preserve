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

   public CControlEditor(Composite composite) {
      this.parent = composite;
      this.scrollbarListener = new Listener() {
         public void handleEvent(Event event) {
         }
      };
      ScrollBar horizontalBar = composite.getHorizontalBar();
      if (horizontalBar != null) {
         horizontalBar.addListener(13, this.scrollbarListener);
      }

      ScrollBar verticalBar = composite.getVerticalBar();
      if (verticalBar != null) {
         verticalBar.addListener(13, this.scrollbarListener);
      }
   }

   Rectangle computeBounds() {
      Rectangle clientArea = this.parent.getClientArea();
      Rectangle bounds = new Rectangle(clientArea.x, clientArea.y, this.minimumWidth, this.minimumHeight);
      if (this.grabHorizontal) {
         bounds.width = Math.max(clientArea.width, this.minimumWidth);
      }

      if (this.grabVertical) {
         bounds.height = Math.max(clientArea.height, this.minimumHeight);
      }

      switch (this.horizontalAlignment) {
         case 16384:
            break;
         case 131072:
            bounds.x = bounds.x + (clientArea.width - bounds.width);
            break;
         default:
            bounds.x = bounds.x + (clientArea.width - bounds.width) / 2;
      }

      switch (this.verticalAlignment) {
         case 128:
            break;
         case 1024:
            bounds.y = bounds.y + (clientArea.height - bounds.height);
            break;
         default:
            bounds.y = bounds.y + (clientArea.height - bounds.height) / 2;
      }

      return bounds;
   }

   public void dispose() {
      if (!this.parent.isDisposed()) {
         ScrollBar horizontalBar = this.parent.getHorizontalBar();
         if (horizontalBar != null) {
            horizontalBar.removeListener(13, this.scrollbarListener);
         }

         ScrollBar verticalBar = this.parent.getVerticalBar();
         if (verticalBar != null) {
            verticalBar.removeListener(13, this.scrollbarListener);
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

   void scroll(Event event) {
      if (this.editor != null && !this.editor.isDisposed()) {
         this.resize();
      }
   }

   public void setEditor(Control control) {
      if (control == null) {
         this.editor = null;
      } else {
         this.editor = control;
         this.resize();
         if (!control.isDisposed()) {
            control.setVisible(true);
         }
      }
   }
}
