package sancho.view.utility;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class ResourcesImageDescriptor extends ImageDescriptor {
   String imageKey;

   public ResourcesImageDescriptor(String name, Image image) {
      this.imageKey = name + "_IMG";
      SResources.putImage(this.imageKey, image);
   }

   public Image createImage() {
      return this.createImage(true);
   }

   public Image createImage(boolean returnMissingImageOnError) {
      return this.createImage(returnMissingImageOnError, Display.getCurrent());
   }

   public Image createImage(boolean returnMissingImageOnError, Device device) {
      return SResources.getImage(this.imageKey);
   }

   public Image createImage(Device device) {
      return this.createImage(true, device);
   }

   public ImageData getImageData() {
      return SResources.getImage(this.imageKey).getImageData();
   }
}
