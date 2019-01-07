/*
 * Copyright 2018 geoagdt.
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
package uk.ac.leeds.ccg.andyt.generic.data.onspd.io;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Files;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Strings;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;

/**
 *
 * @author geoagdt
 */
public class ONSPD_Files extends Generic_Files implements Serializable {

    public transient ONSPD_Strings Strings;

    /**
     *
     * @param s
     */
    public ONSPD_Files(ONSPD_Strings s) {
        super(s.s_data);
        this.Strings = s;
    }

    /**
     *
     * @param s
     * @param dataDirName
     */
    public ONSPD_Files(ONSPD_Strings s, String dataDirName) {
        super(dataDirName);
        this.Strings = s;
    }

    private File inputONSPDDir;

    public File getInputONSPDDir() {
        if (inputONSPDDir == null) {
            inputONSPDDir = new File(
                    getInputPostcodeDir(),
                    Strings.S_ONSPD);
        }
        return inputONSPDDir;
    }

    private File inputPostcodeDir;

    public File getInputPostcodeDir() {
        if (inputPostcodeDir == null) {
            inputPostcodeDir = new File(getInputDataDir(Strings),
                    Strings.S_Postcode);
        }
        return inputPostcodeDir;
    }

    public File getInputONSPDFile(File dir, String namePrefix, int year, String month, String nameAdd) {
        File f;
        File d;
        if (year > 2016) {
            d = new File(dir, "ONSPD" + "_" + month + "_" + year + nameAdd);
        } else {
            d = new File(dir, "ONSPD" + "_" + month + "_" + year);
        }
        d = new File(d, "Data");
        f = new File(d, namePrefix + "_" + month + "_" + year + nameAdd + ".csv");
        return f;
    }

    public File getInputONSPDFile(ONSPD_YM3 YM3) {
        return getInputONSPDFiles().get(YM3);
    }

    TreeMap<ONSPD_YM3, File> InputONSPDFiles;

    /**
     * 2008_FEB 2008_MAY 2008_AUG 2008_NOV 2009_FEB 2009_MAY 2009_AUG 2009_NOV
     * 2010_FEB 2010_MAY 2010_AUG 2010_NOV 2011_MAY 2011_AUG 2011_NOV 2012_FEB
     * 2012_MAY 2012_AUG 2012_NOV 2013_FEB 2013_MAY 2013_AUG 2013_NOV 2014_FEB
     * 2014_MAY 2014_AUG 2014_NOV 2015_FEB 2015_MAY 2015_AUG
     *
     * @return
     */
    public TreeMap<ONSPD_YM3, File> getInputONSPDFiles() {
        if (InputONSPDFiles == null) {
            InputONSPDFiles = new TreeMap<>();
            File d;
            d = getInputONSPDDir();
            File f;
            String namePrefix;
            String month;
            int m;
            String nameAdd;
            for (int year = 2008; year < 2018; year++) {
                if (year < 2011) {
                    namePrefix = "NSPDF";
                    nameAdd = "_UK_1M";
                } else {
                    namePrefix = "ONSPD";
                    // It is odd but the case that the following are "O" not "0"!
                    if (year >= 2011 && year <= 2013) {
                        nameAdd = "_UK_O";
                    } else {
                        nameAdd = "_UK";
                    }
                }
                // FEB
                if (year != 2011) {
                    month = "FEB";
                    m = 2;
                    String nameAdd0;
                    nameAdd0 = nameAdd;
                    if (year == 2010) {
                        nameAdd0 += "_FP";
                    }
                    f = getInputONSPDFile(d, namePrefix, year, month, nameAdd0);
                    InputONSPDFiles.put(new ONSPD_YM3(year, m), f);
                }
                // MAY
                month = "MAY";
                m = 5;
                if (year == 2009 || year == 2010) {
                    nameAdd += "_FP";
                }
                if (year == 2011) {
                    nameAdd = "_O";
                }
                f = getInputONSPDFile(d, namePrefix, year, month, nameAdd);
                InputONSPDFiles.put(new ONSPD_YM3(year, m), f);
                if (year != 2017) {
                    if (year == 2011) {
                        nameAdd = "_UK_O";
                    }
                    // AUG
                    month = "AUG";
                    m = 8;
                    f = getInputONSPDFile(d, namePrefix, year, month, nameAdd);
                    InputONSPDFiles.put(new ONSPD_YM3(year, m), f);
                    // NOV
                    month = "NOV";
                    m = 11;
                    if (year == 2013) {
                        nameAdd = "_UK";
                    }
                    f = getInputONSPDFile(d, namePrefix, year, month, nameAdd);
                    InputONSPDFiles.put(new ONSPD_YM3(year, m), f);
                }
            }
        }
        return InputONSPDFiles;
    }
}
