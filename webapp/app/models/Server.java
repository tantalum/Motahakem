package models;

import java.util.*;
import javax.persistence.*;
import java.io.IOException;

import play.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;

import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.common.KeyType;

import helpers.*;
import plugins.*;

@Entity
public class Server extends Model {

	@Id public Long id;
	@Required public String name;
	@Required public String address;
	@Required public String username;
	@Required public String password;
	public String toAutoUpdate;
	public List<plugins.Plugin> autoRefreshPlugins;

// -------------------- STATIC FUNCTIONS ---------------------------
	public static List<Server> all() {
		return find.all();
	}

	public static void remove(Long id){
		find.ref(id).delete();
	}
// -------------------- MODEL FUNCTIONS ------------------------------
	public CommandResult getUptime() {
		CommandResult cr;
		try {
			cr = SSHHelper.execCommand(this, "uptime");
		} catch (UserAuthException uae) {
			cr = new CommandResult();
			cr.exitCode = 1;
			cr.output = "Login Failure";
		} catch (IOException ioe) {
			cr = new CommandResult();
			cr.exitCode = 1;
			cr.output = "Offline";
		}
		return cr;
	}

	public CommandResult reboot() throws IOException, UserAuthException {
		return SSHHelper.execCommand(this, "reboot");
	}

	public void restartServices() {
		splitAutoRefreshPlugins();
		int status;
		Iterator plugs = autoRefreshPlugins.iterator();
		while(plugs.hasNext()) {
			plugins.Plugin thePlugin = (plugins.Plugin)plugs.next();
			Logger.info("Refreshing: "+thePlugin.getName());
			try {
				status = thePlugin.getStatus(this);
			} catch (IOException e) {
				continue;
			}
			if(status == plugins.Plugin.SERVICE_STOPPED) {
				try {
					thePlugin.restart(this);
				} catch(IOException e) {
					//Do Nothing
				}
			}
		}
	}

	public static Finder<Long,Server> find = new Finder<Long,Server>(
		Long.class, Server.class
	);

// --------------------- HELPER FUNCTIONS ----------------------------

	public void splitAutoRefreshPlugins() {
		autoRefreshPlugins = new ArrayList<plugins.Plugin>();
		String [] names = toAutoUpdate.split(":");
		for(int i = 0; i < names.length; i++) {
			String pname = names[i];
			Logger.info("Looking for plugin: "+pname);
			try {
				plugins.Plugin thePlugin = PluginManager.pluginForName(pname);
				autoRefreshPlugins.add(thePlugin);
			} catch (PluginNotFoundException e) {
				continue;
			}
		}
		Logger.info("autoRefreshPlugins: " + autoRefreshPlugins);
	}

	public void joinAutoRefreshPlugins() {
		//splitAutoRefreshPlugins();
		this.toAutoUpdate = "";
		Logger.info("autoRefresh: " + autoRefreshPlugins);
		Iterator thePlugins = autoRefreshPlugins.iterator();
		while(thePlugins.hasNext()) {
			this.toAutoUpdate += ((plugins.Plugin)thePlugins.next()).getName()+":";
		}

	}
}
