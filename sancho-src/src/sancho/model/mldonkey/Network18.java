package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Network18 extends Network {
   private int connectedServers;
   private boolean chat;
   private boolean multinet;
   private boolean rooms;
   private boolean search;
   private boolean servers;
   private boolean supernodes;
   private boolean upload;
   private boolean virtual;

   Network18(ICore core) {
      super(core);
   }

   public synchronized boolean hasChat() {
      return this.chat;
   }

   public synchronized boolean hasRooms() {
      return this.rooms;
   }

   public synchronized boolean hasServers() {
      return this.servers;
   }

   public synchronized boolean hasSupernodes() {
      return this.supernodes;
   }

   public synchronized boolean hasUpload() {
      return this.upload;
   }

   public synchronized boolean isMultinet() {
      return this.multinet;
   }

   public synchronized boolean isSearchable() {
      return this.search;
   }

   public synchronized boolean isVirtual() {
      return this.virtual;
   }

   public synchronized int numConnectedServers() {
      return this.connectedServers;
   }

   public void read(int id, MessageBuffer buffer) {
      super.read(id, buffer);
      synchronized (this) {
         this.connectedServers = buffer.getInt32();
         int flagCount = buffer.getUInt16();

         for (int i = 0; i < flagCount; i++) {
            switch (buffer.getUInt16()) {
               case 0:
                  this.servers = true;
                  break;
               case 1:
                  this.rooms = true;
                  break;
               case 2:
                  this.multinet = true;
                  break;
               case 3:
                  this.virtual = true;
                  break;
               case 4:
                  this.search = true;
                  break;
               case 5:
                  this.chat = true;
                  break;
               case 6:
                  this.supernodes = true;
                  break;
               case 7:
                  this.upload = true;
            }
         }
      }
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getInt32(), buffer);
   }

   protected synchronized void setConnectedServers(int connectedServers) {
      this.connectedServers = connectedServers;
   }
}
