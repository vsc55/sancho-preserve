package sancho.model.mldonkey;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.StringTokenizer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import sancho.core.CoreFactory;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

public class SharedFile extends AObject implements IObject_UID, IPreview {
   static Pattern pathRE;
   protected long bytesUploaded;
   protected int id;
   protected byte[] md4;
   protected String name;
   protected EnumNetwork networkEnum;
   protected int requests;
   protected long size;

   SharedFile(ICore core) {
      super(core);
   }

   protected void readSubFiles(MessageBuffer buffer) {
   }

   public String[] getSubFileNames() {
      return null;
   }

   public long[] getSubFileSizes() {
      return null;
   }

   public String[] getSubFileMagics() {
      return null;
   }

   public String getMagic() {
      return "";
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

   public String getPreviewURL() {
      CoreFactory coreFactory = Sancho.getCoreFactory();
      String credentials = "";
      if (!coreFactory.getPassword().equals("")) {
         credentials = coreFactory.getUsername() + ":" + coreFactory.getPassword() + "@";
      }

      return "http://" + credentials + coreFactory.getHostname() + ":" + coreFactory.getHTTPPort() + "/preview_upload?q=" + this.getId();
   }

   public synchronized Program getOSPreviewApp() {
      return null;
   }

   public String preview(Program program, int index) {
      return "Not supported";
   }

   public String preview(int index) {
      return this.preview((String)null, index);
   }

   public String preview(String executableOverride, int index) {
      String executable = PreferenceLoader.loadString("previewExecutable");
      String workingDirectory = PreferenceLoader.loadString("previewWorkingDirectory");
      String extensions = PreferenceLoader.loadString("previewExtensions");
      if (!extensions.equals("")) {
         StringTokenizer tokenizer = new StringTokenizer(extensions, ";");
         String extension = "";
         String extensionExecutable = "";

         while (tokenizer.hasMoreTokens()) {
            extension = tokenizer.nextToken();
            if (tokenizer.hasMoreTokens()) {
               extensionExecutable = tokenizer.nextToken();
               if (this.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                  executable = extensionExecutable;
               }
            }
         }
      }

      if (executableOverride != null) {
         executable = executableOverride;
      }

      String url = this.getPreviewURL();
      String[] args = new String[]{executable, url};
      SwissArmy.execInThread(args, workingDirectory.equals("") ? null : workingDirectory);
      return executable;
   }

   public synchronized String getExtension() {
      int dotIndex = this.getName().lastIndexOf(".");
      return dotIndex != -1 ? this.getName().substring(dotIndex + 1).toLowerCase().intern() : "";
   }

   public synchronized ImageDescriptor getFileTypeImageDescriptor() {
      return SResources.getFileTypeImageDescriptor(this.getExtension());
   }

   public synchronized Image getFileTypeImage() {
      return SResources.getFileTypeImage(this.getExtension());
   }

   public synchronized long getBytesUploaded() {
      return this.bytesUploaded;
   }

   public String getED2K() {
      return "ed2k://|file|" + this.getName() + "|" + this.getSize() + "|" + this.getMD4() + "|/";
   }

   public synchronized int getId() {
      return this.id;
   }

   public synchronized String getMD4() {
      return SwissArmy.calcStringOfMD4(this.md4);
   }

   public synchronized String getName() {
      return this.name != null ? this.name : "";
   }

   public synchronized int getRequests() {
      return this.requests;
   }

   public synchronized String getRequestsString() {
      return SwissArmy.calcStringSizeGrouped((long)this.requests);
   }

   public synchronized String getNetworkName() {
      return this.networkEnum.getName();
   }

   public synchronized Image getNetworkImage() {
      return this.networkEnum.getImage();
   }

   public synchronized long getSize() {
      return this.size;
   }

   public synchronized String getSizeString() {
      return SwissArmy.calcStringSize(this.size);
   }

   public synchronized String getUploadedString() {
      return SwissArmy.calcStringSize(this.bytesUploaded);
   }

   public synchronized String[] getUIDs() {
      return null;
   }

   public void parseName() {
      if (this.name.indexOf("/") != -1) {
         Matcher matcher = pathRE.matcher(this.name);
         matcher.find();
         this.name = matcher.group();
      }
   }

   public void read(int id, MessageBuffer buffer) {
      synchronized (this) {
         this.id = id;
         this.networkEnum = this.readNetworkEnum(buffer);
         this.name = buffer.getString();
         this.size = this.readSize(buffer);
         this.bytesUploaded = buffer.getUInt64();
         this.requests = buffer.getInt32();
         this.readUIDs(buffer);
         this.readSubFiles(buffer);
         this.readMagic(buffer);
         this.parseName();
      }
   }

   protected void readMagic(MessageBuffer buffer) {
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getInt32(), buffer);
   }

   protected void readUIDs(MessageBuffer buffer) {
      this.md4 = buffer.getMd4();
   }

   protected long readSize(MessageBuffer buffer) {
      return (long)buffer.getInt32() & 4294967295L;
   }

   public boolean readUpdate(int id, MessageBuffer buffer) {
      long oldBytesUploaded = this.getBytesUploaded();
      int oldRequests = this.getRequests();
      this.read(id, buffer);
      return oldBytesUploaded != this.getBytesUploaded() || oldRequests != this.getRequests();
   }

   protected EnumNetwork readNetworkEnum(MessageBuffer buffer) {
      return this.core.getNetworkCollection().getNetworkEnum(buffer.getInt32());
   }

   public boolean upload(int id, MessageBuffer buffer) {
      long oldBytesUploaded = this.getBytesUploaded();
      int oldRequests = this.getRequests();
      synchronized (this) {
         this.bytesUploaded = buffer.getUInt64();
         this.requests = buffer.getInt32();
      }

      return oldBytesUploaded != this.getBytesUploaded() || oldRequests != this.getRequests();
   }

   public void computeTorrent(String tracker, String comment) {
      Network network = this.core.getNetworkCollection().getByEnum(EnumNetwork.BT);
      if (network != null) {
         byte[] flags = new byte[]{-1, -1};
         Object[] args = new Object[]{
            Integer.valueOf(network.getId()),
            flags,
            Integer.valueOf(SwissArmy.UTF8Length(comment) + 2 + SwissArmy.UTF8Length(tracker) + 2 + 6),
            Short.valueOf((short)1),
            Integer.valueOf(this.getId()),
            tracker,
            comment
         };
         this.core.send((short)63, args);
      }
   }

   static {
      try {
         pathRE = Pattern.compile("[^/]*$");
      } catch (PatternSyntaxException patternSyntaxException) {
         Sancho.pDebug("SharedFile: " + patternSyntaxException);
      }
   }
}
