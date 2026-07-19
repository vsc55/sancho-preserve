package sancho.view.utility;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import sancho.core.Sancho;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.UniformResourceLocator;
import sancho.view.viewer.actions.CTabFolderTabsAction;

public class WidgetFactory {
   public static CTabFolder createCTabFolder(Composite composite) {
      return createCTabFolder(composite, 0);
   }

   public static CTabFolder createCTabFolder(Composite composite, int style) {
      CTabFolder cTabFolder = new CTabFolder(composite, style | 8388608);
      Display display = cTabFolder.getDisplay();
      Color selectionColor = display.getSystemColor(31);
      if (PreferenceLoader.loadBoolean("useGradient")) {
         cTabFolder.setSelectionBackground(selectionColor);
         cTabFolder.setSelectionForeground(display.getSystemColor(30));
      }

      return cTabFolder;
   }

   public static Color changeColor(RGB rgb, int amount) {
      int red = modifyIntColor(rgb.red, amount);
      int green = modifyIntColor(rgb.green, amount);
      int blue = modifyIntColor(rgb.blue, amount);
      return new Color(null, red, green, blue);
   }

   public static Color changeColor(RGB rgb, int amount, int fallback) {
      int red = modifyIntColor(rgb.red, amount, fallback);
      int green = modifyIntColor(rgb.green, amount, fallback);
      int blue = modifyIntColor(rgb.blue, amount, fallback);
      return new Color(null, red, green, blue);
   }

   public static int modifyIntColor(int value, int amount) {
      return modifyIntColor(value, amount, value);
   }

   public static int modifyIntColor(int value, int amount, int fallback) {
      return value + amount >= 0 && value + amount <= 255 ? value + amount : fallback;
   }

   public static MyViewForm createViewForm(Composite composite, boolean forceBorder) {
      return new MyViewForm(composite, 2048 | (!PreferenceLoader.loadBoolean("flatInterface") && !forceBorder ? 0 : 8388608));
   }

   public static GridLayout createGridLayout(int numColumns, int marginWidth, int marginHeight, int horizontalSpacing, int verticalSpacing, boolean makeColumnsEqualWidth) {
      GridLayout gridLayout = new GridLayout();
      gridLayout.numColumns = numColumns;
      gridLayout.marginWidth = marginWidth;
      gridLayout.marginHeight = marginHeight;
      gridLayout.horizontalSpacing = horizontalSpacing;
      gridLayout.verticalSpacing = verticalSpacing;
      gridLayout.makeColumnsEqualWidth = makeColumnsEqualWidth;
      return gridLayout;
   }

   public static GridData createGridData(int style, int widthHint, int heightHint) {
      GridData gridData = style == 0 ? new GridData() : new GridData(style);
      gridData.widthHint = widthHint;
      gridData.heightHint = heightHint;
      return gridData;
   }

   public static RowLayout createRowLayout(boolean wrap, boolean pack, boolean justify, int type, int marginLeft, int marginTop, int marginRight, int marginBottom, int spacing) {
      RowLayout rowLayout = new RowLayout();
      rowLayout.wrap = wrap;
      rowLayout.pack = pack;
      rowLayout.justify = justify;
      rowLayout.type = type;
      rowLayout.marginLeft = marginLeft;
      rowLayout.marginTop = marginTop;
      rowLayout.marginRight = marginRight;
      rowLayout.marginBottom = marginBottom;
      rowLayout.spacing = spacing;
      return rowLayout;
   }

   public static CLabel createCLabel(Composite composite, String textKey, String imageKey) {
      CLabel label = new CLabel(composite, 16384);
      label.setFont(PreferenceLoader.loadFont("headerFontData"));
      label.setText(SResources.getString(textKey));
      label.setLayoutData(new GridData(768));
      label.setImage(SResources.getImage(imageKey));
      if (PreferenceLoader.loadBoolean("useGradient")) {
         label.setForeground(composite.getDisplay().getSystemColor(30));
         Color gradientColor = PreferenceLoader.loadColor("viewGradientColor");
         label.setBackground(new Color[]{gradientColor, composite.getBackground()}, new int[]{100});
      }

      return label;
   }

