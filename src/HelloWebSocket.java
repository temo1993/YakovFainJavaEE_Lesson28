import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/hello")
public class HelloWebSocket {
    @OnOpen
    public void greetTheClient(Session session){
        try{
            session.getBasicRemote().sendText("Hello Stranger!!!");
        }catch (IOException ioe){
            ioe.getMessage();
        }
    }
}
