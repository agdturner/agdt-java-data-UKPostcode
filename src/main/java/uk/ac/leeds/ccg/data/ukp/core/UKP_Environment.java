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
package uk.ac.leeds.ccg.data.ukp.core;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Strings;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Point;
import uk.ac.leeds.ccg.data.ukp.data.UKP_Data;
import uk.ac.leeds.ccg.data.ukp.io.UKP_Files;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.generic.memory.Generic_MemoryManager;

/**
 * The environment.
 * 
 * @author Andy Turner
 * @version 1.0.0
 */
public class UKP_Environment extends Generic_MemoryManager
        implements Serializable {

    public final transient Generic_Environment env;
    public final transient Data_Environment de;
    public final transient UKP_Files files;

    /**
     * For storing an instance of UKP_Data for convenience.
     */
    private UKP_Data handler;

    /**
     * Data.
     */
//    public ONSPD_Data data;
    public transient static final String EOL = System.getProperty("line.separator");

    public UKP_Environment(Data_Environment e) throws IOException, Exception {
        this(e, Paths.get(e.files.getDataDir().toString(), UKP_Strings.s_ONSPD));
    }
            
    public UKP_Environment(Data_Environment e, Path dataDir) throws IOException, Exception {
        /**
         * Init de.
         */
        de = e;
        Path d0 = Paths.get(dataDir.toString(), Generic_Strings.s_generated);
        Path d = Paths.get(d0.toString(), Generic_Strings.s_data);
        de.files.setDir(d);
        de.initLog(Generic_Strings.s_data);
        /**
         * Init env.
         */
        env = e.env;
        d = Paths.get(d0.toString(), Generic_Strings.s_generic);
        env.files.setDir(d);
        env.initLog(Generic_Strings.s_generic);
        /**
         * Init files.
         */
        files = new UKP_Files(dataDir);
        //files = new UKP_Files(de.files.getDir());
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
            if (!UKP_Environment.this.swapSomeData()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean swapSomeData(boolean hoome) {
        try {
            boolean r = swapSomeData();
            checkAndMaybeFreeMemory();
            return r;
        } catch (OutOfMemoryError e) {
            if (hoome) {
                clearMemoryReserve(env);
                boolean r = swapSomeData(HOOMEF);
                initMemoryReserve(env);
                return r;
            } else {
                throw e;
            }
        }
    }

    /**
     * Currently this just tries to cache ONSPD data.
     *
     * @return {@code false} 
     */
    @Override
    public boolean swapSomeData() {
        boolean r = clearSomeData();
        if (r) {
            return r;
        } else {
            env.log("No ONSPD data to clear. Do some coding to try "
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
//        return data.clearAllData();
        return 0;
    }

    public void cacheData() {
//        File f = files.getEnvDataFile();
        String m = "cache data";
        env.logStartTag(m);
//        Generic_IO.writeObject(data, f);
        env.logEndTag(m);
    }

    public final void loadData() {
//        File f = files.getEnvDataFile();
        String m = "load data";
        env.logStartTag(m);
//        data = (ONSPD_Data) Generic_IO.readObject(f);
        env.logEndTag(m);
    }

    /**
     * For returning an instance of UKP_Data for convenience.
     *
     * @return {@link #handler}
     */
    public UKP_Data getHandler() {
        if (handler == null) {
            handler = new UKP_Data(this);
        }
        return handler;
    }

    /**
     * For storing level(s) (OA, LSOA, MSOA, PostcodeSector, PostcodeUnit, ...)
     */
    //protected int level;
    //protected String level;
    protected ArrayList<Integer> levels;
    //protected ArrayList<String> levels;
    //private TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> ONSPDlookups;
    private TreeMap<Integer, TreeMap<UKP_YM3, TreeMap<String, ONSPD_Point>>> ONSPDlookups;

    //public TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> getONSPDlookups() {
    public TreeMap<Integer, TreeMap<UKP_YM3, TreeMap<String, ONSPD_Point>>> getONSPDlookups() throws IOException, ClassNotFoundException {
        if (ONSPDlookups == null) {
            initONSPDLookups();
        }
        return ONSPDlookups;
    }

    protected void initPostcode_Handler() {
        if (handler == null) {
            handler = new UKP_Data(this);
        }
    }

    public void initONSPDLookups() throws IOException, ClassNotFoundException {
        initPostcode_Handler();
        ONSPDlookups = new TreeMap<>();
        levels = new ArrayList<>();
        //levels.add("Unit");
        levels.add(3);
        //levels.add("Sector");
        //levels.add("Area");
        TreeMap<UKP_YM3, Path> ONSPDFiles = files.getInputONSPDFiles();
        Iterator<Integer> ite2 = levels.iterator();
        while (ite2.hasNext()) {
            int level = ite2.next();
            TreeMap<UKP_YM3, TreeMap<String, ONSPD_Point>> ONSPDlookup;
            ONSPDlookup = handler.getPostcodeUnitPointLookups(true,
                    ONSPDFiles, handler.getDefaultLookupFilename());
            ONSPDlookups.put(level, ONSPDlookup);
        }
    }

    //public void setONSPDlookups(TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> ONSPDlookups) {
    public void setONSPDlookups(TreeMap<Integer, TreeMap<UKP_YM3, TreeMap<String, ONSPD_Point>>> ONSPDlookups) {
        this.ONSPDlookups = ONSPDlookups;
    }
}
