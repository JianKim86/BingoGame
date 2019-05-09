
public class BingoPerson {
	private String id; //id 
	private String ip; //ip 
	private String var; //버튼값
	
	private int bingoCnt = 0; //빙고의 갯수
	private int turnCnt = 0;
	private boolean isBinGo = false; //5개이상일경우 true
	private boolean ckTurnCnt = false;
	private boolean isWin = false;
	public BingoPerson(String id, String ip) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.ip = ip;
	}
	//setter
	public void setVar(String var) {this.var = var;}
	public void setIp(String ip) { this.ip = ip; }
	public void setId(String id) { this.id = id; }
	public void setBingoCnt(int bingoCnt) { this.bingoCnt = bingoCnt; }
	public void setTurnCnt(int turnCnt) { this.turnCnt = turnCnt; }
	public void setIsBinGo(boolean isBinGo) { this.isBinGo = isBinGo; }
	public void setIsWin(boolean isWin) { this.isWin = isWin; }
	public void setCkTurnCnt(boolean ckTurnCnt) { this.ckTurnCnt = ckTurnCnt; }
	//getter
	public String getVar() { return var; }
	public String getId() { return id; }
	public String getIp() { return ip; }
	public boolean getIsWin() { return isWin; }
	public int getBingoCnt() { return bingoCnt; }
	public int getTurnCnt() { return turnCnt; }
	public boolean getIsBinGo() { return isBinGo; }
	public boolean getckTurnCnt(){return ckTurnCnt;}
	
}//Person
