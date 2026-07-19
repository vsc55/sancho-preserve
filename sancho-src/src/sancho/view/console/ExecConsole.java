package sancho.view.console;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.io.File;
import java.io.IOException;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.MainWindow;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class ExecConsole implements MyObserver {
   private static final int STDOUT = 1;
   private static final int STDERR = 2;
   private boolean coreStarted;
   private Pattern errorRE;
   private Process execProcess;
   private Color highlightColor;
   private final int MAX_LINES = PreferenceLoader.loadInt("consoleMaxLines");
   private StyledText outputConsole;
   private Shell shell;
   private StreamMonitor stderrMonitor;
   private StreamMonitor stdoutMonitor;
   private Display display;
   private boolean hasExited;

   public ExecConsole(Display display) {
      this.display = display;
      this.createContents();
      this.runExec();
   }

   public void appendLine(StreamMonitor monitor, String line) {
      int lineCount;
      if ((lineCount = this.outputConsole.getLineCount()) > this.MAX_LINES) {
         this.outputConsole.replaceTextRange(0, this.outputConsole.getOffsetAtLine(lineCount - this.MAX_LINES + 5), "");
      }

      this.outputConsole.setCaretOffset(this.outputConsole.getText().length());
      int startOffset = this.outputConsole.getCharCount();
      this.outputConsole.append(line + this.outputConsole.getLineDelimiter());
      if (monitor.getType() == 2) {
         this.outputConsole
            .setStyleRange(new StyleRange(startOffset, line.length(), this.outputConsole.getDisplay().getSystemColor(3), this.outputConsole.getBackground()));
      } else if (this.errorRE.matcher(line).find()) {
         this.outputConsole.setStyleRange(new StyleRange(startOffset, line.length(), this.highlightColor, this.outputConsole.getBackground()));
      }

      this.outputConsole.setCaretOffset(this.outputConsole.getCaretOffset() + line.length());
      this.outputConsole.showSelection();
   }

   public boolean coreStarted() {
      if (this.exited()) {
         this.coreStarted = true;
      }

      return this.coreStarted;
   }

   public boolean exited() {
      if (this.hasExited) {
         return true;
      } else {
         try {
            if (this.execProcess != null) {
               int exitValue = this.execProcess.exitValue();
               if (exitValue > 0 || exitValue <= 0) {
                  this.hasExited = true;
                  return true;
               }
            }
         } catch (IllegalThreadStateException illegalThreadState) {
         }

         return false;
      }
   }

   public void createContents() {
      this.shell = new Shell(this.display, 2160);
      this.shell.setImage(VersionInfo.getProgramIcon());
      this.shell.setText("Core");
      this.shell.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      this.shell.addDisposeListener(new DisposeListener() {
         public synchronized void widgetDisposed(DisposeEvent event) {
            PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
            PreferenceConverter.setValue(preferenceStore, "coreExecutableWindowBounds", ExecConsole.this.shell.getBounds());
         }
      });
      this.shell.addListener(21, new Listener() {
         public void handleEvent(Event event) {
            event.doit = false;
            ExecConsole.this.shell.setVisible(false);
         }
      });
      if (PreferenceLoader.contains("coreExecutableWindowBounds")) {
         this.shell.setBounds(PreferenceLoader.loadRectangle("coreExecutableWindowBounds"));
      }

      this.outputConsole = new StyledText(this.shell, 2826);
      this.outputConsole.setLayoutData(new GridData(1808));
      this.outputConsole.setFont(PreferenceLoader.loadFont("consoleFontData"));
      this.outputConsole.setBackground(PreferenceLoader.loadColor("consoleBackground"));
      this.outputConsole.setForeground(PreferenceLoader.loadColor("consoleForeground"));
      this.highlightColor = PreferenceLoader.loadColor("consoleHighlight");
      this.outputConsole.append("availableProcessors: " + Runtime.getRuntime().availableProcessors() + this.outputConsole.getLineDelimiter());
      this.outputConsole.append("maximumMem: " + Runtime.getRuntime().maxMemory() + this.outputConsole.getLineDelimiter());
      this.outputConsole.append("totalMem: " + Runtime.getRuntime().totalMemory() + this.outputConsole.getLineDelimiter());
      this.outputConsole
         .append("freeMem: " + Runtime.getRuntime().freeMemory() + this.outputConsole.getLineDelimiter() + this.outputConsole.getLineDelimiter());
      Menu menu = new Menu(this.outputConsole);
      MenuItem menuItem = new MenuItem(menu, 8);
      menuItem.setText(SResources.getString("mi.copy"));
      menuItem.addListener(13, new Listener() {
         public void handleEvent(Event event) {
            MainWindow.copyToClipboard(ExecConsole.this.outputConsole.getSelectionText());
         }
      });
      this.outputConsole.setMenu(menu);

      try {
         this.errorRE = Pattern.compile("error", Pattern.CASE_INSENSITIVE);
      } catch (PatternSyntaxException patternSyntaxException) {
         this.errorRE = null;
      }
   }

   public void dispose() {
      if (this.stdoutMonitor != null) {
         this.stdoutMonitor.stop();
      }

      if (this.stderrMonitor != null) {
         this.stderrMonitor.stop();
      }

      if (this.outputConsole != null) {
         this.outputConsole.dispose();
      }

      if (this.shell != null && !this.shell.isDisposed()) {
         this.shell.dispose();
      }
   }

   public Shell getShell() {
      return this.shell;
   }

   public void forceKill() {
      if (PreferenceLoader.loadBoolean("killSpawnedCoreOnExit")) {
         Sancho.send((short)3);
         short interval = 250;
         int iterations = PreferenceLoader.loadInt("killSpawnedCoreDelay") * (1000 / interval);

         for (int i = 0; i < iterations; i++) {
            if (this.execProcess == null) {
               return;
            }

            if (this.exited()) {
               return;
            }

            SwissArmy.threadSleep(interval);
         }

         if (this.execProcess != null) {
            Sancho.pDebug("Hard kill (check for left over .pid/.tmp): " + this.execProcess);
            this.execProcess.destroy();
            Sancho.pDebug("Destroyed: " + this.execProcess);
         }
      }
   }

   public void runExec() {
      try {
         File file = new File(PreferenceLoader.loadStringEnv("coreExecutable"));
         String parent = file.getParent();
         // Use the String[] overload: the single-String exec() splits the command
         // on whitespace, so a core path like C:\Program Files\...\mlnet.exe would
         // try to run "C:\Program". parent (parent dir) can be null for a bare name.
         this.execProcess = Runtime.getRuntime().exec(new String[]{file.toString()}, null, parent == null ? null : new File(parent));
         Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
               ExecConsole.this.forceKill();
            }
         });
         this.stdoutMonitor = new StreamMonitor(this.execProcess.getInputStream(), 1);
         this.stdoutMonitor.addObserver(this);
         Thread stdoutThread = new Thread(this.stdoutMonitor);
         stdoutThread.setDaemon(true);
         stdoutThread.start();
         this.stderrMonitor = new StreamMonitor(this.execProcess.getErrorStream(), 2);
         this.stderrMonitor.addObserver(this);
         Thread stderrThread = new Thread(this.stderrMonitor);
         stderrThread.setDaemon(true);
         stderrThread.start();
      } catch (IOException ioException) {
         Sancho.pDebug("exec:" + ioException);
      }
   }

   public void update(final MyObservable observable, Object arg, int eventType) {
      if (arg instanceof String) {
         final String line = (String)arg;
         if (!this.outputConsole.isDisposed()) {
            this.outputConsole.getDisplay().asyncExec(new Runnable() {
               public void run() {
                  if (!ExecConsole.this.outputConsole.isDisposed()) {
                     ExecConsole.this.appendLine((StreamMonitor)observable, line);
                  }
               }
            });
         }
      }
   }

   // Drains one of the spawned core process's streams and republishes each line to observers.
   private class StreamMonitor extends MyObservable implements Runnable {
      private InputStream inputStream;
      private boolean keepAlive;
      private int type;

      public StreamMonitor(InputStream inputStream, int type) {
         this.keepAlive = true;
         this.inputStream = inputStream;
         this.type = type;
      }

      public int getType() {
         return this.type;
      }

      public void run() {
         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));

            String line;
            while (this.keepAlive && (line = reader.readLine()) != null) {
               if (!ExecConsole.this.coreStarted && line.toLowerCase().indexOf("core started") > -1) {
                  ExecConsole.this.coreStarted = true;
               }

               this.setChanged();
               this.notifyObservers(line);
            }

            reader.close();
         } catch (IOException ioException) {
            Sancho.pDebug("streamMonitor:" + ioException);
         }
      }

      public void stop() {
         this.keepAlive = false;
      }
   }
}
