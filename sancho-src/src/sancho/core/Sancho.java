package sancho.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.MainWindow;
import sancho.view.console.ExecConsole;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.Splash;
import sancho.view.utility.dialogs.BugDialog;

public class Sancho {
   private static CoreFactory coreFactory;
   public static boolean debug;
   public static boolean sDebug;
   private static Display display;
   private static ExecConsole execConsole;
   private static List linkList;
   public static boolean noBrowser;
   public static boolean noCore;
   private static File ourLockFile;
   public static long startTime;
   public static boolean spawnAborted;
   private static StringBuffer stringBuffer = new StringBuffer();
   public static boolean monitorMode;
   public static boolean bHasLoaded;
   public static boolean automated;
   public static boolean startMinimized;
   public static boolean startTray;
   private static boolean deleteLockFile;

   public static boolean hasLoaded() {
      return bHasLoaded;
   }

   public static void addLink(String link) {
      if (linkList == null) {
         linkList = new ArrayList();
      }

      linkList.add(link);
   }

   public static boolean argCheck(char flag, String arg) {
      return arg.length() == 2 && arg.indexOf(flag) == 1 && (arg.startsWith("-") || arg.startsWith("/"));
   }

   public static boolean argCheck(String flag, String arg) {
      return (arg.startsWith("-") || arg.startsWith("/")) && arg.substring(1).equals(flag);
   }

   private static void automatedLaunch() {
      coreFactory.setAutomated(true);
      coreFactory.setWantToConnect(true);
      if (PreferenceLoader.loadBoolean("useLastFile")) {
         coreFactory.readPreferences(SwissArmy.readLastFile());
      }

      if (coreFactory.successfulConnect() == 0) {
         sendDownloadLinks();
      }

      display.dispose();
   }

   private static void createLockFile() {
      try {
         FileOutputStream out = new FileOutputStream(ourLockFile);
         PrintStream printStream = new PrintStream(out);
         printStream.close();
         out.close();
         ourLockFile.deleteOnExit();
      } catch (FileNotFoundException fileNotFound) {
      } catch (IOException ioException) {
      }
   }

   public static void exit(int code) {
      if (stringBuffer.length() > 0) {
         if (VersionInfo.getOSPlatform().equals("Windows") && stringBuffer.length() > 0) {
            MessageBox messageBox = new MessageBox(new Shell(), 34);
            messageBox.setMessage(stringBuffer.toString());
            messageBox.open();
         } else {
            System.out.println(stringBuffer.toString());
         }
      }

      if (display != null && !display.isDisposed()) {
         display.dispose();
      }

      System.exit(code);
   }

   public static ICore getCore() {
      return coreFactory.getCore();
   }

   public static ExecConsole getCoreConsole() {
      return execConsole;
   }

   public static void killCoreConsole() {
      if (execConsole != null) {
         execConsole.forceKill();
         display.syncExec(new Runnable() {
            public void run() {
               execConsole.dispose();
            }
         });
      }

      execConsole = null;
   }

   public static CoreFactory getCoreFactory() {
      return coreFactory;
   }

   public static boolean hasCollectionFactory() {
      return getCore() != null && getCore().getCollectionFactory() != null;
   }

   public static String getUptime() {
      return SwissArmy.calcUptime(startTime);
   }

   private static void initializeResources() {
      try {
         PreferenceLoader.initialize();
      } catch (IOException ioException) {
         ioException.printStackTrace();
         System.exit(2);
      }

      SResources.initialize();
      PreferenceLoader.initialize2();
      File exitLog = new File(VersionInfo.getHomeDirectory() + "exit.log");
      if (exitLog.exists()) {
         exitLog.delete();
      }

      File consoleMsg = new File(VersionInfo.getHomeDirectory() + "console.msg");
      if (consoleMsg.exists()) {
         consoleMsg.delete();
      }
   }

