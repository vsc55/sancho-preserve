package sancho.view.utility.setupWizard;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class SSHHostPage extends HostPage {
   HostPage.TextLabel ssh_host;
   HostPage.SpinnerLabel ssh_lport;
   HostPage.TextLabel ssh_pass;
   HostPage.SpinnerLabel ssh_port;
   HostPage.TextLabel ssh_rhost;
   HostPage.SpinnerLabel ssh_rport;
   HostPage.TextLabel ssh_user;
   Button use_ssh;
   Composite sshComposite;
   Button ssh_fwd_p;
   HostPage.SpinnerLabel ssh_prport;
   HostPage.SpinnerLabel ssh_plport;
   Combo ssh_proxyCombo;
   HostPage.TextLabel ssh_proxy;
   HostPage.TextLabel ssh_proxy_user;
   HostPage.TextLabel ssh_proxy_pass;
   Button r1;
   Button r2;

   protected void createMyControl(Composite var1) {
      super.createMyControl(var1);
      Group var2 = new Group(var1, 16);
      var2.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      GridData var3 = new GridData(768);
      var3.horizontalSpan = 2;
      var2.setLayoutData(var3);
      Composite var4 = new Composite(var2, 0);
      var4.setLayoutData(new GridData(768));
      var4.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 2, 2, false));
      this.use_ssh = new Button(var4, 32);
      this.use_ssh.setText(SResources.getString("hm.use_ssh"));
      var3 = new GridData(768);
      var3.horizontalIndent = 5;
      this.use_ssh.setLayoutData(var3);
      Label var5 = new Label(var4, 258);
      var3 = new GridData(768);
      var5.setLayoutData(var3);
      this.sshComposite = new Composite(var2, 0);
      this.sshComposite.setLayout(WidgetFactory.createGridLayout(6, 5, 2, 5, 2, false));
      this.sshComposite.setLayoutData(new GridData(768));
      this.ssh_host = this.createText2(this.sshComposite, SResources.getString("hm.ssh_host"), "");
      this.ssh_rhost = this.createText2(this.sshComposite, SResources.getString("hm.ssh_rhost"), "");
      this.ssh_user = this.createText2(this.sshComposite, SResources.getString("hm.ssh_user"), "");
      this.ssh_rport = this.createPort2(this.sshComposite, SResources.getString("hm.ssh_rport"), "");
      this.ssh_pass = this.createText2(this.sshComposite, SResources.getString("hm.ssh_pass"), "", true);
      this.ssh_lport = this.createPort2(this.sshComposite, SResources.getString("hm.ssh_lport"), "");
      this.ssh_port = this.createPort2(this.sshComposite, SResources.getString("hm.ssh_port"), "");
      new Label(this.sshComposite, 0);
      new Label(this.sshComposite, 0);
      new Label(this.sshComposite, 0);
      var5 = new Label(this.sshComposite, 258);
      GridData var6 = new GridData(768);
      var6.horizontalSpan = 6;
      var5.setLayoutData(var6);
      Composite var7 = new Composite(this.sshComposite, 0);
      var7.setLayout(WidgetFactory.createGridLayout(6, 0, 0, 5, 2, false));
      GridData var8 = new GridData(768);
      var8.horizontalSpan = 6;
      var7.setLayoutData(var8);
      this.ssh_fwd_p = new Button(var7, 32);
      this.ssh_fwd_p.setText(SResources.getString("hm.ssh_fwd_p"));
      var8 = new GridData(768);
      var8.horizontalSpan = 6;
      this.ssh_fwd_p.setLayoutData(var8);
      this.ssh_plport = this.createPort2(var7, SResources.getString("hm.ssh_plport"), "");
      this.ssh_prport = this.createPort2(var7, SResources.getString("hm.ssh_prport"), "");
      var5 = new Label(this.sshComposite, 258);
      var6 = new GridData(768);
      var6.horizontalSpan = 6;
      var5.setLayoutData(var6);
      Composite var9 = new Composite(this.sshComposite, 0);
      var9.setLayout(WidgetFactory.createGridLayout(4, 0, 0, 5, 2, false));
      var8 = new GridData(768);
      var8.horizontalSpan = 6;
      var9.setLayoutData(var8);
      this.ssh_proxyCombo = new Combo(var9, 8);
      String[] var10 = new String[]{"---", "HTTP", "SOCKS5", "SOCKS4"};
      this.ssh_proxyCombo.setItems(var10);
      this.ssh_proxy = this.createText2(var9, SResources.getString("hm.ssh_proxy"), "");
      new Label(var9, 0);
      Composite var11 = new Composite(var9, 0);
      var11.setLayout(WidgetFactory.createGridLayout(6, 0, 0, 5, 2, false));
      var8 = new GridData(768);
      var8.horizontalSpan = 3;
      var11.setLayoutData(var8);
      this.ssh_proxy_user = this.createText2(var11, SResources.getString("hm.ssh_proxy_user"), "");
      this.ssh_proxy_pass = this.createText2(var11, SResources.getString("hm.ssh_proxy_pass"), "", true);
      this.use_ssh.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            SSHHostPage.this.toggleSSHEnabled(SSHHostPage.this.use_ssh.getSelection());
         }
      });
   }

   public void loadHost(HostObject var1, int var2) {
      super.loadHost(var1, var2);
      var1.ssh_host = PreferenceLoader.loadString("hm_" + var2 + "_ssh_host");
      var1.ssh_user = PreferenceLoader.loadString("hm_" + var2 + "_ssh_user");
      var1.ssh_pass = PreferenceLoader.loadString("hm_" + var2 + "_ssh_pass");
      var1.ssh_port = PreferenceLoader.loadInt("hm_" + var2 + "_ssh_port");
      var1.ssh_rport = PreferenceLoader.loadInt("hm_" + var2 + "_ssh_rport");
      var1.ssh_lport = PreferenceLoader.loadInt("hm_" + var2 + "_ssh_lport");
      var1.ssh_rhost = PreferenceLoader.loadString("hm_" + var2 + "_ssh_rhost");
      var1.use_ssh = PreferenceLoader.loadBoolean("hm_" + var2 + "_use_ssh");
      var1.ssh_fwd_p = PreferenceLoader.loadBoolean("hm_" + var2 + "_ssh_fwd_p");
      var1.ssh_prport = PreferenceLoader.loadInt("hm_" + var2 + "_ssh_prport");
      var1.ssh_plport = PreferenceLoader.loadInt("hm_" + var2 + "_ssh_plport");
      var1.ssh_http_proxy = PreferenceLoader.loadString("hm_" + var2 + "_ssh_http_proxy");
      var1.ssh_socks5_proxy = PreferenceLoader.loadString("hm_" + var2 + "_ssh_socks5_proxy");
      var1.ssh_socks4_proxy = PreferenceLoader.loadString("hm_" + var2 + "_ssh_socks4_proxy");
      var1.ssh_proxy_user = PreferenceLoader.loadString("hm_" + var2 + "_ssh_proxy_user");
      var1.ssh_proxy_pass = PreferenceLoader.loadString("hm_" + var2 + "_ssh_proxy_pass");
   }

   public void resetInfo(HostObject var1) {
      super.resetInfo(var1);
      this.ssh_host.setText(var1.ssh_host);
      this.ssh_user.setText(var1.ssh_user);
      this.ssh_pass.setText(var1.ssh_pass);
      this.ssh_port.setSelection(var1.ssh_port);
      this.ssh_rhost.setText(var1.ssh_rhost);
      this.ssh_rport.setSelection(var1.ssh_rport);
      this.ssh_lport.setSelection(var1.ssh_lport);
      this.use_ssh.setSelection(var1.use_ssh);
      this.ssh_fwd_p.setSelection(var1.ssh_fwd_p);
      this.ssh_prport.setSelection(var1.ssh_prport);
      this.ssh_plport.setSelection(var1.ssh_plport);
      if (var1.ssh_http_proxy != null && !var1.ssh_http_proxy.equals("")) {
         this.ssh_proxy.setText(var1.ssh_http_proxy);
         this.ssh_proxyCombo.select(1);
      } else if (var1.ssh_socks5_proxy != null && !var1.ssh_socks5_proxy.equals("")) {
         this.ssh_proxy.setText(var1.ssh_socks5_proxy);
         this.ssh_proxyCombo.select(2);
      } else if (var1.ssh_socks4_proxy != null && !var1.ssh_socks4_proxy.equals("")) {
         this.ssh_proxy.setText(var1.ssh_socks4_proxy);
         this.ssh_proxyCombo.select(3);
      } else {
         this.ssh_proxy.setText("");
         this.ssh_proxyCombo.select(0);
      }

      this.ssh_proxy_user.setText(var1.ssh_proxy_user);
      this.ssh_proxy_pass.setText(var1.ssh_proxy_pass);
      this.toggleSSHEnabled(var1.use_ssh);
   }

   public void saveCurrent(HostObject var1) {
      super.saveCurrent(var1);
      var1.ssh_port = this.ssh_port.getSelection();
      var1.ssh_user = this.ssh_user.getText();
      var1.ssh_host = this.ssh_host.getText();
      var1.ssh_pass = this.ssh_pass.getText();
      var1.ssh_rhost = this.ssh_rhost.getText();
      var1.ssh_rport = this.ssh_rport.getSelection();
      var1.ssh_lport = this.ssh_lport.getSelection();
      var1.ssh_http_proxy = "";
      var1.ssh_socks5_proxy = "";
      var1.ssh_socks4_proxy = "";
      switch (this.ssh_proxyCombo.getSelectionIndex()) {
         case 1:
            var1.ssh_http_proxy = this.ssh_proxy.getText();
            break;
         case 2:
            var1.ssh_socks5_proxy = this.ssh_proxy.getText();
            break;
         case 3:
            var1.ssh_socks4_proxy = this.ssh_proxy.getText();
      }

      var1.ssh_proxy_user = this.ssh_proxy_user.getText();
      var1.ssh_proxy_pass = this.ssh_proxy_pass.getText();
      var1.use_ssh = this.use_ssh.getSelection();
      var1.ssh_fwd_p = this.ssh_fwd_p.getSelection();
      var1.ssh_plport = this.ssh_plport.getSelection();
      var1.ssh_prport = this.ssh_prport.getSelection();
   }

   public void setToDefault(PreferenceStore var1, int var2) {
      super.setToDefault(var1, var2);
      var1.setToDefault("hm_" + var2 + "_ssh_host");
      var1.setToDefault("hm_" + var2 + "_ssh_port");
      var1.setToDefault("hm_" + var2 + "_ssh_user");
      var1.setToDefault("hm_" + var2 + "_ssh_pass");
      var1.setToDefault("hm_" + var2 + "_ssh_rhost");
      var1.setToDefault("hm_" + var2 + "_ssh_rport");
      var1.setToDefault("hm_" + var2 + "_ssh_lport");
      var1.setToDefault("hm_" + var2 + "_ssh_http_proxy");
      var1.setToDefault("hm_" + var2 + "_ssh_socks5_proxy");
      var1.setToDefault("hm_" + var2 + "_ssh_socks4_proxy");
      var1.setToDefault("hm_" + var2 + "_use_ssh");
      var1.setToDefault("hm_" + var2 + "_ssh_fwd_p");
      var1.setToDefault("hm_" + var2 + "_ssh_prport");
      var1.setToDefault("hm_" + var2 + "_ssh_plport");
      var1.setToDefault("hm_" + var2 + "_ssh_proxy_user");
      var1.setToDefault("hm_" + var2 + "_ssh_proxy_pass");
   }

   public void setValue(PreferenceStore var1, int var2, HostObject var3) {
      super.setValue(var1, var2, var3);
      var1.setValue("hm_" + var2 + "_ssh_host", var3.ssh_host);
      var1.setValue("hm_" + var2 + "_ssh_user", var3.ssh_user);
      var1.setValue("hm_" + var2 + "_ssh_pass", var3.ssh_pass);
      var1.setValue("hm_" + var2 + "_ssh_port", var3.ssh_port);
      var1.setValue("hm_" + var2 + "_ssh_rhost", var3.ssh_rhost);
      var1.setValue("hm_" + var2 + "_ssh_rport", var3.ssh_rport);
      var1.setValue("hm_" + var2 + "_ssh_lport", var3.ssh_lport);
      var1.setValue("hm_" + var2 + "_ssh_http_proxy", var3.ssh_http_proxy);
      var1.setValue("hm_" + var2 + "_ssh_socks5_proxy", var3.ssh_socks5_proxy);
      var1.setValue("hm_" + var2 + "_ssh_socks4_proxy", var3.ssh_socks4_proxy);
      var1.setValue("hm_" + var2 + "_use_ssh", var3.use_ssh);
      var1.setValue("hm_" + var2 + "_ssh_fwd_p", var3.ssh_fwd_p);
      var1.setValue("hm_" + var2 + "_ssh_prport", var3.ssh_prport);
      var1.setValue("hm_" + var2 + "_ssh_plport", var3.ssh_plport);
      var1.setValue("hm_" + var2 + "_ssh_proxy_user", var3.ssh_proxy_user);
      var1.setValue("hm_" + var2 + "_ssh_proxy_pass", var3.ssh_proxy_pass);
   }

   public void toggleSSHEnabled(boolean var1) {
      this.ssh_host.setEnabled(var1);
      this.ssh_user.setEnabled(var1);
      this.ssh_pass.setEnabled(var1);
      this.ssh_port.setEnabled(var1);
      this.ssh_rhost.setEnabled(var1);
      this.ssh_rport.setEnabled(var1);
      this.ssh_lport.setEnabled(var1);
      this.ssh_plport.setEnabled(var1);
      this.ssh_prport.setEnabled(var1);
      this.ssh_fwd_p.setEnabled(var1);
      this.ssh_proxy.setEnabled(var1);
      this.ssh_proxyCombo.setEnabled(var1);
      this.ssh_proxy_user.setEnabled(var1);
      this.ssh_proxy_pass.setEnabled(var1);
   }
}
