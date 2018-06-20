package two;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * A simple queue of ready-to-run processes
 * @author CS 146 Group #5
 *
 */
public class ReadyQueue
{
    private SimProcess currentProc;
    private LinkedList<SimProcess> queue;
    
    /**
     * Initializes a new ReadyQueue
     */
    public ReadyQueue()
    {
        queue = new LinkedList<SimProcess>();
        currentProc = null;
    }
    
    /**
     * Updates the currently running process to be the correct one. If there
     * already is a currently running process, it is moved to the end of the queue
     */
    public void updateCurrentProc()
    {
        if(currentProc != null)
        {
            add(currentProc);
        }
        if(!queue.isEmpty())
            currentProc = queue.removeFirst();
        else
            currentProc = null;
    }
    
    /**
     * Removes the current process and updates its finishing time
     * @param currentTime the time of removal
     * @return the removed process
     */
    public SimProcess removeCurrentProc(float currentTime)
    {
        if(currentProc == null)
            updateCurrentProc();
        SimProcess removedProc = currentProc;
        removedProc.setFinishTime(currentTime);
        currentProc = null;
        updateCurrentProc();
        return removedProc;
    }

    /**
     * Adds a new process to the queue and updates the current process if necessary
     * @param proc the process to be added
     */
    public void add(SimProcess proc)
    {
        queue.addLast(proc);
        if(currentProc == null)
            updateCurrentProc();
    }
    
    /**
     * Sorts the processes in the queue according to the given comparator
     * @param comp the comparator
     */
    public void sort(Comparator<? super SimProcess> comp)
    {
        queue.sort(comp);
    }
    
    /**
     * Returns the currently running process
     * @return the current process
     */
    public SimProcess getCurrentProc()
    {
        if(currentProc == null)
            updateCurrentProc();
        return currentProc;
    }
    
    /**
     * Adds a new process, but as the currently running process
     * @param currentProc the new process
     */
    public void addAsCurrentProc(SimProcess currentProc)
    {
        if(currentProc != null)
            add(this.currentProc);
        this.currentProc = currentProc;
    }
    
    /**
     * Removed starved processes that never even started
     */
    public void evictStarved()
    {
        queue.removeIf((proc) -> (proc.getStartTime() < 0));
        if(currentProc != null && currentProc.getStartTime() < 0)
        {
            currentProc = null;
            updateCurrentProc();
        }  
    }
    
    /**
     * Checks if the queue is empty, including the currentProc variable
     * @return true if there are no processes running or in the queue
     */
    public boolean isEmpty()
    {
        return queue.isEmpty() && currentProc == null;
    }
    
    /**
     * Runs the current process for the given timeslice
     * If the process never ran before, its start time will be updated
     * @param timeSlice the time to run the process
     * @param currentTime the current time
     */
    public void runCurrentProc(float timeSlice, float currentTime)
    {
        if(currentProc == null)
            updateCurrentProc();
        currentProc.run(timeSlice);
        if(currentProc.getStartTime() < 0)
            currentProc.setStartTime(currentTime);
    }
}
