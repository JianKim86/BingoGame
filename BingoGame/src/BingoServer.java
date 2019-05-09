
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class ServerThread extends Thread{
	BingoServer s;
	
   @Override
   public void run() { s = new BingoServer(); s.setting(); } }//ServerThread 메인과 분리 작업
class BingoServer {
    
    private ServerSocket server; // 서버 소켓
    private Socket socket = null; // 받아올 소켓 저장
    private String msg;
    static int clientCont=0;
    /** XXX 03. 사용자들의 정보를 저장하는 맵 */ 
    private Map<String, DataOutputStream> clientMap = new HashMap<String, DataOutputStream>();//for chat
    private List<String> idList = new ArrayList<>();
    private ArrayList<String> uniqueIdAr;
    private int port = 1812; //포트번호
    private static final int THREAD_CNT = 5; 
   
   public void setting() {
       try {
           
           Collections.synchronizedMap(clientMap); //교통정리를 해준다.( clientMap을 네트워크 처리해주는것 ) 
           server = new ServerSocket(port);
          
           while (true) {
               /** XXX 01.서버가 할일 : 방문자를 계속 받아서, 쓰레드 리시버를 계속 생성 */
               
               System.out.println("대기중.....");
               socket = server.accept(); // 여기서 클라이언트 받음
               clientCont ++;
             //  System.out.println(clientCont);
               
               System.out.println(socket.getInetAddress() + "에서 접속했습니다.");             
               Receiver receiver = new Receiver(socket);
               receiver.start();
           }
  
       } catch (IOException e) { }finally {
    	   	if (server != null) { try{server.close();} catch(IOException ex) {}}
       }



   }//setting
   
    public void addClient(String id, DataOutputStream out) throws IOException{
    	  
    	String message = id+"; - "+" [ " + id + " ] 님이 접속하셨습니다.\n";
    	
    	sendMessage(message);
    	 
      
      clientMap.put(id, out);
      idList.add(id);
      //해쉬맵을 어레이로
      uniqueIdAr = new ArrayList<String>(new HashSet<String>(idList));
      System.out.println(uniqueIdAr.toString());
    }//addClient
       
   public void removeClient(String id){
       String message=id+"; - "+" [ " + id + " ] 님이 나가셨습니다. \n";
       String remain = "@remain/";
       clientMap.remove(id);
       idList.remove(id);
       uniqueIdAr.remove(id);
       clientCont --;
       sendMessage(uniqueIdAr.toString() + message);
       sendMessage(uniqueIdAr.toString() + remain);
       
 
       
   }//removeClient
    //메세지 내용 전파 
   

   
    public void sendMessage (String msg){

    	
        Iterator<String> iterator = clientMap.keySet().iterator(); //key셋으로 반복자지정
        String key = "";
        
        
        while(iterator.hasNext()){
            key = iterator.next();// 반복자에서 하나하나 키를 빼온다.
            
	            try{
	                clientMap.get(key).writeUTF( uniqueIdAr.toString() + msg);

	            } catch(IOException e){
	              //  e.printStackTrace();
	            }
            
        }//while
    }//sendMessage
    
    /** XXX 리시버가 할일 : 네트워크 소켓을 받아서 계속듣고 보내는 일. */
    class Receiver extends Thread {
        private DataInputStream in; // 데이터 입력 스트림
        private DataOutputStream out; // 데이터 아웃풋 스트림
        private String id;
 
        public Receiver(Socket socket) {
        	
        	try {
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                id = in.readUTF();
                System.out.println(id+" 님이 입장하였습니다.");
                        
                addClient(id,out);
                
            } catch (IOException e) {
                e.printStackTrace(); 
            } 	
        }
 
        @Override
        public void run() {
 
            try {
                while (in != null) {
                    msg = in.readUTF();// UTF로 읽어들인다.
                 
                    sendMessage(clientCont+"@Strart/");
                     sendMessage(msg);
                   
                }
            } catch (Exception e) {
                //사용접속종료시 여기서 에러발생. 
                removeClient(id);
            }
        }//run
    }//Receiver
   
}//Server
