package pointofsale.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.Test;
import pointofsale.program.*;

/**
 * Testy jednostkowe.
 * @author Lukasz Dzwigulski
 *
 */
public class DevicesTests {

	@Test
	public void testBCScanner() {
		String input = "1111111111111";
		Scanner scanner = new Scanner(input);
		BCScanner bcScanner = new BCScanner.Builder()
									.build();
		
		assertNotNull(bcScanner);
		assertEquals(input, bcScanner.scan(scanner));
	}

	@Test
	public void testDisplay1(){
		//test przy zerowej wartosci rzedow
		Display display = new Display.Builder(0, 16)
				.build();
		
		assertNull(display.getDisplayedText());
		display.setDisplayedText("test");
		assertNull(display.getDisplayedText());
		
		//przypadek gdy mamy tekst o dlugosci MNIEJSZEJ niz maksymalna ilosc znakow (przy liczbie rzedow = 1)
		String toDisplay = "test_p:#10.20";
		Display display2 = new Display.Builder(1, 16)
								.build();
		char displayedChars[][] = new char[1][16];
		char[] result = toDisplay.replaceAll("#", " ").toCharArray();
		for(int i=0; i<result.length; i++){
			displayedChars[0][i] = result[i];
		}
		
		assertNotNull(display2);
		display2.setDisplayedText(toDisplay);
		assertArrayEquals(displayedChars[0], display2.getDisplayedText()[0]);
		
		//przypadek gdy mamy tekst o dlugosci WIEKSZEJ niz maksymalna ilosc znakow (przy liczbie rzedow = 1)
		String toDisplay2 = "test_long_string:#5.50";
		char displayedChars2[][] = new char[1][16];
		char[] result2 = "test_lo 5.50".toCharArray();
		for(int i=0; i<result2.length; i++){
			displayedChars2[0][i] = result2[i];
		}
		
		display2.setDisplayedText(toDisplay2);
		assertArrayEquals(displayedChars2[0], display2.getDisplayedText()[0]);
	}
	
	@Test
	public void testDisplay2(){
		//przypadek gdy mamy wiecej lub rowno rzedow niz slow
		//oraz dlugosc slow jest mniejsza niz maksymalna liczba dozwolonych znakow
		String toDisplay = "test_product:#10.20";
		Display display = new Display.Builder(2, 16)
				.build();
		char displayedChars[][] = new char[2][16];
		String[] splitedToDisplay = toDisplay.split("#");
		for(int i=0; i < display.getRows(); i++){
			char[] toCopy = splitedToDisplay[i].toCharArray();
			
			for(int j=0; j<toCopy.length; j++){
				displayedChars[i][j] = toCopy[j];
			}
		}
		
		display.setDisplayedText(toDisplay);
		for(int i=0; i<display.getRows(); i++){
			assertArrayEquals(displayedChars[i], display.getDisplayedText()[i]);
		}
		
		//przypadek gdy mamy wiecej lub rowno rzedow niz slow
		//ale dlugosc slow w jakims rzedzie jest wieksza od dozwolonej
		String toDisplay2 = "test_product_long:#10.20";
		String cuttedToDisplay2 = "test_product_lon#10.20";
		char displayedChars2[][] = new char[2][16];
		String[] splitedToDisplay2 = cuttedToDisplay2.split("#");
		for(int i=0; i < display.getRows(); i++){
			char[] toCopy = splitedToDisplay2[i].toCharArray();
			
			for(int j=0; j<toCopy.length; j++){
				displayedChars2[i][j] = toCopy[j];
			}
		}
		
		display.setDisplayedText(toDisplay2);
		for(int i=0; i<display.getRows(); i++){
			assertArrayEquals(displayedChars2[i], display.getDisplayedText()[i]);
		}
	}
	
	@Test
	public void testDisplay3(){
		//przypadek gdy mamy mniej rzedow niz slow
		String toDisplay = "test_no_price#test_product:#10.20#test_pr:#401.81";
		Display display = new Display.Builder(2, 20)
				.build();
		char displayedChars[][] = new char[2][20];
		char[] cuttedToDisplay = "test_ test_ 10.20".toCharArray();
		char[] cuttedToDisplay2 = "test_ 401.8 ".toCharArray();
		for(int i=0; i<cuttedToDisplay.length; i++){
			displayedChars[0][i] = cuttedToDisplay[i];
		}
		for(int i=0; i<cuttedToDisplay2.length; i++){
			displayedChars[1][i] = cuttedToDisplay2[i];
		}
		
		display.setDisplayedText(toDisplay);
		for(int i=0; i<display.getRows(); i++){
			assertArrayEquals(displayedChars[i], display.getDisplayedText()[i]);
		}
	}
	
