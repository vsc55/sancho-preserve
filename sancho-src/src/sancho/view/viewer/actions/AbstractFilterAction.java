package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.view.viewer.GView;
import sancho.view.viewer.filters.AbstractViewerFilter;

public abstract class AbstractFilterAction extends Action {
   protected GView gView;
   protected AbstractEnum enumObject;
   protected Class filterClass;
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$StateViewerFilter;

   public AbstractFilterAction(String text, int style, GView gView, AbstractEnum enumObject) {
      super(text, style);
      this.gView = gView;
      this.enumObject = enumObject;
   }

   public boolean isChecked() {
      AbstractViewerFilter filter = this.gView.getFilter(this.filterClass);
      return filter == null ? false : filter.isFiltered(this.enumObject);
   }

   protected boolean doRemove() {
      return this.isChecked();
   }

   public void run() {
      AbstractViewerFilter filter = this.gView.getFilter(this.filterClass);
      if (this.doRemove()) {
         if (filter == null) {
            return;
         }

         if (filter.isFiltered(this.enumObject)) {
            this.removeFilter(filter, this.enumObject);
         }
      } else if (filter == null) {
         this.addFilter(this.createNewFilter(), this.enumObject);
      } else {
         this.addFilter(filter, this.enumObject);
      }
   }

   public abstract AbstractViewerFilter createNewFilter();

   protected void removeFilter(AbstractViewerFilter filter, AbstractEnum enumObject) {
      filter.remove(enumObject);
      if (filter.count() == 0) {
         this.gView.removeFilter(filter);
      } else {
         this.gView.refresh();
      }
   }

   protected void addFilter(AbstractViewerFilter filter, AbstractEnum enumObject) {
      filter.add(enumObject);
      if (filter.count() == 1) {
         this.gView.addFilter(filter);
      } else {
         this.gView.refresh();
      }
   }

   public static boolean isStateFilteredExcept(GView gView, EnumHostState state) {
      AbstractViewerFilter filter = gView.getFilter(
         class$sancho$view$viewer$filters$StateViewerFilter == null
            ? (class$sancho$view$viewer$filters$StateViewerFilter = class$("sancho.view.viewer.filters.StateViewerFilter"))
            : class$sancho$view$viewer$filters$StateViewerFilter
      );
      return filter == null ? false : !filter.isFiltered(state);
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }
}
