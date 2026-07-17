package sancho.view.viewer;

import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class CTreeEditor extends CControlEditor {
   Tree tree;
   TreeItem item;
   int column = 0;
   ControlListener columnListener;
   Rectangle currentBounds = new Rectangle(0, 0, 0, 0);

   public CTreeEditor(Tree var1) {
      super(var1);
      this.tree = var1;
      this.columnListener = new CTreeEditor$1(this);
      this.grabVertical = true;
   }

   Rectangle computeBounds() {
      return this.item != null && this.column >= 0 && !this.item.isDisposed() ? this.item.getBounds(this.column) : new Rectangle(0, 0, 0, 0);
   }

   public void dispose() {
      if (this.column > -1 && this.column < this.tree.getColumnCount()) {
         TreeColumn var1 = this.tree.getColumn(this.column);
         var1.removeControlListener(this.columnListener);
      }

      this.columnListener = null;
      this.tree = null;
      this.item = null;
      this.column = 0;
      super.dispose();
   }

   public int getColumn() {
      return this.column;
   }

   public TreeItem getItem() {
      return this.item;
   }

   public void setColumn(int var1) {
      int var2 = this.tree.getColumnCount();
      if (var2 == 0) {
         this.column = var1 == 0 ? 0 : -1;
         this.resize();
      } else {
         if (this.column > -1 && this.column < var2) {
            TreeColumn var3 = this.tree.getColumn(this.column);
            var3.removeControlListener(this.columnListener);
            this.column = -1;
         }

         if (var1 >= 0 && var1 < this.tree.getColumnCount()) {
            this.column = var1;
            TreeColumn var4 = this.tree.getColumn(this.column);
            var4.addControlListener(this.columnListener);
            this.resize();
         }
      }
   }

   public void setItem(TreeItem var1) {
      this.item = var1;
      if (this.editor != null && !this.editor.isDisposed()) {
         boolean var2 = this.editor.getVisible();
         boolean var3 = var1 != null;
         if (var2 != var3) {
            this.editor.setVisible(var3);
         }
      }

      this.resize();
   }

   public void setEditor(Control var1, TreeItem var2, int var3) {
      this.setItem(var2);
      this.setColumn(var3);
      this.setEditor(var1);
   }

   public void setEditor(Control var1, TreeItem var2) {
      this.setItem(var2);
      this.setEditor(var1);
   }

   void resize() {
      if (!this.tree.isDisposed()) {
         if (this.editor != null && !this.editor.isDisposed()) {
            if (this.item != null && !this.item.isDisposed()) {
               int var1 = this.tree.getColumnCount();
               if (var1 != 0 || this.column == 0) {
                  if (var1 <= 0 || this.column >= 0 && this.column < var1) {
                     Rectangle var2 = this.computeBounds();
                     if (!var2.equals(this.currentBounds)) {
                        this.currentBounds = var2;
                        this.editor.setBackground(this.item.getBackground(this.column));
                        this.editor.setBounds(this.currentBounds);
                     }
                  }
               }
            }
         }
      }
   }
}
