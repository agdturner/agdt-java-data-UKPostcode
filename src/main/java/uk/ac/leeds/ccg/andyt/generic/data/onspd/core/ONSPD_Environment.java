/*
 * Copyright 2018 Andy Turner, CCG, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.andyt.generic.data.onspd.core;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Point;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.io.ONSPD_Files;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;

/**
 *
 * @author geoagdt
 */
public class ONSPD_Environment extends ONSPD_OutOfMemoryErrorHandler
        implements Serializable {

    public transient Generic_Environment ge;
    public transient ONSPD_Strings Strings;
    public transient ONSPD_Files Files;
    
    /**
     * Logging levels.
     */
    public int DEBUG_Level;
    public final int DEBUG_Level_FINEST = 0;
    public final int DEBUG_Level_FINE = 1;
    public final int DEBUG_Level_NORMAL = 2;
    
    /**
     * For storing an instance of ONSPD_Handler for convenience.
     */
    private ONSPD_Handler Handler;

    /**
     * Data.
     */
//    public ONSPD_Data data;

    public transient static final String EOL = System.getProperty("line.separator");

    public ONSPD_Environment(File dataDir) {
        //Memory_Threshold = 3000000000L;
        Strings = new ONSPD_Strings();
        Files = new ONSPD_Files(Strings, dataDir);
        ge = new Generic_Environment(Files, Strings);
//        File f;
//        f = Files.getEnvDataFile();
//        if (f.exists()) {
//            loadData();
//            data.Files = Files;
//            data.Files.Strings = Strings;
//            data.Strings = Strings;
//        } else {
//            data = new ONSPD_Data(Files, Strings);
//        }
    }

    /**
     * A method to try to ensure there is enough memory to continue.
     *
     * @return
     */
    @Override
    public boolean checkAndMaybeFreeMemory() {
        System.gc();
        while (getTotalFreeMemory() < Memory_Threshold) {
//            int clear = clearAllData();
//            if (clear == 0) {
//                return false;
//            }
            if (!swapDataAny()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean swapDataAny(boolean handleOutOfMemoryError) {
        try {
            boolean result = swapDataAny();
            checkAndMaybeFreeMemory();
            return result;
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                clearMemoryReserve();
                boolean result = swapDataAny(HOOMEF);
                initMemoryReserve();
                return result;
            } else {
                throw e;
            }
        }
    }

    /**
     * Currently this just tries to swap ONSPD data.
     *
     * @return
     */
    @Override
    public boolean swapDataAny() {
        boolean r;
        r = clearSomeData();
        if (r) {
            return r;
        } else {
            System.out.println("No ONSPD data to clear. Do some coding to try "
                    + "to arrange to clear something else if needs be. If the "
                    + "program fails then try providing more memory...");
            return r;
        }
    }

    public boolean clearSomeData() {
//        return data.clearSomeData();
        return false;
    }

    public int clearAllData() {
        int r;
//        r = data.clearAllData();
//        return r;
    return 0;
    }
    
    public void cacheData() {
//        File f;
//        f = Files.getEnvDataFile();
        System.out.println("<cache data>");
//        Generic_IO.writeObject(data, f);
        System.out.println("</cache data>");
    }

    public final void loadData() {
//        File f;
//        f = Files.getEnvDataFile();
        System.out.println("<load data>");
//        data = (ONSPD_Data) Generic_IO.readObject(f);
        System.out.println("<load data>");
    }
    
    /**
     * For returning an instance of ONSPD_Handler for convenience.
     *
     * @return
     */
    public ONSPD_Handler getPostcode_Handler() {
        if (Handler == null) {
            Handler = new ONSPD_Handler(this);
        }
        return Handler;
    }
    
    /**
     * For writing output messages to.
     */
    private PrintWriter PrintWriterOut;

    /**
     * For writing error messages to.
     */
    private PrintWriter PrintWriterErr;

    public PrintWriter getPrintWriterOut() {
        return PrintWriterOut;
    }

    public void setPrintWriterOut(PrintWriter PrintWriterOut) {
        this.PrintWriterOut = PrintWriterOut;
    }

    public PrintWriter getPrintWriterErr() {
        return PrintWriterErr;
    }

    public void setPrintWriterErr(PrintWriter PrintWriterErr) {
        this.PrintWriterErr = PrintWriterErr;
    }
    /**
     * Writes s to a new line of the output log and error log and also prints it
     * to std.out.
     *
     * @param s
     */
    public void log(String s) {
        PrintWriterOut.println(s);
        PrintWriterErr.println(s);
        System.out.println(s);
    }

//    private static void log(
//            String message) {
//        log(DW_Log.DW_DefaultLogLevel, message);
//    }
//
//    private static void log(
//            Level level,
//            String message) {
//        Logger.getLogger(DW_Log.DW_DefaultLoggerName).log(level, message);
//    }
    /**
     * Writes s to a new line of the output log and also prints it to std.out.
     *
     * @param s
     * @param println
     */
    public void logO(String s, boolean println) {
        if (PrintWriterOut != null) {
            PrintWriterOut.println(s);
        }
        if (println) {
            System.out.println(s);
        }
    }

    /**
     * Writes s to a new line of the output log and also prints it to std.out if
     * {@code this.DEBUG_Level <= DEBUG_Level}.
     *
     * @param DEBUG_Level
     * @param s
     */
    public void logO(int DEBUG_Level, String s) {
        if (this.DEBUG_Level <= DEBUG_Level) {
            PrintWriterOut.println(s);
            System.out.println(s);
        }
    }

    /**
     * Writes s to a new line of the error log and also prints it to std.err.
     *
     * @param s
     */
    public void logE(String s) {
        if (PrintWriterErr != null) {
            PrintWriterErr.println(s);
        }
        System.err.println(s);
    }

    /**
     * Writes {@code e.getStackTrace()} to the error log and also prints it to
     * std.err.
     *
     * @param e
     */
    public void logE(Exception e) {
        StackTraceElement[] st;
        st = e.getStackTrace();
        for (StackTraceElement st1 : st) {
            logE(st1.toString());
        }
    }

    /**
     * Writes e StackTrace to the error log and also prints it to std.err.
     *
     * @param e
     */
    public void logE(Error e) {
        StackTraceElement[] st;
        st = e.getStackTrace();
        for (StackTraceElement st1 : st) {
            logE(st1.toString());
        }
    }
    
    /**
     * For storing level(s) (OA, LSOA, MSOA, PostcodeSector, PostcodeUnit, ...)
     */
    protected String level;
    protected ArrayList<String> levels;
    private TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> ONSPDlookups;
    
    public TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> getONSPDlookups() {
        if (ONSPDlookups == null) {
            initONSPDLookups();
        }
        return ONSPDlookups;
    }

    protected void initPostcode_Handler() {
        if (Handler == null) {
//            Handler = new ONSPD_Handler(Env.ONSPD_Environment);
            Handler = new ONSPD_Handler(new ONSPD_Environment(Files.getDataDir()));
        }
    }

    public void initONSPDLookups() {
        initPostcode_Handler();
        ONSPDlookups = new TreeMap<>();
        levels = new ArrayList<>();
        levels.add("Unit");
        //levels.add("Sector");
        //levels.add("Area");
        TreeMap<ONSPD_YM3, File> ONSPDFiles;
        ONSPDFiles = Files.getInputONSPDFiles();
        Iterator<String> ite2;
        ite2 = levels.iterator();
        while (ite2.hasNext()) {
            level = ite2.next();
            TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>> ONSPDlookup;
            ONSPDlookup = Handler.getPostcodeUnitPointLookups(true,
                    ONSPDFiles,
                    Handler.getDefaultLookupFilename());
            ONSPDlookups.put(level, ONSPDlookup);
        }
    }
    
    public void setONSPDlookups(TreeMap<String, 
            TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> ONSPDlookups) {
        this.ONSPDlookups = ONSPDlookups;
    }
}
