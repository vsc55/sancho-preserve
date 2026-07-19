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

   public GTableLabelProvider(GView gView) {
      this.gView = gView;
      this.updateDisplay();
   }

   public Color getForeground(Object element, int columnIndex) {
      return null;
   }

   public Color getBackground(Object element, int columnIndex) {
      if (this.alternateColors) {
         IGContentProvider contentProvider = (IGContentProvider)this.gView.getViewer().getContentProvider();
         if (contentProvider instanceof GTableContentProvider) {
            if (((GTableContentProvider)contentProvider).getSFIndex(element) % 2 != 0) {
               return this.alternateColor;
            }
         } else if (contentProvider instanceof GTreeContentProvider && ((GTreeContentProvider)contentProvider).getSFIndex(element) % 2 != 0) {
            return this.alternateColor;
         }
      }

      return null;
   }

   public void addListener(ILabelProviderListener listener) {
   }

   public void dispose() {
   }

   public Image getColumnImage(Object element, int columnIndex) {
      return null;
   }

   public abstract String getColumnText(Object element, int columnIndex);

   public void initialize() {
      this.cViewer = (ICustomViewer)this.gView.getViewer();
   }

   public boolean isLabelProperty(Object element, String property) {
      return true;
   }

   public void removeListener(ILabelProviderListener listener) {
   }

   public void updateDisplay() {
      this.alternateColor = PreferenceLoader.loadColor("tableAlternateBGColor");
      this.alternateColors = PreferenceLoader.loadBoolean("tableAlternateBGColors");
   }
}
