package plugins;

import helpers.*;

import net.schmizz.sshj.userauth.UserAuthException;
import java.io.*;

import models.Server;

public class DNSPlugin implements Plugin {
	
	public String getName() {
		return "DNS";
	}

	public CommandResult start(Server server)throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/bind9 start");
	}

	public CommandResult stop(Server server)throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/bind9 stop");
	}

	public CommandResult restart(Server server)throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/bind9 restart");
	}

	public int getStatus(Server server) throws IOException, UserAuthException {
		CommandResult cr = 
			SSHHelper.execCommand(server, "ps ax | grep -v grep | grep named");
		return cr.exitCode;
	}

	public String getConfiguration(Server server) throws IOException, UserAuthException {
		return SSHHelper.readFile(server, "/etc/bind/named.conf");
	}
}
