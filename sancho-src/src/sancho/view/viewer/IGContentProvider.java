package sancho.view.viewer;

import org.eclipse.jface.viewers.IContentProvider;

public interface IGContentProvider extends IContentProvider {
   void initialize();

   void setActive(boolean active);

   void setVisible(boolean visible);

   void setNeedsRefresh(boolean needsRefresh);
}
