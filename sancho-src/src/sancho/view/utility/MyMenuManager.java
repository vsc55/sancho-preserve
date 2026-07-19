package sancho.view.utility;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MyMenuManager extends MenuManager {
   String imageString;

   public MyMenuManager(String name) {
      super(name);
   }

   public void setImageString(String imageString) {
      this.imageString = imageString;
   }

   // In JFace 3.2 MenuManager exposed its `menuItem`/`menu` fields, so this class
   // recreated fill() to add an icon to the cascade item. Both fields are private
   // now, so we let super.fill() build the item and only reflectively decorate it.
   public void fill(Menu menu, int index) {
      super.fill(menu, index);

      if (this.imageString != null) {
         try {
            java.lang.reflect.Field field = MenuManager.class.getDeclaredField("menuItem");
            field.setAccessible(true);
            MenuItem item = (MenuItem)field.get(this);
            if (item != null && !item.isDisposed()) {
               item.setImage(SResources.getImage(this.imageString));
            }
         } catch (Exception exception) {
            // best-effort icon; ignore if the JFace field layout changed
         }
      }
   }
}
