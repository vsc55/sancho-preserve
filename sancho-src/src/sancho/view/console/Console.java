package sancho.view.console;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.core.Sancho;
import sancho.view.MainWindow;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.WebLauncher;
import sancho.view.utility.WidgetFactory;

public class Console {
   static Pattern regex;
   protected List commandHistory = new ArrayList();
   protected Composite composite;
   protected Color highlightColor;
   protected int historyMark = 0;
   protected StyledText infoDisplay;
   protected Text input;
   protected final int MAX_LINES = PreferenceLoader.loadInt("consoleMaxLines");
   protected Cursor handCursor;
   protected boolean usingHand;
   protected boolean underlineURLs;

   public Console(Composite var1, int var2) {
      this.createContents(var1, var2);
      this.handCursor = new Cursor(this.infoDisplay.getDisplay(), 21);
      // Shows the hand cursor while the pointer hovers over an underlined URL.
      this.infoDisplay.addMouseMoveListener(new MouseMoveListener() {
         public void mouseMove(MouseEvent var1) {
            StyledText var2 = (StyledText)var1.widget;

            try {
               int var3 = var2.getOffsetAtLocation(new Point(var1.x, var1.y));
               StyleRange var4 = var2.getStyleRangeAtOffset(var3);
               if (var4 == null || !var4.underline) {
                  disableHand();
               } else if (!usingHand) {
                  var2.setCursor(handCursor);
                  usingHand = true;
               }
            } catch (IllegalArgumentException var5) {
               disableHand();
            }
         }
      });
      // Clears the hand cursor once the pointer leaves the display.
      this.infoDisplay.addMouseTrackListener(new MouseTrackAdapter() {
         public void mouseExit(MouseEvent var1) {
            disableHand();
         }
      });
      // Opens the URL under the pointer on a stationary left click.
      this.infoDisplay.addMouseListener(new MouseAdapter() {
         int x;
         int y;

         public void mouseDown(MouseEvent var1) {
            this.x = var1.x;
            this.y = var1.y;
         }

         public void mouseUp(MouseEvent var1) {
            if (var1.x == this.x) {
               if (var1.y == this.y) {
                  if (var1.button == 1) {
                     StyledText var2 = (StyledText)var1.widget;
                     int var3 = -1;

                     try {
                        var3 = var2.getOffsetAtLocation(new Point(var1.x, var1.y));
                        int var4 = var2.getLineAtOffset(var3);
                        String var5 = var2.getContent().getLine(var4);
                        int var6 = var2.getOffsetAtLine(var4);
                        StyleRange[] var7 = var2.getStyleRanges(var6, var5.length());
                        if (var7 != null) {
                           for (int var8 = 0; var8 < var7.length; var8++) {
                              if (var7[var8].underline) {
                                 int var9 = var7[var8].start;
                                 int var10 = var9 + var7[var8].length;
                                 if (var3 >= var9 && var3 <= var10) {
                                    String var11 = var2.getTextRange(var9, var7[var8].length);
                                    if (!var11.toLowerCase().startsWith("http://")) {
                                       var11 = "http://" + var11;
                                    }

                                    WebLauncher.openLink(var11);
                                 }
                              }
                           }
                        }
                     } catch (IllegalArgumentException var12) {
                     } catch (Exception var13) {
                     }
                  }
               }
            }
         }
      });
   }

   protected void disableHand() {
      if (this.usingHand) {
         this.infoDisplay.setCursor(null);
         this.usingHand = false;
      }
   }

   public void addMenuItem(Menu var1, String var2, String var3, SelectionAdapter var4) {
      MenuItem var5 = new MenuItem(var1, 8);
      var5.setText(SResources.getString(var2));
      var5.setImage(SResources.getImage(var3));
      var5.addSelectionListener(var4);
   }

   public void append(String var1) {
      int var2;
      if ((var2 = this.infoDisplay.getLineCount()) > this.MAX_LINES) {
         this.infoDisplay.replaceTextRange(0, this.infoDisplay.getOffsetAtLine(var2 - this.MAX_LINES + 5), "");
      }

      this.infoDisplay.setCaretOffset(this.infoDisplay.getText().length());
      this.infoDisplay.append(var1);
      if (this.underlineURLs) {
         int var3 = this.infoDisplay.getCharCount();
         var3 -= var1.length();
         Matcher var4 = regex.matcher(var1);
         while (var4.find()) {
            String var6 = var4.group();
            int var7 = var4.start();
            int var8 = var4.end();
            if (var6.startsWith("\"")) {
                  var7++;
               }

               if (var6.endsWith("\"")) {
                  var8--;
               } else if (var6.endsWith(".")) {
                  var8--;
               } else if (var6.endsWith(",")) {
                  var8--;
               } else if (var6.endsWith("?")) {
                  var8--;
               }

               int var9 = var8 - var7;
               if (var9 > 1) {
                  StyleRange var10 = new StyleRange();
                  var10.start = var3 + var7;
                  var10.length = var9;
                  var10.underline = true;
                  this.infoDisplay.setStyleRange(var10);
               }
            }
      }

      this.infoDisplay.setCaretOffset(this.infoDisplay.getCaretOffset() + var1.length() + 1);
      this.infoDisplay.showSelection();
   }

