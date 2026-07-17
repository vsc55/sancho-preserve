package sancho.view.utility;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class MyMenuManager extends MenuManager {
   String imageString;

   public MyMenuManager(String var1) {
      super(var1);
   }

   public void setImageString(String var1) {
      this.imageString = var1;
   }

   // In JFace 3.2 MenuManager exposed its `menuItem`/`menu` fields, so this class
   // recreated fill() to add an icon to the cascade item. Both fields are private
   // now, so we let super.fill() build the item and only reflectively decorate it.
   public void fill(Menu var1, int var2) {
      super.fill(var1, var2);

      if (this.imageString != null) {
         try {
            java.lang.reflect.Field field = MenuManager.class.getDeclaredField("menuItem");
            field.setAccessible(true);
            MenuItem item = (MenuItem)field.get(this);
            if (item != null && !item.isDisposed()) {
               item.setImage(SResources.getImage(this.imageString));
            }
         } catch (Exception var4) {
            // best-effort icon; ignore if the JFace field layout changed
         }
      }
   }
}
