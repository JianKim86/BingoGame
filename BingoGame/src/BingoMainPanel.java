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
	private JPanel panelTop,panelCenter,panelBottom; //�⺻ panel;
	private JPanel panelNick, panelInfo; //center : �г���, , textArea;
	private JButton btnServer,btnRecord, btnIP, btnSave, btnEnt;// top : ������ư,���ڵ��ư; center : ������ã���ư,client �����ư; bottom : �����ϱ��ư
	private JTextField tfNick;//center : �г��ӹ��� �ʵ�
	private JLabel label;//center : ���Ӹ� ��
	private JTextArea taInfo;//center : �¸����ǵ� ���Ӽ����� �ִ� area	
	private InetAddress IP;
	private BingoPerson p;
	private String ip;//Ŭ���̾�Ʈ ������ 
	private String id; //Ŭ���̾�Ʈ �г���	
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
		//btnServer : ���� ���� ��ư
		btnServer = new JButton("SERVER");
		btnServer.setBorder(BorderFactory.createEmptyBorder(10 , 20 , 10 , 20));
		btnServer.setBackground(new Color(116, 116, 116));
		btnServer.setEnabled(false);
		//btnRecord : �·� ���
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
		tfNick.setText(" �г����� �Է����ּ���");
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
					tfNick.setText(" �г����� �Է��� �ּ���");
					tfNick.setForeground(Color.GRAY);
				}		
			}});
		
		btnSave = new JButton(" �� �� ");
		btnSave.setBorder(BorderFactory.createEmptyBorder(11 , 20 , 9 , 20));
		btnSave.setBackground(new Color(116, 116, 116));
		btnSave.setEnabled(false);
		btnSave.addActionListener(this);
		panelNick.add(tfNick);
		panelNick.add(btnSave,BorderLayout.EAST);
		//taxtArea
		panelInfo = new JPanel(new BorderLayout());

		label =new JLabel("�Բ� ���� �������");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBorder(BorderFactory.createEmptyBorder(9 , 19 , 9 , 19));
		label.setFont(new Font("Morph",Font.BOLD,16));

		taInfo = new JTextArea();
		//ta.setHorizontalAlignment(JTextField.CENTER_ALIGNMENT);		
		taInfo.setEditable(false);
		taInfo.setLineWrap(true);
		
		
		taInfo.setFont(new Font("���",Font.PLAIN,12));
		taInfo.append("  \n ���Ӽ���\n");
		taInfo.append("  ������ �����ڰ� 2�̻��ϰ�� Ȱ��ȭ �˴ϴ�.\n");
		taInfo.append("  ä��â�� �⺻ Ȱ��ȭ�̸�, ���� �� �ֽ��ϴ�. \n");
		taInfo.append("  RECORD���� ���� Runner�� Ȯ���� �����մϴ�.\n");
		taInfo.append("  ���� ����� ������ �����ϸ� ������ ���� 25ȸ�Դϴ�.\n\n");
		taInfo.append(" ���ۼ���\n");
		taInfo.append("  Click ��ư  > �г����Է�  > ���� >\n");
		taInfo.append("  (ù��° Ŭ���̾�Ʈ�� ����Ŭ��) > ����\n\n");
		taInfo.append(" �¸�����\n");
		taInfo.append("  5�� �̻��� ���� ������ �����ư Ȱ��ȭ, Ŭ���� �¸�\n");
		taInfo.append("  GiveUp ��ư Ŭ���� ��������\n");
		
		taInfo.setFocusable(false);

		panelInfo.add(label, BorderLayout.NORTH);
		panelInfo.add(taInfo);

		panelCenter.add(btnIP,BorderLayout.NORTH);
		panelCenter.add(panelNick);
		panelCenter.add(panelInfo,BorderLayout.SOUTH);	




//bottom
		panelBottom = new JPanel(new GridLayout(1, 0));		
		btnEnt = new JButton("Client ����");
		btnEnt.setEnabled(false);
		btnEnt.setBackground(new Color(116, 116, 116));
		btnEnt.setBorder(BorderFactory.createEmptyBorder(31 , 0 , 31 , 0));
		btnEnt.addActionListener(this);

		panelBottom.add(btnEnt);
		
		
		
		//���̱�
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
			//btnRecord : ��Ϻҷ�����
			if(e.getSource() == btnRecord) {
				new BingoLogs();
			}		
			//btnSever :���� �����ϱ�	
			if(e.getSource() == btnServer) {
				//���� ����
				 severs = new ServerThread();
				severs.start();
				
				btnServer.setEnabled(false);
				btnServer.setBackground(new Color(116, 116, 116));
			}//btnServer	
		//btnIP ������ã��
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
				
				if(tfNick.getText().length()!=0 && !(tfNick.getText().equals(" �г����� �Է��� �ּ���")) ) {
					btnSave.setEnabled(true);
					btnSave.setBackground(new Color(213, 213, 213));
					//btnSave.setBackground(new Color(250, 236, 197));
				}
				
			}//btnIP
		//btnSave �����ϱ�	
			if(e.getSource() == btnSave) {
				//��ư����
				if (tfNick.getText().equals(" �г����� �Է����ּ���") ) {

					JFrame frame = new JFrame("JOptionPane showMessageDialog example");
					frame.setTitle("");			
					JOptionPane.showMessageDialog( frame, "  �г����� �Է� �϶�� !!" ,"", JOptionPane.OK_CANCEL_OPTION);

					frame.dispose(); return;
				}

				//Ŭ���̾�Ʈ �κ��� ��
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
			//Ŭ���̾�Ʈ ���� ����
			ct= new ClientThread( p );ct.start();
			btnEnt.setEnabled(false);
			btnServer.setEnabled(false);
			btnRecord.setEnabled(true);
			btnRecord.setBackground(new Color(213, 213, 213));

			
			}//btnEnt
			
		}//actionPerformed
	
}//MainPanel