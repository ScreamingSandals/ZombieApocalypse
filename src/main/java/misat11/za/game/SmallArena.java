package misat11.za.game;

import org.bukkit.Location;

public class SmallArena {

	public String name;
	public Location pos1;
	public Location pos2;
	
	public SmallArena(String name, Location pos1, Location pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.name = name;
	}
}
