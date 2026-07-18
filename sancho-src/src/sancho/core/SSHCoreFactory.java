package sancho.core;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.ProxySOCKS4;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.eclipse.swt.widgets.Display;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.Splash;

public class SSHCoreFactory extends CoreFactory {
   private JSch jsch;
   private Session session;
   private String ssh_host;
   private int ssh_lport = -1;
   private String ssh_pass;
   private int ssh_port = -1;
   private String ssh_rhost;
   private int ssh_rport = -1;
   private String ssh_user;
   private String ssh_socks4_proxy;
   private String ssh_socks5_proxy;
   private String ssh_http_proxy;
   private String ssh_proxy_user;
   private String ssh_proxy_pass;
   private boolean use_ssh;
   private boolean ssh_fwd_p;
   private int ssh_prport = -1;
   private int ssh_plport = -1;

   public SSHCoreFactory(Display display) {
      super(display);
   }

   public String getConnectedString() {
      String serverVersion = "";
      if (this.session != null) {
         serverVersion = "| " + this.session.getServerVersion();
      }

      return super.getConnectedString() + " " + serverVersion;
   }

   public void disconnectSSH() {
      if (this.jsch != null) {
         if (this.session != null) {
            try {
               this.session.delPortForwardingL(this.ssh_lport);
            } catch (JSchException jschException) {
               System.err.println("delPortForwarding: " + jschException);
            }

            this.session.disconnect();
            this.session = null;
         }

         this.jsch = null;
      }
   }

