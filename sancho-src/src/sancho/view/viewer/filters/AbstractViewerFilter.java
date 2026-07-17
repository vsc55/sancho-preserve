package sancho.view.viewer.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.utility.SwissArmy;
import sancho.view.viewer.GView;

public abstract class AbstractViewerFilter extends ViewerFilter {
   protected GView gView;
   protected int filtered;
   protected int counter;

   public AbstractViewerFilter(GView var1) {
      this.gView = var1;
   }

   public void setFiltered(int var1) {
      this.filtered = var1;
      this.counter = SwissArmy.countBits(var1);
   }

   public void add(AbstractEnum var1) {
      if (!this.isFiltered(var1)) {
         this.filtered = this.filtered | var1.getValue();
         this.counter++;
      }
   }

   public void remove(AbstractEnum var1) {
      if (this.isFiltered(var1)) {
         this.filtered = this.filtered - var1.getValue();
         this.counter--;
      }
   }

   public int getFiltered() {
      return this.filtered;
   }

   public int count() {
      return this.counter;
   }

   public boolean isFiltered(AbstractEnum var1) {
      return (this.filtered & var1.getValue()) != 0;
   }

   public abstract boolean select(Viewer var1, Object var2, Object var3);

   public String toString() {
      return GView.filterToInt(this) + "," + this.filtered + ",";
   }
}
