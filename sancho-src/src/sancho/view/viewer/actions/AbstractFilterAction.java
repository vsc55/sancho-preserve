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

   public AbstractFilterAction(String var1, int var2, GView var3, AbstractEnum var4) {
      super(var1, var2);
      this.gView = var3;
      this.enumObject = var4;
   }

   public boolean isChecked() {
      AbstractViewerFilter var1 = this.gView.getFilter(this.filterClass);
      return var1 == null ? false : var1.isFiltered(this.enumObject);
   }

   protected boolean doRemove() {
      return this.isChecked();
   }

   public void run() {
      AbstractViewerFilter var1 = this.gView.getFilter(this.filterClass);
      if (this.doRemove()) {
         if (var1 == null) {
            return;
         }

         if (var1.isFiltered(this.enumObject)) {
            this.removeFilter(var1, this.enumObject);
         }
      } else if (var1 == null) {
         this.addFilter(this.createNewFilter(), this.enumObject);
      } else {
         this.addFilter(var1, this.enumObject);
      }
   }

   public abstract AbstractViewerFilter createNewFilter();

   protected void removeFilter(AbstractViewerFilter var1, AbstractEnum var2) {
      var1.remove(var2);
      if (var1.count() == 0) {
         this.gView.removeFilter(var1);
      } else {
         this.gView.refresh();
      }
   }

   protected void addFilter(AbstractViewerFilter var1, AbstractEnum var2) {
      var1.add(var2);
      if (var1.count() == 1) {
         this.gView.addFilter(var1);
      } else {
         this.gView.refresh();
      }
   }

   public static boolean isStateFilteredExcept(GView var0, EnumHostState var1) {
      AbstractViewerFilter var2 = var0.getFilter(
         class$sancho$view$viewer$filters$StateViewerFilter == null
            ? (class$sancho$view$viewer$filters$StateViewerFilter = class$("sancho.view.viewer.filters.StateViewerFilter"))
            : class$sancho$view$viewer$filters$StateViewerFilter
      );
      return var2 == null ? false : !var2.isFiltered(var1);
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
