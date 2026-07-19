package sancho.model.mldonkey;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import sancho.core.CoreFactory;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.model.mldonkey.enums.EnumExtension;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.enums.EnumPriority;
import sancho.model.mldonkey.utility.FileComment;
import sancho.model.mldonkey.utility.FileState;
import sancho.model.mldonkey.utility.Format;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.UtilityFactory;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.utility.ObjectMap;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.FileClient;
import sancho.view.utility.SResources;

public class File extends AObjectO implements MyObserver, IObject_UID, IPreview {
   public static final int CHANGED_ACTIVE = 1;
   public static final int CHANGED_RAVAIL = 2;
   public static final int CHANGED_DOWNLOADED = 4;
   public static final int CHANGED_ETA = 8;
   public static final int CHANGED_LAST = 16;
   public static final int CHANGED_PERCENT = 32;
   public static final int CHANGED_RATE = 64;
   public static final int CHANGED_STATE = 128;
   public static final int CHANGED_NOT_INTERESTING = 256;
   public static final int CHANGED_SOURCES = 512;
   public static final int CHANGED_AVAIL = 1024;
   public static final int CHANGED_COMMENTS = 2048;
   private int changedBits;
   protected int activeSources;
   protected long age;
   protected long ageTS;
   protected String avail;
   protected int[] chunkAges;
   protected String chunks;
   protected ObjectMap clientWeakMap;
   protected long downloaded;
   protected String downloadedString;
   protected long etaSeconds;
   protected long eta2Seconds;
   protected String etaString;
   protected String eta2String;
   protected EnumExtension extensionEnum;
   protected Set fileClientSet;
   protected EnumFileState fileStateEnum;
   protected Format format;
   protected int id;
   protected int lastSeen;
   protected byte[] md4;
   protected String name;
   protected String[] names;
   protected EnumNetwork networkEnum;
   protected int numChunks;
   protected int numClients;
   protected int numConnectedClients;
   protected float percent;
   protected int priority;
   protected EnumPriority priorityEnum;
   protected float rate;
   protected int relativeAvail;
   protected long size;
   protected int sources;
   protected boolean containsFake;
   protected FileState state;

   File(ICore core) {
      super(core);
      this.state = UtilityFactory.getFileState(core);
      this.format = UtilityFactory.getFormat(core);
      this.clientWeakMap = new ObjectMap(true);
   }

   protected Set getFileClientSet() {
      if (this.fileClientSet == null) {
         this.fileClientSet = Collections.synchronizedSet(new HashSet());
      }

      return this.fileClientSet;
   }

   public synchronized void addChangedBits(int bits) {
      this.changedBits |= bits;
   }

   public synchronized void removeChangedBits(int bits) {
      this.changedBits &= ~bits;
   }

   public synchronized int getChangedBits() {
      return this.changedBits;
   }

   public synchronized void clearChangedBits() {
      this.changedBits = 0;
   }

   public synchronized boolean hasChangedBit(int bits) {
      return (this.changedBits & bits) != 0;
   }

   public String[] getSubFileNames() {
      return null;
   }

   public String[] getSubFileMagics() {
      return null;
   }

   public long[] getSubFileSizes() {
      return null;
   }

   public String getContentRange(int index) {
      long[] sizes = this.getSubFileSizes();
      if (sizes != null && index < sizes.length) {
         long start = 0L;

         for (int i = 0; i < index; i++) {
            start += sizes[i];
         }

         long end = start + sizes[index] - 1L;
         return "bytes=" + start + "-" + end;
      } else {
         return "";
      }
   }

   protected void addFileClient(FileClient fileClient) {
      this.getFileClientSet().add(fileClient);
      this.addChangedBits(512);
      this.notifyChangedProperties();
   }

   public void addSource(Client client) {
      if (!this.clientWeakMap.contains(client)) {
         this.clientWeakMap.add(client);
         client.addObserver(this);
         this.addChangedBits(512);
         if (client.isTransferring()) {
            this.numConnectedClients++;
            this.setActiveSources(1);
            if (this.findFileClient(client) == null) {
               this.addClientToFileClientSet(client);
            }
         } else if (client.isConnected()) {
            this.numConnectedClients++;
         }

         this.notifyChangedProperties();
      }
   }

