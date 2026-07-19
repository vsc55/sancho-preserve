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

   protected void createMyControl(Composite parent) {
      super.createMyControl(parent);
      Group group = new Group(parent, 16);
      group.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      GridData gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      group.setLayoutData(gridData);
      Composite composite = new Composite(group, 0);
      composite.setLayoutData(new GridData(768));
      composite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 2, 2, false));
      this.use_ssh = new Button(composite, 32);
      this.use_ssh.setText(SResources.getString("hm.use_ssh"));
      gridData = new GridData(768);
      gridData.horizontalIndent = 5;
      this.use_ssh.setLayoutData(gridData);
      Label label = new Label(composite, 258);
      gridData = new GridData(768);
      label.setLayoutData(gridData);
      this.sshComposite = new Composite(group, 0);
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
      label = new Label(this.sshComposite, 258);
      GridData gridData2 = new GridData(768);
      gridData2.horizontalSpan = 6;
      label.setLayoutData(gridData2);
      Composite composite2 = new Composite(this.sshComposite, 0);
      composite2.setLayout(WidgetFactory.createGridLayout(6, 0, 0, 5, 2, false));
      GridData gridData3 = new GridData(768);
      gridData3.horizontalSpan = 6;
      composite2.setLayoutData(gridData3);
      this.ssh_fwd_p = new Button(composite2, 32);
      this.ssh_fwd_p.setText(SResources.getString("hm.ssh_fwd_p"));
      gridData3 = new GridData(768);
      gridData3.horizontalSpan = 6;
      this.ssh_fwd_p.setLayoutData(gridData3);
      this.ssh_plport = this.createPort2(composite2, SResources.getString("hm.ssh_plport"), "");
      this.ssh_prport = this.createPort2(composite2, SResources.getString("hm.ssh_prport"), "");
      label = new Label(this.sshComposite, 258);
      gridData2 = new GridData(768);
      gridData2.horizontalSpan = 6;
      label.setLayoutData(gridData2);
      Composite composite3 = new Composite(this.sshComposite, 0);
      composite3.setLayout(WidgetFactory.createGridLayout(4, 0, 0, 5, 2, false));
      gridData3 = new GridData(768);
      gridData3.horizontalSpan = 6;
      composite3.setLayoutData(gridData3);
      this.ssh_proxyCombo = new Combo(composite3, 8);
      String[] items = new String[]{"---", "HTTP", "SOCKS5", "SOCKS4"};
      this.ssh_proxyCombo.setItems(items);
      this.ssh_proxy = this.createText2(composite3, SResources.getString("hm.ssh_proxy"), "");
      new Label(composite3, 0);
      Composite composite4 = new Composite(composite3, 0);
      composite4.setLayout(WidgetFactory.createGridLayout(6, 0, 0, 5, 2, false));
      gridData3 = new GridData(768);
      gridData3.horizontalSpan = 3;
      composite4.setLayoutData(gridData3);
      this.ssh_proxy_user = this.createText2(composite4, SResources.getString("hm.ssh_proxy_user"), "");
      this.ssh_proxy_pass = this.createText2(composite4, SResources.getString("hm.ssh_proxy_pass"), "", true);
      this.use_ssh.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            SSHHostPage.this.toggleSSHEnabled(SSHHostPage.this.use_ssh.getSelection());
         }
      });
   }

   public void loadHost(HostObject host, int index) {
      super.loadHost(host, index);
      host.ssh_host = PreferenceLoader.loadString("hm_" + index + "_ssh_host");
      host.ssh_user = PreferenceLoader.loadString("hm_" + index + "_ssh_user");
      host.ssh_pass = PreferenceLoader.loadString("hm_" + index + "_ssh_pass");
      host.ssh_port = PreferenceLoader.loadInt("hm_" + index + "_ssh_port");
      host.ssh_rport = PreferenceLoader.loadInt("hm_" + index + "_ssh_rport");
      host.ssh_lport = PreferenceLoader.loadInt("hm_" + index + "_ssh_lport");
      host.ssh_rhost = PreferenceLoader.loadString("hm_" + index + "_ssh_rhost");
      host.use_ssh = PreferenceLoader.loadBoolean("hm_" + index + "_use_ssh");
      host.ssh_fwd_p = PreferenceLoader.loadBoolean("hm_" + index + "_ssh_fwd_p");
      host.ssh_prport = PreferenceLoader.loadInt("hm_" + index + "_ssh_prport");
      host.ssh_plport = PreferenceLoader.loadInt("hm_" + index + "_ssh_plport");
      host.ssh_http_proxy = PreferenceLoader.loadString("hm_" + index + "_ssh_http_proxy");
      host.ssh_socks5_proxy = PreferenceLoader.loadString("hm_" + index + "_ssh_socks5_proxy");
      host.ssh_socks4_proxy = PreferenceLoader.loadString("hm_" + index + "_ssh_socks4_proxy");
      host.ssh_proxy_user = PreferenceLoader.loadString("hm_" + index + "_ssh_proxy_user");
      host.ssh_proxy_pass = PreferenceLoader.loadString("hm_" + index + "_ssh_proxy_pass");
   }

   public void resetInfo(HostObject host) {
      super.resetInfo(host);
      this.ssh_host.setText(host.ssh_host);
      this.ssh_user.setText(host.ssh_user);
      this.ssh_pass.setText(host.ssh_pass);
      this.ssh_port.setSelection(host.ssh_port);
      this.ssh_rhost.setText(host.ssh_rhost);
      this.ssh_rport.setSelection(host.ssh_rport);
      this.ssh_lport.setSelection(host.ssh_lport);
      this.use_ssh.setSelection(host.use_ssh);
      this.ssh_fwd_p.setSelection(host.ssh_fwd_p);
      this.ssh_prport.setSelection(host.ssh_prport);
      this.ssh_plport.setSelection(host.ssh_plport);
      if (host.ssh_http_proxy != null && !host.ssh_http_proxy.equals("")) {
         this.ssh_proxy.setText(host.ssh_http_proxy);
         this.ssh_proxyCombo.select(1);
      } else if (host.ssh_socks5_proxy != null && !host.ssh_socks5_proxy.equals("")) {
         this.ssh_proxy.setText(host.ssh_socks5_proxy);
         this.ssh_proxyCombo.select(2);
      } else if (host.ssh_socks4_proxy != null && !host.ssh_socks4_proxy.equals("")) {
         this.ssh_proxy.setText(host.ssh_socks4_proxy);
         this.ssh_proxyCombo.select(3);
      } else {
         this.ssh_proxy.setText("");
         this.ssh_proxyCombo.select(0);
      }

      this.ssh_proxy_user.setText(host.ssh_proxy_user);
      this.ssh_proxy_pass.setText(host.ssh_proxy_pass);
      this.toggleSSHEnabled(host.use_ssh);
   }

   public void saveCurrent(HostObject host) {
      super.saveCurrent(host);
      host.ssh_port = this.ssh_port.getSelection();
      host.ssh_user = this.ssh_user.getText();
      host.ssh_host = this.ssh_host.getText();
      host.ssh_pass = this.ssh_pass.getText();
      host.ssh_rhost = this.ssh_rhost.getText();
      host.ssh_rport = this.ssh_rport.getSelection();
      host.ssh_lport = this.ssh_lport.getSelection();
      host.ssh_http_proxy = "";
      host.ssh_socks5_proxy = "";
      host.ssh_socks4_proxy = "";
      switch (this.ssh_proxyCombo.getSelectionIndex()) {
         case 1:
            host.ssh_http_proxy = this.ssh_proxy.getText();
            break;
         case 2:
            host.ssh_socks5_proxy = this.ssh_proxy.getText();
            break;
         case 3:
            host.ssh_socks4_proxy = this.ssh_proxy.getText();
      }

      host.ssh_proxy_user = this.ssh_proxy_user.getText();
      host.ssh_proxy_pass = this.ssh_proxy_pass.getText();
      host.use_ssh = this.use_ssh.getSelection();
      host.ssh_fwd_p = this.ssh_fwd_p.getSelection();
      host.ssh_plport = this.ssh_plport.getSelection();
      host.ssh_prport = this.ssh_prport.getSelection();
   }

   public void setToDefault(PreferenceStore store, int index) {
      super.setToDefault(store, index);
      store.setToDefault("hm_" + index + "_ssh_host");
      store.setToDefault("hm_" + index + "_ssh_port");
      store.setToDefault("hm_" + index + "_ssh_user");
      store.setToDefault("hm_" + index + "_ssh_pass");
      store.setToDefault("hm_" + index + "_ssh_rhost");
      store.setToDefault("hm_" + index + "_ssh_rport");
      store.setToDefault("hm_" + index + "_ssh_lport");
      store.setToDefault("hm_" + index + "_ssh_http_proxy");
      store.setToDefault("hm_" + index + "_ssh_socks5_proxy");
      store.setToDefault("hm_" + index + "_ssh_socks4_proxy");
      store.setToDefault("hm_" + index + "_use_ssh");
      store.setToDefault("hm_" + index + "_ssh_fwd_p");
      store.setToDefault("hm_" + index + "_ssh_prport");
      store.setToDefault("hm_" + index + "_ssh_plport");
      store.setToDefault("hm_" + index + "_ssh_proxy_user");
      store.setToDefault("hm_" + index + "_ssh_proxy_pass");
   }

   public void setValue(PreferenceStore store, int index, HostObject host) {
      super.setValue(store, index, host);
      store.setValue("hm_" + index + "_ssh_host", host.ssh_host);
      store.setValue("hm_" + index + "_ssh_user", host.ssh_user);
      store.setValue("hm_" + index + "_ssh_pass", host.ssh_pass);
      store.setValue("hm_" + index + "_ssh_port", host.ssh_port);
      store.setValue("hm_" + index + "_ssh_rhost", host.ssh_rhost);
      store.setValue("hm_" + index + "_ssh_rport", host.ssh_rport);
      store.setValue("hm_" + index + "_ssh_lport", host.ssh_lport);
      store.setValue("hm_" + index + "_ssh_http_proxy", host.ssh_http_proxy);
      store.setValue("hm_" + index + "_ssh_socks5_proxy", host.ssh_socks5_proxy);
      store.setValue("hm_" + index + "_ssh_socks4_proxy", host.ssh_socks4_proxy);
      store.setValue("hm_" + index + "_use_ssh", host.use_ssh);
      store.setValue("hm_" + index + "_ssh_fwd_p", host.ssh_fwd_p);
      store.setValue("hm_" + index + "_ssh_prport", host.ssh_prport);
      store.setValue("hm_" + index + "_ssh_plport", host.ssh_plport);
      store.setValue("hm_" + index + "_ssh_proxy_user", host.ssh_proxy_user);
      store.setValue("hm_" + index + "_ssh_proxy_pass", host.ssh_proxy_pass);
   }

   public void toggleSSHEnabled(boolean enabled) {
      this.ssh_host.setEnabled(enabled);
      this.ssh_user.setEnabled(enabled);
      this.ssh_pass.setEnabled(enabled);
      this.ssh_port.setEnabled(enabled);
      this.ssh_rhost.setEnabled(enabled);
      this.ssh_rport.setEnabled(enabled);
      this.ssh_lport.setEnabled(enabled);
      this.ssh_plport.setEnabled(enabled);
      this.ssh_prport.setEnabled(enabled);
      this.ssh_fwd_p.setEnabled(enabled);
      this.ssh_proxy.setEnabled(enabled);
      this.ssh_proxyCombo.setEnabled(enabled);
      this.ssh_proxy_user.setEnabled(enabled);
      this.ssh_proxy_pass.setEnabled(enabled);
   }
}
