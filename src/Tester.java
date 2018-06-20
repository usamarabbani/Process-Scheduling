package two;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class Tester
{
    public static final float TARGET_TIME = 100;
    public static final int MAX_ALG_ITER = 5;
    public static final int MAX_PROCS_FCFS = 40;
    public static final int MAX_PROCS_SJF = 30;
    public static final int MAX_PROCS_SRT = 30;
    public static final int MAX_PROCS_RR = 40;
    public static final int MAX_PROCS_HPFNP = 40;
    public static final int MAX_PROCS_HPFP = 50;
    public static final String NL = System.getProperty("line.separator");
    
    public static void main(String[] args) throws FileNotFoundException
    {
        //Comment out all but one test if you want to test an algorithm individually
        
        testFCFS();
        testSJF();
        testSRT();
        testRR();
        testHPF_NPNA();
        testHPF_NPA();
        testHPF_PNA();
        testHPF_PA();
        System.out.println("Output printed to multiple files");
    }
    
    private static void testFCFS() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("FCFS_OUT.txt"))
        {
            out.println("**********************************************");
            out.println("Running FCFS " + MAX_ALG_ITER + " times.");
            out.println("**********************************************" + NL + NL);
            for(int i = 1; i <= MAX_ALG_ITER; i++)
            {
                out.println("FCFS run #" + i);
                out.println(FCFS.run(SimProcess.GenMultiple(MAX_PROCS_FCFS), TARGET_TIME) + NL + NL);
            }
        }
    }
    
    private static void testSJF() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("SJF_OUT.txt"))
        {
            out.println("**********************************************");
            out.println("Running SJF " + MAX_ALG_ITER + " times.");
            out.println("**********************************************" + NL + NL);
            for(int i = 1; i <= MAX_ALG_ITER; i++)
            {
                out.println("SJF run #" + i);
                out.println(SJF.run(SimProcess.GenMultiple(MAX_PROCS_SJF), TARGET_TIME) + NL + NL);
            }
        }
    }
    
    private static void testSRT() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("SRT_OUT.txt"))
        {
            out.println("**********************************************");
            out.println("Running SRT " + MAX_ALG_ITER + " times.");
            out.println("**********************************************" + NL + NL);
            for(int i = 1; i <= MAX_ALG_ITER; i++)
            {
                out.println("SRT run #" + i);
                out.println(SRT.run(SimProcess.GenMultiple(MAX_PROCS_SRT), TARGET_TIME) + NL + NL);
            }
        }
    }
    
    private static void testRR() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("RR_OUT.txt"))
        {
            out.println("**********************************************");
            out.println("Running RR " + MAX_ALG_ITER + " times.");
            out.println("**********************************************" + NL + NL);
            for(int i = 1; i <= MAX_ALG_ITER; i++)
            {
                out.println("RR run #" + i);
                out.println(RR.run(SimProcess.GenMultiple(MAX_PROCS_RR), TARGET_TIME) + NL + NL);
            }
        }
    }
    
    private static void testHPF_NPNA() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("HPF_NPNA_OUT.txt"))
        {
            out.println("**********************************************");
            out.println("Running HPF Non-preemptive Non-aging " + MAX_ALG_ITER + " times.");
            out.println("**********************************************" + NL + NL);
            for(int i = 1; i <= MAX_ALG_ITER; i++)
            {
                out.println("HPF Non-preemptive Non-aging run #" + i);
                out.println(HPF_NonPre.run(SimProcess.GenMultiple(MAX_PROCS_HPFNP), TARGET_TIME, false) + NL + NL);
            }
        }
    }
    
    private static void testHPF_NPA() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("HPF_NPA.txt"))
        {
            out.println("**********************************************");
            out.println("Running HPF Non-preemptive w/Aging " + MAX_ALG_ITER + " times.");
            out.println("**********************************************" + NL + NL);
            for(int i = 1; i <= MAX_ALG_ITER; i++)
            {
                out.println("HPF Non-preemptive w/Aging run #" + i);
                out.println(HPF_NonPre.run(SimProcess.GenMultiple(MAX_PROCS_HPFNP), TARGET_TIME, true) + NL + NL);
            }
        }
    }
    
    private static void testHPF_PNA() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("HPF_PNA.txt"))
        {
            out.println("**********************************************");
            out.println("Running HPF Preemptive Non-aging " + MAX_ALG_ITER + " times.");
            out.println("**********************************************" + NL + NL);
            for(int i = 1; i <= MAX_ALG_ITER; i++)
            {
                out.println("HPF Preemptive Non-aging run #" + i);
                out.println(HPF_Pre.run(SimProcess.GenMultiple(MAX_PROCS_HPFP), TARGET_TIME, false) + NL + NL);
            }
        }
    }
    
    private static void testHPF_PA() throws FileNotFoundException
    {
        try(PrintWriter out = new PrintWriter("HPF_PA.txt"))
        {
            out.println("**********************************************");
            out.println("Running HPF Preemptive w/Aging " + MAX_ALG_ITER + " times.");
            out.println("**********************************************" + NL + NL);
            for(int i = 1; i <= MAX_ALG_ITER; i++)
            {
                out.println("HPF Preemptive w/Aging run #" + i);
                out.println(HPF_Pre.run(SimProcess.GenMultiple(MAX_PROCS_HPFP), TARGET_TIME, true) + NL + NL);
            }
        }
    }

}
