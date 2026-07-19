package sancho.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.UniformResourceLocator;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.LinkRipper;
import sancho.view.utility.NoDuplicatesCombo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewFrame.ViewListener;

public class WebBrowserTab extends AbstractTab {
   public CTabFolder cTabFolder;
   public NoDuplicatesCombo inputCombo;
   public Pattern regex;
   public Pattern bookmark_href;
   public Pattern bookmark_title;
   public Pattern bookmark_folder;
   public WebBrowserViewFrame viewFrame;
   protected boolean loaded;
   protected boolean loadedBookmarks;
   public int maxFLen = 10;

   public WebBrowserTab(MainWindow mainWindow, String id) {
      super(mainWindow, id);

      try {
         this.regex = Pattern.compile(
            "(ed2k://\\|file\\|[^\\|]+\\|(\\d+)\\|([\\dabcdef]+)\\|)|(sig2dat:///?\\|File:[^\\|]+\\|Length:.+?\\|UUHash:\\=.+?\\=)|(\\\"magnet:\\?xt=.+?\\\")|(magnet:\\?xt=.+?\n)|(magnet:\\?xt=.+)|(http://.+?/.+?\\.torrent.+)|(\"http://.+?/.+?\\.torrent\\?[^>]+\")|(http://.+?/.+?\\.torrent)",
            Pattern.CASE_INSENSITIVE
         );
         this.bookmark_href = Pattern.compile("HREF=\"(.+?)\"");
         this.bookmark_title = Pattern.compile("<A.+?>(.+?)</A>");
         this.bookmark_folder = Pattern.compile("<H3.+?>(.+?)</H3>");
      } catch (PatternSyntaxException patternSyntaxException) {
      }

      this.updateDisplay();
   }

