package com.mycompany.organaiser;

import java.util.ArrayList;

public class SetOfEntities {
	
	ArrayList<Entity> listOfEntities; //Главный лист, хранящий таблицы
	final ArrayList<Column> listCommits;
	
	public SetOfEntities(){
		listOfEntities = new ArrayList<>();
		
		ArrayList<Column> listStoreEntities = new ArrayList<>();
		listStoreEntities.add(new Column("type", "TEXT"));
		listStoreEntities.add(new Column("none", "INTEGER"));
		
		listCommits = new ArrayList<>();
		listCommits.add(new Column("date", "TEXT"));
		listCommits.add(new Column("timeStart", "TEXT"));
		listCommits.add(new Column("timeEnd", "TEXT"));
		listCommits.add(new Column("timeDelta", "TEXT"));
		listCommits.add(new Column("pageStart", "INTEGER"));
		listCommits.add(new Column("pageEnd", "INTEGER"));
		listCommits.add(new Column("pageDelta", "INTEGER"));
		listCommits.add(new Column("description", "TEXT"));
		listCommits.add(new Column("levelAttention", "INTEGER"));
		listCommits.add(new Column("pageInTime", "TEXT"));
		
		listOfEntities.add(new Entity("storeEntities", listStoreEntities));
		listOfEntities.add(new Entity("learnExperiment", listCommits));
		listOfEntities.add(new ConstEntityTask());
	}
	
	public void addEntity(Entity entity){
		listOfEntities.add(entity);
	}
	
	public ArrayList<Entity> getListOfEntities(){
		return listOfEntities;
	}
	
	public String getNameTable(int numberInList){ //правильнее было бы передавать enum в аргументе
		return listOfEntities.get(numberInList).nameOfTable;
	}
	
	public Entity getEntity(int numberInList){
		return listOfEntities.get(numberInList);
	}
	
	public void addEntityCommit(String nameTableFromTask){
		listOfEntities.add(new Entity(nameTableFromTask, listCommits));
	}
}
