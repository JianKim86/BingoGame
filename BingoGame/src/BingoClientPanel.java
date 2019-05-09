import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


public class BingoClientPanel extends JPanel{
	
	private BingoPerson p;
	private JPanel panelGame,panelChat; //�⺻ panel;	
	private String ip,id;//Ŭ���̾�Ʈ ������ ���̵�
	private PanelGame panelLeft; //���� �г� : ����
	private PanelChat panelRight; //�������г� : ä��
	
	public JPanel getPanelChat() { return panelChat; }
	public JPanel getPanelGame() { return panelGame; }
	public PanelGame getPanelLeft() { return panelLeft; }
	public PanelChat getpanelRight() { return panelRight; }
	public NetThread net;
	
	public BingoClientPanel(BingoPerson p) {
		// TODO Auto-generated constructor stub
		this.p = p;
		this.id = p.getId();
		this.ip = p.getIp();
		setLayout(new GridLayout(1, 2));
	//��Ʈ��ũ ����
		net = new NetThread(p);
		net.start();

		setBackground(Color.black);
		panelGame = new JPanel(new BorderLayout());
		
		panelLeft = new PanelGame(p,net);
		Thread tL = new Thread(panelLeft);
		tL.start();
		
		panelChat = new JPanel(new BorderLayout());
		panelRight = new PanelChat(p,net);
	//	Thread tR = new Thread(panelRight);
	//	tR.start();
		
		//JButton b = new JButton("aa");
	
		panelGame.add(panelLeft);
		panelChat.add(panelRight);

		//���̱�
		//add(b);
		add(panelGame);
		add(panelChat);
		
	
	}//constructor
	
//hide Chatpanel
	
	public void panelremove(int w) {
		if(w == 800) setLayout(new GridLayout(1, 2));
		else setLayout(new CardLayout());
	//	panelLeft.CngBorder(w);

	}//panelremove
	
	
}//ClintPanel

class PanelGame extends JPanel implements Runnable, ActionListener{
	private BingoPerson p; 
	private BingoPerson bingP; //�����޴� p;
	private JPanel panelTop,panelBottom; //�⺻ panel;
	private JPanel panelInfo, panelChat; //top : �������� , ���̵�,ä�ù� ���̵忩��;
	private BingoPan bingo;
	private JLabel labelTurn,labelId,labelInfo;//top
	private String id, ip;
	private JButton btnChat,btnGvp,btnWin;
	private int clientCnt=0; //���ӿ� ������ Ŭ���̾�Ʈ ��
	private final int turnCnt = 25; //�ϼ�
	private int widF = 800;
	private int cnt = 0; //���� ��
	private NetThread net;
	private int cont = 0;//�ϼ�
	
