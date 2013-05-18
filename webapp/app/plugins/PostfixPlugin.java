package plugins;

import helpers.*;

import net.schmizz.sshj.userauth.UserAuthException;
import java.io.*;

import models.Server;

public class PostfixPlugin implements Plugin {
	public String getName() {
		return "Postfix";
	}

	public CommandResult stop(Server server) throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/postfix stop");
	}

	public CommandResult start(Server server) throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/postfix start");
	}

	public CommandResult restart(Server server) throws IOException, UserAuthException {
		return SSHHelper.execCommand(server, "/etc/init.d/postfix restart");
	}

	public int getStatus(Server server) throws IOException, UserAuthException {
		CommandResult cr =
			SSHHelper.execCommand(server, "ps ax | grep -v grep | grep postfix/master");
		return cr.exitCode;
	}

	public String getConfiguration(Server server) throws IOException, UserAuthException {
		CommandResult result = SSHHelper.execCommand(server, "postconf");
		return result.output;
	}
}
