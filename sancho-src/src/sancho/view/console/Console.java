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

   public Console(Composite parent, int style) {
      this.createContents(parent, style);
      this.handCursor = new Cursor(this.infoDisplay.getDisplay(), 21);
      // Shows the hand cursor while the pointer hovers over an underlined URL.
      this.infoDisplay.addMouseMoveListener(new MouseMoveListener() {
         public void mouseMove(MouseEvent event) {
            StyledText styledText = (StyledText)event.widget;

            try {
               int offset = styledText.getOffsetAtLocation(new Point(event.x, event.y));
               StyleRange styleRange = styledText.getStyleRangeAtOffset(offset);
               if (styleRange == null || !styleRange.underline) {
                  disableHand();
               } else if (!usingHand) {
                  styledText.setCursor(handCursor);
                  usingHand = true;
               }
            } catch (IllegalArgumentException illegalArgument) {
               disableHand();
            }
         }
      });
      // Clears the hand cursor once the pointer leaves the display.
      this.infoDisplay.addMouseTrackListener(new MouseTrackAdapter() {
         public void mouseExit(MouseEvent event) {
            disableHand();
         }
      });
      // Opens the URL under the pointer on a stationary left click.
      this.infoDisplay.addMouseListener(new MouseAdapter() {
         int x;
         int y;

         public void mouseDown(MouseEvent event) {
            this.x = event.x;
            this.y = event.y;
         }

         public void mouseUp(MouseEvent event) {
            if (event.x == this.x) {
               if (event.y == this.y) {
                  if (event.button == 1) {
                     StyledText styledText = (StyledText)event.widget;
                     int offset = -1;

                     try {
                        offset = styledText.getOffsetAtLocation(new Point(event.x, event.y));
                        int line = styledText.getLineAtOffset(offset);
                        String lineText = styledText.getContent().getLine(line);
                        int lineOffset = styledText.getOffsetAtLine(line);
                        StyleRange[] styleRanges = styledText.getStyleRanges(lineOffset, lineText.length());
                        if (styleRanges != null) {
                           for (int i = 0; i < styleRanges.length; i++) {
                              if (styleRanges[i].underline) {
                                 int start = styleRanges[i].start;
                                 int end = start + styleRanges[i].length;
                                 if (offset >= start && offset <= end) {
                                    String url = styledText.getTextRange(start, styleRanges[i].length);
                                    if (!url.toLowerCase().startsWith("http://")) {
                                       url = "http://" + url;
                                    }

                                    WebLauncher.openLink(url);
                                 }
                              }
                           }
                        }
                     } catch (IllegalArgumentException illegalArgument) {
                     } catch (Exception exception) {
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

   public void addMenuItem(Menu menu, String labelKey, String imageKey, SelectionAdapter listener) {
      MenuItem menuItem = new MenuItem(menu, 8);
      menuItem.setText(SResources.getString(labelKey));
      menuItem.setImage(SResources.getImage(imageKey));
      menuItem.addSelectionListener(listener);
   }

   public void append(String text) {
      int lineCount;
      if ((lineCount = this.infoDisplay.getLineCount()) > this.MAX_LINES) {
         this.infoDisplay.replaceTextRange(0, this.infoDisplay.getOffsetAtLine(lineCount - this.MAX_LINES + 5), "");
      }

      this.infoDisplay.setCaretOffset(this.infoDisplay.getText().length());
      this.infoDisplay.append(text);
      if (this.underlineURLs) {
         int base = this.infoDisplay.getCharCount();
         base -= text.length();
         Matcher matcher = regex.matcher(text);
         while (matcher.find()) {
            String match = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            if (match.startsWith("\"")) {
                  start++;
               }

               if (match.endsWith("\"")) {
                  end--;
               } else if (match.endsWith(".")) {
                  end--;
               } else if (match.endsWith(",")) {
                  end--;
               } else if (match.endsWith("?")) {
                  end--;
               }

               int length = end - start;
               if (length > 1) {
                  StyleRange styleRange = new StyleRange();
                  styleRange.start = base + start;
                  styleRange.length = length;
                  styleRange.underline = true;
                  this.infoDisplay.setStyleRange(styleRange);
               }
            }
      }

      this.infoDisplay.setCaretOffset(this.infoDisplay.getCaretOffset() + text.length() + 1);
      this.infoDisplay.showSelection();
   }

   protected void updateForeground(int start, int length, Color color) {
      StyleRange[] existing = this.infoDisplay.getStyleRanges(start, length);
      if (existing == null) {
         StyleRange styleRange = new StyleRange();
         styleRange.start = start;
         styleRange.length = length;
         styleRange.foreground = color;
         this.infoDisplay.setStyleRange(styleRange);
      } else {
         int cursor = start;

         for (int i = 0; i < existing.length; i++) {
            int rangeStart = existing[i].start;
            if (cursor < rangeStart) {
               StyleRange gapRange = new StyleRange();
               gapRange.start = cursor;
               gapRange.length = rangeStart - cursor;
               gapRange.foreground = color;
               this.infoDisplay.setStyleRange(gapRange);
            }

            cursor = rangeStart + existing[i].length;
            existing[i].foreground = color;
            this.infoDisplay.setStyleRange(existing[i]);
         }

         if (cursor < start + length) {
            StyleRange tailRange = new StyleRange();
            tailRange.start = cursor;
            tailRange.length = start + length - cursor;
            tailRange.foreground = color;
            this.infoDisplay.setStyleRange(tailRange);
         }
      }
   }

   public void appendInput() {
      this.prefixAppend();
      String text = this.input.getText();
      this.appendNewLine(text);
      int offset = this.infoDisplay.getCharCount() - text.length() - this.getLineDelimiter().length();
      this.updateForeground(offset, text.length(), this.highlightColor);
   }

   public void appendNewLine(String text) {
      if (!this.infoDisplay.isDisposed()) {
         this.append(text + this.infoDisplay.getLineDelimiter());
      }
   }

   protected void createContents(Composite parent, int style) {
      this.composite = new Composite(parent, 0);
      this.composite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      this.infoDisplay = new StyledText(this.composite, style | 2 | 256 | 512 | 8);
      this.infoDisplay.setLayoutData(new GridData(1808));
      this.infoDisplay.setIndent(2);
      // Ctrl+F opens the incremental find dialog for the output area.
      this.infoDisplay.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent event) {
            if (event.stateMask == SWT.MOD1 && event.character == 6) {
               new FindDialog().open();
            }
         }
      });
      Menu menu = new Menu(this.infoDisplay);
      this.addMenuItem(menu, "mi.copy", "copy", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            MainWindow.copyToClipboard(infoDisplay.getSelectionText());
         }
      });
      this.addMenuItem(menu, "mi.selectAll", "plus", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            infoDisplay.selectAll();
         }
      });
      this.addMenuItem(menu, "mi.clear", "clear", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            infoDisplay.replaceTextRange(0, infoDisplay.getText().length(), "");
         }
      });
      this.addMenuItem(menu, "l.find", "refine", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            new FindDialog().open();
         }
      });
      this.infoDisplay.setMenu(menu);
      this.input = new Text(this.composite, 2052);
      this.input.setLayoutData(new GridData(768));
      // Input line: history navigation, find/clear shortcuts, paging and submit.
      this.input.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent event) {
            int pageSize = infoDisplay.getClientArea().height / infoDisplay.getLineHeight();
            if ((event.stateMask & SWT.MOD1) != 0 && event.keyCode == 102) {
               new FindDialog().open();
            } else if ((event.stateMask & SWT.MOD1) != 0 && event.keyCode == 108) {
               infoDisplay.replaceTextRange(0, infoDisplay.getText().length(), "");
            } else {
               switch (event.keyCode) {
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
                        event.doit = false;
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
                     if (infoDisplay.getTopIndex() > pageSize) {
                        infoDisplay.setTopIndex(infoDisplay.getTopIndex() - pageSize);
                     } else {
                        infoDisplay.setTopIndex(0);
                     }
                     break;
                  case 16777222:
                     infoDisplay.setTopIndex(infoDisplay.getTopIndex() + pageSize);
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

      protected void configureShell(Shell shell) {
         super.configureShell(shell);
         shell.setImage(SResources.getImage("refine"));
         shell.setText(SResources.getString("l.find"));
      }

      protected void createButtonsForButtonBar(Composite parent) {
         this.createButton(parent, 0, SResources.getString("l.find"), true);
         this.createButton(parent, 1, SResources.getString("b.cancel"), false);
      }

      protected Control createDialogArea(Composite parent) {
         Composite composite = (Composite)super.createDialogArea(parent);
         composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         Label label = new Label(composite, 0);
         label.setText(SResources.getString("l.find") + ": ");
         label.setLayoutData(new GridData(32));
         this.text = new Text(composite, 2052);
         this.text.setLayoutData(new GridData(768));
         // Enter in the find field triggers the next search.
         this.text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
               switch (event.keyCode) {
                  case 13:
                  case 16777296:
                     find();
               }
            }
         });
         this.infoLabel = new Label(composite, 0);
         GridData gridData = new GridData(768);
         gridData.horizontalSpan = 2;
         this.infoLabel.setLayoutData(gridData);
         return composite;
      }

      protected void find() {
         if (this.text.getText().length() > 0) {
            String searchText = this.text.getText().toLowerCase();
            if (!this.lastFind.equals(searchText)) {
               this.lastPos = 0;
            }

            this.lastFind = searchText;
            String content = infoDisplay.getText().toLowerCase();
            int index = content.indexOf(searchText, this.lastPos);
            if (index != -1) {
               this.lastPos = index + 1;
               infoDisplay.setSelection(index, index + searchText.length());
               this.infoLabel.setText("");
            } else {
               this.infoLabel.setText(SResources.getString("l.stringNotFound"));
               this.lastPos = 0;
            }

            this.text.setFocus();
         }
      }

      protected void buttonPressed(int buttonId) {
         if (buttonId == 0) {
            this.find();
         } else {
            super.buttonPressed(buttonId);
         }
      }
   }

   static {
      try {
         regex = Pattern.compile(
            "(\"http://.+?\")|(http://[^\\s]+)|(www.[^\\s]+)|([\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3}[:][\\d]{1,4})|([\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3}[.][\\d]{1,3})",
            Pattern.CASE_INSENSITIVE
         );
      } catch (PatternSyntaxException patternSyntaxException) {
      }
   }
}
