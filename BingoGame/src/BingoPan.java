
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;


public class BingoPan extends JPanel{
	
	NetThread net;
	private BingoPerson p;
	private String id;
	private BingoPerson bingP;
	final int SIZE = 5; //�������� ũ��
	int bingoCnt = 0;   //�ϼ��� ������ ��
	MyEventHandler handler; 
	private JButton [] btnArr = null;//������
	private boolean [][]bArr = new boolean[SIZE][SIZE];//������ üũ���θ� Ȯ���� ���� �迭
	private String[] values = new String[SIZE*SIZE]; //�����ǹ�� �迭
	private int cnt;
	public int getSIZE() { return SIZE;}
	public JButton[] getBtnArr() {return btnArr;}
	public String[] getValues() { return values; }
	private void setValues(int ver) {
		 
		String[] values1 = {
				 "1","2","3","4","5",
				 "6","7","8","9","10",
				 "11","12","13","14","15",
				 "16","17","18","19","20",
				 "21","22","23","24","25"
		};
		if (ver == 1) this.values = values1;

	/* else if(ver == 2){
		 String[] values2 = new String[SIZE*SIZE];
		 for(int i = 0 ; i<SIZE*SIZE;i++)
			 values2[i] = scan.nextLine();
		 this.values = values2;
	 }*/
		 
		 
	}

	 public BingoPan( int ver, BingoPerson p ,NetThread net){
		 
		 this.net = net;
		 this.p = p;
		 bingP = p;
		 this.id =p.getId();
		 net.setGui(this);
	  setLayout(new GridLayout(SIZE , SIZE,3,3));//awt ���μ��� 5���� ����
	  setBackground(new Color(76, 76, 76));

	  btnArr = new JButton[SIZE*SIZE];
	  if (ver == 1) this.shuffle(ver);//���� �������� ���´�
	  else if(ver == 2) this.setValues(ver);
 
	  
	  //���̱�
	  handler = new MyEventHandler();
//����	  
	//  Border thickBorder = new LineBorder(Color.WHITE, 27);
	  
	  for(int i = 0; i < SIZE*SIZE; i++){
		  btnArr[i] = new JButton(this.values[i]);
		  btnArr[i].setBorder(BorderFactory.createEmptyBorder(22 , 25 ,22 , 27));
		  btnArr[i].setFont(new Font("���",Font.BOLD,20));
		  btnArr[i].setBackground(new Color(246, 246, 246));

		  add(btnArr[i]);
		  btnArr[i].addActionListener(handler);
	  }  
	}//constructor
	 //getter
	 public BingoPerson getBingP() {
		return bingP;
	}

	public void sendbtnVar(String val) {
		// TODO Auto-generated method stub
				
			val = id+";v/"+ val;
	        net.sendMessage(val);
	        String ident =id+"@/";
	        net.sendMessage(ident);
	        String turnTnt = "@t/";
	        net.sendMessage(turnTnt);

	}
	
	
	public void openBtn() {
		
		for(int i = 0; i <  btnArr.length; i++){
			btnArr[i].setEnabled(true);
	
		}
	}
	public void rejBtn() {
		
			for(int i = 0; i <  btnArr.length; i++){
				btnArr[i].setEnabled(false);
			}
	}//rejBtn
	public void appendAble(String nextId) {
		
		if (id.equals(nextId)) {
			for(int i = 0; i <  btnArr.length; i++){
				btnArr[i].setEnabled(true);
			}
		}else {
			for(int i = 0; i <  btnArr.length; i++){
				btnArr[i].setEnabled(false);
			}
		}//����Ÿ�ڰ� �ƴϸ�  ��Ȱ��ȭ
	}//appendAble
	
	public void finishGame() {
		System.out.println("finish");
		
	}//finishGame
	
