package sancho.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.ServerSocket;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.view.preferences.PreferenceLoader;

public class SwissArmy {
   private static final float k = 1024.0F;
   private static final float m = 1048576.0F;
   private static final float g = 1.0737418E9F;
   private static final float t = 1.0995116E12F;
   private static String S_D = "d";
   private static String S_H = "h";
   private static String S_M = "m";
   private static String S_S = "s";
   private static String S_TB = "tb";
   private static String S_GB = "gb";
   private static String S_MB = "mb";
   private static String S_KB = "kb";
   private static String S_STB = " TB";
   private static String S_SGB = " GB";
   private static String S_SMB = " MB";
   private static String S_SKB = " KB";
   private static String S_NULL_MD4 = "00000000000000000000000000000000";
   private static final FieldPosition FP = new FieldPosition(1);
   private static DecimalFormat df00 = new DecimalFormat("0.0");
   private static DecimalFormat df000 = new DecimalFormat("0.00");
   private static DecimalFormat dfPercent = new DecimalFormat("0");
   private static DecimalFormat dfRate = new DecimalFormat("0.00");
   private static final DecimalFormat dfGrouped = new DecimalFormat();
   private static boolean humanReadable;
   private static boolean maxMegabytes;
   public static boolean disableUTF8;
   protected static Pattern fakeRE;
   private static long secondsInDay;
   private static long secondsInHour;
   static Pattern linksRE1;
   static Pattern linksRE2;
   static Pattern hrefRE;
   static Pattern envRE;

   public static boolean containsFake(String text) {
      return fakeRE != null && text != null && fakeRE.matcher(text).find();
   }

   public static String[] split(String input, char delimiter) {
      Pattern pattern = null;
      String regex = "([^" + delimiter + "])*";

      try {
         pattern = Pattern.compile(regex);
      } catch (PatternSyntaxException syntaxException) {
         syntaxException.printStackTrace();
      }

      ArrayList tokens = new ArrayList();
      if (input != null) {
         Matcher matcher = pattern.matcher(input);

         while (matcher.find()) {
            String token = matcher.group();
            if (!token.equals("")) {
               tokens.add(token);
            }
         }
      }

      String[] result = new String[tokens.size()];
      tokens.toArray(result);
      return result;
   }

   public static String replaceAll(String input, String regex, String replacement) {
      Pattern pattern = null;

      try {
         pattern = Pattern.compile(regex);
      } catch (PatternSyntaxException syntaxException) {
         syntaxException.printStackTrace();
      }

      return pattern.matcher(input).replaceAll(replacement);
   }

   public static synchronized String calcStringSizeGrouped(long size) {
      return dfGrouped.format(size).intern();
   }

   public static synchronized String calcStringSize(long size) {
      if (size == 0L) {
         return "0";
      } else {
         double value = (double)size;
         if (!humanReadable) {
            return dfGrouped.format(value).intern();
         } else {
            StringBuffer buffer = new StringBuffer(10);
            if (value >= 1.0995116E12F && !maxMegabytes) {
               df000.format(value / 1.0995116E12F, buffer, FP);
               return buffer.append(S_STB).toString().intern();
            } else if (value >= 1.0737418E9F && !maxMegabytes) {
               df000.format(value / 1.0737418E9F, buffer, FP);
               return buffer.append(S_SGB).toString().intern();
            } else if (value >= 1048576.0) {
               df00.format(value / 1048576.0, buffer, FP);
               return buffer.append(S_SMB).toString().intern();
            } else if (value >= 1024.0) {
               df00.format(value / 1024.0, buffer, FP);
               return buffer.append(S_SKB).toString().intern();
            } else {
               return String.valueOf(size).intern();
            }
         }
      }
   }

   public static String calcStringOfSeconds(long seconds) {
      StringBuffer buffer = new StringBuffer(10);
      if (seconds < 60L) {
         return buffer.append(seconds).append(S_S).toString().intern();
      } else {
         long days = seconds / secondsInDay;
         if (days > 9999L) {
            return "";
         } else {
            long remainder = seconds - days * secondsInDay;
            long hours = remainder / secondsInHour;
            remainder -= hours * secondsInHour;
            long minutes = remainder / 60L;
            remainder -= minutes * 60L;
            if (days > 0L) {
               return buffer.append(days).append(S_D).toString().intern();
            } else if (hours > 0L) {
               buffer.append(hours).append(S_H);
               if (minutes > 0L) {
                  buffer.append(" ").append(minutes).append(S_M);
               }

               return buffer.toString().intern();
            } else if (minutes > 0L) {
               buffer.append(minutes).append(S_M);
               if (remainder > 0L) {
                  buffer.append(" ").append(remainder).append(S_S);
               }

               return buffer.toString().intern();
            } else {
               return buffer.append(minutes).append(S_M).toString().intern();
            }
         }
      }
   }

