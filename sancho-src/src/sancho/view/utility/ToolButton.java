package sancho.view.utility;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolButton {
   private boolean active;
   private Image bigActiveImage;
   private Image bigInactiveImage;
   private SelectionListener listener;
   private Image smallActiveImage;
   private Image smallInactiveImage;
   private String text;
   private ToolItem toolItem;
   private int toolItemStyle = 16;
   private String toolTipText;
   private boolean useSmallButtons;

   public ToolButton(ToolBar toolBar, int style) {
      this(toolBar, style, -1);
   }

   public ToolButton(ToolBar toolBar, int style, int index) {
      if (index < 0) {
         this.toolItem = new ToolItem(toolBar, this.toolItemStyle);
      } else {
         this.toolItem = new ToolItem(toolBar, style, this.toolItemStyle);
      }
   }

   public void addSelectionListener(SelectionListener listener) {
      this.listener = listener;
      this.toolItem.addSelectionListener(listener);
   }

   public void dispose() {
      this.toolItem.dispose();
   }

   public ToolBar getParent() {
      return this.toolItem.getParent();
   }

   public Image getSmallActiveImage() {
      return this.smallActiveImage;
   }

   public Image getSmallInActiveImage() {
      return this.smallInactiveImage;
   }

   public String getText() {
      return this.text;
   }

   public ToolItem getToolItem() {
      return this.toolItem;
   }

   public void resetImage() {
      this.setHotImage(this.useSmallButtons ? this.smallActiveImage : this.bigActiveImage);
      this.setImage(this.useSmallButtons ? this.smallInactiveImage : this.bigInactiveImage);
   }

   public void resetItem(ToolBar toolBar) {
      this.toolItem.dispose();
      this.toolItem = new ToolItem(toolBar, this.toolItemStyle);
      this.setText(this.text);
      this.setToolTipText(this.toolTipText);
      this.setActive(this.active);
      this.addSelectionListener(this.listener);
      this.resetImage();
   }

   public void setActive(boolean active) {
      this.toolItem.setSelection(active);
      this.active = active;
   }

   public void setBigActiveImage(Image image) {
      this.bigActiveImage = image;
   }

   public void setBigInactiveImage(Image image) {
      this.bigInactiveImage = image;
   }

   public void setHotImage(Image image) {
      this.toolItem.setHotImage(image);
   }

   public void setImage(Image image) {
      this.toolItem.setImage(image);
   }

   public void setSmallActiveImage(Image image) {
      this.smallActiveImage = image;
   }

   public void setSmallInactiveImage(Image image) {
      this.smallInactiveImage = image;
   }

   public void setText(String text) {
      this.text = text;
      this.toolItem.setText(text);
   }

   public void setToolTipText(String text) {
      this.toolTipText = text;
      this.toolItem.setToolTipText(text);
   }

   public void useSmallButtons(boolean useSmallButtons) {
      this.useSmallButtons = useSmallButtons;
   }
}
