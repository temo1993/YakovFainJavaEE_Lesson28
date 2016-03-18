package tryit;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class StockDecoder implements Decoder.Text<Stock>{
    boolean willDecode = false;

	@Override
	public void init(EndpointConfig config) {}

	public Stock decode(String symbol) throws DecodeException {
        System.out.println("In Decoder: converting " + symbol + " into Stock object");
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        return stock;
	}

    public boolean willDecode(String symbol) {
		StockPriceGeneratorWebSocket stockPriceGenerator = new StockPriceGeneratorWebSocket();
        willDecode = (stockPriceGenerator.getSupportedSymbols().indexOf(symbol.toUpperCase()) != -1);
        return willDecode;
	}

	public void destroy() {}

    boolean decoderSupported(){
        return willDecode;
    }
}
