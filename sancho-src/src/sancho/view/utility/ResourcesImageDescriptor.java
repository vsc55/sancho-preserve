package sancho.view.utility;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class ResourcesImageDescriptor extends ImageDescriptor {
   String imageKey;

   public ResourcesImageDescriptor(String var1, Image var2) {
      this.imageKey = var1 + "_IMG";
      SResources.putImage(this.imageKey, var2);
   }

   public Image createImage() {
      return this.createImage(true);
   }

   public Image createImage(boolean var1) {
      return this.createImage(var1, Display.getCurrent());
   }

   public Image createImage(boolean var1, Device var2) {
      return SResources.getImage(this.imageKey);
   }

   public Image createImage(Device var1) {
      return this.createImage(true, var1);
   }

   public ImageData getImageData() {
      return SResources.getImage(this.imageKey).getImageData();
   }
}
