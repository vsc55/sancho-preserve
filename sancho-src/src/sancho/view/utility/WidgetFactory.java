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
   public static CTabFolder createCTabFolder(Composite var0) {
      return createCTabFolder(var0, 0);
   }

   public static CTabFolder createCTabFolder(Composite var0, int var1) {
      CTabFolder var2 = new CTabFolder(var0, var1 | 8388608);
      Display var3 = var2.getDisplay();
      Color var4 = var3.getSystemColor(31);
      if (PreferenceLoader.loadBoolean("useGradient")) {
         var2.setSelectionBackground(var4);
         var2.setSelectionForeground(var3.getSystemColor(30));
      }

      return var2;
   }

   public static Color changeColor(RGB var0, int var1) {
      int var2 = modifyIntColor(var0.red, var1);
      int var3 = modifyIntColor(var0.green, var1);
      int var4 = modifyIntColor(var0.blue, var1);
      return new Color(null, var2, var3, var4);
   }

   public static Color changeColor(RGB var0, int var1, int var2) {
      int var3 = modifyIntColor(var0.red, var1, var2);
      int var4 = modifyIntColor(var0.green, var1, var2);
      int var5 = modifyIntColor(var0.blue, var1, var2);
      return new Color(null, var3, var4, var5);
   }

   public static int modifyIntColor(int var0, int var1) {
      return modifyIntColor(var0, var1, var0);
   }

   public static int modifyIntColor(int var0, int var1, int var2) {
      return var0 + var1 >= 0 && var0 + var1 <= 255 ? var0 + var1 : var2;
   }

   public static MyViewForm createViewForm(Composite var0, boolean var1) {
      return new MyViewForm(var0, 2048 | (!PreferenceLoader.loadBoolean("flatInterface") && !var1 ? 0 : 8388608));
   }

   public static GridLayout createGridLayout(int var0, int var1, int var2, int var3, int var4, boolean var5) {
      GridLayout var6 = new GridLayout();
      var6.numColumns = var0;
      var6.marginWidth = var1;
      var6.marginHeight = var2;
      var6.horizontalSpacing = var3;
      var6.verticalSpacing = var4;
      var6.makeColumnsEqualWidth = var5;
      return var6;
   }

   public static GridData createGridData(int var0, int var1, int var2) {
      GridData var3 = var0 == 0 ? new GridData() : new GridData(var0);
      var3.widthHint = var1;
      var3.heightHint = var2;
      return var3;
   }

   public static RowLayout createRowLayout(boolean var0, boolean var1, boolean var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      RowLayout var9 = new RowLayout();
      var9.wrap = var0;
      var9.pack = var1;
      var9.justify = var2;
      var9.type = var3;
      var9.marginLeft = var4;
      var9.marginTop = var5;
      var9.marginRight = var6;
      var9.marginBottom = var7;
      var9.spacing = var8;
      return var9;
   }

   public static CLabel createCLabel(Composite var0, String var1, String var2) {
      CLabel var3 = new CLabel(var0, 16384);
      var3.setFont(PreferenceLoader.loadFont("headerFontData"));
      var3.setText(SResources.getString(var1));
      var3.setLayoutData(new GridData(768));
      var3.setImage(SResources.getImage(var2));
      if (PreferenceLoader.loadBoolean("useGradient")) {
         var3.setForeground(var0.getDisplay().getSystemColor(30));
         Color var4 = PreferenceLoader.loadColor("viewGradientColor");
         var3.setBackground(new Color[]{var4, var0.getBackground()}, new int[]{100});
      }

      return var3;
   }

   public static SashForm createSashForm(Composite var0, String var1) {
      String orientationPrefString = var1 + "Orientation";
      int var3 = PreferenceLoader.loadInt(orientationPrefString);
      if (var3 != 256 && var3 != 512) {
         PreferenceStore var4 = PreferenceLoader.getPreferenceStore();
         var3 = var4.getDefaultInt(orientationPrefString);
      }

      SashForm var5 = new SashForm(var0, var3);
      var5.setData("prefString", var1);
      var5.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent var1) {
            PreferenceStore var2 = PreferenceLoader.getPreferenceStore();
            var2.setValue(orientationPrefString, var5.getOrientation());
         }
      });
      return var5;
   }

   public static void loadSashForm(SashForm var0, String var1) {
      String sashChildPrefString = var1 + "Child";
      int var3 = PreferenceLoader.loadIntOrN1(var1 + "Maximized");
      if (sashPrefsExist(var0, var1)) {
         int[] var4 = new int[var0.getChildren().length];

         for (int var5 = 0; var5 < var0.getChildren().length; var5++) {
            Rectangle var6 = PreferenceLoader.loadRectangle(sashChildPrefString + var5);
            var4[var5] = var0.getOrientation() == 256 ? var6.width : var6.height;
         }

         for (int var10 = 0; var10 < var4.length; var10++) {
            if (var4[var10] > 0) {
               var0.setWeights(var4);
               break;
            }
         }
      }

      if (var3 > -1 && var3 <= var0.getChildren().length) {
         var0.setMaximizedControl(var0.getChildren()[var3]);
      }

      PreferenceStore var8 = PreferenceLoader.getPreferenceStore();

      for (int var9 = 0; var9 < var0.getChildren().length; var9++) {
         Control var11 = var0.getChildren()[var9];
         final int childNumber = var9;
         var11.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent var1) {
               Control var2 = (Control)var1.widget;
               if (var2.getBounds().width > 10 && var2.getBounds().height > 10) {
                  PreferenceConverter.setValue(var8, sashChildPrefString + childNumber, var2.getBounds());
               }
            }
         });
      }
   }

   public static boolean sashPrefsExist(SashForm var0, String var1) {
      PreferenceStore var2 = PreferenceLoader.getPreferenceStore();

      for (int var3 = 0; var3 < var0.getChildren().length; var3++) {
         if (!var2.contains(var1 + "Child" + var3)) {
            return false;
         }
      }

      return true;
   }

   public static boolean setMaximizedSashFormControl(SashForm var0, int var1) {
      return var1 > var0.getChildren().length ? false : toggleMaximizedSashFormControl(var0, var0.getChildren()[var1]);
   }

   public static boolean toggleMaximizedSashFormControl(SashForm var0, Control var1) {
      PreferenceStore var2 = PreferenceLoader.getPreferenceStore();
      String var3 = null;
      if (var0.getData("prefString") != null) {
         var3 = (String)var0.getData("prefString") + "Maximized";
      }

      if (var0.getMaximizedControl() != null) {
         var0.setMaximizedControl(null);
         if (var3 != null) {
            var2.setValue(var3, -1);
         }

         return false;
      } else {
         var0.setMaximizedControl(var1);
         if (var3 != null) {
            for (int var4 = 0; var4 < var0.getChildren().length; var4++) {
               if (var1 == var0.getChildren()[var4]) {
                  var2.setValue(var3, var4);
                  break;
               }
            }
         }

         return true;
      }
   }

   public static void createLinkDropTarget(Control var0) {
      DropTarget var1 = new DropTarget(var0, 21);
      UniformResourceLocator uRL = UniformResourceLocator.getInstance();
      TextTransfer var3 = TextTransfer.getInstance();
      var1.setTransfer(new Transfer[]{uRL, var3});
      var1.addDropListener(new DropTargetAdapter() {
         public void dragEnter(DropTargetEvent var1) {
            // Request DROP_LINK only if the source offers it, else COPY, else NONE — forcing
            // detail=4 made SWT reject a COPY-only drag so the drop was never delivered.
            boolean var2 = false;

            for (int var3 = 0; var3 < var1.dataTypes.length; var3++) {
               if (uRL.isSupportedType(var1.dataTypes[var3])) {
                  var2 = true;
                  break;
               }
            }

            if (var2 && (var1.operations & 4) != 0) {
               var1.detail = 4;
            } else if ((var1.operations & 1) != 0) {
               var1.detail = 1;
            } else {
               var1.detail = 0;
            }
         }

         public void drop(DropTargetEvent var1) {
            if (var1.data != null) {
               SwissArmy.sendLink(Sancho.getCore(), (String)var1.data);
            }
         }
      });
   }

   public static void addCTabFolderMenu(CTabFolder var0, String var1) {
      MenuManager var2 = new MenuManager();
      var2.setRemoveAllWhenShown(true);
      var2.addMenuListener(new CTabFolderMenuListener(var0, var1));
      var0.setMenu(var2.createContextMenu(var0));
   }

   // Menu listener that populates a CTabFolder's context menu with the tabs-position action.
   private static class CTabFolderMenuListener implements IMenuListener {
      CTabFolder cTabFolder;
      String prefString;

      public CTabFolderMenuListener(CTabFolder var1, String var2) {
         this.cTabFolder = var1;
         this.prefString = var2;
      }

      public void menuAboutToShow(IMenuManager var1) {
         var1.add(new CTabFolderTabsAction(this.cTabFolder, this.prefString));
      }
   }
}
