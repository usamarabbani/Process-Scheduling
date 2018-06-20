package two;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Shortest job first
 * @author CS 149 Group #5
 *
 */
public class SJF
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
        waitQueue.sort(ScheduleHelper.arrivalComp);
        
        float currentTime = 0;
        ArrayList<String> timeline = new ArrayList<String>();
        boolean hasStarved = false;
        while(!readyQueue.isEmpty() || !waitQueue.isEmpty())
        {
            procSwitch(readyQueue, currentTime);
            hasStarved = checkStarve(procs, readyQueue, waitQueue, currentTime, targetTime, hasStarved);
            if(!readyQueue.isEmpty() || !waitQueue.isEmpty())
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
            ranProcess = readyQueue.getCurrentProc().getProcName();
        }
        
        for(SimProcess proc: waitQueue)
        {
            proc.waitForArrival(TIME_SLICE);
            if(proc.getArrivalTime() <= 0)
                readyQueue.add(proc);
        }
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
        if(!readyQueue.isEmpty() && readyQueue.getCurrentProc().getRunTime() <= 0)
        {
            //This sort ensures that the next running process has the shortest runtime
            readyQueue.sort(ScheduleHelper.runComp);
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
    private static boolean checkStarve(Collection<SimProcess> procs, ReadyQueue readyQueue, LinkedList<SimProcess> waitQueue, float currentTime, float targetTime, boolean hasStarved)
    {
        if(currentTime >= targetTime && !hasStarved)
        {
            readyQueue.evictStarved();
            waitQueue.clear();
            procs.removeIf((proc) -> (proc.getStartTime() < 0));
            hasStarved = true;
        }
        return hasStarved;
    }
}
