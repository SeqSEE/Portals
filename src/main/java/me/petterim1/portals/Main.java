package me.petterim1.portals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.network.protocol.ScriptCustomEventPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

public class Main extends PluginBase implements Listener {

    private static final int configVersion = 3;

    public static Config config;
    
    private List<String> transferring;

    static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        transferring = new ArrayList<>();
        saveDefaultConfig();
        config = getConfig();
        if (config.getInt("configVersion") != configVersion) {
            getLogger().warning("Outdated config file, trying to update it automatically");
            config.set("configVersion", configVersion);
            config.save();
            config = getConfig();
        }
        getServer().getScheduler().scheduleDelayedRepeatingTask(this, new Task(), 10, 10);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent e) {	
		Player p = e.getPlayer();
		this.doneTransferring(p);
		
	}
    
    @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return CommandHandler.onCommand(sender, command, label, args);
	}

	public boolean isTransferring(Player p) {
		return this.transferring.contains(p.getUniqueId().toString());
	}

	public void setTransferring(Player p) {
		if (!this.transferring.contains(p.getUniqueId().toString())) this.transferring.add(p.getUniqueId().toString());		
	}
	
	public void doneTransferring(Player p) {
		if (this.transferring.contains(p.getUniqueId().toString())) this.transferring.remove(p.getUniqueId().toString());		
	}
}

class Task extends Thread {

    @Override
    public void run() {
        for (Player p : Server.getInstance().getOnlinePlayers().values()) {
        	if(p.isAlive() && p.spawned && !Main.instance.isTransferring(p)) {
        		if (Main.config.getSections("portals").size() > 0) {
                    Main.config.getSections("portals").forEach((s, o) -> {
                        ConfigSection c = (ConfigSection) o;
                        Portal portal = new Portal(Main.instance, c);
                        if (p.getLevel().equals(portal.getLevel())) {
                        	
                        	if(portal.getRotation().equalsIgnoreCase("x")) {
                        		if(portal.getType() == PortalType.NUKKIT) {
                        			 if (Math.round(p.getX() * 2) / 2.0 == portal.getX() && Math.round(p.getZ()) <= portal.getZ() + portal.getWidth() && Math.round(p.getZ()) >= portal.getZ()) {
                                         if (p.getY() >= portal.getY() && p.getY() <= portal.getY() + portal.getHeight()) {
                                             if (portal.isResetPosition()) {
                                                 p.teleport(p.getLevel().getSafeSpawn());
                                             }
                                             Server.getInstance().dispatchCommand(p, portal.getCommand());
                                         }
                                     }
                        		} else if(portal.getType() == PortalType.WATERDOG) {
                        			if (Math.round(p.getX() * 2) / 2.0 == portal.getX() && Math.round(p.getZ()) <= portal.getZ() + portal.getWidth() && Math.round(p.getZ()) >= portal.getZ()) {
                                        if (p.getY() >= portal.getY() && p.getY() <= portal.getY() + portal.getHeight()) {
                                        	Main.instance.setTransferring(p);
                                        	ScriptCustomEventPacket pk = new ScriptCustomEventPacket();
		    								pk.eventName = "portals:transfer";
		    								ByteArrayOutputStream out = new ByteArrayOutputStream();
		    								DataOutputStream a = new DataOutputStream(out);
		    								try {
		    									a.writeUTF(portal.getCommand());
		    								} catch (IOException e) {
		    									e.printStackTrace();
		    								}
		    								pk.eventData = out.toByteArray();
		    								p.dataPacket(pk);
                                        }
                                        
                        			}
                        			
                        		} else {
                        			Main.instance.getLogger().error("Unknown portal type: " + portal.getType());
                        		}
                        	} else if(portal.getRotation().equalsIgnoreCase("z")) {
                        		if(portal.getType() == PortalType.NUKKIT) {
                        			if (Math.round(p.getZ() * 2) / 2.0 == portal.getZ() && Math.round(p.getX()) <= portal.getX() + portal.getWidth() && Math.round(p.getX()) >= portal.getX()) {
                                        if (p.getY() >= portal.getY() && p.getY() <= portal.getY() + portal.getHeight()) {
                                            if (portal.isResetPosition()) {
                                                p.teleport(p.getLevel().getSafeSpawn());
                                            }
                                            Server.getInstance().dispatchCommand(p, portal.getCommand());
                                        }
                                    }
                        		} else if(portal.getType() == PortalType.WATERDOG) {
                        			if (Math.round(p.getZ() * 2) / 2.0 == portal.getZ() && Math.round(p.getX()) <= portal.getX() + portal.getWidth() && Math.round(p.getX()) >= portal.getX()) {
                                        if (p.getY() >= portal.getY() && p.getY() <= portal.getY() + portal.getHeight()) {
                                      	Main.instance.setTransferring(p);
                                        	ScriptCustomEventPacket pk = new ScriptCustomEventPacket();
		    								pk.eventName = "portals:transfer";
		    								ByteArrayOutputStream out = new ByteArrayOutputStream();
		    								DataOutputStream a = new DataOutputStream(out);
		    								try {
		    									a.writeUTF(portal.getCommand());
		    								} catch (IOException e) {
		    									e.printStackTrace();
		    								}
		    								pk.eventData = out.toByteArray();
		    								p.dataPacket(pk);
                                        }
                        			}
                        			
    								
                        		} else {
                        			Main.instance.getLogger().error("Unknown portal type: " + portal.getType());
                        		}
                        	} else {
                        		Main.instance.getLogger().error("Unknown portal rotation: " + c.getString("rotation"));
                        	}
                        }
                    });
                }
        	}
            
        }
    }
}
