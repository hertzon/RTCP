import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class RandSave {

	public static void main(String[] args) throws IOException  {
		// TODO Auto-generated method stub
		 ServerSocket serverSocket = null; 
		 Connection conDB = null;
		 int prescaler=0;

		    try { 
		         serverSocket = new ServerSocket(20500); 
		        } 
		    catch (IOException e) 
		        { 
		         System.err.println("No se puede escuchar el puerto: 2055"); 
		         System.exit(1); 
		        } 

		    Socket clientSocket = null; 
		    System.out.println ("Esperando por la conexion del ESP8266...");

		    try { 
		         clientSocket = serverSocket.accept(); 
		        } 
		    catch (IOException e) 
		        { 
		         System.err.println("Accept failed."); 
		         System.exit(1); 
		        } 

		    System.out.println ("Conexion Ok");
		    System.out.println ("Esperando dato de temperatura.....");

		    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
		                                      true); 
		    BufferedReader in = new BufferedReader( 
		            new InputStreamReader( clientSocket.getInputStream())); 
		    clientSocket.setSoTimeout(10000);

		    String inputLine; 

		    while ((inputLine = in.readLine()) != null){ 
		    	Date date = new Date();
		    	Timestamp time=new Timestamp(date.getTime());
		         System.out.println ("Temperatura llega de arduino y ESP8266: " + inputLine+" a las: "+time); 
		         //out.println("Me enviaste: "+inputLine); 
		         

		         if (inputLine.equals("Bye.")){
		             break; 
		         }
		         prescaler++;
		         if (prescaler==15){
		        	 prescaler=0;
		         
			         try {
						Class.forName("org.postgresql.Driver");
						String url="jdbc:postgresql://localhost:5432/postgres";
						try {
							conDB=DriverManager.getConnection(url,"postgres","irf840");
							String query="INSERT INTO temps(temp,time) VALUES("+"'"+inputLine+"'"+","+"'"+time+"'"+");";
							System.out.println("query: "+query+"\r\n"+"\r\n");
							Statement st=conDB.createStatement();
							
							st.executeUpdate(query);
							
							conDB.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//1
							System.out.println("error en 1: "+e);
							e.printStackTrace();
						}
						
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						//2
						System.out.println("error en 2: "+e);
						e.printStackTrace();
					}
		         }
		         
		   } 

		    out.close(); 
		    in.close(); 
		    clientSocket.close(); 
		    serverSocket.close(); 
		   } 

	

}
