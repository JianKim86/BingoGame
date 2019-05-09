import java.awt.Color; 
import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;

public class Main extends JFrame  {
	
	private JPanel panel; //�⺻ panel;
	private BingoMainPanel bingoMainpanel;
	public Main() {
		// TODO Auto-generated constructor stub		
		setTitle("Let's BinGo");
		setBounds(100,80,400,570);
		setLayout(null);
		//setUndecorated(true); //�̰� ����� Ÿ��Ʋ �ٰ� �������. 
		setBackground(new Color(53, 53, 53)); //�̰� ������ ������..
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		panel = new JPanel(); //��ü panel
		panel.setBackground(new Color(0,0,0,22));
		panel.setOpaque(false);
		bingoMainpanel = new BingoMainPanel();
		panel.add(bingoMainpanel);//�����г�
		//add(panel);
		setContentPane(panel);	
		getContentPane().setBackground(new Color(0,0,0,22));
		setVisible(true);
	
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		//������		

	//�ٹ̱�
	
	}//construct
	

	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main m = new Main();
	}//main();
	
	


	

}//Main

