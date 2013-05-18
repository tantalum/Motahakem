package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import views.html.*;
import models.*;
import plugins.*;

import net.schmizz.sshj.userauth.UserAuthException;
import java.io.IOException;

import java.util.*;

@Security.Authenticated(Secured.class)
public class Servers extends Controller {
	
	public static Result servers() {
		Server theServer;
		List <Server> allServers = Server.all();
		Iterator servs = allServers.iterator();
		while(servs.hasNext()) {
			theServer = (Server)servs.next();
			theServer.restartServices();
		}

		return ok(
			views.html.servers.render(allServers, serverForm)
		);
	}

	public static Result newServer() {
		Form<Server> filledForm = serverForm.bindFromRequest();
		ArrayList<plugins.Plugin> toAutoRestart = new ArrayList<plugins.Plugin>();
		Iterator allPlugins = PluginManager.allPlugins().iterator();
		Map<String, String> formData = filledForm.data();
		while(allPlugins.hasNext()) {
			plugins.Plugin plugin = (plugins.Plugin)allPlugins.next();
			if(formData.containsKey(plugin.getName())){
				toAutoRestart.add(plugin);
			}
		}
		if(filledForm.hasErrors()) {
			return badRequest(
				views.html.newServer.render(filledForm)
			);
		} else {
			Server serv = filledForm.get();
			serv.autoRefreshPlugins = toAutoRestart;
			serv.joinAutoRefreshPlugins();
			serv.save();
			return redirect(routes.Servers.servers());
		}
	}

	public static Result status(Long serverId) {
		Server server = Server.find.ref(serverId);
		List<Ping> pings = Ping.findForServer(server);
		return ok(views.html.serverStatus.render(server, pings));
	}
	
	public static Result edit(Long id) {
		Server server = Server.find.ref(id);
		Form<Server> filledForm = serverForm.fill(server);
		return ok(views.html.editServer.render(filledForm));
	}

	public static Result update(Long id) {
		Form<Server> filledForm = serverForm.bindFromRequest();
		ArrayList<plugins.Plugin> toAutoRestart = new ArrayList<plugins.Plugin>();
		Iterator allPlugins = PluginManager.allPlugins().iterator();
		Map<String, String> formData = filledForm.data();
		while(allPlugins.hasNext()) {
			plugins.Plugin plugin = (plugins.Plugin)allPlugins.next();
			if(formData.containsKey(plugin.getName())){
				toAutoRestart.add(plugin);
			}
		}
		if(filledForm.hasErrors()) {
			return badRequest(
				views.html.editServer.render(filledForm)
			);
		} else {
			Server updated = filledForm.get();
			updated.autoRefreshPlugins = toAutoRestart;
			updated.joinAutoRefreshPlugins();
			updated.update(id);
			return redirect(routes.Servers.servers());
		}
	}

	public static Result reboot(Long serverID) {
		Server server = Server.find.ref(serverID);
		try {
			server.reboot();
		} catch(UserAuthException uae){
			return badRequest("User not allowed to reboot server");
		} catch(IOException ioe) {
			return badRequest("Failed to reboot server");
		} 
		return redirect(routes.Servers.servers());
	}

	public static Result remove(Long id){
		Server.remove(id);
		return redirect(routes.Servers.servers());
	}

	static Form<Server> serverForm = form(Server.class);

}
