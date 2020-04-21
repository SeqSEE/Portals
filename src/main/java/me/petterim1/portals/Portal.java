package me.petterim1.portals;

import cn.nukkit.level.Level;
import cn.nukkit.utils.ConfigSection;

public class Portal {

	private PortalType type;
	private Level level;
	private String rotation;
	private int x;
	private double y;
	private int z;
	private boolean resetPosition;
	private double height;
	private double width;
	private String command;

	public Portal(Main portals, ConfigSection config) {
		this.type =  PortalType.getByName(config.getString("type", "nukkit"));
		Level l = portals.getServer().getLevelByName(config.getString("world", "world"));
		this.level = l != null ? l : portals.getServer().getDefaultLevel();
		this.rotation = config.getString("rotation", "x");
		this.x = config.getInt("x", this.level.getSpawnLocation().asBlockVector3().getX());
		this.y = config.getDouble("y", this.level.getSpawnLocation().getY());
		this.z = config.getInt("z", this.level.getSpawnLocation().asBlockVector3().getX());
		this.command = config.getString("command", "none");
		this.height = config.getDouble("height", 2.0);
		this.width = config.getDouble("width", 2.0);
		this.resetPosition = config.getBoolean("resetPosition", true);
	}

	public PortalType getType() {
		return type;
	}

	public Level getLevel() {
		return level;
	}

	public String getRotation() {
		return rotation;
	}

	public int getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public boolean isResetPosition() {
		return resetPosition;
	}

	public void setResetPosition(boolean resetPosition) {
		this.resetPosition = resetPosition;
	}

	public double getHeight() {
		return this.height;
	}

	public double getWidth() {
		return this.width;
	}

	public String getCommand() {
		return this.command;
	}
}
