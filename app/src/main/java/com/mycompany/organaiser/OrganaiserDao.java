package com.mycompany.organaiser;
import java.util.ArrayList;

interface OrganaiserDao {
	boolean add(Commit commit, long idTask);
	Commit getElement(long id);
	ArrayList<Commit> getAll(long idTask);
	boolean update(Commit commit);
	boolean delete(long id);
}
