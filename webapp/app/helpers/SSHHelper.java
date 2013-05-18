package helpers;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;

import java.io.*;
import java.util.concurrent.TimeUnit;

import models.Server;

public class SSHHelper {

	private static SSHClient connectTo(Server server) throws IOException, UserAuthException {
		SSHClient sshClient = new SSHClient();
		sshClient.loadKnownHosts();
		sshClient.connect(server.address);

		sshClient.authPassword(server.username, server.password);
			
		return sshClient;
	}

	public static CommandResult execCommand(Server server, String cmdString) throws IOException, UserAuthException {
		CommandResult result = new CommandResult();
		SSHClient sshClient = SSHHelper.connectTo(server);

		try {
			//Authenticate to the server
			final Session session = sshClient.startSession();
			try {
				final Command cmd = session.exec(cmdString);
				String output = IOUtils.readFully(cmd.getInputStream()).toString();
				String error = IOUtils.readFully(cmd.getErrorStream()).toString();
				cmd.join(5, TimeUnit.SECONDS);
				result.output = output;
				result.error = error;
				result.exitCode = cmd.getExitStatus();
			} finally {
				session.close();
			}
		} finally {
			sshClient.disconnect();
		}
		return result;
	}

	public static String readFile(Server server, String remoteFileName) throws IOException, UserAuthException {
		String line;
		StringBuffer strContent = new StringBuffer("");
		SSHClient sshClient = SSHHelper.connectTo(server);
		File tmpFile = File.createTempFile("confFrom", ".tmp");
		FileSystemFile fsf = new FileSystemFile(tmpFile);
		
		//Download file from the server
		try {
			final SFTPClient sftpClient = sshClient.newSFTPClient();
			try {
				sftpClient.get(remoteFileName, fsf);
			} finally {
				sftpClient.close();
			}
		} finally {
			sshClient.disconnect();
		}

		//Read the file
		File confFile = fsf.getFile();
		BufferedReader reader = new BufferedReader(new FileReader(confFile));
		while((line = reader.readLine()) != null){
			strContent.append(line+"\n");
		}
		return strContent.toString();
	}
}





