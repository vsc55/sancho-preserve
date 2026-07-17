package sancho.view.transfer;

import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;

public class FileClient {
   public static final int ADD = 1;
   public static final int REMOVE = 2;
   private File file;
   private Client client;

   public FileClient(File var1, Client var2) {
      this.file = var1;
      this.client = var2;
   }

   public File getFile() {
      return this.file;
   }

   public Client getClient() {
      return this.client;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof FileClient)) {
         return false;
      } else {
         Client var2 = ((FileClient)var1).getClient();
         File var3 = ((FileClient)var1).getFile();
         return var2.equals(this.getClient()) && var3.equals(this.getFile());
      }
   }
}
