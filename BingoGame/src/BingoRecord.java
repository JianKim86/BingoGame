import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class BingoRecord {
	
	private File oFile;
	private File path;
	private String sendTime;
	private Calendar today = Calendar.getInstance();
	private int year = today.get(Calendar.YEAR);
	private int month = (int)(today.get(Calendar.MONTH)+1);
	private int date = today.get(Calendar.DATE);
	private int min = today.get(Calendar.MINUTE);
	
	
	public String timeRecord(){
		sendTime = year+" : "+month+" : "+date+" : "+min;
		return sendTime;
	}

	
	//받은 스트링을 저장하기
	public void recordWinner(String serverId, String winner, String clientArr) {
		ArrayList<String>strList = new ArrayList<>();
		mkfileName();
		String fileName = serverId+"_"+year+month+date+min;
		oFile = new File(path,"FirstRunner_"+fileName+".txt");
		//get Data
		String winnerName = "@Winner:"+winner;
		String partinGame = "@RunnerList:"+clientArr;

		strList.add(winnerName);
		strList.add(partinGame);

		//save
		
		try {
			FileWriter fr = new FileWriter(oFile);
			PrintWriter pRwite = new PrintWriter(oFile);
			
			for(int i = 0; i<strList.size(); i++) {
				pRwite.println(strList.get(i));	
			}
			pRwite.flush();
			pRwite.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}//recordWinner

	
	private void mkfileName() {				
		
		String foldername = "BingoLog";
		path = new File("D://"+foldername);
		if(!path.exists()) path.mkdirs();
	}


}

class BingoLogs extends JFrame{
	   
	  // JTextField tf;
	   JTextArea textarea = new JTextArea();
	   JMenuBar menuBar=new JMenuBar();
	   public BingoLogs() {
	      // TODO Auto-generated constructor stub
	      
	 
	      //메뉴바 생성

		  setBounds(1200,80,400,570);
		  
			setPreferredSize(new Dimension(400, 400));		
			setUndecorated(true);
			setBackground(new Color(0, 0, 0,112)); //이게 검은색 같은데..
	      //메뉴1의 서브메뉴 생성
	      JMenuItem menuItem=null;
	    
	      JMenu menu1=new JMenu("파일 선택  ∨"); 
	      
	      menu1.setBackground(new Color(76, 76, 76));
	      menu1.setBorder(BorderFactory.createEmptyBorder(5 , 19 , 5 , 19));
	      //메뉴1("File")의 서브메뉴 생성 및 추가 , 메뉴선택 리스너 추가 
			String foldername = "BingoLog";
			File path = new File("D://"+foldername);
			if(!path.exists()) path.mkdirs();
			
			String returnData="";
			ArrayList<String>datas = new ArrayList<>();
			ArrayList<String>files = new ArrayList<>();
		
			File[] fileArr = path.listFiles();
			
			for(File f : fileArr) {
				files.add(f.getName());
			}//for
			if(files.size()<1) {
				menuItem=new JMenuItem("no have record ...");
				menu1.add(menuItem);
			}
			for(int i = 0; i<files.size(); i++) {

				String fileName = files.get(i);
				File fileR = new File(path,fileName);
				menuItem=new JMenuItem(fileR+"");
				menuItem.addActionListener(menuItemListener);
				menu1.add(menuItem);
			}
	      //메뉴바에 메뉴 추가
	      menuBar.add(menu1);	      
	      //JFrame에 메뉴바 설정
	      setJMenuBar(menuBar);
	      //메뉴 선택시의 결과 출력용 JTextField

	    textarea.setToolTipText("결과를 보여주는 영역입니다");
	      
	  	textarea.setLineWrap(true);
	  	textarea.setEditable(false);
	  	
	  	JScrollPane scrollbar = new JScrollPane(textarea);
	  	scrollbar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	  	add(scrollbar);
	  	
	  	JButton ext = new JButton("닫기");
	  	ext.setBorder(BorderFactory.createEmptyBorder(20 , 19 , 20 , 19));
	  	ext.setBackground(new Color(213, 213, 213));
	  	ext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
	  	setVisible(true);

		
		
	  	addWindowListener(new WindowAdapter() {
	  		@Override
	  		public void windowClosing(WindowEvent e) {
	  			// TODO Auto-generated method stub
	  			super.windowClosing(e);
	  			dispose();
	  		}
	  	});
	  	
	    add(scrollbar, BorderLayout.CENTER);
	    add(ext,BorderLayout.SOUTH);
	      
	      
	      pack();
	      setAlwaysOnTop(true);
	      setVisible(true);
	   }

	   //메뉴아이템(서브메뉴) 클릭 리스너 
	   ActionListener menuItemListener = new ActionListener() {
	      
	      @Override
	      public void actionPerformed(ActionEvent ei) {
	         // TODO Auto-generated method stub
	  		ArrayList<String>datas = new ArrayList<>();
	    	  File fileR = new File(ei.getActionCommand());
				//System.out.println(fileR);
				FileReader fr;
				
				try {
					fr = new FileReader(fileR);
					BufferedReader reader = new BufferedReader(fr);
					String line = reader.readLine();
					while(line != null) {
						datas.add(line);
						line = reader.readLine();
					}
					
					for(int i = 0 ; i<datas.size(); i++)
					{
						textarea.append(datas.get(i)+"\n");
					}

					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           
	      }
	   };
	   


}
