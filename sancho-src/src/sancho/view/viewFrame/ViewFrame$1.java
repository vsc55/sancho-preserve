package sancho.view.viewFrame;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import sancho.view.utility.SResources;

class ViewFrame$1 implements MouseTrackListener {
   private Image newImage;
   // $VF: synthetic field
   private final String val$prefImageString;
   // $VF: synthetic field
   private final ViewFrame this$0;

   ViewFrame$1(ViewFrame var1, String var2) {
      this.this$0 = var1;
      this.val$prefImageString = var2;
   }

   public void mouseHover(MouseEvent var1) {
   }

   public void disposeImage() {
      if (this.newImage != null) {
         this.newImage.dispose();
         this.newImage = null;
      }
   }

   public void mouseEnter(MouseEvent var1) {
      this.disposeImage();
      this.newImage = SResources.createActiveImage(SResources.getImageDescriptor(this.val$prefImageString));
      this.this$0.cLabel.setImage(this.newImage);
   }

   public void mouseExit(MouseEvent var1) {
      this.this$0.cLabel.setImage(SResources.getImage(this.val$prefImageString));
      this.disposeImage();
   }
}
