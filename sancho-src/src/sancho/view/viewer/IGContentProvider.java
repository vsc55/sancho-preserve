package sancho.view.viewer;

import org.eclipse.jface.viewers.IContentProvider;

public interface IGContentProvider extends IContentProvider {
   void initialize();

   void setActive(boolean var1);

   void setVisible(boolean var1);

   void setNeedsRefresh(boolean var1);
}
