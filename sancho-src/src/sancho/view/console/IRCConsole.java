package sancho.view.console;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.irc.IRCClient;
import sancho.view.preferences.PreferenceLoader;

public class IRCConsole extends Console {
   Shell shell;
   IRCClient ircClient;
   private Color inNickColor;
   private Color outNickColor;
   private Color joinColor;
   private Color partColor;
   private Color modeColor;
   private String channel;
   private StringBuffer stringBuffer = new StringBuffer();
   public static final int IN_NICK = 1;
   public static final int OUT_NICK = 2;
   public static final int JOIN = 3;
   public static final int PART = 4;
   public static final int MODE = 5;

   public IRCConsole(Composite var1, int var2, String var3) {
      super(var1, var2);
      this.channel = var3;
   }

   public void setConnectedTitle() {
      this.setTitle(this.ircClient.getServer() + "(" + this.ircClient.getNick() + ")");
   }

   public void setTitle(String var1) {
      this.shell.setText(VersionInfo.getName() + "IRC" + (var1 == null ? "" : ": " + var1));
   }

   public void appendNL(String var1) {
      this.infoDisplay.getDisplay().syncExec(new IRCConsole$1(this, var1));
   }

   public void appendRawNL(int[] var1, String[] var2) {
      this.infoDisplay.getDisplay().syncExec(new IRCConsole$2(this, var2, var1));
   }

   public Color intToColor(int var1) {
      switch (var1) {
         case 1:
            return this.inNickColor;
         case 2:
            return this.outNickColor;
         case 3:
            return this.joinColor;
         case 4:
            return this.partColor;
         case 5:
            return this.modeColor;
         default:
            return null;
      }
   }

   public void sendMessage() {
      String var1 = this.input.getText();
      if (var1 != null && var1.length() != 0) {
         if (var1.startsWith("/")) {
            String[] var2 = SwissArmy.split(var1, ' ');
            String var3 = var2[0];
            if (var3.length() > 1) {
               var3 = var3.substring(1, var3.length());
               if (var3.equalsIgnoreCase("quit") || var3.equalsIgnoreCase("exit")) {
                  this.ircClient.cDisconnect();
               } else if (var3.equalsIgnoreCase("help")) {
                  this.appendNewLine("*** Help: /join <#channel>");
                  this.appendNewLine("*** Help: /nick <new nick>");
                  this.appendNewLine("*** Help: /msg <nick> <msg>");
                  this.appendNewLine("*** Help: /topic");
                  this.appendNewLine("*** Help: /topic <topic>");
                  this.appendNewLine("*** Help: /me <action>");
                  this.appendNewLine("*** Help: <input text> to write text to the current channel");
                  this.appendNewLine("*** Help: Close tab to leave channel (min 1)");
                  this.appendNewLine("*** Help: Close window to disconnect");
               } else if (!this.channel.equals("status") && var3.equalsIgnoreCase("topic")) {
                  this.ircClient.sendRawLine("TOPIC " + this.channel);
               } else if (var2.length > 1) {
                  if (!this.channel.equals("status") && var3.equalsIgnoreCase("kick")) {
                     this.ircClient.kick(this.channel, var2[1]);
                  } else if (var3.equalsIgnoreCase("whois")) {
                     this.ircClient.whois(var2[1]);
                  } else if (var3.equalsIgnoreCase("j") || var3.equalsIgnoreCase("join")) {
                     this.ircClient.joinChannel(var2[1]);
                  } else if (var3.equalsIgnoreCase("nick")) {
                     this.ircClient.changeNick(var2[1]);
                     this.shell.getDisplay().timerExec(500, new IRCConsole$3(this));
                  } else if (!this.channel.equals("status") && var3.equalsIgnoreCase("topic")) {
                     String var10 = "";

                     for (int var12 = 1; var12 < var2.length; var12++) {
                        var10 = var10 + var2[var12] + " ";
                     }

                     this.ircClient.setTopic(this.channel, var10);
                  } else if (!this.channel.equals("status") && var3.equalsIgnoreCase("me")) {
                     String var9 = "";

                     for (int var11 = 1; var11 < var2.length; var11++) {
                        var9 = var9 + var2[var11] + " ";
                     }

                     this.ircClient.sendAction(this.channel, var9);
                     String var13 = "* " + this.ircClient.getNick() + " " + var9;
                     this.appendNewLine(var13);
                     int var14 = this.infoDisplay.getCharCount() - var13.length() - this.getLineDelimiter().length() + 2;
                     this.infoDisplay
                        .setStyleRange(new StyleRange(var14, this.ircClient.getNick().length(), this.outNickColor, this.infoDisplay.getBackground()));
                  } else if (!var3.equalsIgnoreCase("m") && !var3.equalsIgnoreCase("msg")) {
                     this.appendNL("*** Unknown command: " + var3);
                  } else {
                     String var4 = "";

                     for (int var5 = 2; var5 < var2.length; var5++) {
                        var4 = var4 + var2[var5] + " ";
                     }

                     this.ircClient.sendMessage(var2[1], var4);
                     String var6 = "[msg(" + var2[1] + ")] " + var4;
                     this.appendNewLine(var6);
                     int var7 = this.infoDisplay.getCharCount() - var6.length() - this.getLineDelimiter().length() + 5;
                     this.infoDisplay.setStyleRange(new StyleRange(var7, var2[1].length(), this.partColor, this.infoDisplay.getBackground()));
                  }
               } else {
                  this.appendNL("*** Unknown command: " + var3);
               }
            }
         } else if (!this.channel.equals("status")) {
            this.ircClient.sendMessage(this.channel, this.input.getText());
            this.fullAppendInput();
         }
      }
   }

