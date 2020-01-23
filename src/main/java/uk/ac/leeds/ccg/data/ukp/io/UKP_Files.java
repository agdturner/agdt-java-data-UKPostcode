/*
 * Copyright 2019 Andy Turner, University of Leeds.
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
package uk.ac.leeds.ccg.data.ukp.io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import uk.ac.leeds.ccg.data.io.Data_Files;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Strings;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class UKP_Files extends Data_Files {

    /**
     * @param dir The directory.
     * @throws java.io.IOException If encountered.
     */
    public UKP_Files(Path dir) throws IOException {
        super(dir);
    }

    private Path inputPostcodeDir;

    public Path getInputPostcodeDir() throws IOException {
        if (inputPostcodeDir == null) {
            inputPostcodeDir = Paths.get(getInputDir().toString(), 
                    UKP_Strings.s_Postcode);
        }
        return inputPostcodeDir;
    }

    private Path inputONSPDDir;

    public Path getInputONSPDDir() throws IOException {
        if (inputONSPDDir == null) {
            inputONSPDDir = Paths.get(getInputPostcodeDir().toString(),
                    UKP_Strings.s_ONSPD);
        }
        return inputONSPDDir;
    }

    private Path generatedPostcodeDir;

    public Path getGeneratedPostcodeDir() throws IOException {
        if (generatedPostcodeDir == null) {
            generatedPostcodeDir = Paths.get(getGeneratedDir().toString(),
                    UKP_Strings.s_Postcode);
        }
        return generatedPostcodeDir;
    }

    private Path generatedONSPDDir;

    public Path getGeneratedONSPDDir() throws IOException {
        if (generatedONSPDDir == null) {
            generatedONSPDDir = Paths.get(getGeneratedPostcodeDir().toString(),
                    UKP_Strings.s_ONSPD);
        }
        return generatedONSPDDir;
    }

    /**
     * @param dir Directory.
     * @param np namePrefix 
     * @param y year
     * @param m month
     * @param nameAdd name added for {@code y > 2016}
     * @return Path to the specific input ONSPD file.
     */
    public Path getInputONSPDFile(Path dir, String np, int y, String m, 
            String nameAdd) {
        Path d;
        String n = "_" + m + "_" + y;
        if (y > 2016) {
            d = Paths.get(dir.toString(), UKP_Strings.s_ONSPD + n + nameAdd);
        } else {
            d = Paths.get(dir.toString(), UKP_Strings.s_ONSPD + n);
        }
        d = Paths.get(d.toString(), UKP_Strings.s_Data);
        Path f = Paths.get(d.toString(), np + n + nameAdd + ".csv");
        return f;
    }

    public Path getInputONSPDFile(UKP_YM3 YM3) throws IOException {
        return getInputONSPDFiles().get(YM3);
    }

    public Path getNSPCLCacheFile() {
        String fn = "cache.dat";
        return Paths.get("C:\\Users\\geoagdt\\data\\generic\\data\\UKP\\NSPCL\\generated", fn);
    }
            
    public Path getNSPCLInputFile(UKP_YM3 YM3) {
        String fn = "NSPCL_NOV19_UK_LU.csv";
        //return Paths.get(getDir(), 
        //return getInputNSPCLFiles().get(YM3);
        return Paths.get("C:\\Users\\geoagdt\\data\\generic\\data\\UKP\\NSPCL\\input\\NSPCL_NOV19_UK_LU", fn);
    }

    TreeMap<UKP_YM3, Path> inputONSPDFiles;

    /**
     * 2008_FEB 2008_MAY 2008_AUG 2008_NOV 2009_FEB 2009_MAY 2009_AUG 2009_NOV
     * 2010_FEB 2010_MAY 2010_AUG 2010_NOV 2011_MAY 2011_AUG 2011_NOV 2012_FEB
     * 2012_MAY 2012_AUG 2012_NOV 2013_FEB 2013_MAY 2013_AUG 2013_NOV 2014_FEB
     * 2014_MAY 2014_AUG 2014_NOV 2015_FEB 2015_MAY 2015_AUG
     *
     * @return {@link #inputONSPDFiles} initialised first if it is {@code null} 
     * @throws java.io.IOException If encountered.
     */
    public TreeMap<UKP_YM3, Path> getInputONSPDFiles() throws IOException {
        if (inputONSPDFiles == null) {
            inputONSPDFiles = new TreeMap<>();
            Path d = getInputONSPDDir();
            Path f;
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
                    inputONSPDFiles.put(new UKP_YM3(year, m), f);
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
                inputONSPDFiles.put(new UKP_YM3(year, m), f);
                if (year != 2017) {
                    if (year == 2011) {
                        nameAdd = "_UK_O";
                    }
                    // AUG
                    month = "AUG";
                    m = 8;
                    f = getInputONSPDFile(d, namePrefix, year, month, nameAdd);
                    inputONSPDFiles.put(new UKP_YM3(year, m), f);
                    // NOV
                    month = "NOV";
                    m = 11;
                    if (year == 2013) {
                        nameAdd = "_UK";
                    }
                    f = getInputONSPDFile(d, namePrefix, year, month, nameAdd);
                    inputONSPDFiles.put(new UKP_YM3(year, m), f);
                }
            }
        }
        return inputONSPDFiles;
    }
}
