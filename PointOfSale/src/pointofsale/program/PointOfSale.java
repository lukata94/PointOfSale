package pointofsale.program;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Klasa punktu sprzedazy.
 * W klasie tej jest cala logika dzialania punktu sprzedazy.
 * @author Lukasz Dzwigulski
 *
 */
public class PointOfSale {
	/**
	 * Instancja skanera kodow kreskowych.
	 */
	private BCScanner bcScanner;
	/**
	 * Instancja wyswietlacza LCD.
	 */
	private Display display;
	/**
	 * Instancja drukarki rachunkow.
	 */
	private Printer printer;
	
	/**
	 * Mapa, ktorej celem jest przechowywanie kodow kreskowych wraz z odpowiadajacym im nazwami produktow.
	 */
	private HashMap<String, String> productsMap = new HashMap<String, String>();
	/**
	 * Mapa, ktorej celem jest przechowywanie nazw produktow wraz z odpowiadajacym im cenami za sztuke.
	 */
	private HashMap<String, Integer> priceMap = new HashMap<String, Integer>();
	
	/**
	 * Skaner zczytujacy dane wejsciowe.
	 */
	private Scanner sc;
	
	/**
	 * Konstruktor z parametrem wejsciowym skanera.
	 * @param sc	skaner
	 */
	public PointOfSale(Scanner sc){
		this.sc = sc;
	}

	public BCScanner getBcScanner() {
		return bcScanner;
	}

	public Display getDisplay() {
		return display;
	}

	public Printer getPrinter() {
		return printer;
	}

	public HashMap<String, String> getProductsMap() {
		return productsMap;
	}

	public HashMap<String, Integer> getPriceMap() {
		return priceMap;
	}

	/**
	 * Metoda inicjalizujaca urzadzenia wejscia/wyjsia z domyslnymi wartosciami (na potrzeby funkcji main).
	 */
	public void initializeDefaultDevices(){
		this.bcScanner = new BCScanner.Builder()
								.brandName("HP")
								.color("Black")
								.lightSource("Red LED")
								.readingDistance(35)
								.build();
		
		this.printer = new Printer.Builder()
								.brandName("Epson")
								.color("Black")
								.printSpeed(80)
								.maxMediaSize(45)
								.build();
		
		this.display = new Display.Builder(2, 20)
								.brandName("Unbranded")
								.modelName("FX424KOL")
								.build();
	}
	
	/**
	 * Metoda inicjalizujaca urzadzenia wejscia/wyjscia na podstawie danych przekazanych metodzie.
	 * @param printer	drukarka rachunkow
	 * @param display	wyswietlacz LCD
	 * @param bcScanner	skaner kodow kreskowych
	 */
	public void initializeDevices(Printer printer, Display display, BCScanner bcScanner){
		this.printer = printer;
		this.display = display;
		this.bcScanner = bcScanner;
	}
	
	/**
	 * Metoda wczytujaca kody kreskowe produktow, nazwy produktow oraz ich wartsci.
	 * Plik wejsciowy musi byc odpowiednio zbudowany.
	 * @param pathDatabase	sciezka do pliku z baza danych
	 */
	public void loadDatabase(String pathDatabase){
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathDatabase));
			String textLine = null;
			while((textLine = br.readLine()) != null){
				String[] data = textLine.split(":");
				productsMap.put(data[0], data[1]);
				priceMap.put(data[1], Integer.parseInt(data[2]));
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Blad! Nie mozna znalezc pliku");
		} catch (IOException e) {
			System.out.println("Blad! Niepoprawna linia w pliku");
		}
	}
	
	/**
	 * Metoda zwracajaca true, jesli odczytany zostanie w skanerze ciag znakow "exit".
	 * @return	true jesli wpisane zostalo "exit", w innych przypadkach false
	 */
	public boolean ifExit(){
		String exit;
		exit = sc.nextLine();
		
		if(exit.equals("exit")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Metoda kasowania produktow w punkcie sprzedazy.
	 * Na poczatku skanowane sa produkty, nastepnie nastepuje podsumowanie zakupow i drukowany jest rachunek.
	 */
	public void startSell(String receiptPath){
		boolean exitFlag = false;
		int totalPrice = 0;
		String totalPriceString = "0";
		ArrayList<String> scannedProducts = new ArrayList<String>();
		
		while(!exitFlag){
			String barCode = bcScanner.scan(sc);
			
			if(barCode.equals("")){
				//ustawiamy 'Invalid bar-code'
				display.setDisplayedText("Invalid bar-code");
			}
			else if(productsMap.containsKey(barCode)){
				String product = productsMap.get(barCode);
				int price = priceMap.get(product);
				String priceToPrint = Integer.toString(price);
				priceToPrint = priceToPrint.substring(0, priceToPrint.length()-2) + "." + priceToPrint.substring(priceToPrint.length()-2);
				
				//ustawiamy 'Produkt + Cena' na LCD
				display.setDisplayedText(product + "#" + priceToPrint);
				
				//zapisujemy produkt z cena do listy produktow + dodajemy do sumy
				scannedProducts.add(product + ": " + priceToPrint);
				totalPrice += price;
			}
			else{
				//ustawiamy 'Product Not Found'
				display.setDisplayedText("Product Not Found");
			}
			
			exitFlag = ifExit();
		}
		
		if(!scannedProducts.isEmpty()){
			//ustawiam totalna kwote do postaci "zlotowki.grosze"
			String s = Integer.toString(totalPrice);
			totalPriceString = s.substring(0, s.length()-2) + "." + s.substring(s.length()-2);
			
			//ustawiamy na LCD laczna sume za zakupy
			display.setDisplayedText("Total:#" + totalPriceString);
			
			//przekazanie danych do druku drukarce
			scannedProducts.add("Total: " + totalPriceString);
			printer.print(scannedProducts, receiptPath);
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PointOfSale pof = new PointOfSale(sc);
		pof.initializeDefaultDevices();
		pof.loadDatabase("database/db.txt");
		pof.startSell("");
	}
}
