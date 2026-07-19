package sancho.view.statusline;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import sancho.core.Sancho;
import sancho.utility.SwissArmy;
import sancho.view.StatusLine;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.UniformResourceLocator;
import sancho.view.utility.MyViewForm;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class LinkEntry {
   private StatusLine statusLine;

   public LinkEntry(StatusLine statusLine, Composite composite) {
      this.statusLine = statusLine;
      this.createContents(composite);
   }

   public void createContents(Composite composite) {
      MyViewForm viewForm = WidgetFactory.createViewForm(composite, false);
      viewForm.setLayoutData(new GridData(1808));
      CLabel label = WidgetFactory.createCLabel(viewForm, "sl.linkEntryHeader", "up_arrow_green");
      label.setFont(PreferenceLoader.loadFont("headerFontData"));
      final Text text = new Text(viewForm, 578);
      text.setLayoutData(new FillLayout());
      text.setFont(PreferenceLoader.loadFont("ircConsoleFontData"));
      text.setForeground(PreferenceLoader.loadColor("ircConsoleInputForeground"));
      text.setBackground(PreferenceLoader.loadColor("ircConsoleInputBackground"));
      text.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent event) {
            if ((event.stateMask & SWT.MOD1) != 0 && (event.character == '\n' || event.character == '\r' || event.character == 16777296)) {
               LinkEntry.this.enterLinks(text);
               event.doit = false;
            }
         }
      });
      Composite toolBarComposite = new Composite(viewForm, 0);
      toolBarComposite.setLayout(WidgetFactory.createGridLayout(1, 1, 1, 0, 0, false));
      ToolBar toolBar = new ToolBar(toolBarComposite, 8519680);
      toolBar.setBackground(toolBar.getDisplay().getSystemColor(22));
      ToolItem torrentsItem = new ToolItem(toolBar, 8);
      torrentsItem.setToolTipText(SResources.getString("sl.addLocalTorrents"));
      torrentsItem.setImage(SResources.getImage("folder-12"));
      torrentsItem.setText("");
      torrentsItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            LinkEntry.this.statusLine.getMainWindow().sendTorrentsFromHD();
         }
      });
      ToolItem clearItem = new ToolItem(toolBar, 8);
      clearItem.setText(SResources.getString("sl.clear"));
      clearItem.setImage(SResources.getImage("clear-12"));
      clearItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            text.setText("");
         }
      });
      ToolItem sendItem = new ToolItem(toolBar, 8);
      sendItem.setText(SResources.getString("sl.send"));
      sendItem.setImage(SResources.getImage("up_arrow_green"));
      sendItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            LinkEntry.this.enterLinks(text);
         }
      });
      viewForm.setTopLeft(label);
      viewForm.setContent(text);
      viewForm.setTopRight(toolBarComposite);
      toolBar.pack();
      if (PreferenceLoader.loadBoolean("dragAndDrop")) {
         this.activateDropTarget(text);
      }
   }

   public void enterLinks(Text text) {
      String input = text.getText();
      Pattern pattern = null;

      try {
         String regex = "(ed2k://\\|file\\|[^\\|]+\\|(\\d+)\\|([\\dabcdef]+)\\|)|(sfdl://\\|.+?\\|[^\\|]+\\|(\\d+)\\|([\\dabcdef]+)\\|)";
         regex = regex + "|(sig2dat:///?\\|File:[^\\|]+\\|Length:.+?\\|UUHash:\\=.+?\\=)|(\\\"magnet:\\?xt=.+?\\\")";
         regex = regex + "|(magnet:\\?xt=.+?\n)|(\"http://.+\\.torrent\\?[^>]+\")|(http://.+\\.torrent)";
         if (text.getLineCount() == 1) {
            regex = regex + "|(magnet:\\?xt=.+)|(http://.+?\\.torrent.+)|(.+?\\.torrent.*)|(.+?\\.torrent)";
         }

         pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
      } catch (PatternSyntaxException patternSyntaxException) {
         patternSyntaxException.printStackTrace();
      }

      Matcher matcher = pattern.matcher(input);
      int count = 0;

      while (matcher.find()) {
         String link = SwissArmy.replaceAll(matcher.group(), "\"", "");
         link = SwissArmy.replaceAll(link, "\n", "");
         if (Sancho.hasCollectionFactory()) {
            SwissArmy.sendLink(Sancho.getCore(), link);
         }

         count++;
      }

      this.statusLine.setText(SResources.getString("sl.linksSent") + count);
      text.setText("");
   }

   private void activateDropTarget(final Text text) {
      byte operations = 23;
      DropTarget dropTarget = new DropTarget(text, operations);
      final UniformResourceLocator urlTransfer = UniformResourceLocator.getInstance();
      final TextTransfer textTransfer = TextTransfer.getInstance();
      final FileTransfer fileTransfer = FileTransfer.getInstance();
      dropTarget.setTransfer(new Transfer[]{urlTransfer, textTransfer, fileTransfer});
      dropTarget.addDropListener(new DropTargetAdapter() {
         public void dragEnter(DropTargetEvent event) {
            if (event.detail == 16) {
               if ((event.operations & 4) != 0) {
                  event.detail = 4;
               } else if ((event.operations & 1) != 0) {
                  event.detail = 1;
               } else if ((event.operations & 2) != 0) {
                  event.detail = 2;
               } else {
                  event.detail = 0;
               }
            }
         }

         public void drop(DropTargetEvent event) {
            if (event.data != null) {
               if (textTransfer.isSupportedType(event.currentDataType) || urlTransfer.isSupportedType(event.currentDataType)) {
                  text.append((String)event.data);
               }

               if (fileTransfer.isSupportedType(event.currentDataType)) {
                  String[] droppedFileNames = (String[])event.data;

                  for (int i = 0; i < droppedFileNames.length; i++) {
                     if (i > 0) {
                        text.append(text.getLineDelimiter());
                     }

                     text.append(droppedFileNames[i]);
                  }
               }
            }
         }
      });
   }
}
