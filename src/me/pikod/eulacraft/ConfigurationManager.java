package me.pikod.eulacraft;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
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
				copyInputStreamToFile(plugin.getResource("config.yml"), settings_file);
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
	
	private void copyInputStreamToFile(InputStream inputStream, File file) throws Exception {
		OutputStream outputStream = new FileOutputStream(file);
		IOUtils.copy(inputStream, outputStream);
		outputStream.close();
		inputStream.close();
	}
}