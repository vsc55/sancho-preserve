package sancho.view.viewer.actions;

import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.view.viewer.GView;
import sancho.view.viewer.filters.AbstractViewerFilter;
import sancho.view.viewer.filters.ExclusionStateViewerFilter;

public class ExclusionStateFilterAction extends AbstractFilterAction {
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$ExclusionStateViewerFilter;

   public ExclusionStateFilterAction(String name, GView gView, AbstractEnum abstractEnum) {
      super(name, 2, gView, abstractEnum);
      this.filterClass = class$sancho$view$viewer$filters$ExclusionStateViewerFilter == null
         ? (class$sancho$view$viewer$filters$ExclusionStateViewerFilter = class$("sancho.view.viewer.filters.ExclusionStateViewerFilter"))
         : class$sancho$view$viewer$filters$ExclusionStateViewerFilter;
   }

   public boolean isChecked() {
      return !super.isChecked();
   }

   protected boolean doRemove() {
      return super.isChecked();
   }

   public AbstractViewerFilter createNewFilter() {
      return new ExclusionStateViewerFilter(this.gView);
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException classNotFoundException) {
         throw new NoClassDefFoundError(classNotFoundException.getMessage());
      }
   }
}
