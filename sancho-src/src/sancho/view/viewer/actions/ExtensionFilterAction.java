package sancho.view.viewer.actions;

import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.view.viewer.GView;
import sancho.view.viewer.filters.AbstractViewerFilter;
import sancho.view.viewer.filters.FileExtensionViewerFilter;

public class ExtensionFilterAction extends AbstractFilterAction {
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$FileExtensionViewerFilter;

   public ExtensionFilterAction(String name, GView gView, AbstractEnum abstractEnum) {
      super(name, 2, gView, abstractEnum);
      this.filterClass = class$sancho$view$viewer$filters$FileExtensionViewerFilter == null
         ? (class$sancho$view$viewer$filters$FileExtensionViewerFilter = class$("sancho.view.viewer.filters.FileExtensionViewerFilter"))
         : class$sancho$view$viewer$filters$FileExtensionViewerFilter;
   }

   public AbstractViewerFilter createNewFilter() {
      return new FileExtensionViewerFilter(this.gView);
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