   public int getNumComments() {
      return 0;
   }

   public synchronized String getNumCommentsString() {
      return String.valueOf(this.getNumComments());
   }

   public void addClientToFileClientSet(Client client) {
      FileClient fileClient = new FileClient(this, client);
      this.addFileClient(fileClient);
      this.core.getFileCollection().sendUpdate(fileClient, 1);
   }

   protected void calcDownloadedString() {
      String previousDownloadedString = this.getDownloadedString();
      this.downloadedString = SwissArmy.calcStringSize(this.getDownloaded());
      if (!previousDownloadedString.equals(this.downloadedString)) {
         this.addChangedBits(4);
      }

      String previousPercentString = this.getPercentString();
      long size = this.getSize();
      this.percent = size > 0L ? (float)this.getDownloaded() / (float)size * 100.0F : 0.0F;
      if (!previousPercentString.equals(this.getPercentString())) {
         this.addChangedBits(32);
      }
   }

   protected void calcETA() {
      if (this.rate == 0.0F) {
         this.etaSeconds = Long.MAX_VALUE;
      } else {
         this.etaSeconds = (long)((float)(this.getSize() - this.getDownloaded()) / (this.getRate() + 1.0F));
      }

      String previousEtaString = this.getEtaString();
      EnumFileState state = this.getFileStateEnum();
      boolean useEta2 = this.core.getFileCollection().eta2();
      if (state != EnumFileState.QUEUED && state != EnumFileState.DOWNLOADED && state != EnumFileState.PAUSED) {
         this.etaString = SwissArmy.calcStringOfSeconds(this.etaSeconds);
         if (useEta2) {
            long downloaded = this.getDownloaded();
            long remaining = this.getSize() - downloaded;
            if (downloaded != 0L && remaining != 0L && this.getAge() != 0L) {
               if (this.ageTS == 0L) {
                  this.ageTS = System.currentTimeMillis();
               }

               long ageSeconds = this.getAge() + (System.currentTimeMillis() - this.ageTS) / 1000L;
               this.eta2Seconds = remaining * ageSeconds / downloaded;
               this.eta2String = SwissArmy.calcStringOfSeconds(this.eta2Seconds);
            } else {
               this.eta2String = "-";
               this.eta2Seconds = Long.MAX_VALUE;
            }
         }
      } else {
         this.etaString = "-";
         if (useEta2) {
            this.eta2Seconds = Long.MAX_VALUE;
            this.eta2String = "-";
         }
      }

      if (this.etaString.equals("")) {
         this.etaString = "-";
      }

      if (!previousEtaString.equals(this.etaString)) {
         this.addChangedBits(8);
      }
   }

   public void connectAll() {
      this.core.send((short)20, Integer.valueOf(this.getId()));
   }

   public synchronized boolean containsFake() {
      return this.containsFake;
   }

   public void dispose() {
      this.clientWeakMap.deleteObservers();
      this.deleteObservers();
   }

   public boolean equals(Object other) {
      return other instanceof File && this.getId() == ((File)other).getId();
   }

   public synchronized FileClient findFileClient(Client client) {
      for (Object fileClientObj : this.getFileClientSet()) { FileClient fileClient = (FileClient)fileClientObj;
         if (client == fileClient.getClient()) {
            return fileClient;
         }
      }

      return null;
   }

   public synchronized int getActiveSources() {
      return this.activeSources;
   }

   public synchronized long getAge() {
      return this.age;
   }

   public synchronized String getAgeString() {
      return SwissArmy.calcStringOfSeconds(System.currentTimeMillis() / 1000L - this.getAge());
   }

   public synchronized Network[] getAllAvailNetworks() {
      return new Network[0];
   }

   public synchronized String getAvail() {
      return this.avail;
   }

   public String getAvails(Network network) {
      return "";
   }

   public synchronized int[] getChunkAges() {
      if (this.chunkAges == null) {
         return null;
      } else {
         int[] ages = new int[this.chunkAges.length];
         System.arraycopy(this.chunkAges, 0, ages, 0, this.chunkAges.length);
         return ages;
      }
   }

   public synchronized String getChunks() {
      return this.chunks != null ? this.chunks : "";
   }

