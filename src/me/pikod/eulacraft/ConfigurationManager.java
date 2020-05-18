package me.pikod.eulacraft;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationManager {
	private File mainFolder;
	
	private File settings_file;
	private YamlConfiguration settings;
	
	private File userdata_file;
	private YamlConfiguration userdata;
	
	private Plugin plugin;
	
	public ConfigurationManager(Plugin plugin) {
		//Set private variables
		this.plugin = plugin;
		mainFolder = plugin.getDataFolder();
		reloadConfig();
	}
	
	public void reloadConfig() {
		if(!mainFolder.exists()) {
			mainFolder.mkdirs();
		}
		settings_file = new File(mainFolder.getPath()+"/config.yml" );
		userdata_file = new File(mainFolder.getPath()+"/data.yml" );
		
		if(!userdata_file.exists()) {
			try {
				userdata_file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
			
		if(!settings_file.exists()) {
			try {
				copy(plugin.getResource("config.yml"), settings_file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		settings = YamlConfiguration.loadConfiguration(settings_file);
		userdata = YamlConfiguration.loadConfiguration(userdata_file);
	}
	
	public void saveUserData() {
		if(!userdata_file.exists()){
			try {
				userdata_file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			userdata.save(userdata_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public YamlConfiguration getSettings() {
		return settings;
	}
	
	public YamlConfiguration getUserData() {
		if(!userdata_file.exists()){
			try {
				userdata_file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userdata;
	}
	public void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}