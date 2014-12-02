import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


public class ServiceInitializer {

	public static void main(String[] args){
		int port =5000;
		System.out.println("Server ON:" + port);
		
		Reactor reactor = new Reactor(port);
		
		try{
			Serializer serializer = new Persister();
			File source = new File("HandlerList.xml");
			ServerListData serverList = serializer.read(ServerListData.class, source);
			
			for(HandlerListData handlerListData : serverList.getServer()){
				
				if("server1".equals(handlerListData.getName())){
					List<HandlerData> handlerList = handlerListData.getHandler();
					for(HandlerData handler : handlerList){
						try{
							reactor.registerHandler(handler.getHeader(), (EventHandler) Class.forName(handler.getHandler()).newInstance());
						}catch(InstantiationException e){
							e.printStackTrace();
						}catch(IllegalAccessException e){
							e.printStackTrace();
						}catch(ClassNotFoundException e){
							e.printStackTrace();
						}
					}
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		/*
		reactor.registerHandler(new StreamSayHelloEventHandler());
		reactor.registerHandler(new StreamUpdateProfileEventHandler());
		*/
		reactor.startServer();
		/*
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			Dispatcher dispatcher = new Dispatcher();
			//Socket connection;
			
			while(true){
				
				//connection = serverSocket.accept();
				//InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
				//BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				//String line = bufferedReader.readLine();
				
				
				//System.out.println("READ: " + line);
				
				dispatcher.dispatch(serverSocket);
			}
		
		}catch(IOException e){
			e.printStackTrace();
		}
		*/
	}
}