	@Test
	public void testPrinter(){
		//zapisac jakis ciag do pliku, nastepnie pobrac i porownac zawartosc
		Printer printer = new Printer.Builder()
								.build();
		ArrayList<String> listToPrint = new ArrayList<String>();
			listToPrint.add("First: 99.90");
			listToPrint.add("Second: 109.90");
		ArrayList<String> toCheck = new ArrayList<String>();
		
		assertNotNull(printer);
		printer.print(listToPrint, "testFiles/");
		File f = new File("testFiles/receipt.txt");
		assertTrue(f.exists());
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String textLine = null;
			while((textLine = br.readLine()) != null){
				toCheck.add(textLine);
			}
			br.close();
		} catch (FileNotFoundException e) {
			fail("Exception " + e);
		} catch (IOException e) {
			fail("Exception " + e);
		}
		assertEquals(listToPrint, toCheck);
	}
	
	@Test
	public void programRun(){
		//inicjalizacja danych do testu programu (dwa przypadki)
		Printer printer = new Printer.Builder()
						.build();
		Display display = new Display.Builder(2, 20)
						.build();
		BCScanner scanner = new BCScanner.Builder()
						.build();
		Printer printer2 = new Printer.Builder()
						.build();
		Display display2 = new Display.Builder(2, 20)
						.build();
		BCScanner scanner2 = new BCScanner.Builder()
						.build();
		
		try {
			Scanner sc1 = new Scanner(new File("testFiles/scannerCommands1.txt"));
			PointOfSale pos1 = new PointOfSale(sc1);
			Scanner sc2 = new Scanner(new File("testFiles/scannerCommands2.txt"));
			PointOfSale pos2 = new PointOfSale(sc2);
			
			//sprawdzenie poprawnosci inicjalizacji
			assertNotNull(pos1);
			pos1.initializeDevices(printer, display, scanner);
			assertNotNull(pos1.getPrinter());
			assertNotNull(pos1.getBcScanner());
			assertNotNull(pos1.getDisplay());
			
			assertNotNull(pos2);
			pos2.initializeDevices(printer2, display2, scanner2);
			assertNotNull(pos2.getPrinter());
			assertNotNull(pos2.getBcScanner());
			assertNotNull(pos2.getDisplay());
			
			//sprawdzenie poprawnosci wczytywania danych z bazy
			HashMap<String, String> productsMap = new HashMap<String, String>();
			HashMap<String, Integer> priceMap = new HashMap<String, Integer>();
			BufferedReader br = new BufferedReader(new FileReader("testFiles/db.txt"));
			String textLine = null;
			while((textLine = br.readLine()) != null){
				String[] data = textLine.split(":");
				productsMap.put(data[0], data[1]);
				priceMap.put(data[1], Integer.parseInt(data[2]));
			}
			br.close();
			
			pos1.loadDatabase("testFiles/db.txt");
			assertEquals(pos1.getPriceMap(), priceMap);
			assertEquals(pos1.getProductsMap(), productsMap);
			pos2.loadDatabase("testFiles/db.txt");
			assertEquals(pos2.getPriceMap(), priceMap);
			assertEquals(pos2.getProductsMap(), productsMap);
			
			//przewidywane dane po symulacji
			String toDisplay1 = "Total:#4.60";
			char displayedChars1[][] = new char[2][20];
			String[] splitedToDisplay = toDisplay1.split("#");
			for(int i=0; i < display.getRows(); i++){
				char[] toCopy = splitedToDisplay[i].toCharArray();
				
				for(int j=0; j<toCopy.length; j++){
					displayedChars1[i][j] = toCopy[j];
				}
			}
			
			String toDisplay2 = "Product Not Found";
			char displayedChars2[][] = new char[2][20];
			char[] result = toDisplay2.toCharArray();
			for(int i=0; i<result.length; i++){
				displayedChars2[0][i] = result[i];
			}
			
			//sprawdzenie wynikow symulacji dla przypadku pierwszego
			Path path = Paths.get("testFiles/receipt.txt");
			Files.deleteIfExists(path);
			
			pos1.startSell("testFiles/");
			for(int i=0; i<display.getRows(); i++){
				assertArrayEquals(displayedChars1[i], display.getDisplayedText()[i]);
			}
			
			ArrayList<String> listToPrint = new ArrayList<String>();
				listToPrint.add("cheese: 2.80");
				listToPrint.add("tomato: 1.80");
				listToPrint.add("Total: 4.60");
			ArrayList<String> toCheck = new ArrayList<String>();
			File f = new File("testFiles/receipt.txt");
			assertTrue(f.exists());
			BufferedReader receipt = new BufferedReader(new FileReader(f));
			String receiptTextLine = null;
			while((receiptTextLine = receipt.readLine()) != null){
				toCheck.add(receiptTextLine);
			}
			receipt.close();

			assertEquals(listToPrint, toCheck);
			
			//sprawdzenie wynikow symulacji dla przypadku drugiego
			Files.deleteIfExists(path);
			
			pos2.startSell("testFiles/");
			for(int i=0; i<display2.getRows(); i++){
				assertArrayEquals(displayedChars2[i], display2.getDisplayedText()[i]);
			}
			
			File f2 = new File("testFiles/receipt.txt");
			assertFalse(f2.exists());
			
		} catch (FileNotFoundException e) {
			fail("Exception " + e);
		} catch (NumberFormatException e) {
			fail("Exception " + e);
		} catch (IOException e) {
			fail("Exception " + e);
		}
	}
}
