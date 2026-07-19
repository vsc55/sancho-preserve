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

   public LinkRipper(Shell shell, MainWindow mainWindow) {
      super(shell);
      this.mainWindow = mainWindow;
   }

   private void activateDropTarget(Text text) {
      byte operations = 23;
      DropTarget dropTarget = new DropTarget(text, operations);
      final UniformResourceLocator urlTransfer = UniformResourceLocator.getInstance();
      TextTransfer textTransfer = TextTransfer.getInstance();
      dropTarget.setTransfer(new Transfer[]{urlTransfer, textTransfer});
      final Text linkEntryText = text;
      dropTarget.addDropListener(new DropTargetAdapter() {
         public void dragEnter(DropTargetEvent event) {
            // Request DROP_LINK only if the source offers it, else COPY, else NONE — forcing
            // detail=4 made SWT reject a COPY-only drag so the drop was never delivered.
            boolean supported = false;

            for (int i = 0; i < event.dataTypes.length; i++) {
               if (urlTransfer.isSupportedType(event.dataTypes[i])) {
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
               linkEntryText.append((String)event.data);
            }
         }
      });
   }

   public void addMenuItem(Menu menu, String textKey, String imageKey, SelectionAdapter listener) {
      MenuItem menuItem = new MenuItem(menu, 8);
      menuItem.setText(SResources.getString(textKey));
      menuItem.setImage(SResources.getImage(imageKey));
      menuItem.addSelectionListener(listener);
   }

   public void addToClipBoard(String text) {
      MainWindow.copyToClipboard(text);
   }

   public boolean close() {
      PreferenceStore store = PreferenceLoader.getPreferenceStore();
      PreferenceConverter.setValue(store, "linkRipperWindowBounds", this.getShell().getBounds());
      this.mainWindow.closeLinkRipper();
      return super.close();
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setImage(SResources.getImage("web-link"));
      shell.setText(VersionInfo.getName() + " " + SResources.getString("l.linkRipper"));
   }

   protected void constrainShellSize() {
      super.constrainShellSize();
      Shell shell = this.getShell();
      if (PreferenceLoader.contains("linkRipperWindowBounds")) {
         shell.setBounds(PreferenceLoader.loadRectangle("linkRipperWindowBounds"));
      } else {
         shell.setSize(500, 300);
         Point location = shell.getLocation();
         this.getShell().setLocation(location.x - 200, location.y);
      }
   }

   protected Control createButtonBar(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      composite.setLayoutData(new GridData(768));
      Button downloadAllButton = new Button(composite, 0);
      downloadAllButton.setLayoutData(new GridData(768));
      downloadAllButton.setText(SResources.getString("b.downloadAll"));
      downloadAllButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            downloadAll();
         }
      });
      Button closeButton = new Button(composite, 0);
      closeButton.setLayoutData(new GridData(128));
      closeButton.setText(SResources.getString("b.close"));
      closeButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            close();
         }
      });
      return composite;
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      Composite topComposite = new Composite(composite, 0);
      topComposite.setLayoutData(new GridData(768));
      topComposite.setLayout(WidgetFactory.createGridLayout(3, 0, 0, 5, 5, false));
      Label label = new Label(topComposite, 0);
      label.setText(SResources.getString("rip.url"));
      label.setLayoutData(new GridData(32));
      this.urlText = new Text(topComposite, 2052);
      if (PreferenceLoader.loadBoolean("dragAndDrop")) {
         this.activateDropTarget(this.urlText);
      }

      this.urlText.setLayoutData(new GridData(768));
      Button ripButton = new Button(topComposite, 0);
      ripButton.setLayoutData(new GridData(128));
      ripButton.setText(SResources.getString("rip.rip"));
      this.urlGroup = new Group(composite, 0);
      this.urlGroup.setLayoutData(new GridData(1808));
      this.urlGroup.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      this.urlGroup.setText(SResources.getString("rip.waiting"));
      this.urlList = new List(this.urlGroup, 2818);
      this.createMenu();
      this.urlList.addListener(3, new Listener() {
         public void handleEvent(Event event) {
            if (event.button == 3) {
               Menu menu = popupMenu.createContextMenu(urlList);
               menu.setLocation(urlList.getDisplay().getCursorLocation());
               menu.setVisible(true);
            }
         }
      });
      this.urlList.setLayoutData(new GridData(1808));
      this.urlList.addMouseListener(new MouseAdapter() {
         public void mouseDoubleClick(MouseEvent event) {
            String[] selection = urlList.getSelection();
            String first = "";
            if (selection.length > 0) {
               first = selection[0];
            }

            if (urlList.getSelectionCount() == 1 && first.toLowerCase().startsWith("ftp") && first.endsWith("/")) {
               urlText.setText(first);
               ripLinks();
            } else {
               downloadSelected();
            }
         }
      });
      ripButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            ripLinks();
         }
      });
      this.urlText.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent event) {
            if (event.character == '\r' || event.character == 16777296) {
               ripLinks();
            }
         }
      });
      return composite;
   }

   private void createMenu() {
      this.popupMenu = new MenuManager("");
      this.popupMenu.setRemoveAllWhenShown(true);
      this.popupMenu.addMenuListener(this);
   }

   public void downloadAll() {
      for (int i = 0; i < this.urlList.getItems().length; i++) {
         SwissArmy.sendLink(Sancho.getCore(), this.urlList.getItems()[i]);
      }

      this.mainWindow.getStatusline().setText(SResources.getString("sl.linksSent") + this.urlList.getItemCount());
   }

   public void downloadSelected() {
      if (this.urlList.getSelectionCount() > 0) {
         String[] selection = this.urlList.getSelection();

         for (int i = 0; i < selection.length; i++) {
            SwissArmy.sendLink(Sancho.getCore(), selection[i]);
         }

         this.mainWindow.getStatusline().setText(SResources.getString("sl.linksSent") + selection.length);
      }
   }

   protected String getRawPage(String urlString) {
      URL url;
      try {
         url = new URL(urlString);
      } catch (MalformedURLException malformedUrl) {
         Sancho.pDebug("LinkRipper: " + malformedUrl);
         return null;
      }

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
         StringBuffer buffer = new StringBuffer();

         String line;
         while ((line = reader.readLine()) != null) {
            buffer.append(line);
         }

         reader.close();
         return buffer.toString();
      } catch (IOException ioException) {
         Sancho.pDebug("LinkRipper: " + ioException);
         return null;
      }
   }

   protected int getShellStyle() {
      return 2160 | (SWT.getPlatform().equals("fox") ? 0 : 0);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      menuManager.add(new DownloadSelectedAction());
      menuManager.add(new DownloadAllAction());
      menuManager.add(new Separator());
      menuManager.add(new CopyAction());
      menuManager.add(new CopyAllAction());
      menuManager.add(new Separator());
      menuManager.add(new ToggleShowAllAction());
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

         String rawPage = this.getRawPage(this.URLtoRip);
         if (snRE.matcher(this.URLtoRip).find()) {
            Matcher frameMatcher = rawPage != null ? frameRE.matcher(rawPage) : null;
            if (frameMatcher != null && frameMatcher.find()) {
               String frameSrc = rawPage.substring(frameMatcher.start(1), frameMatcher.end(1));
               frameSrc = frameSrc + "list_news.html";
               rawPage = this.getRawPage(frameSrc);
            }
         }

         if (rawPage == null) {
            if (this.urlGroup != null && !this.urlGroup.isDisposed()) {
               this.urlGroup.getDisplay().syncExec(new Runnable() {
                  public void run() {
                     urlGroup.setText(SResources.getString("rip.error"));
                  }
               });
               this.ripping = false;
            }
         } else {
            final String[] links = SwissArmy.parseLinks(rawPage);
            if (this.urlGroup != null && !this.urlGroup.isDisposed()) {
               this.urlGroup.getDisplay().syncExec(new Runnable() {
                  public void run() {
                     urlGroup.setText(SResources.getString("l.foundLinks") + "(" + links.length + "):");

                     for (int i = 0; i < links.length; i++) {
                        urlList.add(links[i]);
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
            InputStream inputStream = new URL(this.URLtoRip).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList linkList = new ArrayList();
            String line = "";

            while ((line = reader.readLine()) != null) {
               StringTokenizer tokenizer = new StringTokenizer(line);
               int tokenCount = tokenizer.countTokens();
               if (tokenCount > 8) {
                  StringBuffer buffer = new StringBuffer(this.URLtoRip);

                  for (int i = 0; i < 8; i++) {
                     tokenizer.nextToken();
                  }

                  for (int j = 8; j < tokenCount; j++) {
                     buffer.append(tokenizer.nextToken());
                     if (j != tokenCount - 1) {
                        buffer.append(" ");
                     }
                  }

                  if (line.startsWith("d")) {
                     buffer.append("/");
                  }

                  linkList.add(buffer.toString());
               }
            }

            final String[] linkArray = new String[linkList.size()];
            linkList.toArray(linkArray);
            if (this.urlGroup == null || this.urlGroup.isDisposed()) {
               return;
            }

            this.urlGroup.getDisplay().syncExec(new Runnable() {
               public void run() {
                  urlGroup.setText(SResources.getString("l.foundLinks") + "(" + linkArray.length + "):");

                  for (int i = 0; i < linkArray.length; i++) {
                     urlList.add(linkArray[i]);
                  }
               }
            });
         } catch (Exception exception) {
            this.urlGroup.getDisplay().syncExec(new Runnable() {
               public void run() {
                  urlGroup.setText(SResources.getString("rip.error"));
               }
            });
         }

         this.ripping = false;
      }
   }

   public void setCurrentLinks(String[] rawLinks) {
      this.urlList.removeAll();
      if (rawLinks != null) {
         String[] links = SwissArmy.parseLinks(rawLinks);
         this.urlGroup.setText(SResources.getString("rip.found") + "(" + links.length + "):");

         for (int i = 0; i < links.length; i++) {
            this.urlList.add(links[i]);
         }
      }
   }

   public void setFocus() {
      this.getShell().setFocus();
   }

   public void setInputURL(String url) {
      if (url != null) {
         this.urlText.setText(url);
      }
   }

   static {
      try {
         snRE = Pattern.compile("http://.+?suprnova.org");
         frameRE = Pattern.compile("src='(.+?)'");
         endSlashRE = Pattern.compile("http://.+/");
      } catch (Exception exception) {
      }
   }

   // Context-menu action: copy the selected links to the clipboard.
   private class CopyAction extends Action {
      public CopyAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         String text = "";

         for (int i = 0; i < urlList.getSelection().length; i++) {
            text = text + urlList.getSelection()[i] + "\n";
         }

         if (!text.equals("")) {
            addToClipBoard(text);
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
         String text = "";

         for (int i = 0; i < urlList.getItems().length; i++) {
            text = text + urlList.getItems()[i] + "\n";
         }

         if (!text.equals("")) {
            addToClipBoard(text);
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
