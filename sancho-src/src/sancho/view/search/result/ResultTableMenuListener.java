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

   public ResultTableMenuListener(GTableView var1, CTabItem var2) {
      super(var1);
      this.cTabItem = var2;
   }

   public void initialize() {
      super.initialize();
      if (PreferenceLoader.loadBoolean("searchTooltips")) {
         final ToolTipHandler toolTipHandler = new ToolTipHandler(this.gView.getShell());
         toolTipHandler.activateHoverHelp(this.gView.getComposite(), (ICustomViewer)this.gView.getViewer());
         this.gView.getComposite().addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent var1) {
               toolTipHandler.dispose();
            }
         });
      }
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$Result == null
            ? (class$sancho$model$mldonkey$Result = class$("sancho.model.mldonkey.Result"))
            : class$sancho$model$mldonkey$Result
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         var1.add(new DownloadAction());
         var1.add(new ResultDetailAction());
         var1.add(new RemoveResultsAction());
         var1.add(new Separator());
         var1.add(new CopyNameAction());
         this.addClipboardMenu(var1);
         var1.add(new Separator());
         Result var2 = (Result)this.selectedObjects.get(0);
         this.addWebServicesMenu(var1, var2.getMD4(), var2.getED2K(), var2.getSize());
         this.addSelectAllMenu(var1);
      }
   }

   public void removeSelected() {
      if (this.selectedObjects.size() != 0) {
         ObjectMap var1 = (ObjectMap)this.gView.getViewer().getInput();
         if (var1 != null) {
            var1.remove(this.selectedObjects.toArray());
         }
      }
   }

   protected void onDeleteKey() {
      this.removeSelected();
   }

   public boolean downloadResult(Result var1) {
      if (!Sancho.hasCollectionFactory()) {
         return true;
      } else {
         boolean var2 = PreferenceLoader.loadBoolean("searchForceDownload");
         if (var1.downloaded() && !var2) {
            Shell var3 = this.tableViewer.getTable().getShell();
            MessageBox var4 = new MessageBox(var3, 194);
            var4.setText(SResources.getString("s.alreadyDownloadedTitle"));
            var4.setMessage(var1.getName() + "\n" + SResources.getString("s.alreadyDownloadedText"));
            if (var4.open() == 64) {
               this.gView.getCore().getResultCollection().download(var1, true);
               this.updateItem(5000, var1);
               return true;
            } else {
               return false;
            }
         } else {
            this.gView.getCore().getResultCollection().download(var1, var2);
            this.updateItem(5000, var1);
            return true;
         }
      }
   }

   protected void updateItem(int var1, final Result var2) {
      this.tableViewer.getTable().getDisplay().timerExec(var1, new Runnable() {
         public void run() {
            if (tableViewer != null && tableViewer.getTable() != null && !tableViewer.getTable().isDisposed()) {
               tableViewer.update(new Object[]{var2}, null);
            }
         }
      });
   }

   public void downloadSingleFile(Result var1) {
      this.downloadResult(var1);
      this.postDownloadStats(1, "");
   }

   public void downloadSelected() {
      if (Sancho.hasCollectionFactory()) {
         String var1 = "";
         int var2 = 0;

         for (int var3 = 0; var3 < this.selectedObjects.size(); var3++) {
            Result var4 = (Result)this.selectedObjects.get(var3);
            if (this.downloadResult(var4)) {
               var2++;
            } else {
               var1 = var1 + var4.getName() + "\n";
            }
         }

         this.postDownloadStats(var2, var1);
      }
   }

   public void postDownloadStats(int var1, String var2) {
      SearchTab var3 = (SearchTab)this.cTabItem.getParent().getData();
      var3.getMainWindow().getStatusline().setText(SResources.getString("s.sl.startedDownload") + var1);
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
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
         Result var1 = (Result)selectedObjects.get(0);
         if (var1 instanceof Result) {
            new ResultDetailDialog(gView.getShell(), var1).open();
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
         String var1 = "";
         String var2 = System.getProperty("line.separator");

         for (int var3 = 0; var3 < selectedObjects.size(); var3++) {
            Result var4 = (Result)selectedObjects.get(var3);
            if (var1.length() > 0) {
               var1 = var1 + var2;
            }

            var1 = var1 + var4.getName();
         }

         MainWindow.copyToClipboard(var1);
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

      int[] roundRect(int var1, int var2, int var3, int var4) {
         int[] var5 = new int[]{0, 6, 1, 5, 1, 4, 4, 1, 5, 1, 6, 0};
         int[] var6 = new int[]{-6, 0, -5, 1, -4, 1, -1, 4, -1, 5, 0, 6};
         int[] var7 = new int[]{0, -6, -1, -5, -1, -4, -4, -1, -5, -1, -6, 0};
         int[] var8 = new int[]{6, 0, 5, -1, 4, -1, 1, -4, 1, -5, 0, -6};
         int[] var9 = new int[48];
         int var10 = 0;
         int[] var11 = var5;
         int[] var12 = var6;

         for (int var13 = 0; var13 < var11.length / 2; var13++) {
            var9[var10++] = var1 + var11[2 * var13];
            var9[var10++] = var2 + var11[2 * var13 + 1];
         }

         for (int var14 = 0; var14 < var12.length / 2; var14++) {
            var9[var10++] = var1 + var3 + var12[2 * var14];
            var9[var10++] = var2 + var12[2 * var14 + 1];
         }

         var11 = var7;
         var12 = var8;

         for (int var15 = 0; var15 < var11.length / 2; var15++) {
            var9[var10++] = var1 + var11[2 * var15] + var3;
            var9[var10++] = var2 + var11[2 * var15 + 1] + var4;
         }

         for (int var16 = 0; var16 < var12.length / 2; var16++) {
            var9[var10++] = var1 + var12[2 * var16];
            var9[var10++] = var2 + var12[2 * var16 + 1] + var4;
         }

         return var9;
      }

      public ToolTipHandler(Composite var2) {
         this.pt = new Point(0, 0);
         GC var3 = new GC(var2);
         this.charHeight = var3.getFontMetrics().getHeight();
         var3.dispose();
         this.display = var2.getDisplay();
         this.tipShell = new Shell(this.display, 278540);
         this.tipShell.setBackground(this.display.getSystemColor(29));
         this.tipShell.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent var1) {
               if (roundRect != null) {
                  var1.gc.setBackground(tipShell.getDisplay().getSystemColor(29));
                  var1.gc.setForeground(tipShell.getDisplay().getSystemColor(28));
                  var1.gc.fillPolygon(roundRect);
                  var1.gc.drawPolygon(roundRect);
               }
            }
         });
         this.tipShell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent var1) {
               if (boldFont != null) {
                  boldFont.dispose();
               }

               if (popupMenu != null) {
                  popupMenu.dispose();
               }
            }
         });
         this.tipShell.addListener(21, new Listener() {
            public void handleEvent(Event var1) {
               var1.doit = false;
            }
         });
         this.tipShell.setLayout(WidgetFactory.createGridLayout(1, 10, 5, 0, 5, false));
         this.setColors(this.tipShell);
         Composite var4 = new Composite(this.tipShell, 0);
         var4.setLayoutData(new GridData(768));
         var4.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 3, 0, false));
         this.setColors(var4);
         this.tipLabelImage = new Label(var4, 0);
         this.tipLabelTitle = new Label(var4, 0);
         this.tipLabelTitle.setLayoutData(new GridData(768));
         FontData[] var5 = this.tipLabelTitle.getFont().getFontData();

         for (int var6 = 0; var6 < var5.length; var6++) {
            var5[var6].setStyle(1);
         }

         this.boldFont = new Font(null, var5);
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
            Composite var7 = new Composite(this.tipShell, 0);
            var7.setLayoutData(new GridData(768));
            var7.setLayout(WidgetFactory.createGridLayout(3, 0, 0, 0, 0, false));
            var7.setBackground(this.namesList.getDisplay().getSystemColor(29));
            ToolBar var8 = new ToolBar(var7, 8388608);
            this.setColors(var8);
            var8.setLayoutData(new GridData(1));
            Composite var9 = new Composite(var7, 0);
            this.setColors(var9);
            GridData var10 = new GridData(1808);
            var10.heightHint = 1;
            var9.setLayoutData(var10);
            ToolItem var11 = new ToolItem(var8, 0);
            var11.setImage(SResources.getImage("down_arrow_green"));
            var11.setToolTipText(SResources.getString("s.download"));
            var11.addSelectionListener(new SelectionAdapter() {
               public void widgetSelected(SelectionEvent var1) {
                  downloadSingleFile(result);
               }
            });
            this.toolBar = new ToolBar(var7, 8388608);
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

      public CustomSeparator createSeparator(Composite var1) {
         CustomSeparator var2 = new CustomSeparator(var1);
         var2.setLayoutData(new GridData(768));
         return var2;
      }

      public void setColors(Control var1) {
         var1.setBackground(var1.getDisplay().getSystemColor(29));
         var1.setForeground(var1.getDisplay().getSystemColor(28));
      }

      public void addToolItem(String var1, String var2, final int var3) {
         ToolItem var4 = new ToolItem(this.toolBar, 0);
         var4.setImage(SResources.getImage(var1));
         var4.setToolTipText(SResources.getString(var2));
         var4.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent arg0) {
               WebServicesAction.launch(var3, md4, ed2k, fileSize);
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
            public void mouseDown(MouseEvent var1) {
               if (tipShell.isVisible()) {
                  setVisible(false);
               }
            }
         });
         control.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(MouseEvent var1) {
               if (tipShell.isVisible() && pt.x != var1.x && pt.y != var1.y) {
                  setVisible(false);
               }

               tipWidget = null;
            }

            public void mouseHover(MouseEvent var1) {
               if (var1.widget != null && !var1.widget.isDisposed()) {
                  pt.x = var1.x;
                  pt.y = var1.y;
                  tipPosition = control.toDisplay(pt);
                  Rectangle var2 = tipShell.getBounds();
                  var2.x -= 10;
                  var2.y -= 10;
                  var2.width += 20;
                  var2.height += 20;
                  if (!tipShell.isVisible() || !var2.contains(tipPosition)) {
                     Widget var3 = var1.widget;
                     Table var4 = (Table)var3;
                     int var5 = 0;
                     int var6 = 0;
                     boolean var7 = false;

                     for (int var8 = 0; var8 < var4.getColumns().length; var8++) {
                        if (cViewer.getColumnIDs()[var8] == 1) {
                           var6 = var5 + var4.getColumns()[var8].getWidth();
                           var7 = true;
                           break;
                        }

                        var5 += var4.getColumns()[var8].getWidth();
                     }

                     ScrollBar var9 = var4.getHorizontalBar();
                     int var10 = var9 != null ? var9.getSelection() : 0;
                     var5 -= var10;
                     var6 -= var10;
                     TableItem var17 = var4.getItem(pt);
                     if (var17 == null) {
                        setVisible(false);
                        tipWidget = null;
                     }

                     if (var17 != tipWidget) {
                        tipWidget = var17;
                        if (!var7 || var5 < pt.x && pt.x < var6) {
                           TableItem var11 = var17;
                           result = (Result)var11.getData();
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
                              GridData var12 = new GridData();
                              var12.heightHint = 0;
                              var12.widthHint = 0;
                              namesSeparator.setLayoutData(var12);
                              var12 = new GridData();
                              var12.heightHint = 0;
                              var12.widthHint = 0;
                              namesList.setLayoutData(var12);
                              var12 = new GridData(768);
                              var12.heightHint = 0;
                              var12.widthHint = 0;
                              namesComposite.setLayoutData(var12);
                              tipShell.pack();
                              String[] var13 = result.getNames();
                              if (var13 != null && var13.length > 1) {
                                 int var14 = result.getNames().length;
                                 var14 = var14 > 6 ? 6 : var14;
                                 String[] var15 = new String[var13.length];
                                 System.arraycopy(var13, 0, var15, 0, var13.length);
                                 Arrays.sort(var15, String.CASE_INSENSITIVE_ORDER);
                                 var12 = new GridData(768);
                                 namesSeparator.setLayoutData(new GridData(768));
                                 namesComposite.setLayoutData(var12);
                                 var12 = new GridData();
                                 var12.heightHint = var14 * charHeight;
                                 var12.widthHint = tipShell.getBounds().width;
                                 namesList.setLayoutData(var12);

                                 for (int var16 = 0; var16 < var15.length; var16++) {
                                    namesList.add(var15[var16]);
                                 }
                              } else {
                                 var12 = new GridData();
                                 var12.heightHint = 0;
                                 var12.widthHint = 0;
                                 namesSeparator.setLayoutData(var12);
                                 var12 = new GridData();
                                 var12.heightHint = 0;
                                 var12.widthHint = 0;
                                 namesList.setLayoutData(var12);
                                 var12 = new GridData(768);
                                 var12.heightHint = 0;
                                 namesComposite.setLayoutData(var12);
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

      private void setVisible(boolean var1) {
         this.tipShell.setVisible(var1);
         this.isVisible = var1;
         this.stopTimer();
         if (var1) {
            this.startTimer();
         } else {
            this.result = null;
         }
      }

      private void setHoverLocation(Shell var1, Point var2) {
         this.vscroll = gView.getComposite().getVerticalBar().getSelection();
         Rectangle var3 = var1.getDisplay().getBounds();
         var1.pack();
         Rectangle var4 = var1.getBounds();
         int var5 = PreferenceLoader.loadBoolean("searchTooltipsOffset") ? 10 : -5;
         var4.x = Math.max(Math.min(var2.x, var3.width - var4.width), 0) + var5;
         var4.y = Math.max(Math.min(var2.y, var3.height - var4.height), 0) + var5;
         var1.setLocation(var4.x, var4.y);
         Point var6 = var1.computeSize(-1, -1);
         this.roundRect = this.roundRect(0, 0, var6.x - 1, var6.y - 1);
         this.roundRectR = this.roundRect(0, 0, var6.x, var6.y);
         // setRegion() does not dispose the previously installed region, and this runs
         // on every hover/reposition, so dispose the old one to avoid leaking GDI
         // region handles over a long browsing session.
         if (this.tipRegion != null && !this.tipRegion.isDisposed()) {
            this.tipRegion.dispose();
         }

         Region var7 = new Region();
         var7.add(this.roundRectR);
         var1.setRegion(var7);
         this.tipRegion = var7;
      }

      public void menuAboutToShow(IMenuManager var1) {
         var1.add(new CopyToolTipToClipboardAction());
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

      public CustomSeparator(Composite var2) {
         super(var2, 0);
         this.lineWidth = 1;
         this.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent var1) {
               onPaint(var1);
            }
         });
      }

      public Point computeSize(int var1, int var2, boolean var3) {
         this.checkWidget();
         if (var1 == -1) {
            var1 = this.lineWidth;
         }

         if (var2 == -1) {
            var2 = this.lineWidth;
         }

         return new Point(var1, var2);
      }

      public boolean setFocus() {
         this.checkWidget();
         return false;
      }

      private void onPaint(PaintEvent var1) {
         Rectangle var2 = this.getClientArea();
         if (var2.width != 0 && var2.height != 0) {
            Display var3 = this.getDisplay();
            var1.gc.setLineWidth(this.lineWidth);
            Color var4 = var3.getSystemColor(28);
            var1.gc.setForeground(var4);
            var1.gc.drawLine(0, 0, var2.width - 1, 0);
         }
      }
   }

   // Periodic display callback that hides the tooltip when the cursor moves away or the table scrolls.
   private class ToolTipTimerTask implements Runnable {
      ToolTipHandler toolTipHandler;
      boolean stop;
      Display display;

      public ToolTipTimerTask(ToolTipHandler var2) {
         this.toolTipHandler = var2;
         this.display = var2.display;
      }

      public void run() {
         if (!this.stop && this.display != null && !this.display.isDisposed()) {
            this.display.timerExec(1000, this);
         }

         if (this.toolTipHandler.isVisible) {
            Point var1 = this.toolTipHandler.display.getCursorLocation();
            if (gView != null
               && !gView.isDisposed()
               && gView.getComposite().getVerticalBar() != null) {
               int var2 = gView.getComposite().getVerticalBar().getSelection();
               if (var1.equals(this.toolTipHandler.tipPosition) && var2 == this.toolTipHandler.vscroll) {
                  return;
               }
            }

            Shell var3 = this.toolTipHandler.tipShell;
            if (var3 != null && !var3.isDisposed() && var3.isVisible() && !var3.getBounds().contains(var1)) {
               this.toolTipHandler.setVisible(false);
            }
         }
      }
   }
}
