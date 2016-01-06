package de.unistuttgart.ims.fictionnet.processes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ims.fictionnet.users.User;

/**
 * It's often necessary to run user processes in the background.
 * The ProcessManager takes care of every user's processes.
 * 
 * @author Lukas Rieger, Erol Aktay
 * @version 24-10-2015
 */
public class ProcessManager {
  private final Map<String, ArrayList<Process>> processTable;
  private static ProcessManager theInstance;

  /**
   * Constructor is private for singleton pattern
   */
  private ProcessManager() {
    this.processTable = new HashMap<>();
  }

  /**
   * Returns the instance of the process manager. 
   * 
   * @return processManager's only instance.
   */
  public synchronized static ProcessManager getTheInstance() {
    if (theInstance == null) {
      theInstance = new ProcessManager();
    }
    return theInstance;
  }

  /**
   * Adds a process to a user
   * 
   * @param process
   * @param user
   */
  public void addProcess(final Process process, final User user) {
    ArrayList<Process> usersProcesses;

    if (processTable.containsKey(user.getEmail())) {
      usersProcesses = processTable.get(user.getEmail());
    } else {
      usersProcesses = new ArrayList<>();
    }
    
    usersProcesses.add(process);
    processTable.put(user.getEmail(), usersProcesses);
  }

  /**
   * Returns an arrayList of all the processes belonging to the user
   * 
   * @param user
   * @return processes
   */
  public ArrayList<Process> getUsersProcesses(User user) {
    if (user == null || !processTable.containsKey(user.getEmail())) {
      return new ArrayList<>();
    }
    return processTable.get(user.getEmail());
  }
  
  /**
   * Returns list of all processes
   * 
   * @return processes
   */
  public ArrayList<Process> getProcesses() {
	  ArrayList<Process> result = new ArrayList<>();
	  for (ArrayList<Process> procs : processTable.values()) {
		  result.addAll(procs);
	  }
	  return result;
  }
  
}