   public static SashForm createSashForm(Composite composite, String prefString) {
      String orientationPrefString = prefString + "Orientation";
      int orientation = PreferenceLoader.loadInt(orientationPrefString);
      if (orientation != 256 && orientation != 512) {
         PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
         orientation = preferenceStore.getDefaultInt(orientationPrefString);
      }

      SashForm sashForm = new SashForm(composite, orientation);
      sashForm.setData("prefString", prefString);
      sashForm.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent event) {
            PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
            preferenceStore.setValue(orientationPrefString, sashForm.getOrientation());
         }
      });
      return sashForm;
   }

   public static void loadSashForm(SashForm sashForm, String prefString) {
      String sashChildPrefString = prefString + "Child";
      int maximized = PreferenceLoader.loadIntOrN1(prefString + "Maximized");
      if (sashPrefsExist(sashForm, prefString)) {
         int[] weights = new int[sashForm.getChildren().length];

         for (int i = 0; i < sashForm.getChildren().length; i++) {
            Rectangle rect = PreferenceLoader.loadRectangle(sashChildPrefString + i);
            weights[i] = sashForm.getOrientation() == 256 ? rect.width : rect.height;
         }

         for (int j = 0; j < weights.length; j++) {
            if (weights[j] > 0) {
               sashForm.setWeights(weights);
               break;
            }
         }
      }

      if (maximized > -1 && maximized <= sashForm.getChildren().length) {
         sashForm.setMaximizedControl(sashForm.getChildren()[maximized]);
      }

      PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();

      for (int i = 0; i < sashForm.getChildren().length; i++) {
         Control control = sashForm.getChildren()[i];
         final int childNumber = i;
         control.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
               Control control = (Control)event.widget;
               if (control.getBounds().width > 10 && control.getBounds().height > 10) {
                  PreferenceConverter.setValue(preferenceStore, sashChildPrefString + childNumber, control.getBounds());
               }
            }
         });
      }
   }

   public static boolean sashPrefsExist(SashForm sashForm, String prefString) {
      PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();

      for (int i = 0; i < sashForm.getChildren().length; i++) {
         if (!preferenceStore.contains(prefString + "Child" + i)) {
            return false;
         }
      }

      return true;
   }

   public static boolean setMaximizedSashFormControl(SashForm sashForm, int index) {
      return index > sashForm.getChildren().length ? false : toggleMaximizedSashFormControl(sashForm, sashForm.getChildren()[index]);
   }

   public static boolean toggleMaximizedSashFormControl(SashForm sashForm, Control control) {
      PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
      String prefString = null;
      if (sashForm.getData("prefString") != null) {
         prefString = (String)sashForm.getData("prefString") + "Maximized";
      }

      if (sashForm.getMaximizedControl() != null) {
         sashForm.setMaximizedControl(null);
         if (prefString != null) {
            preferenceStore.setValue(prefString, -1);
         }

         return false;
      } else {
         sashForm.setMaximizedControl(control);
         if (prefString != null) {
            for (int i = 0; i < sashForm.getChildren().length; i++) {
               if (control == sashForm.getChildren()[i]) {
                  preferenceStore.setValue(prefString, i);
                  break;
               }
            }
         }

         return true;
      }
   }

   public static void createLinkDropTarget(Control control) {
      DropTarget dropTarget = new DropTarget(control, 21);
      UniformResourceLocator uRL = UniformResourceLocator.getInstance();
      TextTransfer textTransfer = TextTransfer.getInstance();
      dropTarget.setTransfer(new Transfer[]{uRL, textTransfer});
      dropTarget.addDropListener(new DropTargetAdapter() {
         public void dragEnter(DropTargetEvent event) {
            // Request DROP_LINK only if the source offers it, else COPY, else NONE — forcing
            // detail=4 made SWT reject a COPY-only drag so the drop was never delivered.
            boolean supported = false;

            for (int i = 0; i < event.dataTypes.length; i++) {
               if (uRL.isSupportedType(event.dataTypes[i])) {
                  supported = true;
                  break;
               }
            }

            if (supported && (event.operations & 4) != 0) {
               event.detail = 4;
            } else if ((event.operations & 1) != 0) {
               event.detail = 1;
            } else {
               event.detail = 0;
            }
         }

         public void drop(DropTargetEvent event) {
            if (event.data != null) {
               SwissArmy.sendLink(Sancho.getCore(), (String)event.data);
            }
         }
      });
   }

   public static void addCTabFolderMenu(CTabFolder cTabFolder, String prefString) {
      MenuManager menuManager = new MenuManager();
      menuManager.setRemoveAllWhenShown(true);
      menuManager.addMenuListener(new CTabFolderMenuListener(cTabFolder, prefString));
      cTabFolder.setMenu(menuManager.createContextMenu(cTabFolder));
   }

   // Menu listener that populates a CTabFolder's context menu with the tabs-position action.
   private static class CTabFolderMenuListener implements IMenuListener {
      CTabFolder cTabFolder;
      String prefString;

      public CTabFolderMenuListener(CTabFolder cTabFolder, String prefString) {
         this.cTabFolder = cTabFolder;
         this.prefString = prefString;
      }

      public void menuAboutToShow(IMenuManager menuManager) {
         menuManager.add(new CTabFolderTabsAction(this.cTabFolder, this.prefString));
      }
   }
}
