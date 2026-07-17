package sancho.view.utility;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;

class MyImageDescriptor extends ImageDescriptor {
   private String name;

   MyImageDescriptor(String var1) {
      this.name = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof MyImageDescriptor)) {
         return false;
      } else {
         MyImageDescriptor var2 = (MyImageDescriptor)var1;
         return this.name.equals(var2.name);
      }
   }

   public ImageData getImageData() {
      InputStream var1 = this.getStream();
      ImageData var2 = null;
      if (var1 != null) {
         try {
            var2 = new ImageData(var1);
         } catch (SWTException var13) {
            if (var13.code != 40) {
               throw var13;
            }
         } finally {
            try {
               var1.close();
            } catch (IOException var12) {
            }
         }
      }

      return var2;
   }

   private InputStream getStream() {
      Object var1 = null;
      var1 = SResources.imagesClass.getResourceAsStream("img/" + this.name);
      return var1 == null ? null : new BufferedInputStream((InputStream)var1);
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public String toString() {
      return "MyImageDescriptor(name=" + this.name + ")";
   }
}
