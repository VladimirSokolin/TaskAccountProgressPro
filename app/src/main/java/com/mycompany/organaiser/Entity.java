
package com.mycompany.organaiser;
import java.util.ArrayList;

public class Entity {
	String nameOfTable;
	ArrayList<Column> listTitlesOfColumns; 
	String tag = null;
	int reference = 0;
	
	public Entity(){
		nameOfTable = null;
		listTitlesOfColumns = null;
	}
	
	public Entity(String nameOfTable, ArrayList<Column> listTitlesOfColumns){
		this.nameOfTable = nameOfTable;
		this.listTitlesOfColumns = listTitlesOfColumns;
	}
	
	public Entity(String nameOfTable, ArrayList<Column> listTitlesOfColumns, String tag, int reference){
		this(nameOfTable, listTitlesOfColumns);
		this.tag = tag;
		this.reference = reference;
	}
}
