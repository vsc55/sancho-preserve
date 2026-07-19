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

   public AbstractViewerFilter(GView gView) {
      this.gView = gView;
   }

   public void setFiltered(int filtered) {
      this.filtered = filtered;
      this.counter = SwissArmy.countBits(filtered);
   }

   public void add(AbstractEnum option) {
      if (!this.isFiltered(option)) {
         this.filtered = this.filtered | option.getValue();
         this.counter++;
      }
   }

   public void remove(AbstractEnum option) {
      if (this.isFiltered(option)) {
         this.filtered = this.filtered - option.getValue();
         this.counter--;
      }
   }

   public int getFiltered() {
      return this.filtered;
   }

   public int count() {
      return this.counter;
   }

   public boolean isFiltered(AbstractEnum option) {
      return (this.filtered & option.getValue()) != 0;
   }

   public abstract boolean select(Viewer viewer, Object parentElement, Object element);

   public String toString() {
      return GView.filterToInt(this) + "," + this.filtered + ",";
   }
}