   protected void updateForeground(int var1, int var2, Color var3) {
      StyleRange[] var4 = this.infoDisplay.getStyleRanges(var1, var2);
      if (var4 == null) {
         StyleRange var5 = new StyleRange();
         var5.start = var1;
         var5.length = var2;
         var5.foreground = var3;
         this.infoDisplay.setStyleRange(var5);
      } else {
         int var9 = var1;

         for (int var6 = 0; var6 < var4.length; var6++) {
            int var7 = var4[var6].start;
            if (var9 < var7) {
               StyleRange var8 = new StyleRange();
               var8.start = var9;
               var8.length = var7 - var9;
               var8.foreground = var3;
               this.infoDisplay.setStyleRange(var8);
            }

            var9 = var7 + var4[var6].length;
            var4[var6].foreground = var3;
            this.infoDisplay.setStyleRange(var4[var6]);
         }

         if (var9 < var1 + var2) {
            StyleRange var10 = new StyleRange();
            var10.start = var9;
            var10.length = var1 + var2 - var9;
            var10.foreground = var3;
            this.infoDisplay.setStyleRange(var10);
         }
      }
   }

   public void appendInput() {
      this.prefixAppend();
      String var1 = this.input.getText();
      this.appendNewLine(var1);
      int var2 = this.infoDisplay.getCharCount() - var1.length() - this.getLineDelimiter().length();
      this.updateForeground(var2, var1.length(), this.highlightColor);
   }

   public void appendNewLine(String var1) {
      if (!this.infoDisplay.isDisposed()) {
         this.append(var1 + this.infoDisplay.getLineDelimiter());
      }
   }