	public void appendClick( String msg ) {// taMsg.append(msg); 
		int s = msg.indexOf("/");
	
		
		msg = msg. substring(s+ 1, msg.length()); //��ư ��
		
		System.out.println(msg+"\n");
		JButton btn = null;
		for(int i = 0; i <  btnArr.length; i++){
			if(btnArr[i].getText().equals(msg)) {
				btn =btnArr[i];
			};
			
			for(int j = 0; j < btnArr.length; j++){//��ư�迭�� ���� ������ ��ư �̺�Ʈ�� ��ü�� ������ �ִٸ�..
				if(btnArr[j] == btn){
			if(bArr[j/SIZE][j%SIZE] == true){
				System.out.println("�̹� ���� ��ư�Դϴ�");
					}else{
						bArr[j/SIZE][j%SIZE] = true;
					}
					cnt++; bingP.setBingoCnt(cnt);
					//yellow
					//btnArr[j].setBackground(new Color(225, 112, 18));
					btnArr[j].setBackground(new Color(242, 222, 169));

					break;
				}
			}//for
			print();
			if(checkBingo() == true){
				System.out.println("Bingo~~!");				
			}//if
			
		}//for
         		

	}
	
	
	
//ver1 �迭�� ���� ������ ���´� �ߺ��Ȱ� �ݵ��� ����	  
	private void shuffle(int ver) {
	  // �ݺ����� ����ؼ� ���ڿ� �迭 values �� �� ����� ��ġ�� �ڹٲ۴�.
		setValues(ver);
		for(int i = 0; i < values.length * 2; i++){
			int r1 = (int)(Math.random() * values.length);//0~24
			int r2 = (int)(Math.random() * values.length);//0~24
	   //������ ��������
			String tmp = values[r1];
			values[r1] = values[r2];
			values[r2] = tmp;
		}
	}//shuffle	
//�ֿܼ��� Ȯ���ϱ����� �α�
	public void print(){//�迭 bArr�� ���
		for(int i = 0; i < bArr.length; i++){
			for(int j=0; j < bArr[i].length; j++){
				System.out.print(bArr[i][j]?"O":"X");//true ���ٸ� O �ƴϸ� X ���
			}
			System.out.println();
		}
		System.out.println("----------------------------------");
	  //System.out.println(this.bingoCnt);
	}//print

//����üũ����	
	public boolean checkBingo(){
		this.bingoCnt = 0;//�ϼ��� ������ ��
		int garoCnt = 0; //����
		int seroCnt = 0; //����
		int crossCnt1 = 0; //�밢��
		int corssCnt2 = 0; //���밢��
		for(int i = 0; i <  bArr.length; i++){
			for(int j = 0; j < bArr[i].length; j++){
			//���ΰ˻�
				if(bArr[i][j] == true){
					garoCnt++;
					if(garoCnt == SIZE){//2�� �迭�� 1�� �迭 �������� ������ �Ѵ� begin�� end�� �̿��Ͽ� �����
						int begin = i * SIZE ;
						int end = begin + SIZE ;
						for(int n = begin; n < end; n++) { 
							//btnArr[n].setBackground(new Color(165, 4, 0));
							btnArr[n].setBackground(new Color(225, 112, 18));
 }
					}
				}//���ΰ˻�
			//���ΰ˻�
				if(bArr[j][i]==true){
					seroCnt++;
					if(seroCnt == SIZE){
						for(int n = i; n < btnArr.length; n=n+5){//5�������Ͽ� ���� ����
							//btnArr[n].setBackground(new Color(165, 4, 0)); 
							btnArr[n].setBackground(new Color(225, 112, 18));
						}
					}
				}//���ΰ˻�
			//�밢���˻�	
				if( i==j && bArr[i][j]==true){
					crossCnt1++;
					if(crossCnt1 == SIZE){
						for(int n = 0; n < btnArr.length; n = n+6){//�밢������ 0���� 24���� 6�������Ͽ� ���󺯰�
							//btnArr[n].setBackground(Color.green); 
							//btnArr[n].setBackground(new Color(165, 4, 0));
							btnArr[n].setBackground(new Color(225, 112, 18));
						}
					}
				}//�밢���˻�
			//���밢�� �˻�	
				if( ((bArr.length - 1)-i) == j  && bArr[i][j]==true){
					corssCnt2++;
					if(corssCnt2 == SIZE){
						for(int n = (btnArr.length-4); n > 0; n--){//������ ������ ������ �˻�
							if(n % 4 ==0) {
								//btnArr[n].setBackground(Color.green); //case2
								//btnArr[n].setBackground(new Color(165, 4, 0));
								btnArr[n].setBackground(new Color(225, 112, 18));
							}
						}
					}
				}//���밢�� �˻�
			}//for(j)
			if(garoCnt == SIZE) bingoCnt++; 
			if(seroCnt == SIZE) bingoCnt++;  
			garoCnt = 0;
			seroCnt = 0;
		}//for(i)
		if(crossCnt1 == SIZE) bingoCnt++;  //�밢���� ���밢���� 1���ĸ� �˻����ָ�ȴ�
		if(corssCnt2 == SIZE) bingoCnt++; 
		
		bingP.setBingoCnt(bingoCnt);
		
		if(bingP.getBingoCnt()>=5)bingP.setIsBinGo(true);
	//	System.out.println(bingP.getIsWin());
		return bingoCnt >= SIZE;
	}//checkBingo

	//�������� ������ ���� 
	public BingoPerson getBigp() {
		return bingP;
	}

//������	
	class MyEventHandler extends WindowAdapter implements ActionListener {
	 @Override
	 public void actionPerformed(ActionEvent ae) {//��ư Ŭ���� actionPerformed �޼��� �������̵� �Ͽ� Event �ڵ鸵
		 // TODO Auto-generated method stub
		boolean ckTurnCnt = false; 
		JButton btn = (JButton)ae.getSource();            
		sendbtnVar(btn.getText());
		
		System.out.println(btn.getText());
		bingP.setVar(btn.getText());
		for(int i = 0; i < btnArr.length; i++){//��ư�迭�� ���� ������ ��ư �̺�Ʈ�� ��ü�� ������ �ִٸ�..
			if(btnArr[i] == btn){
		 //System.out.println((i/SIZE) + " ��"  + (i%SIZE) + " ��");
		if(bArr[i/SIZE][i%SIZE] == true){
			System.out.println("�̹� ���� ��ư�Դϴ�");
				}else{
					bArr[i/SIZE][i%SIZE] = true;
				}
		
				cnt++; bingP.setBingoCnt(cnt); 
				ckTurnCnt = true;
				bingP.setCkTurnCnt(ckTurnCnt);
				ckTurnCnt = false;
				
				//btnArr[i].setBackground(Color.YELLOW);
				//btnArr[i].setBackground(new Color(242, 150, 18));
				btnArr[i].setBackground(new Color(242, 222, 169));

				break;
			}
		}//for
		print();
		if(checkBingo() == true){
			
			System.out.println("Bingo~~!");
			
		}
	}//actionPerformed
	 
}	
	
	
}//BingoPan