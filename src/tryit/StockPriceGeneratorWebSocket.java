package tryit;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint(value = "/stock",decoders = {StockDecoder.class}, encoders = {StockEncoder.class})
public class StockPriceGeneratorWebSocket {
	
	private List<String> supportedSymbols = new ArrayList<>();
    static ScheduledExecutorService timer =
            Executors.newSingleThreadScheduledExecutor();

    private static Set<Session> allSessions;

    DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

	public StockPriceGeneratorWebSocket() {
		supportedSymbols.add("AAPL");
		supportedSymbols.add("MSFT");
		supportedSymbols.add("YHOO");
		supportedSymbols.add("AMZN");
		supportedSymbols.add("IBM");
	}

    @OnMessage
    public Stock getPriceQuote(Stock stock, Session session){
        allSessions = session.getOpenSessions();

        // start the scheduler on the very first connection
        if (allSessions.size()==1){
            timer.scheduleAtFixedRate(() ->sendTimeToAll(session),0,1, TimeUnit.SECONDS);
            timer.scheduleAtFixedRate(() ->sendNewPrice(stock,session),0,5, TimeUnit.SECONDS);
        }
        return stock;
    }

    List<String> getSupportedSymbols(){
            return supportedSymbols;
    }

    String getValidSymbols(){
        StringBuilder symbols = new StringBuilder();
        for (String symbolName : supportedSymbols) {
            symbols.append(symbolName);
            symbols.append(" ");
        }
        return symbols.toString();
    }

    private void sendTimeToAll(Session session){
        allSessions = session.getOpenSessions();

        for (Session sess: allSessions){
            try{
                sess.getBasicRemote().sendText("Local time: " +
                        LocalTime.now().format(timeFormatter));

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    private void sendNewPrice(Stock stock,Session session){
        allSessions = session.getOpenSessions();
        for (Session sess: allSessions){
            try{
                stock.setPrice(Math.random()*100);
                sess.getBasicRemote().sendText("New Price: " +
                       stock.getPrice());

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
