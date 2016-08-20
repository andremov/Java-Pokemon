/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemonviolet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 *
 * @author Andres
 */
public class Item {
	static int NUMATTRIB = 10;
	
    private int id;
	private String nameSingular;
	private String namePlural;
	private String nameInternal;
	private int pocket;
	private int price;
	private String description;
	private int useOutBattle;
	private int useInBattle;
	private int pokeRate; 
	private int amount;

	public Item(int id) {
		this.id = id;
		this.amount = 1;
		
		boolean couldCreate = readInfo( this.id );
		
		if (!couldCreate){
			System.err.println("Could not find item with id " + id + ".");
		}
	}
	
	public Item(int id, int amount){
		this.id = id;
		this.amount = amount;
		
		boolean couldCreate = readInfo( this.id );
		
		if (!couldCreate){
			System.err.println("Could not find item with id " + id + ".");
		}
	}

	public Item(String internalName) {
		this.id = getItemID(internalName);
		this.amount = 1;
		
		boolean couldCreate = readInfo( this.id );
		
		if (!couldCreate){
			System.err.println("Could not find item with id " + id + ".");
		}
	}
	
	public Item(String internalName, int amount){
		this.id = getItemID(internalName);
		this.amount = amount;
		
		boolean couldCreate = readInfo( this.id );
		
		if (!couldCreate){
			System.err.println("Could not find item with id " + id + ".");
		}
	}

	private boolean readInfo(int id){
		boolean success = false;
		
		File archivo = new File("listItems.txt");
		try{
			List<String> lines = Files.readAllLines(archivo.toPath());
			String[] iteminfo = lines.get(id-1).split(";");
			for (int i = 0; i < NUMATTRIB; i++){
				String[] partes = iteminfo[i].split("=");
				if (partes[0].compareTo("internalName")==0){
					this.nameInternal = partes[1];
				}else if (partes[0].compareTo( "nameSingular" )==0){
					this.nameSingular = partes[1];
				}else if (partes[0].compareTo("namePlural")==0){
					this.namePlural = partes[1];
				}else if (partes[0].compareTo("pocket")==0){
					this.pocket = Integer.parseInt(partes[1]);
				}else if (partes[0].compareTo("price")==0){
					this.price = Integer.parseInt(partes[1]);
				}else if (partes[0].compareTo("description")==0){
					this.description = partes[1];
				}else if (partes[0].compareTo("useOutBattle")==0){
					this.useOutBattle = Integer.parseInt(partes[1]);
				}else if (partes[0].compareTo("useInBattle")==0){
					this.useInBattle = Integer.parseInt(partes[1]);
				}else if (partes[0].compareTo("pokeRate")==0){
					this.pokeRate = Integer.parseInt(partes[1]);
				}
			}
			success = true;
		} catch (IOException ex) {
		}
		
		return success;
	}

	public String getNameInternal() {
		return nameInternal;
	}

	private int getItemID(String internalName){
		int id = 0;
		File archivo = new File("listItems.txt");
		try{
			boolean foundItem = false;
			List<String> lines = Files.readAllLines(archivo.toPath());
			
			while (foundItem == false){
				String[] iteminfo = lines.get(id).split(";");
				int attribComp = 0;
				while (attribComp < NUMATTRIB && foundItem == false){
					String[] partes = iteminfo[attribComp].split("=");
					if (partes[0].compareTo("internalName")==0){
						if (partes[1].compareTo(internalName)==0){
							foundItem = true;
						}else{
							attribComp = attribComp + 100;
						}
					}else{
						attribComp = attribComp + 1;
					}
				}
				id = id + 1;
			}
		} catch (IOException ex) {
		}
			
		return id;
	}
	
	public int getId() {
		return id;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getNameSingular() {
		return nameSingular;
	}

	public String getNamePlural() {
		return namePlural;
	}

	public int getPocket() {
		return pocket;
	}

	public int getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public int getUseOutBattle() {
		return useOutBattle;
	}

	public int getUseInBattle() {
		return useInBattle;
	}

	public int getPokeRate() {
		return pokeRate;
	}

	public int getAmount() {
		return amount;
	}

}