   public ObjectMap getClientWeakMap() {
      return this.clientWeakMap;
   }

   public String getComment() {
      return "";
   }

   public synchronized int getConnected() {
      return this.numConnectedClients;
   }

   public synchronized long getDownloaded() {
      return this.downloaded;
   }

   public synchronized long getRemaining() {
      return this.getSize() - this.getDownloaded();
   }

   public synchronized String getDownloadedString() {
      return this.downloadedString != null ? this.downloadedString : "";
   }

   public synchronized String getRemainingString() {
      return SwissArmy.calcStringSize(this.getRemaining());
   }

   public synchronized EnumNetwork getEnumNetwork() {
      return this.networkEnum;
   }

   public String getUser() {
      return "";
   }

   public String getGroup() {
      return "";
   }

   public synchronized Program getOSPreviewApp() {
      return SResources.getOSPreviewApp(this.getExtension());
   }

   public synchronized String getExtension() {
      int dotIndex = this.getName().lastIndexOf(".");
      return dotIndex != -1 ? this.getName().substring(dotIndex + 1).toLowerCase().intern() : "";
   }

   public String getED2K() {
      return "ed2k://|file|" + this.getName() + "|" + this.getSize() + "|" + this.getMD4() + "|/";
   }

   public synchronized long getETA() {
      return this.etaSeconds;
   }

   public synchronized long getETA2() {
      return this.eta2Seconds;
   }

   public synchronized String getEtaString() {
      return this.etaString != null ? this.etaString : "";
   }

   public synchronized String getEta2String() {
      return this.eta2String != null ? this.eta2String : "";
   }

   public synchronized int getFileClientSetSize() {
      return this.fileClientSet == null ? 0 : this.getFileClientSet().size();
   }

   public synchronized Object[] getFileClientSetArray() {
      return SwissArmy.toArray(this.getFileClientSet());
   }

   public String getFileFormat() {
      return this.getExtension();
   }

   public void getFileLocations() {
      this.core.send((short)34, Integer.valueOf(this.getId()));
   }

   public synchronized EnumFileState getFileStateEnum() {
      return this.fileStateEnum;
   }

   public synchronized EnumExtension getFileType() {
      return this.extensionEnum != null ? this.extensionEnum : EnumExtension.UNKNOWN;
   }

   public Format getFormat() {
      return this.format;
   }

   public synchronized int getId() {
      return this.id;
   }

   public synchronized int getLastSeen() {
      return this.lastSeen;
   }

   public synchronized String getLastSeenString() {
      return this.lastSeen == 8640000 ? "-" : SwissArmy.calcStringOfSeconds((long)this.lastSeen);
   }

   public synchronized String getHash() {
      return this.getMD4();
   }

   public String getMagic() {
      return "";
   }

   public boolean hasFileComments() {
      return false;
   }

   public FileComment[] getFileComments() {
      return null;
   }

   public synchronized String getMD4() {
      return SwissArmy.calcStringOfMD4(this.md4);
   }

   public synchronized String getName() {
      if (this.name == null) {
         return "";
      } else {
         EnumNetwork network = this.getEnumNetwork();
         if (network != EnumNetwork.GNUT && network != EnumNetwork.GNUT2) {
            return this.name;
         } else {
            String nullChar = String.valueOf('\u0000');
            return SwissArmy.replaceAll(this.name, nullChar, "");
         }
      }
   }

   public synchronized String[] getNames() {
      return this.names != null ? this.names : new String[0];
   }

   public synchronized int getNumChunks() {
      return this.numChunks;
   }

   public synchronized int getNumClients() {
      return this.numClients;
   }

   public synchronized int getNumSources() {
      return this.sources;
   }

   public synchronized float getPercent() {
      return this.percent;
   }

   public synchronized int getPriority() {
      return this.priority;
   }

   public synchronized AbstractEnum getPriorityEnum() {
      return this.priorityEnum;
   }

   public synchronized Image getFileTypeImage() {
      return SResources.getFileTypeImage(this.getFileFormat());
   }

   public synchronized Image getPriorityImage() {
      if (this.priority == 0) {
         return SResources.getImage("prioritynormal");
      } else {
         return this.priority > 0 ? SResources.getImage("priorityhigh") : SResources.getImage("prioritylow");
      }
   }

