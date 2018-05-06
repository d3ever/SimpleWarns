package sexy.criss.simplewars.player;

public interface ISexyPlayer {
	
	int getCurrentWarns();
	void setWarns(int warns);
	void addWarn(int warns);
	void removeWarn(int warns);
	void save();
	
}
