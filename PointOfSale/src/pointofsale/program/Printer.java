package pointofsale.program;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasa drukarki rachunkow.
 * Drukarka rachunkow posiada w miare uniwersalne metody i nie jest zaprogramowana pod konkretny format danych do druku.
 * @author Lukasz Dzwigulski
 *
 */
public class Printer {
	/**
	 * Nazwa marki tej drukarki.
	 */
	private String brandName;
	/**
	 * Kolor tej drukarki.
	 */
	private String color;
	/**
	 * Maksymalna predkosc wydruku tej drukarki (wartosc w mm/s).
	 */
	private int printSpeed;
	/**
	 * Szerokosc zadruku rachunku wychodzacego z tej drukarki (wartosc w mm).
	 */
	private int maxMediaSize;
	
	/**
	 * Prywatny konstruktor z argumentem Buildera.
	 * @param builder	builder klasy
	 */
	private Printer(final Builder builder){
		this.brandName = builder.brandName;
		this.color = builder.color;
		this.printSpeed = builder.printSpeed;
		this.maxMediaSize = builder.maxMediaSize;
	}
	
	/**
	 * Metoda drukujaca rachunek. Drukownaie odbywa sie poprzez wypisanie wszystkich przekazanych wartosci do pliku.
	 * @param receipt	lista z linijkami tekstu do wydruku
	 * @param path	sciezka do miejsca, w ktorym ma zostac zapisany plik
	 */
	public void print(ArrayList<String> receipt, String path){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path + "receipt.txt"));			
			for(String s : receipt){
				bw.write(s);
				bw.newLine();
			}			
			bw.close();			
		} catch (IOException e) {
			System.out.println("Blad przy drukownaiu rachunku!");
		}
	}
	
	public String getBrandName() {
		return brandName;
	}
	public String getColor() {
		return color;
	}
	public int getPrintSpeed() {
		return printSpeed;
	}
	public int getMaxMediaSize() {
		return maxMediaSize;
	}
	
	/**
	 * Statyczna klasa fluent buildera dla klasy Printer.
	 * @author Lukasz Dzwigulski
	 *
	 */
	public static class Builder{
		private String  brandName;
		private String color;
		private int printSpeed;
		private int maxMediaSize;
		
		public Builder brandName(String brandName){
			this.brandName = brandName;
			return this;
		}
		
		public Builder color(String color){
			this.color = color;
			return this;
		}
		
		public Builder printSpeed(int printSpeed){
			this.printSpeed = printSpeed;
			return this;
		}
		
		public Builder maxMediaSize(int maxMediaSize){
			this.maxMediaSize = maxMediaSize;
			return this;
		}
		
		public Printer build(){
			return new Printer(this);
		}
	}
}
