import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import javafx.scene.control.ScrollBar;

public class BingoMainPanel extends JPanel implements ActionListener{
	private JPanel panelTop,panelCenter,panelBottom; //기본 panel;
	private JPanel panelNick, panelInfo; //center : 닉네임, , textArea;
	private JButton btnServer,btnRecord, btnIP, btnSave, btnEnt;// top : 서버버튼,레코드버튼; center : 아이피찾기버튼,client 저장버튼; bottom : 입장하기버튼
	private JTextField tfNick;//center : 닉네임받을 필드
	private JLabel label;//center : 게임명 라벨
	private JTextArea taInfo;//center : 승리조건등 게임설명이 있는 area	
	private InetAddress IP;
	private BingoPerson p;
	private String ip;//클라이언트 아이피 
	private String id; //클라이언트 닉네임	
	private ServerThread severs; 
	public ClientThread ct;
	public BingoMainPanel() {
		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout(0,10));
		setOpaque(false);
		//setBorder(BorderFactory.createEmptyBorder(10 , 0 , 26 , 0));
//top
		panelTop = new JPanel(new BorderLayout(100,0));
		panelTop.setBorder(BorderFactory.createEmptyBorder(16 , 10 , 2 , 10));
		panelTop.setBackground(new Color(76, 76, 76));
		//btnServer : 서버 연동 버튼
		btnServer = new JButton("SERVER");
		btnServer.setBorder(BorderFactory.createEmptyBorder(10 , 20 , 10 , 20));
		btnServer.setBackground(new Color(116, 116, 116));
		btnServer.setEnabled(false);
		//btnRecord : 승률 기록
		btnRecord = new JButton("RECORD");
		btnRecord.setBorder(BorderFactory.createEmptyBorder(10 , 20 , 10 , 20));
		btnRecord.setBackground(new Color(116, 116, 116));
		btnRecord.setEnabled(false);
		
		btnServer.addActionListener(this);
		btnRecord.addActionListener(this);
		panelTop.add(btnRecord,BorderLayout.WEST);	
		panelTop.add(btnServer,BorderLayout.EAST);	

//center
		panelCenter = new JPanel(new BorderLayout(0,6));
		panelCenter.setBorder(BorderFactory.createEmptyBorder(8 , 10 , 0 , 10));
		panelCenter.setBackground(Color.YELLOW);
		panelCenter.setOpaque(false);
		
		//ip
		btnIP = new JButton("Click");
		btnIP.setFont(new Font("Morph",Font.BOLD,14));
		btnIP.setForeground(new Color(225, 112, 18));
		btnIP.setBorder(BorderFactory.createEmptyBorder(8 , 100 , 7 , 100));
		
		btnIP.setBackground(new Color(250, 236, 197));

		btnIP.addActionListener(this);
		
		//nick
		panelNick = new JPanel(new BorderLayout(4,10));
		panelNick.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 4 , 0));
		
		//panelNick.setBackground(new Color(71, 71, 71));
		panelNick.setOpaque(false);
		panelNick.setVisible(false); 

		tfNick = new JTextField(19);
		tfNick.setText(" 닉네임을 입력해주세요");
		tfNick.setForeground(Color.GRAY);
		tfNick.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(tfNick.isEditable()) {tfNick.setText("");
					tfNick.setForeground(Color.black);	
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(tfNick.getText().equals("")) {
					tfNick.setText(" 닉네임을 입력해 주세요");
					tfNick.setForeground(Color.GRAY);
				}		
			}});
		
		btnSave = new JButton(" 저 장 ");
		btnSave.setBorder(BorderFactory.createEmptyBorder(11 , 20 , 9 , 20));
		btnSave.setBackground(new Color(116, 116, 116));
		btnSave.setEnabled(false);
		btnSave.addActionListener(this);
		panelNick.add(tfNick);
		panelNick.add(btnSave,BorderLayout.EAST);
		//taxtArea
		panelInfo = new JPanel(new BorderLayout());

		label =new JLabel("함께 즐기는 빙고게임");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBorder(BorderFactory.createEmptyBorder(9 , 19 , 9 , 19));
		label.setFont(new Font("Morph",Font.BOLD,16));

		taInfo = new JTextArea();
		//ta.setHorizontalAlignment(JTextField.CENTER_ALIGNMENT);		
		taInfo.setEditable(false);
		taInfo.setLineWrap(true);
		
		
		taInfo.setFont(new Font("고딕",Font.PLAIN,12));
		taInfo.append("  \n 게임설명\n");
		taInfo.append("  게임은 참가자가 2이상일경우 활성화 됩니다.\n");
		taInfo.append("  채팅창은 기본 활성화이며, 숨길 수 있습니다. \n");
		taInfo.append("  RECORD에서 지난 Runner의 확인이 가능합니다.\n");
		taInfo.append("  여러 사람과 게임이 가능하며 보드의 턴은 25회입니다.\n\n");
		taInfo.append(" 시작순서\n");
		taInfo.append("  Click 버튼  > 닉네임입력  > 저장 >\n");
		taInfo.append("  (첫번째 클라이언트는 서버클릭) > 입장\n\n");
		taInfo.append(" 승리조건\n");
		taInfo.append("  5개 이상의 빙고를 보유시 빙고버튼 활성화, 클릭시 승리\n");
		taInfo.append("  GiveUp 버튼 클릭시 게임종료\n");
		
		taInfo.setFocusable(false);

		panelInfo.add(label, BorderLayout.NORTH);
		panelInfo.add(taInfo);

		panelCenter.add(btnIP,BorderLayout.NORTH);
		panelCenter.add(panelNick);
		panelCenter.add(panelInfo,BorderLayout.SOUTH);	




