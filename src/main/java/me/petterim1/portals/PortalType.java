package me.petterim1.portals;

public enum PortalType {
	
	NUKKIT("nukkit"), WATERDOG("waterdog");
	
	private String name;
	private String requiredPermission;

	PortalType(String name) {
		this.name = name;
		this.requiredPermission = "portals.portal." + name;
	}

	public String getName() {
		return name;
	}

	public String getRequiredPermission() {
		return requiredPermission;
	}
	
	public static PortalType getByName(String name) {
		for(PortalType p : PortalType.values()) {
			if(p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return NUKKIT;
	}

}
