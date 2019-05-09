
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
   public void run() { s = new BingoServer(); s.setting(); } }//ServerThread ���ΰ� �и� �۾�
class BingoServer {
    
    private ServerSocket server; // ���� ����
    private Socket socket = null; // �޾ƿ� ���� ����
    private String msg;
    static int clientCont=0;
    /** XXX 03. ����ڵ��� ������ �����ϴ� �� */ 
    private Map<String, DataOutputStream> clientMap = new HashMap<String, DataOutputStream>();//for chat
    private List<String> idList = new ArrayList<>();
    private ArrayList<String> uniqueIdAr;
    private int port = 1812; //��Ʈ��ȣ
    private static final int THREAD_CNT = 5; 
   
   public void setting() {
       try {
           
           Collections.synchronizedMap(clientMap); //���������� ���ش�.( clientMap�� ��Ʈ��ũ ó�����ִ°� ) 
           server = new ServerSocket(port);
          
           while (true) {
               /** XXX 01.������ ���� : �湮�ڸ� ��� �޾Ƽ�, ������ ���ù��� ��� ���� */
               
               System.out.println("�����.....");
               socket = server.accept(); // ���⼭ Ŭ���̾�Ʈ ����
               clientCont ++;
             //  System.out.println(clientCont);
               
               System.out.println(socket.getInetAddress() + "���� �����߽��ϴ�.");             
               Receiver receiver = new Receiver(socket);
               receiver.start();
           }
  
       } catch (IOException e) { }finally {
    	   	if (server != null) { try{server.close();} catch(IOException ex) {}}
       }



   }//setting
   
    public void addClient(String id, DataOutputStream out) throws IOException{
    	  
    	String message = id+"; - "+" [ " + id + " ] ���� �����ϼ̽��ϴ�.\n";
    	
    	sendMessage(message);
    	 
      
      clientMap.put(id, out);
      idList.add(id);
      //�ؽ����� ��̷�
      uniqueIdAr = new ArrayList<String>(new HashSet<String>(idList));
      System.out.println(uniqueIdAr.toString());
    }//addClient
       
   public void removeClient(String id){
       String message=id+"; - "+" [ " + id + " ] ���� �����̽��ϴ�. \n";
       String remain = "@remain/";
       clientMap.remove(id);
       idList.remove(id);
       uniqueIdAr.remove(id);
       clientCont --;
       sendMessage(uniqueIdAr.toString() + message);
       sendMessage(uniqueIdAr.toString() + remain);
       
 
       
   }//removeClient
    //�޼��� ���� ���� 
   

   
    public void sendMessage (String msg){

    	
        Iterator<String> iterator = clientMap.keySet().iterator(); //key������ �ݺ�������
        String key = "";
        
        
        while(iterator.hasNext()){
            key = iterator.next();// �ݺ��ڿ��� �ϳ��ϳ� Ű�� ���´�.
            
	            try{
	                clientMap.get(key).writeUTF( uniqueIdAr.toString() + msg);

	            } catch(IOException e){
	              //  e.printStackTrace();
	            }
            
        }//while
    }//sendMessage
    
    /** XXX ���ù��� ���� : ��Ʈ��ũ ������ �޾Ƽ� ��ӵ�� ������ ��. */
    class Receiver extends Thread {
        private DataInputStream in; // ������ �Է� ��Ʈ��
        private DataOutputStream out; // ������ �ƿ�ǲ ��Ʈ��
        private String id;
 
        public Receiver(Socket socket) {
        	
        	try {
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                id = in.readUTF();
                System.out.println(id+" ���� �����Ͽ����ϴ�.");
                        
                addClient(id,out);
                
            } catch (IOException e) {
                e.printStackTrace(); 
            } 	
        }
 
        @Override
        public void run() {
 
            try {
                while (in != null) {
                    msg = in.readUTF();// UTF�� �о���δ�.
                 
                    sendMessage(clientCont+"@Strart/");
                     sendMessage(msg);
                   
                }
            } catch (Exception e) {
                //������������ ���⼭ �����߻�. 
                removeClient(id);
            }
        }//run
    }//Receiver
   
}//Server