   protected void createContents(Composite var1, int var2) {
      this.composite = new Composite(var1, 0);
      this.composite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      this.infoDisplay = new StyledText(this.composite, var2 | 2 | 256 | 512 | 8);
      this.infoDisplay.setLayoutData(new GridData(1808));
      this.infoDisplay.setIndent(2);
      // Ctrl+F opens the incremental find dialog for the output area.
      this.infoDisplay.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            if (var1.stateMask == SWT.MOD1 && var1.character == 6) {
               new FindDialog().open();
            }
         }
      });
      Menu var3 = new Menu(this.infoDisplay);
      this.addMenuItem(var3, "mi.copy", "copy", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            MainWindow.copyToClipboard(infoDisplay.getSelectionText());
         }
      });
      this.addMenuItem(var3, "mi.selectAll", "plus", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            infoDisplay.selectAll();
         }
      });
      this.addMenuItem(var3, "mi.clear", "clear", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            infoDisplay.replaceTextRange(0, infoDisplay.getText().length(), "");
         }
      });
      this.addMenuItem(var3, "l.find", "refine", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            new FindDialog().open();
         }
      });
      this.infoDisplay.setMenu(var3);
      this.input = new Text(this.composite, 2052);
      this.input.setLayoutData(new GridData(768));
      // Input line: history navigation, find/clear shortcuts, paging and submit.
      this.input.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            int var2 = infoDisplay.getClientArea().height / infoDisplay.getLineHeight();
            if ((var1.stateMask & SWT.MOD1) != 0 && var1.keyCode == 102) {
               new FindDialog().open();
            } else if ((var1.stateMask & SWT.MOD1) != 0 && var1.keyCode == 108) {
               infoDisplay.replaceTextRange(0, infoDisplay.getText().length(), "");
            } else {
               switch (var1.keyCode) {
                  case 13:
                  case 16777296:
                     appendInput();
                     sendMessage();
                     if (input.getText().length() > 0) {
                        if (commandHistory.contains(input.getText())) {
                           commandHistory.remove(input.getText());
                        }

                        commandHistory.add(input.getText());
                        historyMark = commandHistory.size() - 1;
                     }

                     input.setText("");
                     break;
                  case 27:
                     input.setText("");
                     break;
                  case 16777217:
                     if (commandHistory.size() > 0) {
                        if (historyMark < 0 || historyMark >= commandHistory.size()) {
                           historyMark = commandHistory.size() - 1;
                        }

                        input.setText((String)commandHistory.get(historyMark--));
                        input.setSelection(input.getText().length());
                        var1.doit = false;
                     }
                     break;
                  case 16777218:
                     if (commandHistory.size() > 0) {
                        if (historyMark >= commandHistory.size() || historyMark < 0) {
                           historyMark = 0;
                        }

                        input.setText((String)commandHistory.get(historyMark++));
                        input.setSelection(input.getText().length());
                     }
                     break;
                  case 16777221:
                     if (infoDisplay.getTopIndex() > var2) {
                        infoDisplay.setTopIndex(infoDisplay.getTopIndex() - var2);
                     } else {
                        infoDisplay.setTopIndex(0);
                     }
                     break;
                  case 16777222:
                     infoDisplay.setTopIndex(infoDisplay.getTopIndex() + var2);
               }
            }
         }
      });
      this.updateDisplay();
   }

   public void dispose() {
      this.input.dispose();
      this.infoDisplay.dispose();
      this.composite.dispose();
      // handCursor was created in the constructor but never disposed, leaking one
      // OS cursor handle per Console instance (status/message/IRC/exec consoles).
      if (this.handCursor != null && !this.handCursor.isDisposed()) {
         this.handCursor.dispose();
      }
   }

   public boolean isDisposed() {
      return this.infoDisplay == null || this.infoDisplay.isDisposed();
   }

   public Composite getComposite() {
      return this.composite;
   }

   public String getLineDelimiter() {
      return this.infoDisplay.getLineDelimiter();
   }

   public void prefixAppend() {
   }

   public void sendMessage() {
      Sancho.send((short)29, this.input.getText());
   }

   public void setFocus() {
      this.input.setFocus();
   }

   public void setActive() {
      this.input.setEnabled(true);
      this.infoDisplay.setEnabled(true);
   }

   public void setInactive() {
      this.input.setEnabled(false);
      this.infoDisplay.setEnabled(false);
   }

   public void updateDisplay() {
      this.infoDisplay.setFont(PreferenceLoader.loadFont("consoleFontData"));
      this.input.setFont(PreferenceLoader.loadFont("consoleFontData"));
      this.highlightColor = PreferenceLoader.loadColor("consoleHighlight");
      this.infoDisplay.setBackground(PreferenceLoader.loadColor("consoleBackground"));
      this.infoDisplay.setForeground(PreferenceLoader.loadColor("consoleForeground"));
      this.input.setBackground(PreferenceLoader.loadColor("consoleInputBackground"));
      this.input.setForeground(PreferenceLoader.loadColor("consoleInputForeground"));
      this.underlineURLs = PreferenceLoader.loadBoolean("consoleUnderlineURLs");
   }

   // Modal dialog that incrementally searches the console output text.
   private class FindDialog extends Dialog {
      Text text;
      Label infoLabel;
      String lastFind;
      int lastPos;

      public FindDialog() {
         super(infoDisplay.getShell());
         this.lastFind = "";
      }

      protected void configureShell(Shell var1) {
         super.configureShell(var1);
         var1.setImage(SResources.getImage("refine"));
         var1.setText(SResources.getString("l.find"));
      }

      protected void createButtonsForButtonBar(Composite var1) {
         this.createButton(var1, 0, SResources.getString("l.find"), true);
         this.createButton(var1, 1, SResources.getString("b.cancel"), false);
      }

      protected Control createDialogArea(Composite var1) {
         Composite var2 = (Composite)super.createDialogArea(var1);
         var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         Label var3 = new Label(var2, 0);
         var3.setText(SResources.getString("l.find") + ": ");
         var3.setLayoutData(new GridData(32));
         this.text = new Text(var2, 2052);
         this.text.setLayoutData(new GridData(768));
         // Enter in the find field triggers the next search.
         this.text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent var1) {
               switch (var1.keyCode) {
                  case 13:
                  case 16777296:
                     find();
               }
            }
         });
         this.infoLabel = new Label(var2, 0);
         GridData var4 = new GridData(768);
         var4.horizontalSpan = 2;
         this.infoLabel.setLayoutData(var4);
         return var2;
      }

      protected void find() {
         if (this.text.getText().length() > 0) {
            String var1 = this.text.getText().toLowerCase();
            if (!this.lastFind.equals(var1)) {
               this.lastPos = 0;
            }

            this.lastFind = var1;
            String var2 = infoDisplay.getText().toLowerCase();
            int var3 = var2.indexOf(var1, this.lastPos);
            if (var3 != -1) {
               this.lastPos = var3 + 1;
               infoDisplay.setSelection(var3, var3 + var1.length());
               this.infoLabel.setText("");
            } else {
               this.infoLabel.setText(SResources.getString("l.stringNotFound"));
               this.lastPos = 0;
            }

            this.text.setFocus();
         }
      }

      protected void buttonPressed(int var1) {
         if (var1 == 0) {
            this.find();
         } else {
            super.buttonPressed(var1);
         }
      }
   }

   static {
      try {
         regex = Pattern.compile(
            "(\"http://.+?\")|(http://[^\\s]+)|(www.[^\\s]+)|([\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3}[:][\\d]{1,4})|([\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3})",
            Pattern.CASE_INSENSITIVE
         );
      } catch (PatternSyntaxException var1) {
      }
   }
}
