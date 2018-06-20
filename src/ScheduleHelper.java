package two;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * A utility class to help with formatting the output of schuduling algorithms
 * @author CS 149 Group #5
 *
 */
public class ScheduleHelper
{
    public static final Comparator<SimProcess> runComp = (p1, p2) -> Float.compare(p1.getRunTime(), p2.getRunTime());
    public static final Comparator<SimProcess> arrivalComp = (p1, p2) -> Float.compare(p1.getArrivalTime(), p2.getArrivalTime());
    public static final String NL = System.getProperty("line.separator");
    
    /**
     * Formats output showing the timeline of processes, the avg turnaround time, 
     * the avg waiting time, the avg response time, and the throughput
     * @param procs The collection of processes
     * @param timeline An ArrayList showing when each process ran
     * @param time The time of completion of the algorithm
     * @return A String with the information in the description
     */
    public static String formatOutput(Collection<SimProcess> procs, ArrayList<String> timeline, float time)
    {
        StringBuilder output = new StringBuilder(); //Since we're appending a lot of data in loops, StringBuilder is preferred to normal String
        procs.forEach((proc) -> {output.append(proc.initInfo() + NL + NL);});
        //This is in case we're collecting statistics on only a subset of completed processes
        if(timeline != null)
        {
            output.append(NL);
            output.append(formatTimeline(timeline));
        }
        
        float totalFinishTime = 0;
        float totalWaitTime = 0;
        float totalResponseTime = 0;
        

        for(SimProcess proc: procs)
        {
            totalFinishTime += proc.getFinishTime();
            totalWaitTime += proc.getFinishTime() - proc.getInitRunTime() - proc.getInitArrivalTime();
            totalResponseTime += proc.getStartTime();
        }
        float avgTurnaround = 0;
        float avgWait = 0;
        float avgResponse = 0;
        if(procs.size() > 0)
        {
            avgTurnaround = totalFinishTime / procs.size();
            avgWait = totalWaitTime / procs.size();
            avgResponse = totalResponseTime / procs.size();
        }
        output.append(NL + "Average turnaround time: " + avgTurnaround);
        output.append(NL + "Average wait time: " + avgWait);
        output.append(NL + "Average response time: " + avgResponse);
        
        float throughput = 0;
        if(time > 0)
            throughput = procs.size() / time; 
        output.append(NL + "Throughput: " + throughput + " processes per quantum");
        return output.toString();
    }
    
    /**
     * This method formats statistical output for each priority level present in the collection of processes.
     * At the very end, it includes output for the whole collection like normal.
     * @param procs The collection of processes
     * @param timeline An ArrayList showing when each process ran
     * @param time The time of completion of the algorithm
     * @return a String with information about each individual priority level and all completed processes as a whole.
     */
    public static String formatPriorityOutput(Collection<SimProcess> procs, ArrayList<String> timeline, float time)
    {
        ArrayList<LinkedList<SimProcess>> queues = new ArrayList<LinkedList<SimProcess>>();
        for(int i = 0; i < SimProcess.MAX_PRIORITY; i++)
        {
            queues.add(new LinkedList<SimProcess>());
        }
        for(SimProcess proc: procs)
        {
            //I'm separating this based on original priority because of aging
            queues.get(proc.getInitPriority() - 1).addLast(proc);
        }
        
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < SimProcess.MAX_PRIORITY; i++)
        {
            LinkedList<SimProcess> queue = queues.get(i);
            output.append("Statistics for priority " + (i + 1) + NL);
            output.append(formatOutput(queue, null, time) + NL + NL);
        }
        
        output.append(NL + NL + "Overall statistics");
        output.append(NL + formatOutput(procs, timeline, time));
        
        return output.toString();
    }
    
    private static String formatTimeline(ArrayList<String> timeline)
    {
        StringBuilder timeChart = new StringBuilder();
        for(int i = 0; i < timeline.size(); i++)
        {
            String currentQuant = String.format("Q%03d: ", i);
            String currentProc = String.format("%4s", timeline.get(i));
            timeChart.append("|" + currentQuant + currentProc + "|");
            
            if((i + 1) % 10 == 0)
                timeChart.append(NL);
        }
        return timeChart.toString();
    }
    
    
}