//bottom
		panelBottom = new JPanel(new GridLayout(1, 0));		
		btnEnt = new JButton("Client 입장");
		btnEnt.setEnabled(false);
		btnEnt.setBackground(new Color(116, 116, 116));
		btnEnt.setBorder(BorderFactory.createEmptyBorder(31 , 0 , 31 , 0));
		btnEnt.addActionListener(this);

		panelBottom.add(btnEnt);
		
		
		
		//붙이기
		add(panelTop,BorderLayout.NORTH);
		add(panelCenter,BorderLayout.CENTER);
		add(panelBottom,BorderLayout.SOUTH);	
	
	}//constructor	

	public JButton getBtnEnt() {
		return btnEnt;
	}
//listener
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//btnRecord : 기록불러오기
			if(e.getSource() == btnRecord) {
				new BingoLogs();
			}		
			//btnSever :서버 연결하기	
			if(e.getSource() == btnServer) {
				//서버 연결
				 severs = new ServerThread();
				severs.start();
				
				btnServer.setEnabled(false);
				btnServer.setBackground(new Color(116, 116, 116));
			}//btnServer	
		//btnIP 아이피찾기
			if(e.getSource() == btnIP) {
				try { 
					IP = InetAddress.getLocalHost(); 
					btnIP.setText(IP.getHostAddress());
					
					//System.out.println("Host Name = [" + ip.getHostName() + "]"); 
					//System.out.println("Host Address = [" + ip.getHostAddress() + "]"); 
					} catch (Exception arge) { System.out.println(arge); } 
				
				btnIP.setEnabled(false);
				btnIP.setBackground(new Color(196, 182, 143));
				tfNick.setBackground(new Color(250, 236, 197));
				btnIP.setVisible(false);
				panelNick.setVisible(true);
				
				if(tfNick.getText().length()!=0 && !(tfNick.getText().equals(" 닉네임을 입력해 주세요")) ) {
					btnSave.setEnabled(true);
					btnSave.setBackground(new Color(213, 213, 213));
					//btnSave.setBackground(new Color(250, 236, 197));
				}
				
			}//btnIP
		//btnSave 저장하기	
			if(e.getSource() == btnSave) {
				//버튼설정
				if (tfNick.getText().equals(" 닉네임을 입력해주세요") ) {

					JFrame frame = new JFrame("JOptionPane showMessageDialog example");
					frame.setTitle("");			
					JOptionPane.showMessageDialog( frame, "  닉네임을 입력 하라고 !!" ,"", JOptionPane.OK_CANCEL_OPTION);

					frame.dispose(); return;
				}

				//클라이언트 로보낼 값
				id = tfNick.getText();
				ip = btnIP.getText();
				p = new BingoPerson(id, ip);
				tfNick.setEditable(false);			
				btnSave.setEnabled(false);
				btnServer.setEnabled(true);
				btnEnt.setEnabled(true);
				btnSave.setBackground(new Color(116, 116, 116));
				btnEnt.setBackground(new Color(213, 213, 213));
				btnServer.setBackground(new Color(213, 213, 213));
				
			}//btnSave

			//btnEnt
			if(e.getSource() == btnEnt) {
			//클라이언트 서버 연결
			ct= new ClientThread( p );ct.start();
			btnEnt.setEnabled(false);
			btnServer.setEnabled(false);
			btnRecord.setEnabled(true);
			btnRecord.setBackground(new Color(213, 213, 213));

			
			}//btnEnt
			
		}//actionPerformed
	
}//MainPanel