package sancho.view.viewer.table;

import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewer.GView;
import sancho.view.viewer.IGContentProvider;
import sancho.view.viewer.tree.GTreeContentProvider;

public abstract class GTableLabelProvider implements ITableLabelProvider, ITableColorProvider {
   protected ICustomViewer cViewer;
   protected GView gView;
   protected boolean alternateColors;
   protected Color alternateColor;

   public GTableLabelProvider(GView var1) {
      this.gView = var1;
      this.updateDisplay();
   }

   public Color getForeground(Object var1, int var2) {
      return null;
   }

   public Color getBackground(Object var1, int var2) {
      if (this.alternateColors) {
         IGContentProvider var3 = (IGContentProvider)this.gView.getViewer().getContentProvider();
         if (var3 instanceof GTableContentProvider) {
            if (((GTableContentProvider)var3).getSFIndex(var1) % 2 != 0) {
               return this.alternateColor;
            }
         } else if (var3 instanceof GTreeContentProvider && ((GTreeContentProvider)var3).getSFIndex(var1) % 2 != 0) {
            return this.alternateColor;
         }
      }

      return null;
   }

   public void addListener(ILabelProviderListener var1) {
   }

   public void dispose() {
   }

   public Image getColumnImage(Object var1, int var2) {
      return null;
   }

   public abstract String getColumnText(Object var1, int var2);

   public void initialize() {
      this.cViewer = (ICustomViewer)this.gView.getViewer();
   }

   public boolean isLabelProperty(Object var1, String var2) {
      return true;
   }

   public void removeListener(ILabelProviderListener var1) {
   }

   public void updateDisplay() {
      this.alternateColor = PreferenceLoader.loadColor("tableAlternateBGColor");
      this.alternateColors = PreferenceLoader.loadBoolean("tableAlternateBGColors");
   }
}
