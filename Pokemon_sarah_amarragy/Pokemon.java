//Bill's comment

package enhancedPokemon;

public class Pokemon implements Comparable<Pokemon>{
	// CLASS VARIABLES
	private String name;
	private String color;
	private int hitPoints;
	private boolean hasTrainer;
	
	// Constructors
	public Pokemon(String name, String color) {
		this.name = name;
		this.color = color;
		this.hitPoints = 15;
		this.hasTrainer = false;
	}
	
	public Pokemon(String name, String color, int hitPoints, boolean hasTrainer) {
		this.name = name;
		this.color = color;
		this.hitPoints = hitPoints;
		this.hasTrainer = hasTrainer;
	}
	
	public Pokemon() {
		this.name = "";
		this.color = "";
		this.hitPoints = 15;
		this.hasTrainer = false;
	}
	
	// Accessors and Mutators (Setters and Getters)
	public String getName() {
		return this.name;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public Boolean hasTrainer() {
		return this.hasTrainer;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	public boolean isHasTrainer() {
		return hasTrainer;
	}

	public void setHasTrainer(boolean hasTrainer) {
		this.hasTrainer = hasTrainer;
	}

	public void setHasTrainer(Boolean hasTrainer) {
		this.hasTrainer = hasTrainer;
	}
	
	// Other methods
	public String toString() {
		return "I am a Pokemon: " + this.name + " : " + this.color + " : " + this.hasTrainer() +
				" : " + this.hitPoints;
	}

	public int compareTo(Pokemon pokemon) {
		return this.name.compareTo(pokemon.name);
	}
	
}
