package plugins;

import java.util.*;

public class PluginManager {

	private static HashMap<String, Plugin> pluginMap;
	private static Boolean initialized = false; //Have the plugins been loaded into the map?

	public static Plugin pluginForName(String name) throws PluginNotFoundException {
		PluginManager.init();
		Plugin thePlugin = pluginMap.get(name);
		if(thePlugin == null) {
			throw new PluginNotFoundException("Cannot find ("+name+") plugin");
		}
		return thePlugin;
	}

	public static Collection<Plugin> allPlugins() {
		PluginManager.init();
		return pluginMap.values();
	}

	public static void init() {
		if(!PluginManager.initialized) {
			Plugin plug;
			PluginManager.pluginMap = new HashMap();
			
			//Add plugins to the map
			plug =  new DNSPlugin();
			PluginManager.pluginMap.put(plug.getName(), plug);
			plug = new DHCPPlugin();
			PluginManager.pluginMap.put(plug.getName(), plug);
			plug = new MySQLPlugin();
			PluginManager.pluginMap.put(plug.getName(), plug);
			plug = new PostfixPlugin();
			PluginManager.pluginMap.put(plug.getName(), plug);

			PluginManager.initialized = true;
		}
	}
}
