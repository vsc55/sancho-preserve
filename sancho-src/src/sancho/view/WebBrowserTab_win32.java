package sancho.view;

import java.io.File;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.internal.ole.win32.IDispatch;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;
import sancho.core.Sancho;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.LinkRipper;
import sancho.view.utility.SResources;

public class WebBrowserTab_win32 extends WebBrowserTab {
   public boolean ctrlDown;

   static String[] SortDir(String var0, String[] var1) {
      if (var1.length > 1) {
         for (int var3 = 0; var3 < var1.length - 1; var3++) {
            for (int var4 = var3 + 1; var4 < var1.length; var4++) {
               File var5 = new File(var0 + File.separator + var1[var3]);
               File var6 = new File(var0 + File.separator + var1[var4]);
               if (var6.isDirectory() && var5.isFile()) {
                  String var8 = var1[var3];
                  var1[var3] = var1[var4];
                  var1[var4] = var8;
               } else if (var5.isDirectory() && var6.isDirectory() && var5.getName().compareToIgnoreCase(var6.getName()) > 0) {
                  String var7 = var1[var3];
                  var1[var3] = var1[var4];
                  var1[var4] = var7;
               } else if (var5.isFile() & var6.isFile() && var5.getName().compareToIgnoreCase(var6.getName()) > 0) {
                  String var2 = var1[var3];
                  var1[var3] = var1[var4];
                  var1[var4] = var2;
               }
            }
         }
      }

      return var1;
   }

   public WebBrowserTab_win32(MainWindow var1, String var2) {
      super(var1, var2);
   }

   public void createFavoritesMenu(IMenuManager var1) {
      super.createFavoritesMenu(var1);
      if (this.loaded) {
         String var2 = PreferenceLoader.loadStringEnv("favoritesDirectory");
         File var3 = new File(var2);
         if (var2 != null && !var2.equals("") && var3.exists() && var3.isDirectory()) {
            this.traverseTree(var1, var2);
         } else {
            if (!this.loadedBookmarks) {
               var1.add(new WebBrowserTab_win32$StringAction(SResources.getString("p.emptyFavorites")));
            }
         }
      }
   }

   public void traverseTree(IMenuManager var1, String var2) {
      File var3 = new File(var2);
      String[] var4 = SortDir(var3.getPath(), var3.list());

      for (int var5 = 0; var5 < var4.length; var5++) {
         String var6 = var2 + File.separator + var4[var5];
         File var7 = new File(var6);
         if (var7.isFile() && var7.getName().endsWith("url")) {
            var1.add(new WebBrowserTab_win32$URLFile(this, var7));
         } else if (var7.isDirectory()) {
            MenuManager var8 = new MenuManager(this.formatTitle(var7.getName()));
            this.traverseTree(var8, var6);
            var1.add(var8);
         }
      }
   }

   public void updateTitle(String var1) {
      if (!var1.equals("")) {
         this.viewFrame.updateCLabelText(var1);
      }
   }

   protected void onChanged(LocationEvent var1) {
      Browser var2 = (Browser)var1.widget;
      if (var2 != null && !var2.isDisposed()) {
         CTabItem var3 = (CTabItem)var2.getData("cTabItem");
         if (var3 == this.cTabFolder.getSelection()) {
            if (this.getMainWindow().getLinkRipper() != null) {
               LinkRipper var4 = this.getMainWindow().getLinkRipper();
               var4.setInputURL(var1.location);
               var4.setCurrentLinks(this.getCurrentLinks());
            }

            this.inputCombo.setText(var1.location);
         }

         this.setupCtrlKey(var2);
      }
   }

   protected void setupCtrlKey(Browser var1) {
      OleAutomation var2 = (OleAutomation)var1.getWebBrowser();
      if (var2 == null) {
         Sancho.pDebug("Null Ole");
      } else {
         int[] var3 = var2.getIDsOfNames(new String[]{"Document"});
         if (var3 != null) {
            Variant var4 = var2.getProperty(var3[0]);
            if (var4 != null && var4.getType() != 0) {
               OleAutomation var5 = var4.getAutomation();
               WebBrowserTab_win32$EventDispatch var6 = new WebBrowserTab_win32$EventDispatch(this, -2147412107, var2);
               IDispatch var7 = new IDispatch(var6.getAddress());
               Variant var8 = new Variant(var7);
               var5.setProperty(-2147412107, var8);
               var6 = new WebBrowserTab_win32$EventDispatch(this, -2147412106, var2);
               var7 = new IDispatch(var6.getAddress());
               var8 = new Variant(var7);
               var5.setProperty(-2147412106, var8);
               var5.dispose();
            }
         }
      }
   }

   protected boolean ctrlDown() {
      return this.ctrlDown;
   }

   public String[] getCurrentLinks() {
      Browser var1 = this.getSelectedBrowser();
      if (var1 != null && !var1.isDisposed()) {
         OleAutomation var2 = (OleAutomation)var1.getWebBrowser();
         if (var2 == null) {
            Sancho.pDebug("Null Ole");
            return new String[0];
         } else {
            int[] var3 = var2.getIDsOfNames(new String[]{"Document"});
            if (var3 == null) {
               return null;
            } else {
               Variant var4 = var2.getProperty(var3[0]);
               if (var4 != null && var4.getType() != 0) {
                  OleAutomation var5 = var4.getAutomation();
                  int[] var6 = var5.getIDsOfNames(new String[]{"links"});
                  if (var6 == null) {
                     return null;
                  } else {
                     var4 = var5.getProperty(var6[0]);
                     if (var4 != null && var4.getType() != 0) {
                        OleAutomation var7 = var4.getAutomation();
                        int[] var8 = var7.getIDsOfNames(new String[]{"length"});
                        if (var8 == null) {
                           return null;
                        } else {
                           var4 = var7.getProperty(var8[0]);
                           if (var4 != null && var4.getType() != 0) {
                              int var9 = (int)var4.getFloat();
                              if (var9 <= 0) {
                                 return null;
                              } else {
                                 int[] var10 = var7.getIDsOfNames(new String[]{"item"});
                                 if (var10 == null) {
                                    return null;
                                 } else {
                                    String[] var11 = new String[var9];
                                    Variant[] var12 = new Variant[1];

                                    for (int var13 = 0; var13 < var9; var13++) {
                                       Variant var14 = new Variant(var13);
                                       var12[0] = var14;
                                       Variant var15 = var7.invoke(var10[0], var12);
                                       var11[var13] = var15.getString();
                                       var15.dispose();
                                       var14.dispose();
                                    }

                                    return var11;
                                 }
                              }
                           } else {
                              return null;
                           }
                        }
                     } else {
                        return null;
                     }
                  }
               } else {
                  return null;
               }
            }
         }
      } else {
         return null;
      }
   }
}