   public static String calcStringOfSecondsFull(long seconds) {
      StringBuffer buffer = new StringBuffer(10);
      if (seconds < 60L) {
         return buffer.append(seconds).append(S_S).toString().intern();
      } else {
         long days = seconds / secondsInDay;
         long remainder = seconds - days * secondsInDay;
         long hours = remainder / secondsInHour;
         remainder -= hours * secondsInHour;
         long minutes = remainder / 60L;
         remainder -= minutes * 60L;
         boolean hasOutput = false;
         if (days > 0L) {
            buffer.append(days).append(S_D);
            hasOutput = true;
         }

         if (hours > 0L) {
            if (hasOutput) {
               buffer.append(" ");
            }

            buffer.append(hours).append(S_H);
            hasOutput = true;
         }

         if (minutes > 0L) {
            if (hasOutput) {
               buffer.append(" ");
            }

            buffer.append(minutes).append(S_M);
            hasOutput = true;
         }

         if (remainder > 0L) {
            if (hasOutput) {
               buffer.append(" ");
            }

            buffer.append(remainder).append(S_S);
         }

         return buffer.toString().intern();
      }
   }

   public static String calcUptime(long startTime) {
      long uptimeSeconds = 0L;
      long now = System.currentTimeMillis();
      if (now > startTime) {
         uptimeSeconds = (now - startTime) / 1000L;
      }

      return calcStringOfSeconds(uptimeSeconds);
   }

   public static String calcTimeOfSeconds(long seconds) {
      StringBuffer buffer = new StringBuffer(10);
      long hours = seconds / 60L / 60L;
      long remainder = seconds - hours * 60L * 60L;
      long minutes = remainder / 60L;
      remainder -= minutes * 60L;
      if (hours > 0L) {
         buffer.append(hours);
         buffer.append(S_H);
      }

      if (minutes > 0L) {
         buffer.append(minutes);
         buffer.append(S_M);
      }

      if (remainder > 0L) {
         buffer.append(remainder);
         buffer.append(S_S);
      }

      return buffer.toString();
   }

   public static String calcStringOfMD4(byte[] hash) {
      if (hash == null) {
         return S_NULL_MD4;
      } else {
         StringBuffer buffer = new StringBuffer(32);
         int length = hash.length;

         for (int i = 0; i < length; i++) {
            short b = (short)(hash[i] & 255);
            if (b <= 15) {
               buffer.append(0);
            }

            buffer.append(Integer.toHexString(b));
         }

         return buffer.toString().intern();
      }
   }

   public static long stringSizeToLong(String value, String unit) {
      // multiplier must be long: 2^40 (TB) overflows int, so the old code left it 0
      // and every "TB" size converted to 0 bytes. double keeps precision at GB/TB.
      long multiplier = 1L;
      if (unit.equalsIgnoreCase(S_KB)) {
         multiplier = 1024L;
      } else if (unit.equalsIgnoreCase(S_MB)) {
         multiplier = 1048576L;
      } else if (unit.equalsIgnoreCase(S_GB)) {
         multiplier = 1073741824L;
      } else if (unit.equalsIgnoreCase(S_TB)) {
         multiplier = 1099511627776L;
      }

      double number;
      try {
         number = Double.parseDouble(value);
      } catch (NumberFormatException notANumber) {
         number = 1.0;
      }

      return (long)(number * (double)multiplier);
   }

   public static int log2(int value) {
      int result;
      for (result = -1; value > 0; result++) {
         value >>= 1;
      }

      return result;
   }

   public static int countBits(int value) {
      value = (value >>> 1 & 1431655765) + (value & 1431655765);
      value = (value >>> 2 & 858993459) + (value & 858993459);
      value = (value >>> 4 & 252645135) + (value & 252645135);
      value = (value >>> 8 & 16711935) + (value & 16711935);
      return (value >>> 16) + (value & 65535);
   }

