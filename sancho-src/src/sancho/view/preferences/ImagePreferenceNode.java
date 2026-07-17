/*
 * Added during the modern-SWT/JFace port of the decompiled 0.9.4-59 sources.
 */
package sancho.view.preferences;

import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * A {@link PreferenceNode} that shows an icon while wrapping a live
 * {@link IPreferencePage} instance.
 *
 * The original code used {@code new PreferenceNode(id, page, imageDescriptor)},
 * a constructor removed from modern JFace. Modern JFace only offers
 * {@code PreferenceNode(id, label, ImageDescriptor, className)} (which creates the
 * page lazily by class name) or {@code PreferenceNode(id, page)} (no icon), so we
 * override {@link #getLabelImage()} to supply the icon for the page-instance case.
 */
public class ImagePreferenceNode extends PreferenceNode {
   private final ImageDescriptor imageDescriptor;
   private Image nodeImage;

   public ImagePreferenceNode(String id, IPreferencePage page, ImageDescriptor image) {
      super(id, page);
      this.imageDescriptor = image;
   }

   public Image getLabelImage() {
      if (this.nodeImage == null && this.imageDescriptor != null) {
         this.nodeImage = this.imageDescriptor.createImage();
      }
      return this.nodeImage;
   }

   public void disposeResources() {
      if (this.nodeImage != null) {
         this.nodeImage.dispose();
         this.nodeImage = null;
      }
      super.disposeResources();
   }
}
