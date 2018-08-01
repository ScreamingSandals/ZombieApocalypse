package misat11.za.game;

import java.util.ArrayList;
import java.util.List;

public class PhaseInfo {
	
	private int countdown;
	private List<MonsterInfo> monsters = new ArrayList<MonsterInfo>();
	
	public PhaseInfo(int countdown) {
		this.countdown = countdown;
	}
	
	public void addMonster(MonsterInfo info) {
		this.monsters.add(info);
	}
	
	public void removeMonster(MonsterInfo info) {
		if (this.monsters.contains(info)) {
			this.monsters.remove(info);
		}
	}
	
	public int getCountdown() {
		return this.countdown;
	}
	
	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}
	
	public void phaseRun() {
		
	}
}
