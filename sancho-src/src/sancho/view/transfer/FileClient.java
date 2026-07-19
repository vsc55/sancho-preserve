package sancho.view.transfer;

import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;

public class FileClient {
   public static final int ADD = 1;
   public static final int REMOVE = 2;
   private File file;
   private Client client;

   public FileClient(File file, Client client) {
      this.file = file;
      this.client = client;
   }

   public File getFile() {
      return this.file;
   }

   public Client getClient() {
      return this.client;
   }

   public boolean equals(Object other) {
      if (!(other instanceof FileClient)) {
         return false;
      } else {
         Client client = ((FileClient)other).getClient();
         File file = ((FileClient)other).getFile();
         return client.equals(this.getClient()) && file.equals(this.getFile());
      }
   }
}
