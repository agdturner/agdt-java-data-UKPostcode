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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
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

    public final transient Generic_Environment ge;
    public final transient ONSPD_Strings Strings;
    public final transient ONSPD_Files Files;

    /**
     * For storing an instance of ONSPD_Handler for convenience.
     */
    private ONSPD_Handler Handler;

    /**
     * Data.
     */
//    public ONSPD_Data data;
    public transient static final String EOL = System.getProperty("line.separator");

    public ONSPD_Environment(Generic_Environment ge) {
        //Memory_Threshold = 3000000000L;
        this.ge = ge;
        Strings = new ONSPD_Strings();
        Files = new ONSPD_Files(Strings, ge.getFiles().getDataDir());
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
    public ONSPD_Handler getHandler() {
        if (Handler == null) {
            Handler = new ONSPD_Handler(this);
        }
        return Handler;
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
            Handler = new ONSPD_Handler(this);
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
                    ONSPDFiles, Handler.getDefaultLookupFilename());
            ONSPDlookups.put(level, ONSPDlookup);
        }
    }

    public void setONSPDlookups(TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> ONSPDlookups) {
        this.ONSPDlookups = ONSPDlookups;
    }
}
