package com.mycompany.organaiser;
import java.util.ArrayList;

public interface OrganaiserDaoTask{
	public boolean addTask(Task task);
	public Task getTask(long id);
	public ArrayList<Task> getAllTask();
	public boolean update(Task task);
	public boolean delete(long id);
}
