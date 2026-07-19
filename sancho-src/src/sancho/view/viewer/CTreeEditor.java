package sancho.view.viewer;

import org.eclipse.swt.events.ControlEvent;
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

   public CTreeEditor(Tree tree) {
      super(tree);
      this.tree = tree;
      this.columnListener = new ControlListener() {
         public void controlMoved(ControlEvent event) {
            CTreeEditor.this.resize();
         }

         public void controlResized(ControlEvent event) {
            CTreeEditor.this.resize();
         }
      };
      this.grabVertical = true;
   }

   Rectangle computeBounds() {
      return this.item != null && this.column >= 0 && !this.item.isDisposed() ? this.item.getBounds(this.column) : new Rectangle(0, 0, 0, 0);
   }

   public void dispose() {
      if (this.column > -1 && this.column < this.tree.getColumnCount()) {
         TreeColumn column = this.tree.getColumn(this.column);
         column.removeControlListener(this.columnListener);
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

   public void setColumn(int columnIndex) {
      int columnCount = this.tree.getColumnCount();
      if (columnCount == 0) {
         this.column = columnIndex == 0 ? 0 : -1;
         this.resize();
      } else {
         if (this.column > -1 && this.column < columnCount) {
            TreeColumn oldColumn = this.tree.getColumn(this.column);
            oldColumn.removeControlListener(this.columnListener);
            this.column = -1;
         }

         if (columnIndex >= 0 && columnIndex < this.tree.getColumnCount()) {
            this.column = columnIndex;
            TreeColumn column = this.tree.getColumn(this.column);
            column.addControlListener(this.columnListener);
            this.resize();
         }
      }
   }

   public void setItem(TreeItem item) {
      this.item = item;
      if (this.editor != null && !this.editor.isDisposed()) {
         boolean editorVisible = this.editor.getVisible();
         boolean itemPresent = item != null;
         if (editorVisible != itemPresent) {
            this.editor.setVisible(itemPresent);
         }
      }

      this.resize();
   }

   public void setEditor(Control control, TreeItem item, int columnIndex) {
      this.setItem(item);
      this.setColumn(columnIndex);
      this.setEditor(control);
   }

   public void setEditor(Control control, TreeItem item) {
      this.setItem(item);
      this.setEditor(control);
   }

   void resize() {
      if (!this.tree.isDisposed()) {
         if (this.editor != null && !this.editor.isDisposed()) {
            if (this.item != null && !this.item.isDisposed()) {
               int columnCount = this.tree.getColumnCount();
               if (columnCount != 0 || this.column == 0) {
                  if (columnCount <= 0 || this.column >= 0 && this.column < columnCount) {
                     Rectangle bounds = this.computeBounds();
                     if (!bounds.equals(this.currentBounds)) {
                        this.currentBounds = bounds;
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
