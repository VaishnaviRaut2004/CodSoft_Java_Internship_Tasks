import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";


    public static double getExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        URL url = new URL(API_URL + baseCurrency);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

    
        String responseString = response.toString();
        String searchString = "\"" + targetCurrency + "\":";
        int startIndex = responseString.indexOf(searchString) + searchString.length();
        int endIndex = responseString.indexOf(",", startIndex);
        String rateString = responseString.substring(startIndex, endIndex).trim();

        return Double.parseDouble(rateString);
    }

    
    public static double convertCurrency(double amount, double exchangeRate) {
        return amount * exchangeRate;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("          Currency Converter             ");
        System.out.println("=========================================");

        System.out.print("Enter base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter target currency (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        try {
            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
            double convertedAmount = convertCurrency(amount, exchangeRate);
            
            System.out.println("=========================================");
            System.out.printf("Converted Amount: %.2f %s%n", convertedAmount, targetCurrency);
            System.out.printf("Exchange Rate: 1 %s = %.4f %s%n", baseCurrency, exchangeRate, targetCurrency);
            System.out.println("=========================================");
        } catch (Exception e) {
            System.out.println("Error fetching exchange rates: " + e.getMessage());
        }

        scanner.close();
    }
}