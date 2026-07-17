package sancho.view;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.COMObject;
import org.eclipse.swt.internal.ole.win32.GUID;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

class WebBrowserTab_win32$EventDispatch {
   private COMObject iDispatch;
   private int refCount;
   private int eventID;
   static final int onkeyup = -2147412106;
   static final int onkeydown = -2147412107;
   OleAutomation auto;
   // $VF: synthetic field
   private final WebBrowserTab_win32 this$0;

   WebBrowserTab_win32$EventDispatch(WebBrowserTab_win32 var1, int var2, OleAutomation var3) {
      this.this$0 = var1;
      this.refCount = 0;
      this.eventID = var2;
      this.auto = var3;
      this.createCOMInterfaces();
   }

   long getAddress() {
      return this.iDispatch.getAddress();
   }

   private void createCOMInterfaces() {
      this.iDispatch = new WebBrowserTab_win32$1(this, new int[]{2, 0, 0, 1, 3, 4, 8});
   }

   private void disposeCOMInterfaces() {
      if (this.iDispatch != null) {
         this.iDispatch.dispose();
      }

      this.iDispatch = null;
   }

   private int AddRef() {
      this.refCount++;
      return this.refCount;
   }

   public boolean getKeyState(String var1) {
      int[] var2 = this.auto.getIDsOfNames(new String[]{"Document"});
      if (var2 == null) {
         return false;
      } else {
         Variant var3 = this.auto.getProperty(var2[0]);
         if (var3 != null && var3.getType() != 0) {
            OleAutomation var4 = var3.getAutomation();
            int[] var5 = var4.getIDsOfNames(new String[]{"parentWindow"});
            Variant var6 = var4.getProperty(var5[0]);
            if (var6 != null && var6.getType() != 0) {
               OleAutomation var7 = var6.getAutomation();
               int[] var8 = var7.getIDsOfNames(new String[]{"event"});
               Variant var9 = var7.getProperty(var8[0]);
               OleAutomation var10 = var9.getAutomation();
               int[] var11 = var10.getIDsOfNames(new String[]{var1});
               Variant var12 = var10.getProperty(var11[0]);
               var6.dispose();
               var10.dispose();
               var7.dispose();
               var9.dispose();
               var4.dispose();
               boolean var13 = var12.getBoolean();
               var12.dispose();
               return var13;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   private int Invoke(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      switch (this.eventID) {
         case -2147412107:
         case -2147412106:
            this.this$0.ctrlDown = this.getKeyState("ctrlKey");
         default:
            return 0;
      }
   }

   private int QueryInterface(int var1, int var2) {
      if (var1 != 0 && var2 != 0) {
         GUID var3 = new GUID();
         COM.MoveMemory(var3, var1, GUID.sizeof);
         if (!COM.IsEqualGUID(var3, COM.IIDIUnknown) && !COM.IsEqualGUID(var3, COM.IIDIDispatch)) {
            COM.MoveMemory(var2, new int[]{0}, OS.PTR_SIZEOF);
            return -2147467262;
         } else {
            COM.MoveMemory(var2, new long[]{this.iDispatch.getAddress()}, OS.PTR_SIZEOF);
            this.AddRef();
            return 0;
         }
      } else {
         return -2147024809;
      }
   }

   int Release() {
      this.refCount--;
      if (this.refCount == 0) {
         this.disposeCOMInterfaces();
      }

      return this.refCount;
   }

   // $VF: synthetic method
   static int access$000(WebBrowserTab_win32$EventDispatch var0, int var1, int var2) {
      return var0.QueryInterface(var1, var2);
   }

   // $VF: synthetic method
   static int access$100(WebBrowserTab_win32$EventDispatch var0) {
      return var0.AddRef();
   }

   // $VF: synthetic method
   static int access$200(WebBrowserTab_win32$EventDispatch var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      return var0.Invoke(var1, var2, var3, var4, var5, var6, var7, var8);
   }
}
