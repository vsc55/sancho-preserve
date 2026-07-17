package sancho.view.irc;

import org.jibble.pircbot.User;

public interface IIRCListener {
   void onAction(String var1, String var2, String var3, String var4, String var5);

   void onConnect();

   void onDeop(String var1, String var2, String var3, String var4, String var5);

   void onDeVoice(String var1, String var2, String var3, String var4, String var5);

   void onDisconnect();

   void onJoin(String var1, String var2, String var3, String var4);

   void onKick(String var1, String var2, String var3, String var4, String var5, String var6);

   void onMessage(String var1, String var2, String var3, String var4, String var5);

   void onNickChange(String var1, String var2, String var3, String var4);

   void onNotice(String var1, String var2, String var3, String var4, String var5);

   void onOp(String var1, String var2, String var3, String var4, String var5);

   void onPart(String var1, String var2, String var3, String var4);

   void onPrivateMessage(String var1, String var2, String var3, String var4);

   void onQuit(String var1, String var2, String var3, String var4);

   void onTopic(String var1, String var2, String var3, long var4, boolean var6);

   void onUserList(String var1, User[] var2);

   void onVoice(String var1, String var2, String var3, String var4, String var5);

   void onWhoisUser(String var1, String var2, String var3, String var4, String var5, String var6);
}
