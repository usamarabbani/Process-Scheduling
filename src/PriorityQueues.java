package two;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Several queues for each priority level
 * @author CS 149 Group #5
 *
 */
public class PriorityQueues
{
    private ArrayList<LinkedList<SimProcess>> multiQueue;
    private SimProcess currentProc;
    
    /**
     * Initializes a new set of Priority Queues
     * @param maxPriority
     */
    public PriorityQueues(int maxPriority)
    {
        multiQueue = new ArrayList<LinkedList<SimProcess>>();
        for(int i = 0; i < maxPriority; i++)
        {
            multiQueue.add(new LinkedList<SimProcess>());
        }
        currentProc = null;
    }
    
    /**
     * Changes the priority of the contained processes to be higher if they aged enough
     * @param maxAge the age at which to change priority
     */
    public void changePriority(float maxAge)
    {
        if(!isEmpty())
        {
            for(int i = 1; i < multiQueue.size(); i++)
            {
                LinkedList<SimProcess> currentQueue = multiQueue.get(i);
                final int priority = i;
                for(SimProcess proc: currentQueue)
                {
                    if(proc.getAge() >= maxAge)
                    {
                        proc.setPriority(priority);
                        add(proc);
                    }
                }
                currentQueue.removeIf((proc) -> proc.getPriority() == priority);
            }
            if(currentProc != null)
            {
                if(currentProc.getAge() >= maxAge)
                {
                    currentProc.decPriority();
                }
            }
        }
    }
    
    /**
     * Ages every contained process by the given timeslice, including the current one
     * @param timeSlice
     */
    public void ageProcesses(float timeSlice)
    {
        for(LinkedList<SimProcess> queue: multiQueue)
        {
            for(SimProcess proc: queue)
            {
                proc.incAge(timeSlice);
            }
        }
        if(currentProc != null)
        {
            currentProc.incAge(timeSlice);
        }
    }
    
    /**
     * Adds a new process to the appropriate queue
     * @param proc the new process
     */
    public void add(SimProcess proc)
    {
        proc.setAge(0);
        multiQueue.get(proc.getPriority() - 1).addLast(proc);
        if(currentProc == null)
            updateCurrentProc();
    }
    
    /**
     * Gets the currently running process
     * @return the current process
     */
    public SimProcess getCurrentProc()
    {  
        if(currentProc == null)
            updateCurrentProc();
        return currentProc;
    }
    
    /**
     * Removes the currently running process while updating its finish time
     * @param currentTime the time of completion
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
     * Checks if all of the queues are empty and if the current process is null
     * @return true if all queues are empty and the current process is null
     */
    public boolean isEmpty()
    {
        for(LinkedList<SimProcess> queue: multiQueue)
        {
            if(!queue.isEmpty())
                return false;
        }
        return currentProc == null;
    }

    /**
     * Evicts starved processes from the queues and removes the current process if it's
     * also starved
     */
    public void evictStarved()
    {
       
        
        for(LinkedList<SimProcess> queue: multiQueue)
        {
            queue.removeIf((proc) -> (proc.getStartTime() < 0));
        }
        if(currentProc != null && currentProc.getStartTime() < 0)
        {
            currentProc = null;
            updateCurrentProc();
        }   
    }
    
    /**
     * Finds the new currently running process and moves the current process to the end of
     * an appropriate queue if possible
     */
    public void updateCurrentProc()
    {
        if(currentProc != null)
            add(currentProc);
        for(int i = 0; i < multiQueue.size(); i++)
        {
            if(!multiQueue.get(i).isEmpty())
            {
                currentProc = multiQueue.get(i).removeFirst();
                return;
            }
        }
        currentProc = null;
    }
    
    /**
     * Runs the current process and updates its starting time if needed
     * @param timeSlice the time to run the process
     * @param currentTime the current time of the algorithm calling the method
     */
    public void runCurrentProc(float timeSlice, float currentTime)
    {
        if(currentProc == null)
            updateCurrentProc();
        currentProc.run(timeSlice);
        if(currentProc.getStartTime() < 0) 
            currentProc.setStartTime(currentTime);
    }
    
    /**
     * Returns a string showing the contents of the queue and currently running process
     */
    public String toString()
    {
        String str = "Current = ";
        if(currentProc != null)
            str += currentProc.getProcName();
        for(int i= 0; i < multiQueue.size(); i++)
        {
            str += "\nPriority " + (i + 1);
            for(SimProcess proc: multiQueue.get(i))
            {
                str += " " + proc.getProcName();
            }
        }
        return str;
    }
    
    

    
}
