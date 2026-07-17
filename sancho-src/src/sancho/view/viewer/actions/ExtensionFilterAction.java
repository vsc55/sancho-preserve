package sancho.view.viewer.actions;

import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.view.viewer.GView;
import sancho.view.viewer.filters.AbstractViewerFilter;
import sancho.view.viewer.filters.FileExtensionViewerFilter;

public class ExtensionFilterAction extends AbstractFilterAction {
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$FileExtensionViewerFilter;

   public ExtensionFilterAction(String var1, GView var2, AbstractEnum var3) {
      super(var1, 2, var2, var3);
      this.filterClass = class$sancho$view$viewer$filters$FileExtensionViewerFilter == null
         ? (class$sancho$view$viewer$filters$FileExtensionViewerFilter = class$("sancho.view.viewer.filters.FileExtensionViewerFilter"))
         : class$sancho$view$viewer$filters$FileExtensionViewerFilter;
   }

   public AbstractViewerFilter createNewFilter() {
      return new FileExtensionViewerFilter(this.gView);
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