   private static void interactiveLaunch() {
      startTime = System.currentTimeMillis();
      new Splash(display);
      if (!PreferenceLoader.loadString("coreExecutable").equals("")) {
         spawnCore();
      }

      if (noCore || coreFactory.interactiveConnect() == 0) {
         new MainWindow(display);
         PreferenceLoader.saveStore();
         PreferenceLoader.cleanUp();
         if (coreFactory.isConnected()) {
            coreFactory.disconnect();
         }
      }

      if (deleteLockFile && ourLockFile != null) {
         ourLockFile.delete();
      }

      Splash.dispose();
   }

   public static void main(String[] args) {
      // Note: tolerance for live model data mutating mid-sort now lives in
      // GSorter.sort() (a local stable merge sort), so the global
      // -Djava.util.Arrays.useLegacyMergeSort flag is no longer needed.
      Display.setAppName("sancho");
      display = new Display();
      display.addListener(12, new Listener() {
         public void handleEvent(Event event) {
            if (deleteLockFile && ourLockFile != null) {
               ourLockFile.delete();
            }
         }
      });
      if (VersionInfo.isGNU() && Prov.x == 2) {
         System.out.println("");
      }

      coreFactory = new SSHCoreFactory(display);
      parseArgs(args);
      initializeResources();
      coreFactory.initialize();
      if (linkList != null) {
         automated = true;
         deleteLockFile = false;
         automatedLaunch();
         exit(0);
      }

      if (!debug && !PreferenceLoader.loadBoolean("allowMultipleInstances")) {
         boolean lockExists = false;
         ourLockFile = new File(VersionInfo.getHomeDirectory() + ".lock");
         lockExists = ourLockFile.exists();
         if (!lockExists) {
            deleteLockFile = true;
            createLockFile();
         } else {
            MessageBox messageBox = new MessageBox(new Shell(display), 193);
            messageBox.setText(SResources.getString("core.multipleCoresTitle"));
            messageBox.setMessage(SResources.getString("core.multipleCoresText"));
            if (messageBox.open() == 128) {
               deleteLockFile = false;
               exit(0);
            } else {
               deleteLockFile = true;
            }
         }
      }

      interactiveLaunch();
      exit(0);
   }

   public static void parseArgs(String[] args) {
      for (int i = 0; i < args.length; i++) {
         if (!parseSingle(args[i])) {
            if (args.length > i + 1 && parseDouble(args[i], args[i + 1])) {
               i++;
            } else {
               addLink(args[i]);
            }
         }
      }
   }

   public static void pDebug(String message) {
      if (debug || sDebug) {
         System.err.println("[" + System.currentTimeMillis() + "] " + message);
      }
   }