   public static void threadSleep(int millis) {
      try {
         Thread.sleep((long)millis);
      } catch (InterruptedException interrupted) {
      }
   }

   public static String replaceEnvVars(String input) {
      if (input == null) {
         return input;
      }

      Matcher matcher = envRE.matcher(input);
      if (!matcher.find()) {
         return input;
      } else {
         String result = "";
         int lastEnd = 0;

         do {
            int start = matcher.start(1);
            result = result + input.substring(lastEnd, start);
            lastEnd = matcher.end(1);
            String token = input.substring(start, lastEnd);
            String varName = replaceAll(token, "%", "");

            try {
               String envValue = System.getenv(varName);
               if (envValue != null) {
                  token = envValue;
               }
            } catch (Exception ignoredException) {
            } catch (Error ignoredError) {
            }

            result = result + token;
         } while (matcher.find());

         return result + input.substring(lastEnd);
      }
   }

   public static String[] parseLinks(String[] lines) {
      Pattern pattern = PreferenceLoader.loadBoolean("linkRipperShowAll") ? linksRE2 : linksRE1;
      ArrayList links = new ArrayList();
      Object link = null;

      for (int i = 0; i < lines.length; i++) {
         if (lines[i] != null) {
            Matcher matcher = pattern.matcher(lines[i]);
            if (matcher.find()) {
               link = matcher.group();
               if (!links.contains(link)) {
                  links.add(link);
               }
            }
         }
      }

      String[] result = new String[links.size()];
      links.toArray(result);
      Arrays.sort(result, String.CASE_INSENSITIVE_ORDER);
      return result;
   }

   public static String[] parseLinks(String html) {
      ArrayList matches = new ArrayList();
      if (html != null) {
         Matcher matcher = hrefRE.matcher(html);

         while (matcher.find()) {
            matches.add(matcher.group());
         }
      }

      StringBuffer buffer = new StringBuffer();
      String[] result = new String[matches.size()];

      for (int i = 0; i < result.length; i++) {
         buffer.setLength(0);
         buffer.append((String)matches.get(i));
         int equalsIndex = buffer.indexOf("=");
         if (equalsIndex != -1 && buffer.length() > equalsIndex + 1) {
            buffer.delete(0, equalsIndex + 1);
         }

         buffer.deleteCharAt(buffer.length() - 1);
         lrTrim(buffer);
         result[i] = replaceAll(buffer.toString(), "\"", "");
      }

      return parseLinks(result);
   }

   public static void lrTrim(StringBuffer buffer) {
      while (buffer.length() > 0 && (buffer.charAt(0) == '"' || buffer.charAt(0) == ' ')) {
         buffer.deleteCharAt(0);
      }

      int lastIndex = 0;

      while ((lastIndex = buffer.length() - 1) > 0 && (buffer.charAt(lastIndex) == '"' || buffer.charAt(lastIndex) == ' ')) {
         buffer.deleteCharAt(lastIndex);
      }
   }

   public static String getRandomString(int length) {
      Random random = new Random();
      random.setSeed(System.currentTimeMillis());
      StringBuffer buffer = new StringBuffer(length);
      byte range = 26;

      for (int i = 0; i < length; i++) {
         int code = 97 + random.nextInt(range);
         buffer.append((char)code);
      }

      return buffer.toString();
   }

   public static Object[] toArray(Collection collection) {
      ArrayList list = new ArrayList(collection.size());

      try {
         Iterator iterator = collection.iterator();

         while (iterator.hasNext()) {
            list.add(iterator.next());
         }
      } catch (NoSuchElementException noSuchElement) {
      }

      return list.toArray();
   }

   public static void clear(Map map) {
      int remaining = map.size();

      try {
         Iterator iterator = map.entrySet().iterator();

         while (--remaining >= 0) {
            try {
               iterator.next();
               iterator.remove();
            } catch (NoSuchElementException noSuchElement) {
            }
         }
      } catch (NoSuchElementException noSuchElement) {
      }
   }