   public void setShell(Shell var1) {
      this.shell = var1;
   }

   public void fullAppendInput() {
      this.prefixAppend();
      String var1 = this.input.getText();
      this.appendNewLine(var1);
      int var2 = this.infoDisplay.getCharCount() - var1.length() - this.getLineDelimiter().length();
      this.infoDisplay.setStyleRange(new StyleRange(var2, var1.length(), this.highlightColor, this.infoDisplay.getBackground()));
   }

   public void appendInput() {
   }

   public void prefixAppend() {
      int var1 = this.infoDisplay.getCharCount() + 1;
      this.infoDisplay.append("<" + this.ircClient.getNick() + "> ");
      this.infoDisplay.setStyleRange(new StyleRange(var1, this.ircClient.getNick().length(), this.outNickColor, this.infoDisplay.getBackground()));
   }

   public void sendRawMessage(String var1, String var2) {
      this.ircClient.sendMessage(var1, var2);
   }

   public void setIRCClient(IRCClient var1) {
      this.ircClient = var1;
   }

   public void updateDisplay() {
      this.infoDisplay.setFont(PreferenceLoader.loadFont("ircConsoleFontData"));
      this.input.setFont(PreferenceLoader.loadFont("ircConsoleFontData"));
      this.highlightColor = PreferenceLoader.loadColor("ircConsoleHighlight");
      this.infoDisplay.setBackground(PreferenceLoader.loadColor("ircConsoleBackground"));
      this.infoDisplay.setForeground(PreferenceLoader.loadColor("ircConsoleForeground"));
      this.input.setBackground(PreferenceLoader.loadColor("ircConsoleInputBackground"));
      this.input.setForeground(PreferenceLoader.loadColor("ircConsoleInputForeground"));
      this.inNickColor = PreferenceLoader.loadColor("ircInNickColor");
      this.outNickColor = PreferenceLoader.loadColor("ircOutNickColor");
      this.joinColor = PreferenceLoader.loadColor("ircJoinColor");
      this.partColor = PreferenceLoader.loadColor("ircPartColor");
      this.modeColor = PreferenceLoader.loadColor("ircModeColor");
   }

   // $VF: synthetic method
   static StringBuffer access$000(IRCConsole var0) {
      return var0.stringBuffer;
   }
}
