package sancho.view.utility;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;

class MyImageDescriptor extends ImageDescriptor {
   private String name;

   MyImageDescriptor(String name) {
      this.name = name;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof MyImageDescriptor)) {
         return false;
      } else {
         MyImageDescriptor other = (MyImageDescriptor)obj;
         return this.name.equals(other.name);
      }
   }

   public ImageData getImageData() {
      InputStream stream = this.getStream();
      ImageData imageData = null;
      if (stream != null) {
         try {
            imageData = new ImageData(stream);
         } catch (SWTException swtException) {
            if (swtException.code != 40) {
               throw swtException;
            }
         } finally {
            try {
               stream.close();
            } catch (IOException ioException) {
            }
         }
      }

      return imageData;
   }

   private InputStream getStream() {
      Object stream = null;
      stream = SResources.imagesClass.getResourceAsStream("img/" + this.name);
      return stream == null ? null : new BufferedInputStream((InputStream)stream);
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public String toString() {
      return "MyImageDescriptor(name=" + this.name + ")";
   }
}