   public static void execInThread(final String[] cmdArray, final String workingDir) {
      Thread thread = new Thread() {
         public void run() {
            Runtime runtime = Runtime.getRuntime();

            try {
               File dir = null;
               if (workingDir != null) {
                  dir = new File(workingDir);
               }

               Process process;
               if (workingDir != null && dir != null && dir.exists()) {
                  process = runtime.exec(cmdArray, null, dir);
               } else {
                  process = runtime.exec(cmdArray);
               }

               StreamMonitor errorMonitor = new StreamMonitor(process.getErrorStream());
               StreamMonitor outputMonitor = new StreamMonitor(process.getInputStream());
               errorMonitor.setDaemon(true);
               errorMonitor.start();
               outputMonitor.setDaemon(true);
               outputMonitor.start();
               process.waitFor();
            } catch (Exception error) {
               error.printStackTrace();
               Sancho.pDebug("execInThread: " + error);
            }
         }
      };
      thread.setDaemon(true);
      thread.start();
   }

   // Drains a spawned process's stdout/stderr so it can't block on a full pipe buffer.
   private static class StreamMonitor extends Thread {
      InputStream inputStream;

      StreamMonitor(InputStream inputStream) {
         this.inputStream = inputStream;
      }

      public void run() {
         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));

            while (reader.readLine() != null) {
            }