   public synchronized ImageDescriptor getFileTypeImageDescriptor() {
      return SResources.getFileTypeImageDescriptor(this.getFileFormat());
   }

   public synchronized float getRate() {
      return this.rate;
   }

   public synchronized String getRateString() {
      EnumFileState state = this.getFileStateEnum();
      if (state != EnumFileState.PAUSED && state != EnumFileState.QUEUED && state != EnumFileState.DOWNLOADED) {
         return this.getRate() == 0.0F ? "-" : SwissArmy.rateToString(this.getRate());
      } else {
         return state.getName();
      }
   }

   public synchronized int getRelativeAvail() {
      return this.relativeAvail;
   }

   public int getAvgRating() {
      return -1;
   }

   public synchronized Image getCommentImage() {
      int rating = this.getAvgRating();
      if (rating < 0) {
         return SResources.getImage("blank-16");
      } else {
         switch (rating) {
            case 1:
               return SResources.getImage("filerating1");
            case 2:
               return SResources.getImage("filerating2");
            case 3:
               return SResources.getImage("filerating3");
            case 4:
               return SResources.getImage("filerating4");
            case 5:
               return SResources.getImage("filerating5");
            default:
               return SResources.getImage("filerating0");
         }
      }
   }

   public synchronized Image getAvgRatingImage() {
      int rating = this.getAvgRating();
      if (rating < 0) {
         return null;
      } else {
         switch (rating) {
            case 1:
               return SResources.getImage("dotrating1");
            case 2:
               return SResources.getImage("dotrating2");
            case 3:
               return SResources.getImage("dotrating3");
            case 4:
               return SResources.getImage("dotrating4");
            case 5:
               return SResources.getImage("dotrating5");
            default:
               return SResources.getImage("dotrating0");
         }
      }
   }

