package pointofsale.program;

/**
 * Klasa wyswietlacza LCD.
 * Wyswietlacz wyswietla na swoim wyswietlaczu przekazany tekst, odpowiednio wczesniej sformatowany przez wyswietlacz.
 * @author Lukasz Dzwigulski
 *
 */
public class Display {
	/**
	 * Liczba rzedow tekstu jakie moze wyswietlic ten wyswietlacz.
	 */
	private int rows;
	/**
	 * Maksymalna liczba znakow jaka moze wyswietlic w jednym rzedzie ten wyswietlacz.
	 */
	private int characters;
	/**
	 * Nazwa marki tego wyswietlacza.
	 */
	private String brandName;
	/**
	 * NAzwa modelu tego wyswietlacza.
	 */
	private String modelName;
	/**
	 * Znaki aktualnie wyswietlane na wyswietlaczu.
	 */
	private char[][] displayedText;
	
	/**
	 * Prywatny konstruktor przyjmujacy jako parametr builder.
	 * @param builder	builder klasy
	 */
	private Display(final Builder builder){
		this.brandName = builder.brandName;
		this.modelName = builder.modelName;
		this.rows = builder.rows;
		this.characters = builder.characters;
		if(this.rows>0){
			this.displayedText = new char[this.rows][this.characters];
		}
	}
	
	/**
	 * Metoda ustawiajaca przekazany tekst na wyswietlaczu LCD.
	 * Przekazany tekst jest wczesniej odpowiednio skracany i dopasowywany do okienka wyswietlacza.
	 * @param text	tekst do wyswietlenia
	 */
	public void setDisplayedText(String text) {
		String[] splitedText = text.split("#");
		String toDisplay = "";
		
		if(displayedText==null){
			return;
		}
		else if(rows==1){
			int maxWordLength = (characters-splitedText.length+1)/splitedText.length;
			int index = 0;
			
			for(String s : splitedText){
				if(s.length() > maxWordLength){
					s = s.substring(0, maxWordLength);
				}
				index++;
				if(index<splitedText.length){
					toDisplay = toDisplay + s + " ";
				}
				else{
					toDisplay = toDisplay + s;
				}
			}
		}
		else{
			if(splitedText.length <= rows){
				for(String s : splitedText){
					if(s.length() > characters){
						s = s.substring(0, characters);
					}
					toDisplay = toDisplay + s + "#";
				}
			}
			else{
				int wordsPerRow = (splitedText.length/rows + 1);
				int maxWordLength = (characters-wordsPerRow+1)/wordsPerRow;
				int index = 1;
				
				for(String s : splitedText){
					if(s.length() >= maxWordLength){
						s = s.substring(0, maxWordLength-1);
					}
					
					if(index%wordsPerRow == 0){
						toDisplay = toDisplay + s + "#";
					}
					else{
						toDisplay = toDisplay + s + " ";
					}
					index++;
				}
			}
		}
		
		String[] linesToDisplay = toDisplay.split("#");
		displayedText = new char[rows][characters];
		
		for(int i=0; i < rows; i++){
			if (i < linesToDisplay.length){
				char[] toCopy = linesToDisplay[i].toCharArray();
				
				for(int j=0; j<toCopy.length; j++){
					displayedText[i][j] = toCopy[j];
				}
			}
		}
	}
	
	public char[][] getDisplayedText() {
		return displayedText;
	}
	public String getBrandName() {
		return brandName;
	}
	public String getModelName() {
		return modelName;
	}
	public int getRows() {
		return rows;
	}
	public int getCharacters() {
		return characters;
	}
	
	/**
	 * Statyczna klasa fluent buildera dla klasy Display.
	 * @author Lukasz Dzwigulski
	 *
	 */
	public static class Builder{
		private String brandName;
		private String modelName;
		private final int rows;
		private final int characters;
		
		public Builder(final int rows, final int characters){
			this.rows = rows;
			this.characters = characters;
		}
		
		public Builder brandName(String brandName){
			this.brandName = brandName;
			return this;
		}
		
		public Builder modelName(String modelName){
			this.modelName = modelName;
			return this;
		}
		
		public Display build(){
			return new Display(this);
		}
	}
}
