import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


class ClientThread extends Thread{
	private BingoPerson p;
	BingoClient pp;
	public ClientThread(BingoPerson p) { this.p = p; }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		pp = new BingoClient(p);
	}
}//ClientThread

public class BingoClient extends JFrame{
	private BingoPerson p;
	//private BingoPerson bingoP; //돌려받는 p;
	private String ip, id; //ip,id
	private JPanel panel; //기본 panel;
	private int wf = 800;
	private BingoClientPanel clientPanel;

	public BingoClientPanel getClientPanel() { return clientPanel; }
	public BingoClient(BingoPerson p) {
		// TODO Auto-generated constructor stub
		this.p = p;
		//this.bingoP = p;
		this.id = p.getId();
		this.ip = p.getIp();
		
		setTitle("Let's BinGo");
		setBounds(400,200,wf,600);
		setResizable(false);
		setBackground(new Color(53, 53, 53)); 

		panel = new JPanel(); //전체 panel	  
		panel.setBackground(new Color(53, 53, 53)); 
//ui용			
		clientPanel = new BingoClientPanel(p);
		panel.add(clientPanel,BorderLayout.CENTER);//메인패널
		
		add(panel);
		setVisible(true);

//리스너
		clientPanel.getPanelLeft().getBtnChat().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				int w = clientPanel.getPanelLeft().getWidth();
				
				if (wf == 800) {setSize(w+36, 600); wf = w;}
				else { wf = 800; setSize(wf,600);}
				clientPanel.panelremove(wf);
			}
		});
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
			
		});
		
	//	windowhandle handler = new windowhandle();
	//	clientPanel.getPanelLeft().getBtnGvp().addActionListener(handler);
		
	}//constructor
	
/*	class windowhandle extends WindowAdapter implements ActionListener{
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			dispose();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			
		}
	}//Intently
		*/
	//p return


	
}//Client

//NetThread 
class NetThread extends Thread {
	private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private PanelChat chat;
    private PanelGame gamepanelTop;
    private BingoPan game;
    private BingoPerson p;
    private String msg,ip,id,winnerId,winnerRCD,clientCont;
    static int cont = 0;
    public NetThread(BingoPerson p) {
		// TODO Auto-generated constructor stub
    	this.p = p;
    	this.id = p.getId();
    	this.ip = p.getIp();

	}//constructor
  
    public void setGui(BingoPan game) {
        this.game = game;
    }
    public void setGui(PanelGame gamepanelTop) {
        this.gamepanelTop = gamepanelTop;
    }
    
    public void setGui(PanelChat chat) {
        this.chat = chat;
    }
    
    @Override
    public void run() {
    	// TODO Auto-generated method stub
    	connect();
    }
    
 
    public void connect(){
        
    	try {
            socket = new Socket(ip, 1812);
            System.out.println("서버에 연결됨");
            
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            
            //접속하자마자 닉네임 전송하면, 서버가 닉네임으로 인식 
            out.writeUTF(id);
            System.out.println("클라이언트 : 닉네임 전송완료 ");
            while(in!=null){
            	
            	msg = in.readUTF();
            /*	if(msg.charAt(1)!='[')
            	game.appendMsg(msg);*/
            	//else 
            	//체팅유무
            	
            	int r = msg.indexOf("]");//전체 참가자 명단
            	int s = msg.indexOf("[");
            	String ident; 
            	String ids = msg.substring(s+1,r);
            	String[] idArr = ids.split(", ");
            	ArrayList<String> idList = new ArrayList<>(Arrays.asList(idArr));
            	int idLength = idList.size();
            	String serverId =idList.get(0);
            	BingoRecord record = new BingoRecord();
            	msg = msg.substring(r+1,msg.length());//서버에서 받은 메세지 부분
            	System.out.println(msg);
            	System.out.println(Arrays.toString(idArr));
            	String clientArr = Arrays.toString(idArr);
           //채팅
            	if(msg.contains("; - ")){ 
            		chat.appendMsg(msg);//채팅 
            	}   	
          //게임
            	if(msg.contains("@GameStart/")) {
            		if (idLength<2) {game.rejBtn();}
            		else game.openBtn();
            		
            	}
            	if (msg.contains("@finishGame/")) {
            		game.rejBtn();
            		gamepanelTop.lockBtn();
            	}
            	/* 보수필요
            	if (msg.contains("@remain/")) {
            		if (idLength<2) { gamepanelTop.sendWin(); }
            		else {
            			String log = "; - " + Arrays.toString(idArr) +" 이 남았습니다.";
            			chat.appendMsg(log);
            		}
            	
            	
            	}*/	
	            //클릭버튼 전체표시	
            	else if (msg.contains(";v/")){ game.appendClick(msg); } //버튼벨류
	            //턴넘기기	
	            else if(msg.contains("@/")){ 
            		int l = msg.indexOf("@");
            		//System.out.println(l);
            		ident = msg.substring(0,l);
            		int k = 0;
            		for(int i = idLength-1; i>=0; i--) { 
            			if( idList.get(i).equals(ident)) { 
            				//System.out.println(i);
            				if(i>0) k = i-1;
            				else k = idLength-1;
            			}
            				String nextId = idList.get(k);
            				game.appendAble(nextId);
            			
            		}
            	}
            //게임패널 인포 턴수 표시	
            	else if (msg.contains("@t/")) {
            		cont++;
            		if (cont<= 25) {gamepanelTop.appendInfoChange(cont); game.finishGame(); }            		
            	}
            	
            	else if (msg.contains("@Strart/")) {
            		int l = msg.indexOf("@");
            		
            		clientCont = msg.substring(0,l);
            		gamepanelTop.appendlabelTurnChange(clientCont);
            		
            	}
            //다이알로그 빙고	
            	else if (msg.contains("@win/")) {
            		int l = msg.indexOf("@");
            		int sp = msg.indexOf("/");
            		winnerId = msg.substring(0,l);
            		winnerRCD = msg.substring(sp+1,msg.length());
            		//기록하기
            		record.recordWinner(serverId, winnerId,clientArr);
            		//체팅창에 알리기
            		gamepanelTop.appendWinLog(winnerId+" [ "+winnerRCD+" ]"+"  BinGo !!  ");
            		chat.appendMsg(";   "+winnerId+" [ "+winnerRCD+" ]"+"  BinGo !!  ");//채팅 
            		//게임종료
            		gamepanelTop.exitbye();
            	
            	}
            	
            }//
            
            
        } catch (IOException e) {
            //e.printStackTrace();
        	System.out.println("서버접속 실패");
        	JFrame frame = new JFrame("");		
			JOptionPane.showMessageDialog( frame, "  서버를 연결해주세요    " ,"", JOptionPane.OK_CANCEL_OPTION);

			System.exit(0);
        }   	

    	
    }//connect
    
 
    
    public void sendMessage(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace(); System.out.println("메세지전송실패");
        
        }
    }
    
    public void setNickname(String id){
        this.id = id;
    }

}//netThread
