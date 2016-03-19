package tryit;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint(value = "/stock",decoders = {StockDecoder.class}, encoders = {StockEncoder.class})
public class StockPriceGeneratorWebSocket {
    private List<String> supportedSymbols = new ArrayList<>();
    static ScheduledExecutorService timer =
            Executors.newSingleThreadScheduledExecutor();
    Set<Session> allSessions= Collections.synchronizedSet(new HashSet<>());

    public StockPriceGeneratorWebSocket() {
        supportedSymbols.add("AAPL");
        supportedSymbols.add("MSFT");
        supportedSymbols.add("YHOO");
        supportedSymbols.add("AMZN");
        supportedSymbols.add("IBM");
    }

    @OnMessage
    public Stock getPriceQuote(Stock stock, Session session) {
        allSessions = session.getOpenSessions();

        // start the scheduler on the very first connection
        if (allSessions.size() > 0) {
            timer.scheduleAtFixedRate(() -> stock.setPrice(Math.random()*100), 5, 5, TimeUnit.SECONDS);
            timer.scheduleAtFixedRate(() -> sendNewPrice(stock), 5, 5, TimeUnit.SECONDS);
        }
        stock.setPrice(Math.random()*100);

        return stock;
    }

    List<String> getSupportedSymbols() {
        return supportedSymbols;
    }

    String getValidSymbols() {
        StringBuilder symbols = new StringBuilder();
        for (String symbolName : supportedSymbols) {
            symbols.append(symbolName);
            symbols.append(" ");
        }
        return symbols.toString();
    }

    private void sendNewPrice(Stock stock) {

        for (Session sess : allSessions) {
            try {
                if (getSupportedSymbols().indexOf(StockDecoder.getSymbol().toUpperCase()) != -1) {
                    sess.getBasicRemote().sendText("New Price for " + stock.getSymbol() + " : " + stock.getPrice());
                }else{
                    allSessions.remove(sess);
                }


            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}