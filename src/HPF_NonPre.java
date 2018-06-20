package two;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Highest priority first non-preemptive
 * @author CS 149 Group #5
 *
 */
public class HPF_NonPre
{
    private static final float MAX_AGE = 5;
    private static final float TIME_SLICE = 1;
    /**
     * Runs the algorithm on the collection of hopefully new processes
     * @param procs a collection of new processes
     * @param targetTime the time at which starved processes are purged
     * @param isAging true if the priorities should be 
     * changed based on a process's age since last changing priority
     * @return a string showing statistics about the run
     */
    public static String run(Collection<SimProcess> procs, float targetTime, boolean isAging)
    {
        LinkedList<SimProcess> waitQueue = new LinkedList<SimProcess>();
        waitQueue.addAll(procs);
        waitQueue.sort(ScheduleHelper.arrivalComp);
        PriorityQueues readyQueues = new PriorityQueues(SimProcess.MAX_PRIORITY);
        
        float currentTime = 0;
        ArrayList<String> timeline = new ArrayList<String>();
        boolean hasStarved = false;
        while(!readyQueues.isEmpty() || !waitQueue.isEmpty())
        {
            
            procSwitch(readyQueues, currentTime);
            hasStarved = checkStarve(procs, readyQueues, waitQueue, currentTime, targetTime, hasStarved);
            if(!readyQueues.isEmpty() || !waitQueue.isEmpty())
            {
                timeline.add(runTimeSlice(readyQueues, waitQueue, currentTime, isAging));
                currentTime += TIME_SLICE;
            }
            //System.out.println(readyQueues.toString());
        }
        return ScheduleHelper.formatPriorityOutput(procs, timeline, currentTime);
    }

    /**
     * Runs a process and waits for arriving processes for a defined timeslice
     * @param readyQueues the multi level queue of ready processes
     * @param waitQueue the queue of arriving processes
     * @param currentTime the current time thus far
     * @param isAging true if the priorities should be 
     * changed based on a process's age since last changing priority
     * @return the name of the process that ran, if any
     */
    private static String runTimeSlice(PriorityQueues readyQueues,
            LinkedList<SimProcess> waitQueue, float currentTime, boolean isAging)
    {
        String ranProcess = "wait";
        if(!readyQueues.isEmpty())
        {
            readyQueues.runCurrentProc(TIME_SLICE, currentTime);
            ranProcess = readyQueues.getCurrentProc().getProcName();
        }
        if(isAging)
        {
            readyQueues.ageProcesses(TIME_SLICE);
            readyQueues.changePriority(MAX_AGE);
        }

        for(SimProcess proc: waitQueue)
        {
            proc.waitForArrival(TIME_SLICE);
            if(proc.getArrivalTime() <= 0)
                readyQueues.add(proc);
        }
        waitQueue.removeIf((proc) -> proc.getArrivalTime() <= 0);
        
        return ranProcess;
    }

    /**
     * Purges the queues and original collection of starved processes if necessary
     * @param procs the original collection of processes
     * @param readyQueues the multi-level queue of ready processes
     * @param waitQueue the queue of arriving processes
     * @param currentTime the current time thus far
     * @param targetTime the time at which to purge starved processes
     * @param hasStarved true if a purge already occurred
     * @return true if a purge occurs, false otherwise
     */
    private static boolean checkStarve(Collection<SimProcess> procs,
            PriorityQueues readyQueues, LinkedList<SimProcess> waitQueue,
            float currentTime, float targetTime, boolean hasStarved)
    {
        if(currentTime >= targetTime && !hasStarved)
        {  
            readyQueues.evictStarved();
            waitQueue.clear();
            procs.removeIf((proc) -> (proc.getStartTime() < 0));
        }
        return hasStarved;
    }

    /**
     * A method that removes finished processes and/or performs a process switch,
     * or neither if these actions are unneeded
     * @param readyQueues the multi-level queue of ready processes
     * @param currentTime the current time thus far
     */
    private static void procSwitch(PriorityQueues readyQueues, float currentTime)
    {
        
        if(!readyQueues.isEmpty()  && readyQueues.getCurrentProc().getRunTime() <= 0)
        {
            readyQueues.removeCurrentProc(currentTime);
        }
    }
}
