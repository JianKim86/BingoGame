
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
	final int SIZE = 5; //빙고판의 크기
	int bingoCnt = 0;   //완성된 라인의 수
	MyEventHandler handler; 
	private JButton [] btnArr = null;//빙고판
	private boolean [][]bArr = new boolean[SIZE][SIZE];//빙고판 체크여부를 확인을 위한 배열
	private String[] values = new String[SIZE*SIZE]; //빙고판밸류 배열
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
	  setLayout(new GridLayout(SIZE , SIZE,3,3));//awt 가로세로 5개식 생성
	  setBackground(new Color(76, 76, 76));

	  btnArr = new JButton[SIZE*SIZE];
	  if (ver == 1) this.shuffle(ver);//값을 무작위로 섞는다
	  else if(ver == 2) this.setValues(ver);
 
	  
	  //붙이기
	  handler = new MyEventHandler();
//세팅	  
	//  Border thickBorder = new LineBorder(Color.WHITE, 27);
	  
	  for(int i = 0; i < SIZE*SIZE; i++){
		  btnArr[i] = new JButton(this.values[i]);
		  btnArr[i].setBorder(BorderFactory.createEmptyBorder(22 , 25 ,22 , 27));
		  btnArr[i].setFont(new Font("고딕",Font.BOLD,20));
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
		}//다음타자가 아니면  비활성화
	}//appendAble
	
	public void finishGame() {
		System.out.println("finish");
		
	}//finishGame
	
	public void appendClick( String msg ) {// taMsg.append(msg); 
		int s = msg.indexOf("/");
	
		
		msg = msg. substring(s+ 1, msg.length()); //버튼 값
		
		System.out.println(msg+"\n");
		JButton btn = null;
		for(int i = 0; i <  btnArr.length; i++){
			if(btnArr[i].getText().equals(msg)) {
				btn =btnArr[i];
			};
			
			for(int j = 0; j < btnArr.length; j++){//버튼배열과 현제 눌려진 버튼 이벤트의 객체가 같은게 있다면..
				if(btnArr[j] == btn){
			if(bArr[j/SIZE][j%SIZE] == true){
				System.out.println("이미 누른 버튼입니다");
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
	
	
	
//ver1 배열의 값을 난수로 섞는다 중복된값 반듯이 제외	  
	private void shuffle(int ver) {
	  // 반복문을 사용해서 문자열 배열 values 의 각 요소의 위치를 뒤바꾼다.
		setValues(ver);
		for(int i = 0; i < values.length * 2; i++){
			int r1 = (int)(Math.random() * values.length);//0~24
			int r2 = (int)(Math.random() * values.length);//0~24
	   //스와핑 로직삽입
			String tmp = values[r1];
			values[r1] = values[r2];
			values[r2] = tmp;
		}
	}//shuffle	
//콘솔에서 확인하기위한 로그
	public void print(){//배열 bArr을 출력
		for(int i = 0; i < bArr.length; i++){
			for(int j=0; j < bArr[i].length; j++){
				System.out.print(bArr[i][j]?"O":"X");//true 같다면 O 아니면 X 출력
			}
			System.out.println();
		}
		System.out.println("----------------------------------");
	  //System.out.println(this.bingoCnt);
	}//print

//빙고체크로직	
	public boolean checkBingo(){
		this.bingoCnt = 0;//완성된 라인의 수
		int garoCnt = 0; //가로
		int seroCnt = 0; //세로
		int crossCnt1 = 0; //대각선
		int corssCnt2 = 0; //역대각선
		for(int i = 0; i <  bArr.length; i++){
			for(int j = 0; j < bArr[i].length; j++){
			//가로검사
				if(bArr[i][j] == true){
					garoCnt++;
					if(garoCnt == SIZE){//2차 배열을 1차 배열 형식으로 만들어야 한다 begin과 end를 이용하여 만든다
						int begin = i * SIZE ;
						int end = begin + SIZE ;
						for(int n = begin; n < end; n++) { 
							//btnArr[n].setBackground(new Color(165, 4, 0));
							btnArr[n].setBackground(new Color(225, 112, 18));
 }
					}
				}//가로검사
			//세로검사
				if(bArr[j][i]==true){
					seroCnt++;
					if(seroCnt == SIZE){
						for(int n = i; n < btnArr.length; n=n+5){//5식증가하여 색상 변경
							//btnArr[n].setBackground(new Color(165, 4, 0)); 
							btnArr[n].setBackground(new Color(225, 112, 18));
						}
					}
				}//세로검사
			//대각선검사	
				if( i==j && bArr[i][j]==true){
					crossCnt1++;
					if(crossCnt1 == SIZE){
						for(int n = 0; n < btnArr.length; n = n+6){//대각선으로 0부터 24까지 6식증가하여 색상변경
							//btnArr[n].setBackground(Color.green); 
							//btnArr[n].setBackground(new Color(165, 4, 0));
							btnArr[n].setBackground(new Color(225, 112, 18));
						}
					}
				}//대각선검사
			//역대각선 검사	
				if( ((bArr.length - 1)-i) == j  && bArr[i][j]==true){
					corssCnt2++;
					if(corssCnt2 == SIZE){
						for(int n = (btnArr.length-4); n > 0; n--){//포문을 역으로 돌려서 검사
							if(n % 4 ==0) {
								//btnArr[n].setBackground(Color.green); //case2
								//btnArr[n].setBackground(new Color(165, 4, 0));
								btnArr[n].setBackground(new Color(225, 112, 18));
							}
						}
					}
				}//역대각선 검사
			}//for(j)
			if(garoCnt == SIZE) bingoCnt++; 
			if(seroCnt == SIZE) bingoCnt++;  
			garoCnt = 0;
			seroCnt = 0;
		}//for(i)
		if(crossCnt1 == SIZE) bingoCnt++;  //대각선과 역대각선은 1번식만 검사해주면된다
		if(corssCnt2 == SIZE) bingoCnt++; 
		
		bingP.setBingoCnt(bingoCnt);
		
		if(bingP.getBingoCnt()>=5)bingP.setIsBinGo(true);
	//	System.out.println(bingP.getIsWin());
		return bingoCnt >= SIZE;
	}//checkBingo

	//개인정보 리턴을 위한 
	public BingoPerson getBigp() {
		return bingP;
	}

//리스너	
	class MyEventHandler extends WindowAdapter implements ActionListener {
	 @Override
	 public void actionPerformed(ActionEvent ae) {//버튼 클릭스 actionPerformed 메서드 오버라이딩 하여 Event 핸들링
		 // TODO Auto-generated method stub
		boolean ckTurnCnt = false; 
		JButton btn = (JButton)ae.getSource();            
		sendbtnVar(btn.getText());
		
		System.out.println(btn.getText());
		bingP.setVar(btn.getText());
		for(int i = 0; i < btnArr.length; i++){//버튼배열과 현제 눌려진 버튼 이벤트의 객체가 같은게 있다면..
			if(btnArr[i] == btn){
		 //System.out.println((i/SIZE) + " 행"  + (i%SIZE) + " 열");
		if(bArr[i/SIZE][i%SIZE] == true){
			System.out.println("이미 누른 버튼입니다");
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