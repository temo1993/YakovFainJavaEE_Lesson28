package tryit;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class StockEncoder implements Encoder.Text<Stock>{

	public void init(EndpointConfig config) {}

	public String encode(Stock stock) throws EncodeException {
        StockPriceGeneratorWebSocket stocks = new StockPriceGeneratorWebSocket();
        String encode;
        if (stocks.getSupportedSymbols().indexOf(StockDecoder.getSymbol().toUpperCase()) != -1) {
            System.out.println("In Encoder: Serializing Stock object into String");
            encode = stock.getSymbol() + ": " + stock.getPrice();
        } else {
            encode = "Not a valid symbol.Use one of these: " + stocks.getValidSymbols();
        }
        return encode;
	}

	public void destroy() {}
}
