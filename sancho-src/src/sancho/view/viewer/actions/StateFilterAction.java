package sancho.view.viewer.actions;

import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.view.viewer.GView;
import sancho.view.viewer.filters.AbstractViewerFilter;
import sancho.view.viewer.filters.StateViewerFilter;

public class StateFilterAction extends AbstractFilterAction {
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$StateViewerFilter;

   public StateFilterAction(String name, GView gView, AbstractEnum abstractEnum) {
      super(name, 2, gView, abstractEnum);
      this.filterClass = class$sancho$view$viewer$filters$StateViewerFilter == null
         ? (class$sancho$view$viewer$filters$StateViewerFilter = class$("sancho.view.viewer.filters.StateViewerFilter"))
         : class$sancho$view$viewer$filters$StateViewerFilter;
   }

   public AbstractViewerFilter createNewFilter() {
      return new StateViewerFilter(this.gView);
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException classNotFound) {
         throw new NoClassDefFoundError(classNotFound.getMessage());
      }
   }
}
