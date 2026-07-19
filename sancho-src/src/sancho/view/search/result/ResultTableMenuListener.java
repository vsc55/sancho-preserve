package sancho.view.search.result;

import java.util.Arrays;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

import sancho.core.Sancho;
import sancho.model.mldonkey.Result;
import sancho.utility.ObjectMap;
import sancho.utility.VersionInfo;
import sancho.view.MainWindow;
import sancho.view.SearchTab;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewer.actions.WebServicesAction;
import sancho.view.viewer.table.GTableMenuListener;
import sancho.view.viewer.table.GTableView;

public class ResultTableMenuListener extends GTableMenuListener {
   private CTabItem cTabItem;
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Result;

   public ResultTableMenuListener(GTableView tableView, CTabItem cTabItem) {
      super(tableView);
      this.cTabItem = cTabItem;
   }

   public void initialize() {
      super.initialize();
      if (PreferenceLoader.loadBoolean("searchTooltips")) {
         final ToolTipHandler toolTipHandler = new ToolTipHandler(this.gView.getShell());
         toolTipHandler.activateHoverHelp(this.gView.getComposite(), (ICustomViewer)this.gView.getViewer());
         this.gView.getComposite().addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
               toolTipHandler.dispose();
            }
         });
      }
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$Result == null
            ? (class$sancho$model$mldonkey$Result = class$("sancho.model.mldonkey.Result"))
            : class$sancho$model$mldonkey$Result
      );
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         menuManager.add(new DownloadAction());
         menuManager.add(new ResultDetailAction());
         menuManager.add(new RemoveResultsAction());
         menuManager.add(new Separator());
         menuManager.add(new CopyNameAction());
         this.addClipboardMenu(menuManager);
         menuManager.add(new Separator());
         Result result = (Result)this.selectedObjects.get(0);
         this.addWebServicesMenu(menuManager, result.getMD4(), result.getED2K(), result.getSize());
         this.addSelectAllMenu(menuManager);
      }
   }

   public void removeSelected() {
      if (this.selectedObjects.size() != 0) {
         ObjectMap objectMap = (ObjectMap)this.gView.getViewer().getInput();
         if (objectMap != null) {
            objectMap.remove(this.selectedObjects.toArray());
         }
      }
   }

   protected void onDeleteKey() {
      this.removeSelected();
   }

   public boolean downloadResult(Result result) {
      if (!Sancho.hasCollectionFactory()) {
         return true;
      } else {
         boolean force = PreferenceLoader.loadBoolean("searchForceDownload");
         if (result.downloaded() && !force) {
            Shell shell = this.tableViewer.getTable().getShell();
            MessageBox messageBox = new MessageBox(shell, 194);
            messageBox.setText(SResources.getString("s.alreadyDownloadedTitle"));
            messageBox.setMessage(result.getName() + "\n" + SResources.getString("s.alreadyDownloadedText"));
            if (messageBox.open() == 64) {
               this.gView.getCore().getResultCollection().download(result, true);
               this.updateItem(5000, result);
               return true;
            } else {
               return false;
            }
         } else {
            this.gView.getCore().getResultCollection().download(result, force);
            this.updateItem(5000, result);
            return true;
         }
      }
   }

   protected void updateItem(int delay, final Result result) {
      this.tableViewer.getTable().getDisplay().timerExec(delay, new Runnable() {
         public void run() {
            if (tableViewer != null && tableViewer.getTable() != null && !tableViewer.getTable().isDisposed()) {
               tableViewer.update(new Object[]{result}, null);
            }
         }
      });
   }

   public void downloadSingleFile(Result result) {
      this.downloadResult(result);
      this.postDownloadStats(1, "");
   }

   public void downloadSelected() {
      if (Sancho.hasCollectionFactory()) {
         String failedNames = "";
         int count = 0;

         for (int i = 0; i < this.selectedObjects.size(); i++) {
            Result result = (Result)this.selectedObjects.get(i);
            if (this.downloadResult(result)) {
               count++;
            } else {
               failedNames = failedNames + result.getName() + "\n";
            }
         }

         this.postDownloadStats(count, failedNames);
      }
   }

   public void postDownloadStats(int count, String failedNames) {
      SearchTab searchTab = (SearchTab)this.cTabItem.getParent().getData();
      searchTab.getMainWindow().getStatusline().setText(SResources.getString("s.sl.startedDownload") + count);
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }

   // Context-menu action that downloads all selected results.
   private class DownloadAction extends Action {
      public DownloadAction() {
         super(SResources.getString("s.r.download"));
         this.setImageDescriptor(SResources.getImageDescriptor("down_arrow_green"));
      }

      public void run() {
         downloadSelected();
      }
   }

   // Context-menu action that opens the detail dialog for the first selected result.
   private class ResultDetailAction extends Action {
      public ResultDetailAction() {
         super(SResources.getString("s.r.resultDetails"));
         this.setImageDescriptor(SResources.getImageDescriptor("info"));
      }

      public void run() {
         Result result = (Result)selectedObjects.get(0);
         if (result instanceof Result) {
            new ResultDetailDialog(gView.getShell(), result).open();
         }
      }
   }

   // Context-menu action that removes the selected results from the table.
   private class RemoveResultsAction extends Action {
      public RemoveResultsAction() {
         super(SResources.getString("b.remove"));
         this.setImageDescriptor(SResources.getImageDescriptor("minus"));
      }

      public void run() {
         removeSelected();
      }
   }

   // Context-menu action that copies the names of the selected results to the clipboard.
   private class CopyNameAction extends Action {
      public CopyNameAction() {
         super(SResources.getString("s.r.copyName"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         String text = "";
         String separator = System.getProperty("line.separator");

         for (int i = 0; i < selectedObjects.size(); i++) {
            Result result = (Result)selectedObjects.get(i);
            if (text.length() > 0) {
               text = text + separator;
            }

            text = text + result.getName();
         }

         MainWindow.copyToClipboard(text);
      }
   }

   // Hover tooltip popup that shows details, alternate names and quick web-service actions for a result.
   private class ToolTipHandler implements IMenuListener {
      private Shell tipShell;
      private Label tipLabelImage;
      private Label tipLabelTitle;
      private Label tipLabelText;
      private Widget tipWidget;
      private Point tipPosition;
      private List namesList;
      private CustomSeparator namesSeparator;
      private Point pt;
      private Font boldFont;
      private ToolBar toolBar;
      private Composite namesComposite;
      private long fileSize;
      private String md4;
      private String ed2k;
      private int charHeight;
      private Result result;
      private MenuManager popupMenu;
      private Display display;
      private boolean isVisible;
      int vscroll;
      private ToolTipTimerTask timerTask;
      private int[] roundRect;
      private int[] roundRectR;
      private Region tipRegion;

      int[] roundRect(int x, int y, int width, int height) {
         int[] topLeft = new int[]{0, 6, 1, 5, 1, 4, 4, 1, 5, 1, 6, 0};
         int[] topRight = new int[]{-6, 0, -5, 1, -4, 1, -1, 4, -1, 5, 0, 6};
         int[] bottomRight = new int[]{0, -6, -1, -5, -1, -4, -4, -1, -5, -1, -6, 0};
         int[] bottomLeft = new int[]{6, 0, 5, -1, 4, -1, 1, -4, 1, -5, 0, -6};
         int[] points = new int[48];
         int index = 0;
         int[] corners = topLeft;
         int[] cornersRight = topRight;

         for (int i = 0; i < corners.length / 2; i++) {
            points[index++] = x + corners[2 * i];
            points[index++] = y + corners[2 * i + 1];
         }

         for (int i = 0; i < cornersRight.length / 2; i++) {
            points[index++] = x + width + cornersRight[2 * i];
            points[index++] = y + cornersRight[2 * i + 1];
         }

         corners = bottomRight;
         cornersRight = bottomLeft;

         for (int i = 0; i < corners.length / 2; i++) {
            points[index++] = x + corners[2 * i] + width;
            points[index++] = y + corners[2 * i + 1] + height;
         }

         for (int i = 0; i < cornersRight.length / 2; i++) {
            points[index++] = x + cornersRight[2 * i];
            points[index++] = y + cornersRight[2 * i + 1] + height;
         }

         return points;
      }

      public ToolTipHandler(Composite composite) {
         this.pt = new Point(0, 0);
         GC gc = new GC(composite);
         this.charHeight = gc.getFontMetrics().getHeight();
         gc.dispose();
         this.display = composite.getDisplay();
         this.tipShell = new Shell(this.display, 278540);
         this.tipShell.setBackground(this.display.getSystemColor(29));
         this.tipShell.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
               if (roundRect != null) {
                  event.gc.setBackground(tipShell.getDisplay().getSystemColor(29));
                  event.gc.setForeground(tipShell.getDisplay().getSystemColor(28));
                  event.gc.fillPolygon(roundRect);
                  event.gc.drawPolygon(roundRect);
               }
            }
         });
         this.tipShell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
               if (boldFont != null) {
                  boldFont.dispose();
               }

               if (popupMenu != null) {
                  popupMenu.dispose();
               }
            }
         });
         this.tipShell.addListener(21, new Listener() {
            public void handleEvent(Event event) {
               event.doit = false;
            }
         });
         this.tipShell.setLayout(WidgetFactory.createGridLayout(1, 10, 5, 0, 5, false));
         this.setColors(this.tipShell);
         Composite headerComposite = new Composite(this.tipShell, 0);
         headerComposite.setLayoutData(new GridData(768));
         headerComposite.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 3, 0, false));
         this.setColors(headerComposite);
         this.tipLabelImage = new Label(headerComposite, 0);
         this.tipLabelTitle = new Label(headerComposite, 0);
         this.tipLabelTitle.setLayoutData(new GridData(768));
         FontData[] fontData = this.tipLabelTitle.getFont().getFontData();

         for (int i = 0; i < fontData.length; i++) {
            fontData[i].setStyle(1);
         }

         this.boldFont = new Font(null, fontData);
         this.tipLabelTitle.setFont(this.boldFont);
         this.setColors(this.tipLabelImage);
         this.setColors(this.tipLabelTitle);
         this.tipLabelImage.setLayoutData(new GridData(1));
         this.createSeparator(this.tipShell);
         this.tipLabelText = new Label(this.tipShell, 0);
         this.setColors(this.tipLabelText);
         this.tipLabelText.setLayoutData(new GridData(772));
         this.namesComposite = new Composite(this.tipShell, 0);
         this.namesComposite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 5, false));
         this.namesComposite.setLayoutData(new GridData(768));
         this.setColors(this.namesComposite);
         this.namesSeparator = this.createSeparator(this.namesComposite);
         this.namesList = new List(this.namesComposite, 768);
         this.setColors(this.namesList);
         this.createSeparator(this.tipShell);
         if (!PreferenceLoader.loadBoolean("searchTooltipsOffset")) {
            Composite toolBarComposite = new Composite(this.tipShell, 0);
            toolBarComposite.setLayoutData(new GridData(768));
            toolBarComposite.setLayout(WidgetFactory.createGridLayout(3, 0, 0, 0, 0, false));
            toolBarComposite.setBackground(this.namesList.getDisplay().getSystemColor(29));
            ToolBar downloadToolBar = new ToolBar(toolBarComposite, 8388608);
            this.setColors(downloadToolBar);
            downloadToolBar.setLayoutData(new GridData(1));
            Composite spacerComposite = new Composite(toolBarComposite, 0);
            this.setColors(spacerComposite);
            GridData gridData = new GridData(1808);
            gridData.heightHint = 1;
            spacerComposite.setLayoutData(gridData);
            ToolItem downloadToolItem = new ToolItem(downloadToolBar, 0);
            downloadToolItem.setImage(SResources.getImage("down_arrow_green"));
            downloadToolItem.setToolTipText(SResources.getString("s.download"));
            downloadToolItem.addSelectionListener(new SelectionAdapter() {
               public void widgetSelected(SelectionEvent event) {
                  downloadSingleFile(result);
               }
            });
            this.toolBar = new ToolBar(toolBarComposite, 8388608);
            this.setColors(this.toolBar);
            this.toolBar.setLayoutData(new GridData(128));
            if (VersionInfo.useWebServices()) {
               this.addToolItem("bitzi", "mi.web.bitzi", 1);
               this.addToolItem("edonkey", "mi.web.filedonkey", 2);
               this.addToolItem("razorback2", "mi.web.razorback2", 5);
            }
         }

         this.popupMenu = new MenuManager("");
         this.popupMenu.setRemoveAllWhenShown(true);
         this.popupMenu.addMenuListener(this);
         this.tipLabelText.setMenu(this.popupMenu.createContextMenu(this.tipShell));
      }

      public CustomSeparator createSeparator(Composite composite) {
         CustomSeparator separator = new CustomSeparator(composite);
         separator.setLayoutData(new GridData(768));
         return separator;
      }

      public void setColors(Control control) {
         control.setBackground(control.getDisplay().getSystemColor(29));
         control.setForeground(control.getDisplay().getSystemColor(28));
      }

      public void addToolItem(String imageName, String tooltipKey, final int serviceId) {
         ToolItem toolItem = new ToolItem(this.toolBar, 0);
         toolItem.setImage(SResources.getImage(imageName));
         toolItem.setToolTipText(SResources.getString(tooltipKey));
         toolItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
               WebServicesAction.launch(serviceId, md4, ed2k, fileSize);
               setVisible(false);
            }
         });
      }

      public void startTimer() {
         this.timerTask = new ToolTipTimerTask(this);
         this.display.timerExec(1000, this.timerTask);
      }

      public void stopTimer() {
         if (this.timerTask != null) {
            this.timerTask.stop = true;
            this.timerTask = null;
         }
      }

      public void dispose() {
         if (this.tipShell != null) {
            this.tipShell.dispose();
            this.tipShell = null;
         }

         if (this.tipRegion != null && !this.tipRegion.isDisposed()) {
            this.tipRegion.dispose();
            this.tipRegion = null;
         }

         this.stopTimer();
      }

      public void activateHoverHelp(final Control control, final ICustomViewer cViewer) {
         control.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
               if (tipShell.isVisible()) {
                  setVisible(false);
               }
            }
         });
         control.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(MouseEvent event) {
               if (tipShell.isVisible() && pt.x != event.x && pt.y != event.y) {
                  setVisible(false);
               }

               tipWidget = null;
            }

            public void mouseHover(MouseEvent event) {
               if (event.widget != null && !event.widget.isDisposed()) {
                  pt.x = event.x;
                  pt.y = event.y;
                  tipPosition = control.toDisplay(pt);
                  Rectangle rect = tipShell.getBounds();
                  rect.x -= 10;
                  rect.y -= 10;
                  rect.width += 20;
                  rect.height += 20;
                  if (!tipShell.isVisible() || !rect.contains(tipPosition)) {
                     Widget widget = event.widget;
                     Table table = (Table)widget;
                     int columnStart = 0;
                     int columnEnd = 0;
                     boolean found = false;

                     for (int i = 0; i < table.getColumns().length; i++) {
                        if (cViewer.getColumnIDs()[i] == 1) {
                           columnEnd = columnStart + table.getColumns()[i].getWidth();
                           found = true;
                           break;
                        }

                        columnStart += table.getColumns()[i].getWidth();
                     }

                     ScrollBar scrollBar = table.getHorizontalBar();
                     int scroll = scrollBar != null ? scrollBar.getSelection() : 0;
                     columnStart -= scroll;
                     columnEnd -= scroll;
                     TableItem item = table.getItem(pt);
                     if (item == null) {
                        setVisible(false);
                        tipWidget = null;
                     }

                     if (item != tipWidget) {
                        tipWidget = item;
                        if (!found || columnStart < pt.x && pt.x < columnEnd) {
                           TableItem hoverItem = item;
                           result = (Result)hoverItem.getData();
                           if (result == null) {
                              setVisible(false);
                              tipWidget = null;
                           } else {
                              tipLabelImage.setImage(result.getFileTypeImage());
                              tipLabelTitle.setText(result.getName());
                              md4 = "";
                              ed2k = "";
                              md4 = result.getMD4().toUpperCase();
                              ed2k = result.getED2K();
                              fileSize = result.getSize();
                              tipLabelText.setText(result.getToolTipContent());
                              namesList.removeAll();
                              namesList.pack();
                              GridData gridData = new GridData();
                              gridData.heightHint = 0;
                              gridData.widthHint = 0;
                              namesSeparator.setLayoutData(gridData);
                              gridData = new GridData();
                              gridData.heightHint = 0;
                              gridData.widthHint = 0;
                              namesList.setLayoutData(gridData);
                              gridData = new GridData(768);
                              gridData.heightHint = 0;
                              gridData.widthHint = 0;
                              namesComposite.setLayoutData(gridData);
                              tipShell.pack();
                              String[] names = result.getNames();
                              if (names != null && names.length > 1) {
                                 int nameCount = result.getNames().length;
                                 nameCount = nameCount > 6 ? 6 : nameCount;
                                 String[] sortedNames = new String[names.length];
                                 System.arraycopy(names, 0, sortedNames, 0, names.length);
                                 Arrays.sort(sortedNames, String.CASE_INSENSITIVE_ORDER);
                                 gridData = new GridData(768);
                                 namesSeparator.setLayoutData(new GridData(768));
                                 namesComposite.setLayoutData(gridData);
                                 gridData = new GridData();
                                 gridData.heightHint = nameCount * charHeight;
                                 gridData.widthHint = tipShell.getBounds().width;
                                 namesList.setLayoutData(gridData);

                                 for (int i = 0; i < sortedNames.length; i++) {
                                    namesList.add(sortedNames[i]);
                                 }
                              } else {
                                 gridData = new GridData();
                                 gridData.heightHint = 0;
                                 gridData.widthHint = 0;
                                 namesSeparator.setLayoutData(gridData);
                                 gridData = new GridData();
                                 gridData.heightHint = 0;
                                 gridData.widthHint = 0;
                                 namesList.setLayoutData(gridData);
                                 gridData = new GridData(768);
                                 gridData.heightHint = 0;
                                 namesComposite.setLayoutData(gridData);
                              }

                              setHoverLocation(tipShell, tipPosition);
                              setVisible(true);
                           }
                        } else {
                           setVisible(false);
                        }
                     }
                  }
               }
            }
         });
      }

      private void setVisible(boolean visible) {
         this.tipShell.setVisible(visible);
         this.isVisible = visible;
         this.stopTimer();
         if (visible) {
            this.startTimer();
         } else {
            this.result = null;
         }
      }

      private void setHoverLocation(Shell shell, Point point) {
         this.vscroll = gView.getComposite().getVerticalBar().getSelection();
         Rectangle displayBounds = shell.getDisplay().getBounds();
         shell.pack();
         Rectangle bounds = shell.getBounds();
         int offset = PreferenceLoader.loadBoolean("searchTooltipsOffset") ? 10 : -5;
         bounds.x = Math.max(Math.min(point.x, displayBounds.width - bounds.width), 0) + offset;
         bounds.y = Math.max(Math.min(point.y, displayBounds.height - bounds.height), 0) + offset;
         shell.setLocation(bounds.x, bounds.y);
         Point size = shell.computeSize(-1, -1);
         this.roundRect = this.roundRect(0, 0, size.x - 1, size.y - 1);
         this.roundRectR = this.roundRect(0, 0, size.x, size.y);
         // setRegion() does not dispose the previously installed region, and this runs
         // on every hover/reposition, so dispose the old one to avoid leaking GDI
         // region handles over a long browsing session.
         if (this.tipRegion != null && !this.tipRegion.isDisposed()) {
            this.tipRegion.dispose();
         }

         Region region = new Region();
         region.add(this.roundRectR);
         shell.setRegion(region);
         this.tipRegion = region;
      }

      public void menuAboutToShow(IMenuManager menuManager) {
         menuManager.add(new CopyToolTipToClipboardAction());
      }

      // Tooltip context-menu action that copies the current result's tooltip text to the clipboard.
      public class CopyToolTipToClipboardAction extends Action {
         public CopyToolTipToClipboardAction() {
            this.setText(SResources.getString("copy to clipboard"));
            this.setImageDescriptor(SResources.getImageDescriptor("copy"));
         }

         public void run() {
            if (result != null) {
               MainWindow.copyToClipboard(result.getToolTip());
            }
         }
      }
   }

   // Thin custom-drawn horizontal separator line used inside the tooltip popup.
   private static final class CustomSeparator extends Canvas {
      private int lineWidth;

      public CustomSeparator(Composite composite) {
         super(composite, 0);
         this.lineWidth = 1;
         this.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
               onPaint(event);
            }
         });
      }

      public Point computeSize(int wHint, int hHint, boolean changed) {
         this.checkWidget();
         if (wHint == -1) {
            wHint = this.lineWidth;
         }

         if (hHint == -1) {
            hHint = this.lineWidth;
         }

         return new Point(wHint, hHint);
      }

      public boolean setFocus() {
         this.checkWidget();
         return false;
      }

      private void onPaint(PaintEvent event) {
         Rectangle rect = this.getClientArea();
         if (rect.width != 0 && rect.height != 0) {
            Display display = this.getDisplay();
            event.gc.setLineWidth(this.lineWidth);
            Color color = display.getSystemColor(28);
            event.gc.setForeground(color);
            event.gc.drawLine(0, 0, rect.width - 1, 0);
         }
      }
   }

   // Periodic display callback that hides the tooltip when the cursor moves away or the table scrolls.
   private class ToolTipTimerTask implements Runnable {
      ToolTipHandler toolTipHandler;
      boolean stop;
      Display display;

      public ToolTipTimerTask(ToolTipHandler toolTipHandler) {
         this.toolTipHandler = toolTipHandler;
         this.display = toolTipHandler.display;
      }

      public void run() {
         if (!this.stop && this.display != null && !this.display.isDisposed()) {
            this.display.timerExec(1000, this);
         }

         if (this.toolTipHandler.isVisible) {
            Point cursorLocation = this.toolTipHandler.display.getCursorLocation();
            if (gView != null
               && !gView.isDisposed()
               && gView.getComposite().getVerticalBar() != null) {
               int selection = gView.getComposite().getVerticalBar().getSelection();
               if (cursorLocation.equals(this.toolTipHandler.tipPosition) && selection == this.toolTipHandler.vscroll) {
                  return;
               }
            }

            Shell shell = this.toolTipHandler.tipShell;
            if (shell != null && !shell.isDisposed() && shell.isVisible() && !shell.getBounds().contains(cursorLocation)) {
               this.toolTipHandler.setVisible(false);
            }
         }
      }
   }
}
