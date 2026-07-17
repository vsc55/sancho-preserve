package sancho.model.mldonkey.enums;

import org.eclipse.swt.graphics.Image;
import sancho.view.utility.SResources;

public class EnumHostState extends AbstractEnum {
   private static final String S_I_TRANSFERRING = "ep_transferring";
   private static final String S_I_CONNECTING = "ep_connecting";
   private static final String S_I_ASKING = "ep_asking";
   private static final String S_I_NO_NEEDED = "ep_noneeded";
   private static final String S_I_UNKNOWN = "ep_unknown";
   public static final EnumHostState UNKNOWN = new EnumHostState(0, "unknown");
   public static final EnumHostState NOT_CONNECTED = new EnumHostState(1, "notConnected");
   public static final EnumHostState CONNECTING = new EnumHostState(2, "connecting");
   public static final EnumHostState CONNECTED_INITIATING = new EnumHostState(4, "connectedInitiating");
   public static final EnumHostState CONNECTED_DOWNLOADING = new EnumHostState(8, "connectedDownloading");
   public static final EnumHostState CONNECTED = new EnumHostState(16, "connected");
   public static final EnumHostState CONNECTED_AND_QUEUED = new EnumHostState(32, "connectedAndQueued");
   public static final EnumHostState NEW_HOST = new EnumHostState(64, "newHost");
   public static final EnumHostState REMOVE_HOST = new EnumHostState(128, "removeHost");
   public static final EnumHostState BLACKLISTED = new EnumHostState(256, "blacklisted");
   public static final EnumHostState NOT_CONNECTED_WAS_QUEUED = new EnumHostState(512, "notConnectedWasQueued");

   private EnumHostState(int var1, String var2) {
      super(var1, "e.state." + var2);
   }

   public static EnumHostState byteToEnum(byte var0) {
      switch (var0) {
         case 0:
            return NOT_CONNECTED;
         case 1:
            return CONNECTING;
         case 2:
            return CONNECTED_INITIATING;
         case 3:
            return CONNECTED_DOWNLOADING;
         case 4:
            return CONNECTED;
         case 5:
            return CONNECTED_AND_QUEUED;
         case 6:
            return NEW_HOST;
         case 7:
            return REMOVE_HOST;
         case 8:
            return BLACKLISTED;
         case 9:
            return NOT_CONNECTED_WAS_QUEUED;
         case 10:
            return CONNECTED;
         default:
            return UNKNOWN;
      }
   }

   public Image getImage() {
      if (this == CONNECTED_DOWNLOADING) {
         return SResources.getImage("ep_transferring");
      } else if (this == CONNECTING || this == CONNECTED_INITIATING) {
         return SResources.getImage("ep_connecting");
      } else if (this == CONNECTED_AND_QUEUED || this == NOT_CONNECTED_WAS_QUEUED) {
         return SResources.getImage("ep_asking");
      } else {
         return this == BLACKLISTED ? SResources.getImage("ep_noneeded") : SResources.getImage("ep_unknown");
      }
   }
}
