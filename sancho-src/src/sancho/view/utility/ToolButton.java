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

   public ToolButton(ToolBar var1, int var2) {
      this(var1, var2, -1);
   }

   public ToolButton(ToolBar var1, int var2, int var3) {
      if (var3 < 0) {
         this.toolItem = new ToolItem(var1, this.toolItemStyle);
      } else {
         this.toolItem = new ToolItem(var1, var2, this.toolItemStyle);
      }
   }

   public void addSelectionListener(SelectionListener var1) {
      this.listener = var1;
      this.toolItem.addSelectionListener(var1);
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

   public void resetItem(ToolBar var1) {
      this.toolItem.dispose();
      this.toolItem = new ToolItem(var1, this.toolItemStyle);
      this.setText(this.text);
      this.setToolTipText(this.toolTipText);
      this.setActive(this.active);
      this.addSelectionListener(this.listener);
      this.resetImage();
   }

   public void setActive(boolean var1) {
      this.toolItem.setSelection(var1);
      this.active = var1;
   }

   public void setBigActiveImage(Image var1) {
      this.bigActiveImage = var1;
   }

   public void setBigInactiveImage(Image var1) {
      this.bigInactiveImage = var1;
   }

   public void setHotImage(Image var1) {
      this.toolItem.setHotImage(var1);
   }

   public void setImage(Image var1) {
      this.toolItem.setImage(var1);
   }

   public void setSmallActiveImage(Image var1) {
      this.smallActiveImage = var1;
   }

   public void setSmallInactiveImage(Image var1) {
      this.smallInactiveImage = var1;
   }

   public void setText(String var1) {
      this.text = var1;
      this.toolItem.setText(var1);
   }

   public void setToolTipText(String var1) {
      this.toolTipText = var1;
      this.toolItem.setToolTipText(var1);
   }

   public void useSmallButtons(boolean var1) {
      this.useSmallButtons = var1;
   }
}
