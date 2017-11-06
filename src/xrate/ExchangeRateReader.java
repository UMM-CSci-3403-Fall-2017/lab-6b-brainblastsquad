package xrate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Provide access to basic currency exchange rate services.
 * 
 * @author PUT YOUR TEAM NAME HERE
 */
public class ExchangeRateReader {

    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     * 
     * @param baseURL
     *            the base URL for requests
     */
	
	public String baseURL;
	
    public ExchangeRateReader(String baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     * 
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException, ParserConfigurationException, SAXException {
    	
    	// Creating the right url
        String url = baseURL + Integer.toString(year) + "/";
        String checkMonth = Integer.toString(month) + "/";
        String checkDay = Integer.toString(day);
        
        // Check if month or day is less than ten, if so add a 0 before the number
        if(month < 10) {
        	checkMonth = "0" + Integer.toString(month) + "/";
        	
        }
        
        if(day < 10) {
        	checkDay = "0" + Integer.toString(day);
        	
        }
        
        // Creates the full url
        url = url + checkMonth + checkDay + ".xml";
        
        URL finalURL = new URL(url);
        InputStream xmlStream = finalURL.openStream();
        
        // Creates a document in which we can parse through to find the information we need
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xmlStream);
        doc.getDocumentElement().normalize();
         
        // Creates two NodeLists from our documents infromation by the currency_code and rate tags
        NodeList codes = doc.getElementsByTagName("currency_code");
        NodeList rates = doc.getElementsByTagName("rate");
        
        // Loop to find currencyCode in the NodeList codes, once found sets toReturn to rates.item(i) where
        // i is the same index which currencyCode was found in codes.item(i)
        float toReturn = Float.MIN_VALUE;
        for(int i = 0; i < codes.getLength(); i++) {
        	if(codes.item(i).getNodeType() == codes.item(i).ELEMENT_NODE) {
        		if(currencyCode.equals(codes.item(i).getTextContent())) {
        		toReturn = new Float(rates.item(i).getTextContent());
        		}
        	}
        }
        
        return toReturn;
    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     * 
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public float getExchangeRate(String fromCurrency, String toCurrency, int year, int month, int day) throws IOException, ParserConfigurationException, SAXException {
    	return Float.MIN_VALUE; 
    }
}