	public JButton getBtnChat() {return btnChat;}
	public void setClientCont(int clientCnt){ //sever���� ����
		this.clientCnt = clientCnt;
	}
	public JPanel getPanelTop() {return panelTop; }
	public JButton getBtnGvp() { return btnGvp; }
	public PanelGame(BingoPerson p, NetThread net) {
		// TODO Auto-generated constructor stub
		this.p = p;
		this.id = p.getId();
		this.ip = p.getIp();
		this.net = net;
			
	setLayout(new BorderLayout(10, 10));
	setBackground(new Color(53, 53, 53)); //�̰� ������ ������..
	//setBorder(BorderFactory.createEmptyBorder(16 , 0 , 26 , 10));
//top	
	panelTop = new JPanel(new GridLayout(2,1));
	panelTop.setBackground(new Color(53, 53, 53)); //�̰� ������ ������..
	panelTop.setBorder(BorderFactory.createEmptyBorder(16 , 0 , 0 , 0));
	
	//chat
	panelChat = new JPanel(new BorderLayout());
	panelChat.setBackground(new Color(76, 76, 76));

	btnChat = new JButton(">"); //���߿� �̹����� ��ü
	btnChat.setBorder(BorderFactory.createEmptyBorder(4 , 4 , 4 , 4));
	btnChat.setBackground(new Color(213, 213, 213));
	panelChat.add(btnChat,BorderLayout.EAST);
	

	//info
	panelInfo = new JPanel(new GridLayout(1, 3));
	panelInfo.setBackground(new Color(76, 76, 76));
	
	labelTurn = new JLabel("");
	labelTurn.setForeground(Color.white);
	labelTurn.setHorizontalAlignment(SwingConstants.LEFT);	
	labelId = new JLabel(id);
	labelId.setForeground(Color.white);
	labelId.setHorizontalAlignment(SwingConstants.CENTER);
	labelInfo = new JLabel(cont + " / "+ turnCnt);
	labelInfo.setForeground(Color.white);
	labelInfo.setHorizontalAlignment(SwingConstants.RIGHT);
	panelInfo.add(labelTurn);
	panelInfo.add(labelId);
	panelInfo.add(labelInfo);
	
	panelTop.add(panelChat);
	panelTop.add(panelInfo);
//center
	bingo = new BingoPan(1, p, net);
//bottom	
	panelBottom = new JPanel(new GridLayout(1, 2));
	btnGvp = new JButton("Giveup(EXIT)");
	btnGvp.setBorder(BorderFactory.createEmptyBorder(30 , 0 , 30 , 0));
	btnGvp.setBackground(new Color(213, 213, 213));
	btnWin = new JButton("Bingo");
	btnWin.setBorder(BorderFactory.createEmptyBorder(30 , 0 , 30 , 0));
	btnWin.setBackground(new Color(116, 116, 116));

	btnWin.setEnabled(false);
	panelBottom.add(btnGvp);
	panelBottom.add(btnWin);
	btnWin.addActionListener(this);
	btnGvp.addActionListener(this);
	
	
	add(panelTop,BorderLayout.NORTH);
	add(bingo,BorderLayout.CENTER);
	add(panelBottom,BorderLayout.SOUTH);

	//Thread tG = new Thread(bingo);
	//tG.start();

	//net = new NetThread(p);
	net.setGui(this);
	//net.start();
	startGame();

	}//constructor
	public void exitbye() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public String winnerCheck() {
		BingoRecord record = new BingoRecord();
		String logMsg = id+"@win/"+ record.timeRecord();
		return logMsg;
	}
	public void sendWin() {
		btnWin.setBackground(new Color(213, 213, 213));
		net.sendMessage(winnerCheck());
	}
	
	//���� ���� ���̾� �α�
	public void startGame() {
		JFrame frame = new JFrame("");
		JOptionPane.showMessageDialog( frame, "     ���ݺ��� ������ ��������" ," START", JOptionPane.OK_CANCEL_OPTION);

		frame.dispose();
		net.sendMessage("@GameStart/");
	}
	//winner ���̾� ����
	public void appendWinLog(String logMsg) {
		
		JFrame frame = new JFrame("JOptionPane showMessageDialog example");
		//frame.setTitle("WINNER");
		JOptionPane.showMessageDialog( frame, logMsg ," WINNER", JOptionPane.OK_CANCEL_OPTION);
		//JOptionPane.showMessageDialog( frame, logMsg );
		
		frame.dispose();
		btnWin.setEnabled(false);
		net.sendMessage("@finishGame/");
//ssss		
		}
	
	public void lockBtn() {
	//	btnWin.setEnabled(false);
		btnGvp.setEnabled(false);
		btnGvp.setBackground(new Color(116, 116, 116));
		
	}
	//�ϼ�
	public void appendInfoChange(int cont) {
		labelInfo.setText(cont+" / "+ turnCnt);
		
	}
	//���� �ο�
	public void appendlabelTurnChange(String clientCont) {
		labelTurn.setText("���� �ο�  : "+clientCont);
		
	}
//panelTop �е��ٲٱ�
	public void CngBorder(int w) {
		if (w != 800) panelTop.setBorder(BorderFactory.createEmptyBorder(16 , 10 , 0 , 10));
		else panelTop.setBorder(BorderFactory.createEmptyBorder(16 , 10 , 0 , 10));	
	}
//p return
	public void setBigP(BingoPerson bingP) {
		this.bingP = bingP;
	}
	public BingoPerson getBigp() {
		return bingP;
	}
	
	
//�����ư Ȱ��ȭ 	
	public void runBing() {
		btnWin.setEnabled(true);
		btnWin.setBackground(new Color(225, 112, 18));

		btnGvp.setEnabled(false);
		btnGvp.setBackground(new Color(116, 116, 116));

	 }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		boolean isBing = true;
		