   public int initializeSSH() {
      if (!this.use_ssh) {
         return 0;
      } else if (SwissArmy.portInUse(this.ssh_lport)) {
         return 0;
      } else {
         try {
            this.jsch = new JSch();
            String fileSeparator = System.getProperty("file.separator");
            String sshDir = VersionInfo.getUserHomeDirectory() + fileSeparator + ".ssh" + fileSeparator;
            this.jsch.setKnownHosts(sshDir + "known_hosts");
            this.addIdentity(this.jsch, new File(sshDir + "id_dsa"), this.ssh_pass);
            this.addIdentity(this.jsch, new File(sshDir + "id_rsa"), this.ssh_pass);
            this.session = this.jsch.getSession(this.ssh_user, this.ssh_host, this.ssh_port);
            this.addProxy(this.session);
            this.session.setPassword(this.ssh_pass);
            this.session.setUserInfo(new SSHUserInfo());
            this.session.connect();
            this.session.setPortForwardingL(this.ssh_lport, this.ssh_rhost, this.ssh_rport);
            if (this.ssh_fwd_p) {
               this.session.setPortForwardingL(this.ssh_plport, this.ssh_rhost, this.ssh_prport);
            }

            return 0;
         } catch (Exception exception) {
            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace, true));
            return this.autoReconnecting
               ? 2
               : this.errorHandling(
                  SResources.getString("core.sshFailedTitle"), SResources.getString("core.sshFailedText") + "\n" + exception + "\n---\n" + stackTrace.toString()
               );
         }
      }
   }

   private void addProxy(Session session) {
      if (this.ssh_http_proxy != null && !this.ssh_http_proxy.equals("") && this.ssh_http_proxy.indexOf(":") > 0) {
         String proxyHost = this.ssh_http_proxy.substring(0, this.ssh_http_proxy.indexOf(58));
         int proxyPort = Integer.parseInt(this.ssh_http_proxy.substring(this.ssh_http_proxy.indexOf(58) + 1));
         ProxyHTTP proxy = new ProxyHTTP(proxyHost, proxyPort);
         if (this.ssh_proxy_user != null && !this.ssh_proxy_user.equals("")) {
            String proxyPass = "";
            if (this.ssh_proxy_pass != null) {
               proxyPass = this.ssh_proxy_pass;
            }

            proxy.setUserPasswd(this.ssh_proxy_user, proxyPass);
         }

         session.setProxy(proxy);
      } else if (this.ssh_socks5_proxy != null && !this.ssh_socks5_proxy.equals("") && this.ssh_socks5_proxy.indexOf(":") > 0) {
         String proxyHost = this.ssh_socks5_proxy.substring(0, this.ssh_socks5_proxy.indexOf(58));
         int proxyPort = Integer.parseInt(this.ssh_socks5_proxy.substring(this.ssh_socks5_proxy.indexOf(58) + 1));
         ProxySOCKS5 proxy = new ProxySOCKS5(proxyHost, proxyPort);
         if (this.ssh_proxy_user != null && !this.ssh_proxy_user.equals("")) {
            String proxyPass = "";
            if (this.ssh_proxy_pass != null) {
               proxyPass = this.ssh_proxy_pass;
            }

            proxy.setUserPasswd(this.ssh_proxy_user, proxyPass);
         }

         session.setProxy(proxy);
      } else if (this.ssh_socks4_proxy != null && !this.ssh_socks4_proxy.equals("") && this.ssh_socks4_proxy.indexOf(":") > 0) {
         String proxyHost = this.ssh_socks4_proxy.substring(0, this.ssh_socks4_proxy.indexOf(58));
         int proxyPort = Integer.parseInt(this.ssh_socks4_proxy.substring(this.ssh_socks4_proxy.indexOf(58) + 1));
         ProxySOCKS4 proxy = new ProxySOCKS4(proxyHost, proxyPort);
         if (this.ssh_proxy_user != null && !this.ssh_proxy_user.equals("")) {
            String proxyPass = "";
            if (this.ssh_proxy_pass != null) {
               proxyPass = this.ssh_proxy_pass;
            }

            proxy.setUserPasswd(this.ssh_proxy_user, proxyPass);
         }

         session.setProxy(proxy);
      }
   }

   private void addIdentity(JSch jsch, File keyFile, String passphrase) throws JSchException {
      if (keyFile.exists()) {
         if (passphrase != null && !passphrase.equals("")) {
            jsch.addIdentity(keyFile.getAbsolutePath(), passphrase);
         } else {
            jsch.addIdentity(keyFile.getAbsolutePath());
         }
      }
   }

   public String getHTTPPort() {
      return this.ssh_fwd_p ? String.valueOf(this.ssh_plport) : super.getHTTPPort();
   }

   public void readPreferences(int hostIndex, boolean reload) {
      super.readPreferences(hostIndex, reload);
      this.use_ssh = this.use_ssh && !reload ? this.use_ssh : PreferenceLoader.loadBoolean("hm_" + hostIndex + "_use_ssh");
      this.ssh_user = this.ssh_user != null && !reload ? this.ssh_user : PreferenceLoader.loadString("hm_" + hostIndex + "_ssh_user");
      this.ssh_host = this.ssh_host != null && !reload ? this.ssh_host : PreferenceLoader.loadString("hm_" + hostIndex + "_ssh_host");
      this.ssh_pass = this.ssh_pass != null && !reload ? this.ssh_pass : PreferenceLoader.loadString("hm_" + hostIndex + "_ssh_pass");
      this.ssh_port = this.ssh_port != -1 && !reload ? this.ssh_port : PreferenceLoader.loadInt("hm_" + hostIndex + "_ssh_port");
      this.ssh_rhost = this.ssh_rhost != null && !reload ? this.ssh_rhost : PreferenceLoader.loadString("hm_" + hostIndex + "_ssh_rhost");
      this.ssh_rport = this.ssh_rport != -1 && !reload ? this.ssh_rport : PreferenceLoader.loadInt("hm_" + hostIndex + "_ssh_rport");
      this.ssh_lport = this.ssh_lport != -1 && !reload ? this.ssh_lport : PreferenceLoader.loadInt("hm_" + hostIndex + "_ssh_lport");
      this.ssh_fwd_p = this.ssh_fwd_p && !reload ? this.ssh_fwd_p : PreferenceLoader.loadBoolean("hm_" + hostIndex + "_ssh_fwd_p");
      this.ssh_prport = this.ssh_prport != -1 && !reload ? this.ssh_prport : PreferenceLoader.loadInt("hm_" + hostIndex + "_ssh_prport");
      this.ssh_plport = this.ssh_plport != -1 && !reload ? this.ssh_plport : PreferenceLoader.loadInt("hm_" + hostIndex + "_ssh_plport");
      this.ssh_socks5_proxy = this.ssh_socks5_proxy != null && !reload ? this.ssh_socks5_proxy : PreferenceLoader.loadString("hm_" + hostIndex + "_ssh_socks5_proxy");
      this.ssh_socks4_proxy = this.ssh_socks4_proxy != null && !reload ? this.ssh_socks4_proxy : PreferenceLoader.loadString("hm_" + hostIndex + "_ssh_socks4_proxy");
      this.ssh_http_proxy = this.ssh_http_proxy != null && !reload ? this.ssh_http_proxy : PreferenceLoader.loadString("hm_" + hostIndex + "_ssh_http_proxy");
      this.ssh_proxy_user = this.ssh_proxy_user != null && !reload ? this.ssh_proxy_user : PreferenceLoader.loadString("hm_" + hostIndex + "_ssh_proxy_user");
      this.ssh_proxy_pass = this.ssh_proxy_pass != null && !reload ? this.ssh_proxy_pass : PreferenceLoader.loadString("hm_" + hostIndex + "_ssh_proxy_pass");
   }

   public void setDisconnected() {
      this.disconnectSSH();
      super.setDisconnected();
   }

   public int startCore() {
      int sshResult = this.initializeSSH();
      return sshResult != 2 && sshResult != 1 ? super.startCore() : sshResult;
   }

   // JSch callback: routes SSH password / passphrase / yes-no / message prompts to the
   // CoreFactory dialogs (was SSHCoreFactory$SSHUserInfo, with its showMessage Runnable).
   private class SSHUserInfo implements UserInfo {
      String passwd;

      public String getPassphrase() {
         return this.passwd;
      }

      public String getPassword() {
         return this.passwd;
      }

      public boolean promptPassphrase(String message) {
         String entered = SSHCoreFactory.this.askForPassword("/SSH", message);
         SSHCoreFactory.this.sResult = null;
         if (entered == null) {
            return false;
         } else {
            this.passwd = entered;
            return true;
         }
      }

      public boolean promptPassword(String message) {
         String entered = SSHCoreFactory.this.askForPassword("/SSH", message);
         SSHCoreFactory.this.sResult = null;
         if (entered == null) {
            return false;
         } else {
            this.passwd = entered;
            return true;
         }
      }

      public boolean promptYesNo(String message) {
         return SSHCoreFactory.this.createYesNoBox(VersionInfo.getName() + "/SSH", message);
      }

      public void showMessage(final String message) {
         SSHCoreFactory.this.display.syncExec(new Runnable() {
            public void run() {
               Splash.setVisible(false);
               CoreFactory.openInformation(null, VersionInfo.getName() + "/SSH", message);
               Splash.setVisible(true);
            }
         });
      }
   }
}