            reader.close();
         } catch (IOException error) {
            error.printStackTrace();
         }
      }
   }

   public static byte[] fileToByteArray(String path) {
      File file = new File(path);
      if (file.exists()) {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         short bufferSize = 8192;
         byte[] buffer = new byte[bufferSize];

         try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, bufferSize)) != -1) {
               out.write(buffer, 0, bytesRead);
            }

            in.close();
            out.close();
            return out.toByteArray();
         } catch (IOException ioException) {
            Sancho.pDebug(ioException.toString());
            ioException.printStackTrace();
            return null;
         }
      } else {
         Sancho.pDebug("File not found (permissions?): " + file.getAbsolutePath());
         return null;
      }
   }

   public static void sendLocalTorrent(String path, ICore core) {
      if (Sancho.hasCollectionFactory()) {
         Network network = core.getNetworkCollection().getByEnum(EnumNetwork.BT);
         if (network != null) {
            byte[] data = fileToByteArray(replaceAll(path, "\"", ""));
            if (data != null) {
               byte[] header = new byte[]{-1, -1};
               Object[] args = new Object[]{Integer.valueOf(network.getId()), header, Integer.valueOf(data.length + 2), Short.valueOf((short)0), data};
               Sancho.send((short)63, args);
               Sancho.pDebug("Sent Size: " + data.length);
               // (removed a threadSleep(1000) that ran on the UI thread after the
               // send was already flushed, freezing the GUI ~1s per local torrent.)
            }
         }
      }
   }

   public static boolean isSupportedProtocol(String link) {
      String[] protocols = new String[]{"http:", "ftp:", "ssh:", "ed2k:", "magnet:", "sig2dat:"};
      String lower = link.toLowerCase();

      for (int i = 0; i < protocols.length; i++) {
         if (lower.startsWith(protocols[i])) {
            return true;
         }
      }

      return false;
   }

   public static boolean portInUse(int port) {
      try {
         ServerSocket serverSocket = new ServerSocket(port);
         serverSocket.close();
         return false;
      } catch (BindException bindException) {
         return true;
      } catch (IOException ioException) {
         return true;
      }
   }

   public static String sfdl2ed2k(String link) {
      int firstBar = link.indexOf("|");
      if (firstBar < 0) {
         return link;
      } else {
         int secondBar = link.indexOf("|", firstBar + 1);
         if (secondBar < 0) {
            return link;
         } else {
            link = "ed2k://|file" + link.substring(secondBar);
            return replaceAll(link, "\"", "");
         }
      }
   }

   public static void sendLink(ICore core, String link) {
      String lower = link.toLowerCase();
      if (lower.startsWith("sfdl://")) {
         link = sfdl2ed2k(link);
         lower = sfdl2ed2k(lower);
      }

      if (core != null && Sancho.hasCollectionFactory()) {
         Network network = core.getNetworkCollection().getByEnum(EnumNetwork.BT);
         Sancho.pDebug("Network: " + (network != null ? "BT" : "null"));
         if (core.getProtocol() > 25 && network != null && !isSupportedProtocol(lower)) {
            sendLocalTorrent(link, core);
         } else if (!lower.startsWith("ftp:") && !lower.startsWith("ssh:") && (!lower.startsWith("http:") || lower.endsWith("torrent") || lower.endsWith("tor"))) {
            Sancho.send((short)8, link);
         } else {
            Sancho.send((short)29, "http " + link);
         }
      }
   }

   public static int readLastFile() {
      File file = new File(VersionInfo.getHomeDirectory() + ".last");
      if (!file.exists()) {
         return 0;
      } else {
         BufferedReader reader = null;

         byte result;
         try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            try {
               return Integer.parseInt(line);
            } catch (NumberFormatException notANumber) {
               return 0;
            }
         } catch (FileNotFoundException fileNotFound) {
            return 0;
         } catch (IOException ioException) {
            result = 0;
         } finally {
            try {
               if (reader != null) {
                  reader.close();
               }
            } catch (IOException closeException) {
               return 0;
            }
         }

         return result;
      }
   }

   public static void writeLastFile(int index) {
      File file = new File(VersionInfo.getHomeDirectory() + ".last");

      try {
         FileOutputStream out = new FileOutputStream(file);
         PrintStream printStream = new PrintStream(out);
         printStream.println(index);
         printStream.close();
         out.close();
      } catch (FileNotFoundException fileNotFound) {
      } catch (IOException ioException) {
      }
   }

   public static String stringNoAccel(String text) {
      return replaceAll(text, "&", "&&");
   }

   public static synchronized void updatePreferences() {
      dfRate = updateDF(PreferenceLoader.loadInt("dlRateDecimals"));
      dfPercent = updateDF(PreferenceLoader.loadInt("dlPercentDecimals"));
      humanReadable = PreferenceLoader.loadBoolean("humanReadable");
      maxMegabytes = PreferenceLoader.loadBoolean("maxMegabytes");
      int decimals = PreferenceLoader.loadInt("humanReadableDecimals");
      disableUTF8 = PreferenceLoader.loadBoolean("disableUTF8");
      if (decimals < 0) {
         df00 = new DecimalFormat("0.0");
         df000 = new DecimalFormat("0.00");
      } else {
         df00 = updateDF(decimals);
         df000 = updateDF(decimals);
      }
   }

   public static synchronized String calcRate(float rate) {
      StringBuffer buffer = new StringBuffer(8);
      df000.format((double)rate, buffer, FP);
      return buffer.toString().intern();
   }

   public static synchronized String percentToString(float percent) {
      StringBuffer buffer = new StringBuffer(10);
      dfPercent.format((double)percent, buffer, FP);
      buffer.append("%");
      return buffer.toString().intern();
   }

   public static synchronized String rateToString(float rate) {
      return dfRate.format((double)(rate / 1000.0F)).intern();
   }

   public static int UTF8Length(String text) {
      byte[] bytes;
      try {
         bytes = text.getBytes("UTF8");
      } catch (UnsupportedEncodingException unsupportedEncoding) {
         bytes = text.getBytes();
      }

      return bytes.length;
   }

   public static DecimalFormat updateDF(int decimals) {
      String pattern = "0";
      if (decimals > 0) {
         pattern = pattern + ".";

         for (int i = 0; i < decimals; i++) {
            pattern = pattern + "0";
         }
      }

      return new DecimalFormat(pattern);
   }

   static {
      dfGrouped.setGroupingSize(3);

      try {
         fakeRE = Pattern.compile("fake", Pattern.CASE_INSENSITIVE);
      } catch (PatternSyntaxException syntaxException) {
         fakeRE = null;
      }

      secondsInDay = 86400L;
      secondsInHour = 3600L;
      String baseLinksRegex = "(ed2k://\\|file\\|[^\\|]+\\|(\\d+)\\|([\\dabcdef]+)\\|)|(sig2dat:///?\\|File:[^\\|]+\\|Length:.+?\\|UUHash:.+?\\=.+?\\=)|(magnet:\\?xt=.+)|(\"http://.+\\.torrent\\?[^>]+\")|(http://.+\\.torrent)";
      String allLinksRegex = baseLinksRegex + "|(http://.+)" + "|(ftp://.+)";
      String hrefRegex = "href.*?=.+?>";
      String envRegex = "(%.+?%)";

      try {
         linksRE1 = Pattern.compile(baseLinksRegex, Pattern.CASE_INSENSITIVE);
         linksRE2 = Pattern.compile(baseLinksRegex + allLinksRegex, Pattern.CASE_INSENSITIVE);
         hrefRE = Pattern.compile(hrefRegex, Pattern.CASE_INSENSITIVE);
         envRE = Pattern.compile(envRegex, Pattern.CASE_INSENSITIVE);
      } catch (PatternSyntaxException syntaxException) {
      }
   }
}
