package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import models.*;
import plugins.*;

import net.schmizz.sshj.userauth.UserAuthException;
import java.io.IOException;

@Security.Authenticated(Secured.class)
public class Plugins extends Controller {
	public static Result pluginPane(Long serverID, String pluginName){
		Server server = Server.find.ref(serverID);
		plugins.Plugin thePlugin;
		String config;
		
		server.restartServices();
		//Find the plugin
		try{
			thePlugin = PluginManager.pluginForName(pluginName);
		}catch(PluginNotFoundException ex){
			return badRequest("Plugin ("+pluginName+") does not exist");
		}

		//Make sure the server is online by grabbing the configuration
		try {
			config = thePlugin.getConfiguration(server);
		} catch(UserAuthException uae) {
			return badRequest("Failed to loging to server " + server.name);
		} catch(IOException ioe) {
			return badRequest("Failed to get configuration for "+thePlugin.getName() + ". Please make sure the server is online and that the " + thePlugin.getName() + " service is installed.");
		}
		
		return ok(views.html.pluginPane.render(thePlugin, server, config));
	}

	public static Result start(Long serverID, String pluginName){
		Server server = Server.find.ref(serverID);
		plugins.Plugin thePlugin;

		try {
			thePlugin = PluginManager.pluginForName(pluginName);
		}catch(PluginNotFoundException pnfe) {
			return badRequest("Plugin ("+pluginName+") does not exist");
		}

		try {
			thePlugin.start(server);
		} catch (UserAuthException uae) {
			return badRequest("Failed to login to server "+server.name);
		} catch (IOException ioe) {
			return badRequest("Failed to connect to server "+server.name+". Please make sure the server is online");
		}
		return redirect(routes.Plugins.pluginPane(server.id, pluginName));
	}

	public static Result stop(Long serverID, String pluginName){
		Server server = Server.find.ref(serverID);
		plugins.Plugin thePlugin;

		try {
			thePlugin = PluginManager.pluginForName(pluginName);
		}catch(PluginNotFoundException pnfe) {
			return badRequest("Plugin ("+pluginName+") does not exist");
		}

		try {
			thePlugin.stop(server);
		} catch (UserAuthException uae) {
			return badRequest("Failed to login to server "+server.name);
		} catch (IOException ioe) {
			return badRequest("Failed to connect to server "+server.name+". Please make sure the server is online");
		}
		return redirect(routes.Plugins.pluginPane(server.id, pluginName));
	}
	public static Result restart(Long serverID, String pluginName){
		Server server = Server.find.ref(serverID);
		plugins.Plugin thePlugin;

		try {
			thePlugin = PluginManager.pluginForName(pluginName);
		}catch(PluginNotFoundException pnfe) {
			return badRequest("Plugin ("+pluginName+") does not exist");
		}

		try {
			thePlugin.restart(server);
		} catch (UserAuthException uae) {
			return badRequest("Failed to login to server "+server.name);
		} catch (IOException ioe) {
			return badRequest("Failed to connect to server "+server.name+". Please make sure the server is online");
		}
		return redirect(routes.Plugins.pluginPane(server.id, pluginName));
	}
}