		//���Ϲ޴� p�� ����
		while (isBing) {
			
			//�����ư Ȱ��ȭ
		//	
			setBigP(bingo.getBigp());
			getBigp().setCkTurnCnt(bingo.getBigp().getckTurnCnt()); //���� ����
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean r = (getBigp().getckTurnCnt());
			if (r) {
			
				if(getBigp().getIsBinGo()) {runBing(); isBing = false;}
				//System.out.println("5��������"+getBigp().getIsBinGo());
			}
		}
		
	}//run
//������
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	//������ ũ�� ��ȭ ä��â ����� ���̱�
		if(e.getSource() == btnChat) {
			//FilesIO���� ��� �޼ҵ� ȣ��				
			if(widF==800) widF = 400;
			else widF = 800;
		}

		if(e.getSource() == btnWin) {
			//������ ���̾�α� ����
			sendWin();
			//���̾�α� �޼ҵ� ����
			lockBtn();
		}
	
		if(e.getSource() == btnGvp) {
			JFrame frame = new JFrame("");
			//frame.setTitle("FINISH");
			JOptionPane.showMessageDialog( frame, "������ �����մϴ�." ," FINISH", JOptionPane.OK_CANCEL_OPTION);
			//JOptionPane.showMessageDialog( frame, "������ �����մϴ�." );
			System.exit(0);			
		}
	
	}//actionPerformed

	//������ ���ڸ� ������ �޼ҵ�
	
	
}//PanelGame



class PanelChat extends JPanel{
	private BingoPerson p;
	private JPanel panelBottom; //�⺻ panel;
	private String id;
	private String ip;
	private JButton btnSand;// Bottom : �������ư
	private JTextField tfMsg;//bottom : ä�ó��� �Է� �ʵ�
	private JTextArea taMsg;//center : ä�ó����� ���̴� �����	
	private NetThread net;
	public PanelChat(BingoPerson p, NetThread net) {
		// TODO Auto-generated constructor stub
		this.net = net;
		this.p = p;
		this.id = p.getId();
		this.ip = p.getIp();

	setLayout(new BorderLayout(10, 10));
	setBorder(BorderFactory.createEmptyBorder(78 , 10 , 0 , 0));
	setBackground(new Color(53, 53, 53)); //�̰� ������ ������..
	
	taMsg = new JTextArea();
	taMsg.setEditable(false);
	taMsg.setLineWrap(true);
	JScrollPane scrollta = new JScrollPane(taMsg);
	
	scrollta.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	
	
	panelBottom = new JPanel(new BorderLayout(0,10));
	tfMsg = new JTextField();
	btnSand = new JButton("sand");
	
	panelBottom.add(tfMsg);
	panelBottom.add(btnSand,BorderLayout.EAST);
	
	add(scrollta,BorderLayout.CENTER);
	add(panelBottom,BorderLayout.SOUTH);
	
	tfMsg.addKeyListener(new KeyAdapter() {
		
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			//super.keyPressed(e);
			int keyCode = e.getKeyCode();
			
			switch(keyCode) {
			case KeyEvent.VK_ENTER:
				sendmsg();
				break;
			}
		}
	});
	btnSand.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			sendmsg();
		}
	});
	
//	net = new NetThread(p);
	net.setGui(this);
	

	}//constructor
	
	public void appendMsg( String msg ) { 
		int l = msg.indexOf(";");
		msg = msg.substring(l+1, msg.length());
		
		taMsg.append(msg); 
		
	};

	public void sendmsg() {
		// TODO Auto-generated method stub
				
		 String msg = id+"; - "+id+" : "+ tfMsg.getText() + "\n";
	        net.sendMessage(msg);
	       // appendMsg(msg);
	        tfMsg.setText("");
	}
	
	
}