   private void activateDropTarget(Combo combo) {
      DropTarget dropTarget = new DropTarget(combo, 21);
      final UniformResourceLocator uRL = UniformResourceLocator.getInstance();
      TextTransfer textTransfer = TextTransfer.getInstance();
      dropTarget.setTransfer(new Transfer[]{uRL, textTransfer});
      final Combo linkEntryCombo = combo;
      dropTarget.addDropListener(new DropTargetAdapter() {
         public void dragEnter(DropTargetEvent event) {
            // Only request DROP_LINK when the source actually offers it; forcing detail=4
            // unconditionally made SWT reject a COPY-only drag (plain text/selection), so
            // the drop was silently never delivered. Fall back to COPY, else NONE.
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
               linkEntryCombo.setText((String)event.data);
            }
         }
      });
   }

   public void browserBack() {
      this.browserBack(this.getSelectedBrowser());
   }

   public void browserBack(Browser browser) {
      if (browser != null && !browser.isDisposed()) {
         browser.back();
      }
   }

   public void browserForward() {
      this.browserForward(this.getSelectedBrowser());
   }

   public void browserForward(Browser browser) {
      if (browser != null && !browser.isDisposed()) {
         browser.forward();
      }
   }

   public void browserRefresh() {
      this.browserRefresh(this.getSelectedBrowser());
   }

   public void browserRefresh(Browser browser) {
      if (browser != null && !browser.isDisposed()) {
         browser.refresh();
      }
   }

   public void browserStop() {
      this.browserStop(this.getSelectedBrowser());
   }

   public void browserStop(Browser browser) {
      if (browser != null && !browser.isDisposed()) {
         browser.stop();
      }
   }

   public Browser createBrowser(Composite parent) {
      Browser browser;
      try {
         // SWT removed the old Mozilla/XULRunner backend (SWT.MOZILLA). On Windows
         // the plain default (SWT.NONE) is still the legacy Internet Explorer engine,
         // so prefer the modern Edge (Chromium/WebView2) backend there; if WebView2
         // is unavailable it throws, and we fall back to the platform default.
         if ("win32".equals(SWT.getPlatform())) {
            try {
               browser = new Browser(parent, SWT.EDGE);
            } catch (SWTError edgeUnavailable) {
               Sancho.pDebug("SWT.EDGE unavailable, falling back to default browser: " + edgeUnavailable.toString());
               browser = new Browser(parent, SWT.NONE);
            }
         } else {
            browser = new Browser(parent, SWT.NONE);
         }
      } catch (SWTError browserError) {
         Sancho.pDebug(browserError.toString());
         this.viewFrame.updateCLabelText("Browser failed (see FAQ): " + browserError.toString());
         this.viewFrame.updateCLabelToolTip(browserError.toString());
         return null;
      } catch (Exception otherError) {
         otherError.printStackTrace();
         return null;
      }

      this.loaded = true;
      browser.setLayoutData(new GridData(1808));
      browser.addStatusTextListener(new StatusTextListener() {
         public void changed(StatusTextEvent event) {
            Browser eventBrowser = (Browser)event.widget;
            if (eventBrowser != null && !eventBrowser.isDisposed()) {
               CTabItem tabItem = (CTabItem)eventBrowser.getData("cTabItem");
               if (tabItem == WebBrowserTab.this.cTabFolder.getSelection()) {
                  WebBrowserTab.this.getMainWindow().getStatusline().setText(event.text);
               }
            }
         }
      });
      browser.addTitleListener(new TitleListener() {
         public void changed(TitleEvent event) {
            Browser eventBrowser = (Browser)event.widget;
            if (eventBrowser != null && !eventBrowser.isDisposed()) {
               CTabItem tabItem = (CTabItem)eventBrowser.getData("cTabItem");
               // Null-check BEFORE setText: a title event can arrive before createBrowserTab
               // stores "cTabItem" (the Edge backend delivers titles from an async pump), so
               // dereferencing tabItem first threw an NPE and the title stopped updating.
               if (tabItem != null && !tabItem.isDisposed()) {
                  tabItem.setText(event.title);
                  if (tabItem == WebBrowserTab.this.cTabFolder.getSelection()) {
                     WebBrowserTab.this.viewFrame.updateCLabelText(event.title);
                  }
               }
            }
         }
      });
      browser.addCloseWindowListener(new CloseWindowListener() {
         public void close(WindowEvent event) {
            Browser eventBrowser = (Browser)event.widget;
            if (eventBrowser != null && !eventBrowser.isDisposed()) {
               CTabItem tabItem = (CTabItem)eventBrowser.getData("cTabItem");
               if (tabItem != null && !tabItem.isDisposed() && !WebBrowserTab.this.cTabFolder.isDisposed()) {
                  tabItem.dispose();
               }
            }
         }
      });
      browser.addOpenWindowListener(new OpenWindowListener() {
         public void open(WindowEvent event) {
            Browser newBrowser = WebBrowserTab.this.createBrowserTab();
            if (newBrowser != null) {
               event.browser = newBrowser;
            }
         }
      });
      browser.addLocationListener(new LocationListener() {
         public void changed(LocationEvent event) {
            WebBrowserTab.this.onChanged(event);
         }

         public void changing(LocationEvent event) {
            if (event.location != null) {
               String location = event.location;
               if (WebBrowserTab.this.regex.matcher(location).find()) {
                  WebBrowserTab.this.getMainWindow().getStatusline().setText(SResources.getString("l.sending") + location);
                  event.doit = false;
                  Sancho.send((short)8, location);
               } else if (event.top) {
                  WebBrowserTab.this.onChangingTop(event);
               }
            }
         }
      });
      return browser;
   }

   public Browser createBrowserTab() {
      Composite composite = new Composite(this.cTabFolder, 0);
      CTabItem item = new CTabItem(this.cTabFolder, 0);
      item.setControl(composite);
      item.setText("blank");
      composite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      this.inputCombo = new NoDuplicatesCombo(composite, 0);
      this.inputCombo.setLayoutData(new GridData(768));
      // Bind this combo to its tab so every read/write targets the right one; the
      // shared this.inputCombo field only ever points at the last-created tab, so with
      // multiple tabs the URL used to land in the wrong (last) tab's address bar.
      item.setData("inputCombo", this.inputCombo);

      try {
         if (SWT.getPlatform().equals("win32") && PreferenceLoader.loadBoolean("dragAndDrop")) {
            this.activateDropTarget(this.inputCombo);
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      this.inputCombo.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent event) {
            NoDuplicatesCombo combo = (NoDuplicatesCombo)event.widget;
            if (event.character == '\r' || event.keyCode == 16777296) {
               WebBrowserTab.this.navigate(combo.getText());
               combo.add(combo.getText(), 0);
               combo.setText("");
            }
         }
      });
      Browser browser = this.createBrowser(composite);
      if (browser != null) {
         browser.setData("cTabItem", item);
         item.setData("browser", browser);
      }

      return browser;
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.maxFLen = Math.max(PreferenceLoader.loadInt("maxFavoriteLength"), 10);
   }

   protected void createContents(Composite parent) {
      this.viewFrame = new WebBrowserViewFrame(parent, "tab.webbrowser", "tab.webbrowser.buttonSmall", this);
      this.addViewFrame(this.viewFrame);
      this.cTabFolder = WidgetFactory.createCTabFolder(
         this.viewFrame.getChildComposite(), 64 | (PreferenceLoader.loadBoolean("webBrowserCTabFolderTabsOnTop") ? 128 : 1024)
      );
      WidgetFactory.addCTabFolderMenu(this.cTabFolder, "webBrowserCTabFolder");
      if (this.createBrowserTab() != null) {
         this.cTabFolder.setSelection(0);
         this.cTabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
            public void close(CTabFolderEvent event) {
               CTabItem tabItem = (CTabItem)event.item;
               Browser browser = (Browser)tabItem.getData("browser");
               if (WebBrowserTab.this.cTabFolder.getItemCount() == 1) {
                  if (browser != null && !browser.isDisposed()) {
                     browser.setUrl("about:blank");
                  }

                  NoDuplicatesCombo combo = WebBrowserTab.this.getInputCombo(tabItem);
                  if (combo != null) {
                     combo.setText("");
                  }

                  WebBrowserTab.this.viewFrame.updateCLabelText(SResources.getString("tab.webbrowser"));
                  tabItem.setText("blank");
                  event.doit = false;
               } else if (browser != null && !browser.isDisposed()) {
                  browser.dispose();
               }
            }
         });
         this.cTabFolder.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               CTabItem tabItem = (CTabItem)event.item;
               Browser browser = (Browser)tabItem.getData("browser");
               NoDuplicatesCombo combo = WebBrowserTab.this.getInputCombo(tabItem);
               if (combo != null) {
                  if (browser != null && !browser.isDisposed()) {
                     combo.setText(browser.getUrl());
                  }

                  combo.setFocus();
               }

               WebBrowserTab.this.viewFrame.updateCLabelText(tabItem.getText());
            }
         });
      }
   }

   public void createFavoritesMenu(IMenuManager menuManager) {
      if (this.loaded) {
         menuManager.add(new NewBrowserTabAction());
         menuManager.add(new Separator());
         String path = PreferenceLoader.loadStringEnv("bookmarksFile");
         File file = new File(path);
         if (path != null && !path.equals("") && file.exists()) {
            this.loadedBookmarks = true;

            try {
               BufferedReader reader = new BufferedReader(new FileReader(file));
               this.traverseBookmarks(reader, menuManager);
            } catch (Exception exception) {
               System.out.println(exception);
            }
         }

         path = PreferenceLoader.loadStringEnv("adrFile");
         file = new File(path);
         if (path != null && !path.equals("") && file.exists()) {
            this.loadedBookmarks = true;

            try {
               BufferedReader reader = new BufferedReader(new FileReader(file));
               this.traverseADR(reader, menuManager);
            } catch (Exception exception) {
               System.out.println(exception);
            }
         }
      }
   }

   protected void addIfFull(IMenuManager menuManager, String name, String url) {
      if (name != null && url != null) {
         menuManager.add(new ADRBookmark(name, url));
      }
   }

   public void traverseADR(BufferedReader reader, IMenuManager menuManager) {
      ArrayList menuStack = new ArrayList();
      byte state = 0;
      Object current = menuManager;
      String name = null;
      String url = null;

      try {
         String line;
         while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("#FOLDER")) {
               state = 1;
            } else if (line.startsWith("-")) {
               int index = menuStack.size() - 1;
               if (index >= 0) {
                  IMenuManager parent = (IMenuManager)menuStack.get(index);
                  menuStack.remove(index);
                  parent.add((IContributionItem)current);
                  current = parent;
               }

               state = 0;
            } else if (line.startsWith("#URL")) {
               name = null;
               url = null;
               state = 2;
            } else if (line.startsWith("NAME=")) {
               if (state == 1) {
                  menuStack.add(current);
                  current = new MenuManager(this.formatTitle(line.substring(5)));
               } else if (state == 2) {
                  name = line.substring(5);
                  this.addIfFull((IMenuManager)current, name, url);
               }
            } else if (line.startsWith("URL=")) {
               url = line.substring(4);
               this.addIfFull((IMenuManager)current, name, url);
            }
         }

         reader.close();
      } catch (IOException ioException) {
         System.out.println(ioException);
      }
   }

   public void traverseBookmarks(BufferedReader reader, IMenuManager menuManager) {
      ArrayList menuStack = new ArrayList();
      boolean started = false;
      int depth = 0;
      Object current = menuManager;

      try {
         String line;
         while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("<DL>")) {
               if (!started) {
                  started = true;
               } else {
                  depth++;
               }
            } else if (!line.startsWith("</DL>")) {
               if (line.startsWith("<DT>")) {
                  if (line.indexOf("HREF=") != -1) {
                     ((IMenuManager)current).add(new NSBookmark(line));
                  } else {
                     menuStack.add(current);
                     Matcher matcher = this.bookmark_folder.matcher(line);
                     String title = "Folder";
                     if (matcher.find()) {
                        int start = matcher.start(1);
                        int end = matcher.end(1);
                        if (!matcher.find()) {
                           title = line.substring(start, end);
                        }
                     }

                     current = new MenuManager(this.formatTitle(title));
                  }
               }
            } else {
               for (int i = menuStack.size(); i >= depth && i > 0; i--) {
                  IMenuManager parent = (IMenuManager)menuStack.get(i - 1);
                  menuStack.remove(i - 1);
                  parent.add((IContributionItem)current);
                  current = parent;
               }

               depth--;
            }
         }

         reader.close();
      } catch (IOException ioException) {
         System.out.println(ioException);
      }
   }

   public String getInputText() {
      NoDuplicatesCombo combo = this.getSelectedInputCombo();
      return combo == null ? "" : combo.getText();
   }

   public NoDuplicatesCombo getInputCombo(CTabItem item) {
      return item == null ? null : (NoDuplicatesCombo)item.getData("inputCombo");
   }

   public NoDuplicatesCombo getSelectedInputCombo() {
      return this.cTabFolder == null ? null : this.getInputCombo(this.cTabFolder.getSelection());
   }

   public String[] getCurrentLinks() {
      return new String[0];
   }

   public Browser getSelectedBrowser() {
      if (this.cTabFolder == null) {
         return null;
      } else {
         CTabItem item = this.cTabFolder.getSelection();
         return item == null ? null : (Browser)item.getData("browser");
      }
   }

   public void navigate(Browser browser, String url) {
      if (browser != null && !browser.isDisposed()) {
         if (url.indexOf("//") == -1) {
            url = "http://" + url;
         }

         browser.setUrl(url);
      }
   }

   public void navigate(String url) {
      this.navigate(this.getSelectedBrowser(), url);
   }

   protected void onChanged(LocationEvent event) {
      Browser browser = (Browser)event.widget;
      CTabItem item = (CTabItem)browser.getData("cTabItem");
      if (item == this.cTabFolder.getSelection()) {
         if (this.getMainWindow().getLinkRipper() != null) {
            LinkRipper linkRipper = this.getMainWindow().getLinkRipper();
            linkRipper.setInputURL(event.location);
         }

         NoDuplicatesCombo combo = this.getInputCombo(item);
         if (combo != null) {
            combo.setText(event.location);
         }
      }
   }

   protected void onChangingTop(LocationEvent event) {
      Browser browser = (Browser)event.widget;
      CTabItem item = (CTabItem)browser.getData("cTabItem");
      NoDuplicatesCombo combo = this.getInputCombo(item);
      if (combo != null) {
         combo.setText(event.location);
      }
   }

   protected String formatTitle(String title) {
      int length = title.length();
      if (length > this.maxFLen) {
         String head = title.substring(0, this.maxFLen - 7);
         String tail = title.substring(length - 4, length);
         title = head + "..." + tail;
      }

      if (title.indexOf("&") != -1) {
         title = SwissArmy.replaceAll(title, "&", "&&");
      }

      return title;
   }

   // The tab's view frame: a browser toolbar (configurable link buttons + stop/refresh/
   // back/forward) plus the favorites menu wiring. Non-static inner class so its tool
   // buttons can reach the enclosing WebBrowserTab directly.
   public class WebBrowserViewFrame extends ViewFrame {
      private int numToolItems;

      public WebBrowserViewFrame(Composite parent, String label, String icon, AbstractTab tab) {
         super(parent, label, icon, tab);
         this.createViewToolBar();
         this.createViewListener(new WebBrowserViewListener(this));
      }

      public void createViewToolBar() {
         super.createViewToolBar();
         this.numToolItems = PreferenceLoader.loadInt("webBrowserToolItems");

         for (int i = 1; i < this.numToolItems + 1; i++) {
            final int toolIndex = i;
            this.addToolItem(PreferenceLoader.loadString("webBrowserToolItem" + i), String.valueOf(i), new SelectionAdapter() {
               public void widgetSelected(SelectionEvent event) {
                  WebBrowserTab.this.navigate(PreferenceLoader.loadString("webBrowserToolItem" + toolIndex));
               }
            });
         }

         this.addToolItem("ti.web.sancho", "ProgramIcon", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               WebBrowserTab.this.navigate(VersionInfo.getHomePage2());
            }
         });
         this.addToolSeparator();
         this.addToolItem("ti.web.stop", "page-stop", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               WebBrowserTab.this.browserStop();
            }
         });
         this.addToolItem("ti.web.refresh", "page-refresh", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               WebBrowserTab.this.browserRefresh();
            }
         });
         this.addToolSeparator();
         this.addToolItem("ti.web.back", "page-back", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               WebBrowserTab.this.browserBack();
            }
         });
         this.addToolItem("ti.web.forward", "page-forward", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               WebBrowserTab.this.browserForward();
            }
         });
      }

      public void updateDisplay() {
         super.updateDisplay();
         if (this.numToolItems != PreferenceLoader.loadInt("webBrowserToolItems") && this.toolBar != null) {
            for (int i = this.toolBar.getItemCount() - 1; i >= 0; i--) {
               this.toolBar.getItems()[i].dispose();
            }

            this.toolBar.dispose();
            this.createViewToolBar();
            this.toolBar.getParent().layout();
         } else if (this.toolBar != null) {
            for (int i = 1; i <= this.numToolItems; i++) {
               this.toolBar.getItems()[i - 1].setToolTipText(PreferenceLoader.loadString("webBrowserToolItem" + i));
            }
         }
      }
   }

   // Routes the view frame's context-menu build to the tab's favorites menu.
   public class WebBrowserViewListener extends ViewListener {
      public WebBrowserViewListener(ViewFrame frame) {
         super(frame);
      }

      public void menuAboutToShow(IMenuManager menu) {
         WebBrowserTab.this.createFavoritesMenu(menu);
      }
   }

   // Favorites-menu action: open a fresh browser tab and select it.
   public class NewBrowserTabAction extends Action {
      public NewBrowserTabAction() {
         super(SResources.getString("l.newBrowserTab"));
      }

      public void run() {
         Browser browser = WebBrowserTab.this.createBrowserTab();
         if (browser != null) {
            CTabItem tabItem = (CTabItem)browser.getData("cTabItem");
            WebBrowserTab.this.cTabFolder.setSelection(tabItem);
         }
      }
   }

   // A Netscape-style (bookmarks.html) favorite parsed from a <DT><A HREF=...> line.
   public class NSBookmark extends Action {
      String href;

      public NSBookmark(String line) {
         Matcher titleMatcher = WebBrowserTab.this.bookmark_title.matcher(line);
         Matcher hrefMatcher = WebBrowserTab.this.bookmark_href.matcher(line);
         String title = "Unknown";
         if (titleMatcher.find()) {
            int start = titleMatcher.start(1);
            int end = titleMatcher.end(1);
            if (!titleMatcher.find()) {
               title = line.substring(start, end);
            }
         }

         if (hrefMatcher.find()) {
            int start = hrefMatcher.start(1);
            int end = hrefMatcher.end(1);
            if (!hrefMatcher.find()) {
               this.href = line.substring(start, end);
            }
         }

         this.setText(WebBrowserTab.this.formatTitle(title));
         this.setImageDescriptor(SResources.getImageDescriptor("web-link-m"));
      }

      public void run() {
         if (this.href != null && !this.href.equals("")) {
            WebBrowserTab.this.navigate(WebBrowserTab.this.getSelectedBrowser(), this.href);
         }
      }
   }

   // An Opera-style (.adr) favorite: an explicit name + URL pair.
   public class ADRBookmark extends Action {
      String URL;

      public ADRBookmark(String name, String url) {
         this.setText(WebBrowserTab.this.formatTitle(name));
         this.setImageDescriptor(SResources.getImageDescriptor("web-link-o"));
         this.URL = url;
      }

      public void run() {
         if (this.URL != null && !this.URL.equals("")) {
            WebBrowserTab.this.navigate(WebBrowserTab.this.getSelectedBrowser(), this.URL);
         }
      }
   }
}
