package plugins;

import helpers.*;
import net.schmizz.sshj.userauth.UserAuthException;
import java.io.*;

import models.Server;

public class MySQLPlugin implements Plugin {

	public String getName() {
		return "MySQL";
	}

	public CommandResult start(Server server) throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/mysql start");
	}

	public CommandResult stop(Server server) throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/mysql stop");
	}

	public CommandResult restart(Server server) throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/mysql restart");
	}

	public int getStatus(Server server) throws IOException, UserAuthException {
		CommandResult cr = 
			SSHHelper.execCommand(server, "ps ax | grep -v grep | grep mysqld");
		return cr.exitCode;
	}

	public String getConfiguration(Server server) throws IOException, UserAuthException {
		return SSHHelper.readFile(server, "/etc/mysql/my.cnf");
	}
}
