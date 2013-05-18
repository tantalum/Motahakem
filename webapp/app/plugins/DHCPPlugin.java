package plugins;

import helpers.*;

import net.schmizz.sshj.userauth.UserAuthException;
import java.io.*;

import models.Server;

public class DHCPPlugin implements Plugin {
	public String getName() {
		return "DHCP";
	}

	public CommandResult start(Server server) throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/isc-dhcp-server start");
	}

	public CommandResult stop(Server server) throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/isc-dhcp-server stop");
	}

	public CommandResult restart(Server server) throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/isc-dhcp-server restart");
	}

	public int getStatus(Server server) throws IOException, UserAuthException {
		CommandResult cr = 
			SSHHelper.execCommand(server, "ps ax | grep -v grep | grep dhcpd");
		return cr.exitCode;
	}

	public String getConfiguration(Server server) throws IOException, UserAuthException {
		return SSHHelper.readFile(server, "/etc/dhcp/dhcpd.conf");
	}
}
