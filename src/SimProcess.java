package two;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simplified model of a process.
 * @author CS 149 Group #5
 */
public class SimProcess
{
    private float arrivalTime;
    private final float initArrivalTime;
    private float runTime;
    private final float initRunTime;
    private int priority;
    private final int initPriority;
    private String procName;
    private float startTime;
    private float finishTime;
    private float allotedTime;
    private float age;
    
    public final static float MAX_ARRIVAL_TIME = 99;
    public final static float MIN_ARRIVAL_TIME = 0;
    public final static float MAX_RUN_TIME = 10;
    public final static float MIN_RUN_TIME = 0.1f;
    public final static int MAX_PRIORITY = 4;
    
    public SimProcess(float arrivalTime, float runTime, int priority, String procName)
    {
        this.arrivalTime = arrivalTime;
        initArrivalTime = arrivalTime;
        this.runTime = runTime;
        initRunTime = runTime;
        this.priority = priority;
        initPriority = priority;
        this.procName = procName;
        startTime = -1;
        finishTime = -1;
        allotedTime = 0;
        age = 0;
    }
    
    /**
     * Returns the current time until arrival
     * @return the time until arrival
     */
    public float getArrivalTime()
    {
        return arrivalTime;
    }
    
    /**
     * Returns the current time left until the process completes
     * @return the time until completion
     */
    public float getRunTime()
    {
        return runTime;
    }
    
    /**
     * Returns the current priority of the process
     * @return the priority
     */
    public int getPriority()
    {
        return priority;
    }
    
    /**
     * Updates the arrival time of the process
     * @param timeDec The amount to be decremented from the arrival time, measured in quanta.
     */
    public void waitForArrival(float waitTime)
    {
        if(waitTime > arrivalTime) 
            arrivalTime = 0;
        else 
            arrivalTime -= waitTime;
    }
    
    /**
     * Updates the runtime
     * @param elapsedTime the timeslice (quanta) in which to run the process and then switch to a new one
     */
    public void run(float elapsedTime)
    {
        if(elapsedTime > runTime)
            runTime = 0;
        else 
            runTime -= elapsedTime;
        decAllotedTime(elapsedTime);
    }
    
    /**
     * Sets the priority of the thread
     * @param priority the new priority
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
    
    /**
     * Lowers the priority value of the process
     */
    public void decPriority()
    {
        if(priority > 1)
        {
            priority--;
        }
    }
    
    /**
     * Gets the name of the process
     * @return the name
     */
    public String getProcName()
    {
        return procName;
    }
    
    /**
     * Gets the start time of the process
     * @return start time
     */
    public float getStartTime()
    {
        return startTime;
    }
    
    /**
     * Sets the start time of the process
     * @param startTime the new start time
     */
    public void setStartTime(float startTime)
    {
        this.startTime = startTime;
    }
    
    /**
     * Gets the finishing time of the process
     * @return the finishing time
     */
    public float getFinishTime()
    {
        return finishTime;
    }
    
    /**
     * Sets the finishing time of the process
     * @param finishTime the new finishing time
     */
    public void setFinishTime(float finishTime)
    {
        this.finishTime = finishTime;
    }
    
    /**
     * Returns the initial arrival time, expected run time, and priority of the process 
     * @return a String with the initial information of the process.
     */
    public String initInfo()
    {
        String info = "Process Name: " + procName;
        info += "\nArrival time: " + initArrivalTime;
        info += "\nEstimated runtime: " + initRunTime;
        info += "\nPriority: " + initPriority;
        return info;
    }
    
    /**
     * Returns the initial arrival time of the process
     * @return the initial arrival time
     */
    public float getInitArrivalTime()
    {
        return initArrivalTime;
    }
    
    /**
     * Returns the initial expected run time
     * @return the initial expected run time
     */
    public float getInitRunTime()
    {
        return initRunTime;
    }
    
    /**
     * Returns the initial priority
     * @return the initial priority
     */
    public int getInitPriority()
    {
        return initPriority;
    }
    
    /**
     * Generates a process with random arrival time, run time, and priority.
     * @param procName the name of the process
     * @return the new process
     */
    public static SimProcess GenRandProcess(String procName)
    {
        Random ran = new Random();
        float arrivalTime = MIN_ARRIVAL_TIME + (MAX_ARRIVAL_TIME - MIN_ARRIVAL_TIME) * ran.nextFloat();          
        float runTime = MIN_RUN_TIME + (MAX_RUN_TIME - MIN_RUN_TIME) * ran.nextFloat();
        int priority = 1 + ran.nextInt(MAX_PRIORITY);
        return new SimProcess(arrivalTime, runTime, priority, procName);
    }
    
    /**
     * Returns several randomly generated processes
     * @param num the number of desired processes
     * @return an ArrayList containing the processes
     */
    public static ArrayList<SimProcess> GenMultiple(int num)
    {
        ArrayList<SimProcess> procs = new ArrayList<SimProcess>();
        for(int i = 1; i <= num; i++)
        {
            procs.add(GenRandProcess("P" + i));
        }
        return procs;
    }
    
    /**
     * Sets the alloted time for the process to run until switching occurs
     * @param allotedTime
     */
    public void setAllotedTime(float allotedTime)
    {
        this.allotedTime = allotedTime;
    }
    
    /**
     * Gets the alloted time for the process to run until switching occurs
     * @return alloted time
     */
    public float getAllotedTime()
    {
        return allotedTime;
    }
    
    /**
     * Decrements the alloted time based on the time that passed
     * @param timeSlice the elapsed time
     */
    public void decAllotedTime(float timeSlice)
    {
        if(timeSlice > allotedTime) 
            allotedTime = 0;
        else
            allotedTime -= timeSlice;
    }
    
    /**
     * Sets the age of the process
     * @param age the new age
     */
    public void setAge(float age)
    {
        this.age = age;
    }
    
    /**
     * Gets the current age of the process
     * @return the current age
     */
    public float getAge()
    {
        return age;
    }
    
    /**
     * Increases the age of the process by the given timeslice
     * @param timeSlice the time elapsed
     */
    public void incAge(float timeSlice)
    {
        age += timeSlice;
    }
}
