package sancho.view;

import org.eclipse.swt.internal.ole.win32.COMObject;

class WebBrowserTab_win32$1 extends COMObject {
   // $VF: synthetic field
   private final WebBrowserTab_win32$EventDispatch this$1;

   WebBrowserTab_win32$1(WebBrowserTab_win32$EventDispatch var1, int[] var2) {
      super(var2);
      this.this$1 = var1;
   }

   public int method0(int[] var1) {
      return WebBrowserTab_win32$EventDispatch.access$000(this.this$1, var1[0], var1[1]);
   }

   public int method1(int[] var1) {
      return WebBrowserTab_win32$EventDispatch.access$100(this.this$1);
   }

   public int method2(int[] var1) {
      return this.this$1.Release();
   }

   public int method6(int[] var1) {
      return WebBrowserTab_win32$EventDispatch.access$200(this.this$1, var1[0], var1[1], var1[2], var1[3], var1[4], var1[5], var1[6], var1[7]);
   }
}
