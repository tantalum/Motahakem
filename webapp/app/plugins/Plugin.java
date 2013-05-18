package plugins;

import helpers.CommandResult;
import net.schmizz.sshj.userauth.UserAuthException;
import java.io.IOException;

import models.Server;

public interface Plugin {
	public static final int SERVICE_RUNNING = 0;
	public static final int SERVICE_STOPPED = 1;

	public String getName();
	public CommandResult start(Server server) throws IOException, UserAuthException;
	public CommandResult stop(Server server) throws IOException, UserAuthException;
	public CommandResult restart(Server server) throws IOException, UserAuthException;
	public int getStatus(Server server) throws IOException, UserAuthException;
	public String getConfiguration(Server server) throws IOException, UserAuthException;
}
