package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.FileComment;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.UtilityFactory;
import sancho.utility.SwissArmy;

public class File41 extends File36 {
   String[] subFileMagics;
   String magic;
   String user;
   String group;
   FileComment[] fileComments;
   int avgRating = -1;

   File41(ICore core) {
      super(core);
   }

   public void chown(String user) {
      String command = "chown \"" + user + "\" " + this.getId();
      this.core.send((short)29, command);
      this.core.send((short)37, Integer.valueOf(this.getId()));
   }

   public void chgrp(String group) {
      String command = "chgrp \"" + group + "\" " + this.getId();
      this.core.send((short)29, command);
      this.core.send((short)37, Integer.valueOf(this.getId()));
   }

   public synchronized String getUser() {
      return this.user;
   }

   public synchronized String getGroup() {
      return this.group;
   }

   public synchronized int getAvgRating() {
      return this.avgRating;
   }

   protected void readSubFiles(MessageBuffer buffer) {
      int count = buffer.getUInt16();
      if (count > 0) {
         this.subFileNames = new String[count];
         this.subFileSizes = new long[count];
         this.subFileMagics = new String[count];

         for (int i = 0; i < count; i++) {
            this.subFileNames[i] = buffer.getString();
            this.subFileSizes[i] = buffer.getUInt64();
            this.subFileMagics[i] = buffer.getString();
         }
      }
   }

   protected void readUser(MessageBuffer buffer) {
      this.user = buffer.getString();
   }

   protected void readGroup(MessageBuffer buffer) {
      this.group = buffer.getString();
   }

   protected void readMagic(MessageBuffer buffer) {
      this.magic = buffer.getString();
   }

   protected void readFileComments(MessageBuffer buffer) {
      int count = buffer.getUInt16();
      if (count > 0) {
         this.avgRating = 0;
         int previousCount = 0;
         if (this.fileComments != null) {
            previousCount = this.fileComments.length;
         }

         if (previousCount != count) {
            this.addChangedBits(2048);
         }

         this.fileComments = new FileComment[count];
         int ratedCount = 0;
         int ratingSum = 0;

         for (int i = 0; i < count; i++) {
            this.fileComments[i] = UtilityFactory.getFileComment(this.core);
            this.fileComments[i].read(buffer);
            int rating = this.fileComments[i].getRating();
            if (rating < 0) {
               rating = 0;
            } else if (rating > 5) {
               rating = 5;
            }

            if (rating > 0) {
               ratedCount++;
               ratingSum += rating;
            }
         }

         if (ratedCount > 0) {
            this.avgRating = (int)Math.round((double)ratingSum / (double)ratedCount);
         }
      }
   }

   public synchronized int getNumComments() {
      return this.fileComments == null ? 0 : this.fileComments.length;
   }

   public String getNumCommentsString() {
      return String.valueOf(this.getNumComments());
   }

   protected void fakeCheck() {
      super.fakeCheck();
      if (!this.containsFake && this.fileComments != null) {
         for (int i = 0; i < this.fileComments.length; i++) {
            this.containsFake = SwissArmy.containsFake(this.fileComments[i].getComment());
            if (this.containsFake) {
               break;
            }
         }
      }
   }

   public synchronized String getMagic() {
      return this.magic != null ? this.magic : "";
   }

   public synchronized String[] getSubFileMagics() {
      if (this.subFileNames == null) {
         return null;
      } else {
         String[] magics = new String[this.subFileMagics.length];
         System.arraycopy(this.subFileMagics, 0, magics, 0, this.subFileMagics.length);
         return magics;
      }
   }

   public synchronized boolean hasFileComments() {
      return this.fileComments != null;
   }

   public synchronized FileComment[] getFileComments() {
      return this.fileComments;
   }
}