   public static synchronized void fDebug(String message, String fileName) {
      try {
         FileOutputStream out = new FileOutputStream(VersionInfo.getHomeDirectory() + fileName, true);
         PrintStream printStream = new PrintStream(out);
         printStream.print(message);
         if (!message.endsWith("\n")) {
            printStream.print("\n");
         }

         printStream.close();
         out.close();
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   public static boolean parseDouble(String arg, String value) {
      if (argCheck('c', arg)) {
         PreferenceLoader.setPrefFile(value);
         return true;
      } else if (argCheck('j', arg)) {
         PreferenceLoader.setHomeDirectory(value);
         return true;
      } else if (argCheck('l', arg)) {
         addLink(value);
         return true;
      } else if (argCheck('r', arg) || argCheck('R', arg)) {
         PreferenceLoader.jvm = value;
         return true;
      } else if (argCheck('f', arg)) {
         PreferenceLoader.setLocaleString(value);
         return true;
      } else if (argCheck('H', arg) || argCheck('h', arg)) {
         String[] hostPort = SwissArmy.split(value, ':');
         if (hostPort.length == 2) {
            try {
               int port = Integer.parseInt(hostPort[1]);
               coreFactory.setHostPort(hostPort[0], port);
            } catch (NumberFormatException notANumber) {
            }
         }

         return true;
      } else if (argCheck('U', arg) || argCheck('u', arg)) {
         coreFactory.setUsername(value);
         return true;
      } else if (!argCheck('P', arg) && !argCheck('p', arg)) {
         return false;
      } else {
         coreFactory.setPassword(value);
         return true;
      }
   }

   public static boolean parseSingle(String arg) {
      if (argCheck('?', arg)) {
         printHelp();
         exit(1);
         return false;
      } else if (argCheck("min", arg)) {
         startMinimized = true;
         return true;
      } else if (argCheck("tray", arg)) {
         startTray = true;
         return true;
      } else if (argCheck('v', arg) || argCheck('V', arg)) {
         printVersion();
         exit(1);
         return false;
      } else if (argCheck('d', arg)) {
         debug = true;
         return true;
      } else if (argCheck('b', arg)) {
         noBrowser = true;
         return true;
      } else if (argCheck('m', arg)) {
         monitorMode = true;
         return true;
      } else if (argCheck('n', arg)) {
         noCore = true;
         return true;
      } else {
         return false;
      }
   }

   public static void printHelp() {
      initializeResources();
      printVersion();
      stringBuffer.append("Usage: " + VersionInfo.getName() + " [OPTION]...\n\n");
      printOption('c', "<filename>", "cmdline.c");
      printOption('j', "<directory>", "cmdline.j");
      printOption('l', "<linkname>", "cmdline.l");
      printOption('h', "<host:port>", "cmdline.h");
      printOption('u', "<username>", "cmdline.u");
      printOption('p', "<password>", "cmdline.p");
      printOption('f', "<xx_XX>", "cmdline.f");
      stringBuffer.append("\n");
      printOption('b', "", "cmdline.b");
      printOption('n', "", "cmdline.n");
      printOption('d', "", "cmdline.d");
      printOption('m', "", "cmdline.m");
      stringBuffer.append("\n");
      printOption("min", "", "cmdline.min");
      printOption("tray", "", "cmdline.tray");
   }

   public static void printOption(char flag, String param, String descKey) {
      stringBuffer.append("  -" + flag + " " + param + " \t" + SResources.getString(descKey) + "\n");
   }

   public static void printOption(String flag, String param, String descKey) {
      stringBuffer.append("  -" + flag + " " + param + " \t" + SResources.getString(descKey) + "\n");
   }

   public static void printVersion() {
      stringBuffer.append(VersionInfo.getName() + " " + VersionInfo.getVersion() + "\n");
   }

   private static void sendDownloadLinks() {
      if (getCore() != null) {
         for (int i = 0; i < linkList.size(); i++) {
            SwissArmy.sendLink(getCore(), (String)linkList.get(i));
         }
      }
   }

   private static void spawnCore() {
      Splash.updateText("splash.spawningCore");
      File coreExe = new File(PreferenceLoader.loadString("coreExecutable"));
      int port = PreferenceLoader.loadInt("hm_0_port");
      spawnAborted = SwissArmy.portInUse(port);
      if (spawnAborted) {
         Splash.updateText("splash.spawningAborted");
      } else {
         if (execConsole == null && coreExe.exists() && coreExe.isFile()) {
            execConsole = new ExecConsole(display);
            int attempts = 0;

            while (attempts++ < 235 && !execConsole.coreStarted()) {
               SwissArmy.threadSleep(250);
            }
         }
      }
   }

   public static void send(short opcode, Object[] args) {
      if (getCore() != null) {
         getCore().send(opcode, args);
      }
   }

   public static void send(short opcode, Object arg) {
      send(opcode, new Object[]{arg});
   }

   public static void send(short opcode) {
      send(opcode, null);
   }

   public static void threadException(final String message, final Exception exception) {
      if (debug) {
         exception.printStackTrace();
      } else if (display != null && !display.isDisposed()) {
         display.asyncExec(new Runnable() {
            public void run() {
               StringWriter writer = new StringWriter();
               writer.write("From Thread: " + message + "\n\n");
               if (exception != null) {
                  exception.printStackTrace(new PrintWriter(writer, true));
               } else {
                  writer.write("NULL EXCEPTION");
               }

               new BugDialog(new Shell(display), writer.toString()).open();
            }
         });
      }
   }
}
