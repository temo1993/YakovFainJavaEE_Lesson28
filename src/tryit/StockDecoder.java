package tryit;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class StockDecoder implements Decoder.Text<Stock>{
    private static String symbolEntered;

	@Override
	public void init(EndpointConfig config) {}

	public Stock decode(String symbol) throws DecodeException {
        System.out.println("In Decoder: converting " + symbol + " into Stock object");
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        return stock;
	}

    public boolean willDecode(String symbol) {
        symbolEntered = symbol;
		return true;
	}

	public void destroy() {}

    public static String getSymbol(){
        return symbolEntered;
    }
}