   public synchronized String getRelativeAvailString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(this.relativeAvail);
      buffer.append("%");
      return buffer.toString().intern();
   }

   public synchronized long getSize() {
      return this.size;
   }

   public synchronized String getSizeString() {
      return SwissArmy.calcStringSize(this.getSize());
   }

   public int getSources() {
      return this.clientWeakMap.size();
   }

   public String getSourcesString() {
      int sources = this.getSources();
      if (sources == 0) {
         return "-";
      } else {
         int activeSources = this.getActiveSources();
         return activeSources > 0 ? sources + "(" + activeSources + ")" : String.valueOf(sources).intern();
      }
   }

   public synchronized String getPercentString() {
      return SwissArmy.percentToString(this.getPercent());
   }

   public synchronized String getPriorityString() {
      if (this.core.getFileCollection().verboseNumbers) {
         return this.priority == 0 ? this.priorityEnum.getName() : this.priorityEnum.getName() + "(" + this.priority + ")";
      } else {
         return String.valueOf(this.priority);
      }
   }

   public synchronized String[] getUIDs() {
      return null;
   }

   public boolean hasAvails() {
      return false;
   }

   public int hashCode() {
      return this.getId();
   }

   public boolean isActive() {
      EnumFileState state = this.getFileStateEnum();
      return state == EnumFileState.DOWNLOADING || state == EnumFileState.PAUSED || state == EnumFileState.QUEUED;
   }

   public boolean isInteresting() {
      EnumFileState state = this.getFileStateEnum();
      return state == EnumFileState.DOWNLOADING || state == EnumFileState.PAUSED || state == EnumFileState.DOWNLOADED || state == EnumFileState.QUEUED;
   }

   public void manualClean() {
      synchronized (this.clientWeakMap) {
         Object[] clients = this.clientWeakMap.getKeyArray();

         for (int i = 0; i < clients.length; i++) {
            Client client = (Client)clients[i];
            if (!this.core.getClientCollection().containsKey(client.getId())) {
               this.clientWeakMap.removeFromMain(client);
            }
         }
      }
   }

   public void notifyChangedProperties() {
      this.setChanged();
      this.notifyObservers();
      this.removeChangedBits(1024);
   }

   public String preview(int index) {
      return this.preview((String)null, index);
   }

   protected String getTempFileName(int index) {
      String workingDir = PreferenceLoader.loadString("previewWorkingDirectory");
      if (workingDir != null && !workingDir.equals("")) {
         if (!workingDir.endsWith(java.io.File.separator)) {
            workingDir = workingDir + java.io.File.separator;
         }

         String[] urnPrefixes = new String[]{"ed2k", "bitprint", "sha1", "ttr", "md5", "sig2dat", "bt"};

         for (int i = 0; i < urnPrefixes.length; i++) {
            String urnName = "urn_" + urnPrefixes[i] + "_" + this.getHash().toUpperCase();
            java.io.File urnFile = new java.io.File(workingDir + urnName);
            if (urnFile.exists()) {
               return urnName;
            }
         }
      }

      String tempName = this.getEnumNetwork().getTempFilePrefix() + this.getHash().toUpperCase();
      String[] subFileNames = this.getSubFileNames();
      if (index >= 0 && subFileNames != null && subFileNames.length > index) {
         String subPath = tempName + java.io.File.separator + subFileNames[index];
         String fullPath = workingDir + subPath;
         java.io.File subFile = new java.io.File(fullPath);
         if (subFile.exists()) {
            return subPath;
         }
      }

      return tempName;
   }

   public String preview(Program program, int index) {
      String workingDir = PreferenceLoader.loadString("previewWorkingDirectory");
      String fileName = this.getTempFileName(index);
      if (!workingDir.equals("") && !workingDir.endsWith(java.io.File.separator)) {
         workingDir = workingDir + java.io.File.separator;
      }

      fileName = this.repSep(fileName);
      if (fileName.startsWith(java.io.File.separator)) {
         fileName = fileName.substring(1);
      }

      String path = workingDir + fileName;
      program.execute(path);
      return path;
   }

   private String repSep(String path) {
      // Literal char replacement, not SwissArmy.replaceAll: the latter compiles the
      // "from" string as a regex, so a lone "\\" (single backslash) threw
      // PatternSyntaxException and then NPE'd — crashing preview on "/"-separator
      // platforms (Linux/macOS). A no-op when the char is absent, so no guard needed.
      String separator = java.io.File.separator;
      if (separator.equals("/")) {
         path = path.replace('\\', '/');
      } else if (separator.equals("\\")) {
         path = path.replace('/', '\\');
      }

      return path;
   }

   public String getPreviewURL() {
      CoreFactory coreFactory = Sancho.getCoreFactory();
      String auth = "";
      if (!coreFactory.getPassword().equals("")) {
         auth = coreFactory.getUsername() + ":" + coreFactory.getPassword() + "@";
      }

      return "http://" + auth + coreFactory.getHostname() + ":" + coreFactory.getHTTPPort() + "/preview_download?q=" + this.getId();
   }

   public String preview(String executableOverride, int index) {
      String executable = PreferenceLoader.loadString("previewExecutable");
      String workingDir = PreferenceLoader.loadString("previewWorkingDirectory");
      boolean useHttp = PreferenceLoader.loadBoolean("previewUseHttp");
      String extensions = PreferenceLoader.loadString("previewExtensions");
      if (!extensions.equals("")) {
         StringTokenizer tokenizer = new StringTokenizer(extensions, ";");
         String extension = "";
         String extExecutable = "";

         while (tokenizer.hasMoreTokens()) {
            extension = tokenizer.nextToken();
            if (tokenizer.hasMoreTokens()) {
               extExecutable = tokenizer.nextToken();
               if (this.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                  executable = extExecutable;
               }
            }
         }
      }

      if (executableOverride != null) {
         executable = executableOverride;
      }

      String url = this.getPreviewURL();
      String fileName = this.getTempFileName(index);
      int sepIndex = 0;
      if (fileName.indexOf("/") != -1) {
         sepIndex = fileName.lastIndexOf("/");
      }

      if (fileName.indexOf("\\") != -1) {
         int backslashIndex = fileName.lastIndexOf("\\");
         sepIndex = Math.max(backslashIndex, sepIndex);
      }

      if (sepIndex > 0) {
         String dir = fileName.substring(0, sepIndex);
         dir = this.repSep(dir);
         String remainder = fileName.substring(sepIndex);
         if (remainder.length() > 0) {
            remainder = remainder.substring(1);
         }

         if (!workingDir.equals("") && !workingDir.endsWith(java.io.File.separator)) {
            workingDir = workingDir + java.io.File.separator;
         }

         workingDir = workingDir + dir;
         fileName = remainder;
      }

      String result = "";
      if (executable.equals("")) {
         this.core.send((short)30, Integer.valueOf(this.getId()));
      } else {
         String[] args = new String[]{executable, useHttp ? url : fileName};
         if (!useHttp) {
            result = "";
            if (!workingDir.equals("")) {
               result = result + workingDir;
               if (!workingDir.endsWith(java.io.File.separator)) {
                  result = result + java.io.File.separator;
               }
            }

            result = result + fileName;
         }

         SwissArmy.execInThread(args, workingDir.equals("") ? null : workingDir);
      }

      return result;
   }

   public void read(int id, MessageBuffer buffer) {
      synchronized (this) {
         this.id = id;
         this.networkEnum = this.readNetwork(buffer.getInt32());
         this.names = buffer.getStringList();
         this.md4 = buffer.getMd4();
         this.size = this.readSize(buffer);
         this.downloaded = this.readDownloaded(buffer);
         this.calcDownloadedString();
         this.sources = buffer.getInt32();
         this.numClients = buffer.getInt32();
         this.readState(buffer);
         this.setChunks(buffer.getString(false));
         this.readAvailability(buffer);
         this.readRate(buffer);
         this.chunkAges = this.readChunkAges(buffer);
         this.age = this.readAge(buffer);
         this.getFormat().read(buffer);
         this.name = buffer.getString();
         this.setLastSeen(buffer.getInt32());
         this.setPriority(buffer.getSignedInt32());
         this.readComment(buffer);
         this.readUIDs(buffer);
         this.readSubFiles(buffer);
         this.readMagic(buffer);
         this.readFileComments(buffer);
         this.readUser(buffer);
         this.readGroup(buffer);
         this.calcETA();
         this.calcFileType();
         this.fakeCheck();
      }

      if ((this.getChangedBits() & 128) != 0 && this.state.getState() == EnumFileState.DOWNLOADED) {
         Sancho.getCoreFactory().notifyObject(this);
      }

      this.notifyChangedProperties();
   }

   protected void readUser(MessageBuffer buffer) {
   }

   protected void readGroup(MessageBuffer buffer) {
   }

   protected void readSubFiles(MessageBuffer buffer) {
   }

   protected void readMagic(MessageBuffer buffer) {
   }

   protected void readFileComments(MessageBuffer buffer) {
   }

   protected void fakeCheck() {
      if (!this.containsFake) {
         this.containsFake = SwissArmy.containsFake(this.name);
      }

      if (!this.containsFake) {
         this.containsFake = SwissArmy.containsFake(this.getComment());
      }

      if (!this.containsFake) {
         for (int i = 0; i < this.names.length; i++) {
            this.containsFake = SwissArmy.containsFake(this.names[i]);
            if (this.containsFake) {
               break;
            }
         }
      }
   }

   protected void readUIDs(MessageBuffer buffer) {
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getInt32(), buffer);
   }

   protected long readAge(MessageBuffer buffer) {
      if (this.core.getFileCollection().eta2()) {
         this.ageTS = System.currentTimeMillis();
      }

      try {
         return Long.parseLong(buffer.getString(false));
      } catch (NumberFormatException notANumber) {
         return 0L;
      }
   }

   protected int[] readChunkAges(MessageBuffer buffer) {
      String[] tokens = buffer.getStringList(false);
      int[] ages = new int[tokens.length];

      for (int i = 0; i < tokens.length; i++) {
         // Guard the parse like readAge() does: a non-numeric/empty token from the
         // core must not throw out of the read loop (which would break the socket
         // message stream). Default that chunk's age to 0.
         try {
            ages[i] = (int)(System.currentTimeMillis() / 1000L) - Integer.parseInt(tokens[i]);
         } catch (NumberFormatException notANumber) {
            ages[i] = 0;
         }
      }

      return ages;
   }

   protected void readComment(MessageBuffer buffer) {
   }

   protected long readDownloaded(MessageBuffer buffer) {
      return (long)buffer.getInt32() & 4294967295L;
   }

   protected void readRate(MessageBuffer buffer) {
      float newRate = buffer.getFloat();
      if (this.rate != newRate) {
         this.addChangedBits(64);
      }

      this.rate = newRate;
   }

   protected long readSize(MessageBuffer buffer) {
      return (long)buffer.getInt32() & 4294967295L;
   }

   protected void readState(MessageBuffer buffer) {
      boolean wasInteresting = this.isInteresting();
      EnumFileState previousState = this.getFileStateEnum();
      this.state.read(buffer);
      this.fileStateEnum = this.state.getState();
      if (wasInteresting && !this.isInteresting()) {
         this.addChangedBits(256);
      }

      if (previousState != this.getFileStateEnum()) {
         this.addChangedBits(128);
         if (this.state.getState() == EnumFileState.DOWNLOADED
            && PreferenceLoader.loadBoolean("downloadCompleteLog")
            && this.getName() != null
            && !this.getName().equals("")) {
            try {
               PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new java.io.File(VersionInfo.getDownloadLogFile()), true)));
               writer.write(System.currentTimeMillis() + " " + this.getED2K() + "\n");
               writer.close();
            } catch (FileNotFoundException fileNotFound) {
               Sancho.pDebug(fileNotFound.toString());
            } catch (IOException ioException) {
               Sancho.pDebug(ioException.toString());
            }
         }
      }
   }

   public void readUpdate(MessageBuffer buffer) {
      synchronized (this) {
         this.downloaded = this.readDownloaded(buffer);
         this.calcDownloadedString();
         this.readRate(buffer);
         this.setLastSeen(buffer.getInt32());
         this.calcETA();
      }

      this.notifyChangedProperties();
   }

   public FileClient removeFileClient(Client client) {
      FileClient fileClient = this.findFileClient(client);
      if (fileClient != null) {
         this.getFileClientSet().remove(fileClient);
         this.addChangedBits(512);
         this.notifyChangedProperties();
      }

      return fileClient;
   }

   public void removeSource(MessageBuffer buffer) {
      int id = buffer.getInt32();
      Client client = (Client)this.core.getClientCollection().get(id);
      if (client != null) {
         this.clientWeakMap.remove(client);
         client.deleteObserver(this);
         if (client.countObservers() == 0) {
            if (client.isConnected() && this.numConnectedClients > 0) {
               this.numConnectedClients--;
            }

            this.core.getClientCollection().removeSource(id, client);
         }

         this.addChangedBits(512);
         this.notifyChangedProperties();
      }
   }

   public void saveFileAs(String name) {
      Object[] args = new Object[]{Integer.valueOf(this.getId()), name};
      this.core.send((short)13, args);
   }

   public void sendPriority(boolean relative, int priority) {
      Object[] args = new Object[]{Integer.valueOf(this.getId()), null};
      if (relative) {
         priority += this.getPriority();
      }

      args[1] = Integer.valueOf(priority);
      this.core.send((short)51, args);
   }

   public void sendPriority(EnumPriority priority) {
      this.sendPriority(false, priority.getMaxValue());
   }

   public void setActiveSources(int delta) {
      int previousActiveSources = this.getActiveSources();
      synchronized (this) {
         if (delta == 0) {
            this.activeSources = 0;
            Object[] clients = this.clientWeakMap.getKeyArray();

            for (int i = 0; i < clients.length; i++) {
               Client client = (Client)clients[i];
               if (client.isTransferring()) {
                  this.activeSources++;
               }
            }
         } else {
            this.activeSources += delta;
         }
      }

      if (previousActiveSources != this.getActiveSources()) {
         this.addChangedBits(1);
         this.notifyChangedProperties();
      }
   }

   protected void readAvailability(MessageBuffer buffer) {
      this.avail = buffer.getString(false);
      this.setRelativeAvail();
   }

   protected void setChunks(String chunks) {
      this.chunks = chunks;
      this.numChunks = 0;

      for (int i = 0; i < this.chunks.length(); i++) {
         char c = this.chunks.charAt(i);
         if (c == '2' || c == '3') {
            this.numChunks++;
         }
      }
   }

   public void setComment(String comment) {
      String command = "comment " + this.getMD4() + " \"" + comment + "\"";
      this.core.send((short)29, command);
      this.core.send((short)37, Integer.valueOf(this.getId()));
   }

   public void requestFileInfo() {
      this.core.send((short)37, Integer.valueOf(this.getId()));
   }

   public void chown(String user) {
   }

   public void chgrp(String group) {
   }

   protected void calcFileType() {
      int dotIndex = this.getName().lastIndexOf(".");
      String extension = this.name.substring(dotIndex + 1, this.name.length());
      EnumExtension extensionEnum = EnumExtension.GET_EXT(extension);
      this.extensionEnum = extensionEnum != null ? extensionEnum : EnumExtension.UNKNOWN;
   }

   protected void setLastSeen(int lastSeen) {
      int previousLastSeen = this.getLastSeen();
      this.lastSeen = lastSeen;
      if (previousLastSeen != this.lastSeen) {
         this.addChangedBits(16);
      }
   }

   public void rename(String name) {
      name = "rename " + this.getId() + " \"" + name + "\"";
      this.core.send((short)29, name);
      this.core.send((short)37, Integer.valueOf(this.id));
   }

   protected EnumNetwork readNetwork(int networkId) {
      return this.core.getNetworkCollection().getNetworkEnum(networkId);
   }

   protected void setPriority(int priority) {
      this.priority = priority;
      this.priorityEnum = EnumPriority.intToEnum(priority);
   }

   public void setRelativeAvail() {
      int previousRelativeAvail = this.relativeAvail;
      this.relativeAvail = 0;
      int total = 0;
      int available = 0;
      if (this.avail == null) {
         this.avail = "";
      }

      String chunks = this.getChunks();
      if (this.avail.length() > 0 && this.avail.length() == chunks.length()) {
         for (int i = 0; i < this.avail.length(); i++) {
            if (chunks.charAt(i) == '0' || chunks.charAt(i) == '1') {
               total++;
               if (this.avail.charAt(i) > 0) {
                  available++;
               }
            }
         }

         if (total > 0) {
            this.relativeAvail = (int)((float)available / (float)total * 100.0F);
         }
      }

      if (previousRelativeAvail != this.relativeAvail) {
         this.addChangedBits(2);
      }
   }

   public void setState(EnumFileState newState) {
      EnumFileState currentState = this.getFileStateEnum();
      Object[] args = new Object[2];
      byte opcode = 23;
      args[0] = Integer.valueOf(this.id);
      if (currentState == EnumFileState.PAUSED && newState == EnumFileState.DOWNLOADING) {
         args[1] = Byte.valueOf((byte)1);
      } else if ((currentState == EnumFileState.DOWNLOADING || currentState == EnumFileState.QUEUED) && newState == EnumFileState.PAUSED) {
         args[1] = Byte.valueOf((byte)0);
      } else {
         if (newState != EnumFileState.CANCELLED) {
            return;
         }

         opcode = 11;
         args = new Object[]{Integer.valueOf(this.id)};
      }

      this.core.send(opcode, args);
   }

   protected boolean checkFileNum(Client client) {
      return true;
   }

   public void update(MyObservable observable, Object arg, int flags) {
      if (observable instanceof Client) {
         if (arg == null) {
            Client client = (Client)observable;
            if ((flags & 4) != 0) {
               if (this.checkFileNum(client)) {
                  this.setActiveSources(1);
                  FileClient fileClient = this.findFileClient(client);
                  byte updateType = 0;
                  if (fileClient == null) {
                     fileClient = new FileClient(this, client);
                     updateType = 1;
                     this.addFileClient(fileClient);
                  }

                  this.core.getFileCollection().sendUpdate(fileClient, updateType);
               }
            } else {
               FileClient removedFileClient;
               if ((flags & 8) != 0 && (removedFileClient = this.removeFileClient(client)) != null) {
                  this.setActiveSources(-1);
                  this.core.getFileCollection().sendUpdate(removedFileClient, 2);
               }
            }

            if ((flags & 1) != 0) {
               this.numConnectedClients++;
            } else if ((flags & 2) != 0 && this.numConnectedClients > 0) {
               this.numConnectedClients--;
            }
         }

         this.clientWeakMap.addOrUpdate(observable);
      }
   }

   public void verifyChunks() {
      this.core.send((short)24, Integer.valueOf(this.getId()));
   }
}
