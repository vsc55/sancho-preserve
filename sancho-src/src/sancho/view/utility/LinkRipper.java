package sancho.view.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.core.Sancho;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.MainWindow;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.UniformResourceLocator;

public class LinkRipper extends Dialog implements Runnable, IMenuListener {
   static Pattern endSlashRE;
   static Pattern frameRE;
   static Pattern snRE;
   MainWindow mainWindow;
   MenuManager popupMenu;
   boolean ripping;
   Thread thread;
   Group urlGroup;
   List urlList;
   Text urlText;
   String URLtoRip;

   public LinkRipper(Shell var1, MainWindow var2) {
      super(var1);
      this.mainWindow = var2;
   }

   private void activateDropTarget(Text var1) {
      byte var2 = 23;
      DropTarget var3 = new DropTarget(var1, var2);
      final UniformResourceLocator var4 = UniformResourceLocator.getInstance();
      TextTransfer var5 = TextTransfer.getInstance();
      var3.setTransfer(new Transfer[]{var4, var5});
      final Text linkEntryText = var1;
      var3.addDropListener(new DropTargetAdapter() {
         public void dragEnter(DropTargetEvent var1) {
            // Request DROP_LINK only if the source offers it, else COPY, else NONE — forcing
            // detail=4 made SWT reject a COPY-only drag so the drop was never delivered.
            boolean var2 = false;

            for (int var3 = 0; var3 < var1.dataTypes.length; var3++) {
               if (var4.isSupportedType(var1.dataTypes[var3])) {
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
               linkEntryText.append((String)var1.data);
            }
         }
      });
   }

   public void addMenuItem(Menu var1, String var2, String var3, SelectionAdapter var4) {
      MenuItem var5 = new MenuItem(var1, 8);
      var5.setText(SResources.getString(var2));
      var5.setImage(SResources.getImage(var3));
      var5.addSelectionListener(var4);
   }

   public void addToClipBoard(String var1) {
      MainWindow.copyToClipboard(var1);
   }

   public boolean close() {
      PreferenceStore var1 = PreferenceLoader.getPreferenceStore();
      PreferenceConverter.setValue(var1, "linkRipperWindowBounds", this.getShell().getBounds());
      this.mainWindow.closeLinkRipper();
      return super.close();
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(SResources.getImage("web-link"));
      var1.setText(VersionInfo.getName() + " " + SResources.getString("l.linkRipper"));
   }

   protected void constrainShellSize() {
      super.constrainShellSize();
      Shell var1 = this.getShell();
      if (PreferenceLoader.contains("linkRipperWindowBounds")) {
         var1.setBounds(PreferenceLoader.loadRectangle("linkRipperWindowBounds"));
      } else {
         var1.setSize(500, 300);
         Point var2 = var1.getLocation();
         this.getShell().setLocation(var2.x - 200, var2.y);
      }
   }

   protected Control createButtonBar(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      var2.setLayoutData(new GridData(768));
      Button var3 = new Button(var2, 0);
      var3.setLayoutData(new GridData(768));
      var3.setText(SResources.getString("b.downloadAll"));
      var3.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            downloadAll();
         }
      });
      Button var4 = new Button(var2, 0);
      var4.setLayoutData(new GridData(128));
      var4.setText(SResources.getString("b.close"));
      var4.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            close();
         }
      });
      return var2;
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      Composite var3 = new Composite(var2, 0);
      var3.setLayoutData(new GridData(768));
      var3.setLayout(WidgetFactory.createGridLayout(3, 0, 0, 5, 5, false));
      Label var4 = new Label(var3, 0);
      var4.setText(SResources.getString("rip.url"));
      var4.setLayoutData(new GridData(32));
      this.urlText = new Text(var3, 2052);
      if (PreferenceLoader.loadBoolean("dragAndDrop")) {
         this.activateDropTarget(this.urlText);
      }

      this.urlText.setLayoutData(new GridData(768));
      Button var5 = new Button(var3, 0);
      var5.setLayoutData(new GridData(128));
      var5.setText(SResources.getString("rip.rip"));
      this.urlGroup = new Group(var2, 0);
      this.urlGroup.setLayoutData(new GridData(1808));
      this.urlGroup.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      this.urlGroup.setText(SResources.getString("rip.waiting"));
      this.urlList = new List(this.urlGroup, 2818);
      this.createMenu();
      this.urlList.addListener(3, new Listener() {
         public void handleEvent(Event var1) {
            if (var1.button == 3) {
               Menu var2 = popupMenu.createContextMenu(urlList);
               var2.setLocation(urlList.getDisplay().getCursorLocation());
               var2.setVisible(true);
            }
         }
      });
      this.urlList.setLayoutData(new GridData(1808));
      this.urlList.addMouseListener(new MouseAdapter() {
         public void mouseDoubleClick(MouseEvent var1) {
            String[] var2 = urlList.getSelection();
            String var3 = "";
            if (var2.length > 0) {
               var3 = var2[0];
            }

            if (urlList.getSelectionCount() == 1 && var3.toLowerCase().startsWith("ftp") && var3.endsWith("/")) {
               urlText.setText(var3);
               ripLinks();
            } else {
               downloadSelected();
            }
         }
      });
      var5.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            ripLinks();
         }
      });
      this.urlText.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            if (var1.character == '\r' || var1.character == 16777296) {
               ripLinks();
            }
         }
      });
      return var2;
   }

   private void createMenu() {
      this.popupMenu = new MenuManager("");
      this.popupMenu.setRemoveAllWhenShown(true);
      this.popupMenu.addMenuListener(this);
   }

   public void downloadAll() {
      for (int var1 = 0; var1 < this.urlList.getItems().length; var1++) {
         SwissArmy.sendLink(Sancho.getCore(), this.urlList.getItems()[var1]);
      }

      this.mainWindow.getStatusline().setText(SResources.getString("sl.linksSent") + this.urlList.getItemCount());
   }

   public void downloadSelected() {
      if (this.urlList.getSelectionCount() > 0) {
         String[] var1 = this.urlList.getSelection();

         for (int var2 = 0; var2 < var1.length; var2++) {
            SwissArmy.sendLink(Sancho.getCore(), var1[var2]);
         }

         this.mainWindow.getStatusline().setText(SResources.getString("sl.linksSent") + var1.length);
      }
   }

   protected String getRawPage(String var1) {
      URL var2;
      try {
         var2 = new URL(var1);
      } catch (MalformedURLException var6) {
         Sancho.pDebug("LinkRipper: " + var6);
         return null;
      }

      try {
         BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.openStream()));
         StringBuffer var4 = new StringBuffer();

         String var5;
         while ((var5 = var3.readLine()) != null) {
            var4.append(var5);
         }

         var3.close();
         return var4.toString();
      } catch (IOException var7) {
         Sancho.pDebug("LinkRipper: " + var7);
         return null;
      }
   }

   protected int getShellStyle() {
      return 2160 | (SWT.getPlatform().equals("fox") ? 0 : 0);
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new DownloadSelectedAction());
      var1.add(new DownloadAllAction());
      var1.add(new Separator());
      var1.add(new CopyAction());
      var1.add(new CopyAllAction());
      var1.add(new Separator());
      var1.add(new ToggleShowAllAction());
   }

   public void ripLinks() {
      if (!this.ripping) {
         this.ripping = true;
         this.urlList.removeAll();
         this.URLtoRip = this.urlText.getText();
         if (this.URLtoRip.equals("")) {
            this.ripping = false;
         } else {
            this.urlGroup.setText(SResources.getString("rip.ripping"));
            this.thread = new Thread(this);
            this.thread.start();
         }
      }
   }

   public void run() {
      if (!this.URLtoRip.toLowerCase().startsWith("ftp")) {
         if (!this.URLtoRip.toLowerCase().startsWith("http")) {
            this.URLtoRip = "http://" + this.URLtoRip;
         }

         if (!endSlashRE.matcher(this.URLtoRip).find()) {
            this.URLtoRip = this.URLtoRip + "/";
         }

         String var11 = this.getRawPage(this.URLtoRip);
         if (snRE.matcher(this.URLtoRip).find()) {
            Matcher var12 = var11 != null ? frameRE.matcher(var11) : null;
            if (var12 != null && var12.find()) {
               String var14 = var11.substring(var12.start(1), var12.end(1));
               var14 = var14 + "list_news.html";
               var11 = this.getRawPage(var14);
            }
         }

         if (var11 == null) {
            if (this.urlGroup != null && !this.urlGroup.isDisposed()) {
               this.urlGroup.getDisplay().syncExec(new Runnable() {
                  public void run() {
                     urlGroup.setText(SResources.getString("rip.error"));
                  }
               });
               this.ripping = false;
            }
         } else {
            final String[] var13 = SwissArmy.parseLinks(var11);
            if (this.urlGroup != null && !this.urlGroup.isDisposed()) {
               this.urlGroup.getDisplay().syncExec(new Runnable() {
                  public void run() {
                     urlGroup.setText(SResources.getString("l.foundLinks") + "(" + var13.length + "):");

                     for (int var1 = 0; var1 < var13.length; var1++) {
                        urlList.add(var13[var1]);
                     }
                  }
               });
               this.ripping = false;
            }
         }
      } else {
         if (!this.URLtoRip.endsWith("/")) {
            this.URLtoRip = this.URLtoRip + "/";
         }

         try {
            InputStream var1 = new URL(this.URLtoRip).openStream();
            BufferedReader var2 = new BufferedReader(new InputStreamReader(var1));
            ArrayList var3 = new ArrayList();
            String var4 = "";

            while ((var4 = var2.readLine()) != null) {
               StringTokenizer var5 = new StringTokenizer(var4);
               int var6 = var5.countTokens();
               if (var6 > 8) {
                  StringBuffer var7 = new StringBuffer(this.URLtoRip);

                  for (int var8 = 0; var8 < 8; var8++) {
                     var5.nextToken();
                  }

                  for (int var9 = 8; var9 < var6; var9++) {
                     var7.append(var5.nextToken());
                     if (var9 != var6 - 1) {
                        var7.append(" ");
                     }
                  }

                  if (var4.startsWith("d")) {
                     var7.append("/");
                  }

                  var3.add(var7.toString());
               }
            }

            final String[] var17 = new String[var3.size()];
            var3.toArray(var17);
            if (this.urlGroup == null || this.urlGroup.isDisposed()) {
               return;
            }

            this.urlGroup.getDisplay().syncExec(new Runnable() {
               public void run() {
                  urlGroup.setText(SResources.getString("l.foundLinks") + "(" + var17.length + "):");

                  for (int var1 = 0; var1 < var17.length; var1++) {
                     urlList.add(var17[var1]);
                  }
               }
            });
         } catch (Exception var10) {
            this.urlGroup.getDisplay().syncExec(new Runnable() {
               public void run() {
                  urlGroup.setText(SResources.getString("rip.error"));
               }
            });
         }

         this.ripping = false;
      }
   }

   public void setCurrentLinks(String[] var1) {
      this.urlList.removeAll();
      if (var1 != null) {
         String[] var2 = SwissArmy.parseLinks(var1);
         this.urlGroup.setText(SResources.getString("rip.found") + "(" + var2.length + "):");

         for (int var3 = 0; var3 < var2.length; var3++) {
            this.urlList.add(var2[var3]);
         }
      }
   }

   public void setFocus() {
      this.getShell().setFocus();
   }

   public void setInputURL(String var1) {
      if (var1 != null) {
         this.urlText.setText(var1);
      }
   }

   static {
      try {
         snRE = Pattern.compile("http://.+?suprnova.org");
         frameRE = Pattern.compile("src='(.+?)'");
         endSlashRE = Pattern.compile("http://.+/");
      } catch (Exception var1) {
      }
   }

   // Context-menu action: copy the selected links to the clipboard.
   private class CopyAction extends Action {
      public CopyAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         String var1 = "";

         for (int var2 = 0; var2 < urlList.getSelection().length; var2++) {
            var1 = var1 + urlList.getSelection()[var2] + "\n";
         }

         if (!var1.equals("")) {
            addToClipBoard(var1);
         }
      }
   }

   // Context-menu action: copy every ripped link to the clipboard.
   private class CopyAllAction extends Action {
      public CopyAllAction() {
         super(SResources.getString("mi.copyAll"));
         this.setImageDescriptor(SResources.getImageDescriptor("plus"));
      }

      public void run() {
         String var1 = "";

         for (int var2 = 0; var2 < urlList.getItems().length; var2++) {
            var1 = var1 + urlList.getItems()[var2] + "\n";
         }

         if (!var1.equals("")) {
            addToClipBoard(var1);
         }
      }
   }

   // Context-menu action: send every ripped link to the core for download.
   private class DownloadAllAction extends Action {
      public DownloadAllAction() {
         super(SResources.getString("mi.downloadAll"));
         this.setImageDescriptor(SResources.getImageDescriptor("down_arrow_green"));
      }

      public void run() {
         downloadAll();
      }
   }

   // Context-menu action: send the selected links to the core for download.
   private class DownloadSelectedAction extends Action {
      public DownloadSelectedAction() {
         super(SResources.getString("mi.downloadSelected"));
         this.setImageDescriptor(SResources.getImageDescriptor("down_arrow_yellow"));
      }

      public void run() {
         downloadSelected();
      }
   }

   // Context-menu toggle: persist the "show all links" preference (no outer state needed).
   private static class ToggleShowAllAction extends Action {
      public ToggleShowAllAction() {
         super(SResources.getString("mi.showAll"), 2);
      }

      public boolean isChecked() {
         return PreferenceLoader.loadBoolean("linkRipperShowAll");
      }

      public void run() {
         PreferenceLoader.getPreferenceStore().setValue("linkRipperShowAll", !this.isChecked());
      }
   }
}
