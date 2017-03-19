package pointofsale.program;

import java.util.Scanner;

/**
 * Klasa skanera kodow kreskowych.
 * Skaner skanuje tekst ktory jest kodem i przekazuje go w metodzie.
 * @author Lukasz Dzwigulski
 *
 */
public class BCScanner {
	/**
	 * Nazwa marki tego skanera.
	 */
	private String brandName;
	/**
	 * Kolor tego skanera.
	 */
	private String color;
	/**
	 * Zrodlo swiatla z jakiego korzysta ten skaner.
	 */
	private String lightSource;
	/**
	 * Maksymalna odleglosc z jakiej skaner potrafi odczytac kod (wartosc w cm).
	 */
	private int readingDistance;
	
	/**
	 * Prywatny konstruktor skanera z argumentem buildera.
	 * @param builder	builder skanera
	 */
	private BCScanner(final Builder builder){
		this.brandName = builder.brandName;
		this.color = builder.color;
		this.lightSource = builder.lightSource;
		this.readingDistance = builder.readingDistance;
	}
	
	/**
	 * Metoda skanujaca i przekazywujaca zeskanowany kod.
	 * @param sc	zrodlo wprowadzania danych do zeskanowania
	 * @return	zeskanowany kod
	 */
	public String scan(Scanner sc){
		String code;
		code = sc.nextLine();
		return code;
	}
	
	public String getBrandName() {
		return brandName;
	}

	public String getColor() {
		return color;
	}

	public String getLightSource() {
		return lightSource;
	}

	public int getReadingDistance() {
		return readingDistance;
	}

	/**
	 * Statyczna klasa fluent buildera dla klasy BCSkanner.
	 * @author Lukasz Dzwigulski
	 *
	 */
	public static class Builder{
		private String brandName;
		private String color;
		private String lightSource;
		private int readingDistance;
		
		public Builder brandName(String brandName){
			this.brandName = brandName;
			return this;
		}
		
		public Builder color(String color){
			this.color = color;
			return this;
		}
		
		public Builder lightSource(String lightSource){
			this.lightSource = lightSource;
			return this;
		}
		
		public Builder readingDistance(int readingDistance){
			this.readingDistance = readingDistance;
			return this;
		}
		
		public BCScanner build(){
			return new BCScanner(this);
		}
	}
}
