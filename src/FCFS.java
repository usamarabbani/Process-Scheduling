package two;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * First come first serve algorithm
 * @author CS 149 Group #5
 */
public class FCFS
{
    private static final float TIME_SLICE = 1;
    
    /**
     * Runs the algorithm on the collection of hopefully new processes
     * @param procs a collection of new processes
     * @param targetTime the time at which starved processes are purged
     * @return a string showing statistics about the run
     */
    public static String run(Collection<SimProcess> procs, float targetTime)
    {
        ReadyQueue readyQueue = new ReadyQueue();
        LinkedList<SimProcess> waitQueue = new LinkedList<SimProcess>();
        waitQueue.addAll(procs);
        waitQueue.sort(ScheduleHelper.arrivalComp); //sort by arrival time
        float currentTime = 0;
        ArrayList<String> timeline = new ArrayList<String>(); //This is to help with the statistics later
        boolean hasStarved = false; //true if all queues and the original collection were purged of starved processes
        while(!waitQueue.isEmpty() || !readyQueue.isEmpty())
        {
            procSwitch(readyQueue, currentTime);
            hasStarved = checkStarve(procs, readyQueue, waitQueue, currentTime, targetTime, hasStarved);
            //procSwitch or hasStarved may have emptied the queues
            if(!waitQueue.isEmpty() || !readyQueue.isEmpty())
            {
                timeline.add(runTimeSlice(readyQueue, waitQueue, currentTime));
                currentTime += TIME_SLICE; 
            }
        }
        return ScheduleHelper.formatOutput(procs, timeline, currentTime);
    }
    
    /**
     * Runs a process and waits for arriving processes for a defined timeslice
     * @param readyQueue the queue of ready processes
     * @param waitQueue the queue of arriving processes
     * @param currentTime the current time thus far
     * @return the name of the process that ran, if any
     */
    private static String runTimeSlice(ReadyQueue readyQueue, LinkedList<SimProcess> waitQueue, float currentTime)
    {
        String ranProcess = "wait";
        if(!readyQueue.isEmpty())
        {
            readyQueue.runCurrentProc(TIME_SLICE, currentTime);
            //This will record the process's name in the timeline
            ranProcess = readyQueue.getCurrentProc().getProcName();
        }
        for(SimProcess proc: waitQueue)
        {
            proc.waitForArrival(TIME_SLICE);
            if(proc.getArrivalTime() <= 0)
                readyQueue.add(proc);
        }
        //I don't want to manually use an iterator and remove all the newly arrived processes
        waitQueue.removeIf((proc) -> proc.getArrivalTime() <= 0);
        return ranProcess;
    }
    
    /**
     * A method that removes finished processes and/or performs a process switch,
     * or neither if these actions are unneeded
     * @param readyQueue the queue of ready processes
     * @param currentTime the current time thus far
     */
    private static void procSwitch(ReadyQueue readyQueue, float currentTime)
    {
        //This checks is the current process has finished running
        if(!readyQueue.isEmpty()  && readyQueue.getCurrentProc().getRunTime() <= 0)
        {
            readyQueue.removeCurrentProc(currentTime);
        }
    }
    
    /**
     * Purges the queues and original collection of starved processes if necessary
     * @param procs the original collection of processes
     * @param readyQueue the queue of ready processes
     * @param waitQueue the queue of arriving processes
     * @param currentTime the current time thus far
     * @param targetTime the time at which to purge starved processes
     * @param hasStarved true if a purge already occurred
     * @return true if a purge occurs, false otherwise
     */
    private static boolean checkStarve(Collection<SimProcess> procs, ReadyQueue readyQueue, 
            LinkedList<SimProcess> waitQueue, float currentTime, 
            float targetTime, boolean hasStarved)
    {
        if(currentTime >= targetTime && !hasStarved)
        {
          //Not all processes in the ready queue actually start
            readyQueue.evictStarved();
          //I can just clear it because no process in the ready queue ever goes back into waiting for arrival.
            waitQueue.clear(); 
          //This needs to be cleaned too because it's used for output
            procs.removeIf((proc) -> (proc.getStartTime() < 0));
            hasStarved = true;
        }
        return hasStarved;
    }

}
