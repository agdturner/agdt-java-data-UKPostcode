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
package uk.ac.leeds.ccg.data.ukp.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.data.id.Data_RecordID;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Environment;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Object;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Point;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record0;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record1;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2008_02Feb;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2011_05May;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2012_11Nov;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2013_02Feb;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2013_05May;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2013_08Aug;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2014_11Nov;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2015_05May;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2015_08Aug;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Record_2016_02Feb;
import uk.ac.leeds.ccg.data.ukp.io.UKP_Files;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.ukpc.UKPC_Checker;

/**
 * A class for handling coordinate data and area codes for UK postcodes.
 * https://geoportal.statistics.gov.uk/Docs/PostCodes/ONSPD_AUG_2013_csv.zip
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class UKP_Data extends UKP_Object {

    /**
     * For checking the format of postcodes.
     */
    public final transient UKPC_Checker checker;

    /**
     * For convenience this is the same as super.files
     */
    protected final transient UKP_Files files;

    // Area Types
    // Postcode
    public static final int TYPE_AREA = 0;
    public static final int TYPE_DISTRICT = 1;
    public static final int TYPE_SECTOR = 2;
    public static final int TYPE_UNIT = 3;
    // Census
    public static final int TYPE_OA = 4;
    public static final int TYPE_LSOA = 5;
    public static final int TYPE_MSOA = 6;
    public static final int TYPE_LAD = 7;
    // Others    
    public static final int TYPE_ParliamentaryConstituency = 8;
    public static final int TYPE_StatisticalWard = 9;

    //public final String TYPE_UNIT = "Unit";
    //public final String TYPE_SECTOR = "Sector";
    //public final String TYPE_DISTRICT = "District";
    //public final String TYPE_AREA = "Area";
    TreeMap<String, TreeMap<UKP_YM3, TreeMap<String, ONSPD_Point>>> ONSPDLookups;

    public double getDistanceBetweenPostcodes(ONSPD_Point aPoint,
            ONSPD_Point bPoint, ONSPD_Point cPoint, ONSPD_Point dPoint,
            UKP_YM3 yM30v, UKP_YM3 yM31v, Data_RecordID PostcodeID0,
            Data_RecordID PostcodeID1) {
        double r = 0.0d;
        if (aPoint != null && bPoint != null) {
            r = aPoint.getDistance(bPoint);
        } else {
            oe.env.log("<Issue calculating distance between PostcodeID0 "
                    + PostcodeID0 + " and PostcodeID1 " + PostcodeID1 + "/>",
                    true);
            if (aPoint == null) {
                oe.env.log("No point look up for PostcodeID0 " + PostcodeID0
                        + " in " + yM30v, true);
                if (cPoint != null) {
                    oe.env.log("However there is a look up for PostcodeID0 "
                            + PostcodeID0 + " in " + yM31v
                            + "! Maybe use this instead?", true);
                }
            }
            if (bPoint == null) {
                oe.env.log("No point look up for PostcodeID1 " + PostcodeID1
                        + " in " + yM31v, true);
                if (dPoint != null) {
                    oe.env.log("However there is a look up for PostcodeID1 "
                            + PostcodeID1 + " in " + yM30v
                            + "! Maybe use this instead?", true);
                }
            }
            oe.env.log("</Issue calculating distance between PostcodeID0 "
                    + PostcodeID0 + " and PostcodeID1 " + PostcodeID1 + ">",
                    true);
        }
        return r;
    }

    public double getDistanceBetweenPostcodes(UKP_YM3 yM30v, UKP_YM3 yM31v,
            String postcode0, String postcode1) throws IOException,
            ClassNotFoundException {
        double r = 0.0d;
        ONSPD_Point a = getPointFromPostcode(yM30v, TYPE_UNIT, postcode0);
        ONSPD_Point b = getPointFromPostcode(yM31v, TYPE_UNIT, postcode1);
        if (a != null && b != null) {
            r = a.getDistance(b);
        } else {
            oe.env.log("<Issue calculating distance between PostcodeID0 "
                    + postcode0 + " and PostcodeID1 " + postcode1 + "/>",
                    true);
            if (a == null) {
                oe.env.log("No point look up for PostcodeID0 " + postcode0
                        + " in " + yM30v, true);
                a = getPointFromPostcode(yM31v, TYPE_UNIT, postcode0);
                if (a != null) {
                    oe.env.log("However there is a look up for PostcodeID0 "
                            + postcode0 + " in " + yM31v
                            + "! Maybe use this instead?", true);
                }
            }
            if (b == null) {
                oe.env.log("No point look up for PostcodeID1 " + postcode1
                        + " in " + yM31v, true);
                b = getPointFromPostcode(yM30v, TYPE_UNIT, postcode1);
                if (b != null) {
                    oe.env.log("However there is a look up for PostcodeID1 "
                            + postcode1 + " in " + yM30v
                            + "! Maybe use this instead?", true);
                }
            }
            oe.env.log("</Issue calculating distance between PostcodeID0 "
                    + postcode0 + " and PostcodeID1 " + postcode1 + ">",
                    true);
        }
        return r;
    }

    /**
     * @param ym3 nearestYM3ForONSPDLookup.
     * @param level Expects either "Unit", "Sector" or "Area".
     * @param p postcode
     * @return point
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    public ONSPD_Point getPointFromPostcode(UKP_YM3 ym3, int level, String p)
            throws IOException, ClassNotFoundException {
        return getPointFromPostcodeNew(ym3, level, formatPostcode(p));
    }

    /**
     * @param ym3 nearestYM3ForONSPDLookup
     * @param level Expects either "Unit", "Sector" or "Area"
     * @param p Formatted postcode.
     * @return point
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    public ONSPD_Point getPointFromPostcodeNew(UKP_YM3 ym3, int level, String p)
            throws IOException, ClassNotFoundException {
        return oe.getONSPDlookups().get(level).get(ym3).get(p);
    }

    /**
     * 2008_FEB 2008_MAY 2008_AUG 2008_NOV 2009_FEB 2009_MAY 2009_AUG 2009_NOV
     * 2010_FEB 2010_MAY 2010_AUG 2010_NOV
     *
     * 2011_MAY 2011_AUG 2011_NOV 2012_FEB 2012_MAY 2012_AUG 2012_NOV 2013_FEB
     * 2013_MAY 2013_AUG 2013_NOV 2014_FEB 2014_MAY 2014_AUG 2014_NOV 2015_FEB
     * 2015_MAY 2015_AUG 2015_NOV 2016_FEB 2016_MAY 2016_AUG 2016_NOV
     *
     * @param ym3 The YM3.
     * @return The nearest ym3 for the ONSPD Lookup.
     */
    public UKP_YM3 getNearestYM3ForONSPDLookup(UKP_YM3 ym3) {
        UKP_YM3 defaultLatest = new UKP_YM3(2017, 5);
        int year = ym3.getYear();
        int month = ym3.getMonth();
        if (year > 2016) {
            if (month == 1 || month == 2) {
                return new UKP_YM3(year, 2);
            }
            return defaultLatest;
        } else if (year < 2008) {
            return new UKP_YM3(2017, 2);
        } else if (year == 2011) {
            // There was no realease in February!
            if (month < 6) {
                return new UKP_YM3(2011, 5);
            } else if (month < 9) {
                return new UKP_YM3(2011, 8);
            } else if (month < 12) {
                return new UKP_YM3(2011, 11);
            } else {
                return new UKP_YM3(2012, 2);
            }
        } else {
            if (month < 3) {
                return new UKP_YM3(year, 2);
            } else if (month < 6) {
                return new UKP_YM3(year, 5);
            } else if (month < 9) {
                return new UKP_YM3(year, 8);
            } else if (month < 12) {
                return new UKP_YM3(year, 11);
            } else {
                if (year == 2010) {
                    return new UKP_YM3(2011, 5);
                } else {
                    if (year == 2016) {
                        return defaultLatest;
                    } else {
                        return new UKP_YM3(year + 1, 2);
                    }
                }
            }
        }
    }

    public UKP_YM3 getDefaultYM3() {
        return new UKP_YM3(2013, 8);
    }

    /**
     * @param p Formatted postcode.
     * @return {@code p} with a space added between the first and second parts
     * if it is long enough.
     */
    public String getPostcodePrintFormat(String p) {
        int length = p.length();
        if (length < 5) {
            return p;
        } else {
            return p.substring(0, length - 3) + " "
                    + p.substring(length - 3, length);
        }
    }

    /**
     * @param up unformattedUnitPostcode
     * @return A better format of the unformattedUnitPostcode
     */
    public String formatPostcode(String up) {
        if (up == null) {
            return "";
        } else {
            return up.replaceAll("[^A-Za-z0-9]", "");
        }
//        String result;
//        String postcodeNoSpaces;
//        if (unformattedUnitPostcode == null) {
//            result = "";
//        } else {
//            // Replace anything that is not Roman A-Z or a-z or 0-9 with nothing.
//            postcodeNoSpaces = unformattedUnitPostcode.replaceAll("[^A-Za-z0-9]", "");
////            postcodeNoSpaces = unformattedUnitPostcode.replaceAll(" ", "");
////            postcodeNoSpaces = postcodeNoSpaces.replaceAll("'", "");
////            postcodeNoSpaces = postcodeNoSpaces.replaceAll("\\.", "");
////            postcodeNoSpaces = postcodeNoSpaces.replaceAll("-", "");
////            postcodeNoSpaces = postcodeNoSpaces.replaceAll("_", "");
//            if (postcodeNoSpaces.length() < 5) {
//                //System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " too few characters to format.");
//                result = postcodeNoSpaces;
//            } else {
//                int length;
//                length = postcodeNoSpaces.length();
//                String firstPartPostcode;
//                firstPartPostcode = postcodeNoSpaces.substring(0, length - 3);
//                String secondPartPostcode;
//                secondPartPostcode = postcodeNoSpaces.substring(length - 3, length);
//                result = firstPartPostcode + " " + secondPartPostcode;
//            }
//        }
////        String[] postcodeSplit = unformattedUnitPostcode.split(" ");
////        if (postcodeSplit.length > 3) {
////            System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " cannot be formatted into a unit postcode");
////        } else {
////            if (postcodeSplit.length == 3) {
////                result = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
////                if (postcodeSplit[2].length() != 3) {
////                    System.out.println("Unusual length of second part of postcode " + unformattedUnitPostcode);
////                }
////            } else {
////                if (postcodeSplit.length == 2) {
////                    result = postcodeSplit[0] + " " + postcodeSplit[1];
////                    if (postcodeSplit[1].length() > 4) {
////                        System.out.println("Unusual length of first part of postcode " + unformattedUnitPostcode);
////                    }
////                }
////            }
////        }
//        return result;
    }

    /**
     * @param input Map where values are postcodes for which the coordinates are
     * to be returned as ONSPD_Points.
     * @param yM3v The ym3.
     * @return Map with keys as in input and values that are points. If no look
     * up is found for a postcode then it is not added.
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    public TreeMap<String, ONSPD_Point> postcodeToPoints(
            TreeMap<String, String> input, UKP_YM3 yM3v) throws IOException,
            ClassNotFoundException {
        TreeMap<String, ONSPD_Point> r = new TreeMap<>();
        Iterator<String> ite = input.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            String postcode = input.get(key);
            ONSPD_Point p = getPointFromPostcode(yM3v, TYPE_UNIT, postcode);
            if (p == null) {
                System.out.println("No point for postcode " + postcode);
            } else {
                r.put(key, p);
            }
        }
        return r;
    }

    /**
     * For Unit Postcode "LS2 9JT": Postcode District = "LS2";
     * https://www.getthedata.com/postcode
     *
     * @param p unitPostcode
     * @return The postcode district.
     */
    public static String getPostcodeDistrict(String p) {
        String r = "";
        String p1 = p.trim();
        if (p1.length() < 3) {
            //throw new Exception("Postcode format exception 1 in getPostcodeSector(" + unitPostcode + " )");
            return r;
        } else {
            String[] pp = p1.split(" ");
            switch (pp.length) {
                case 2:
                    r = pp[0];
                    return r;
                case 1:
                    int length = p1.length();
                    r = p1.substring(0, length - 2);
                    r = r.trim();
                    if (r.length() < 3) {
                        return "";
                    }
                    return r;
                default:
                    //throw new Exception("Postcode format exception 2 in getPostcodeSector(" + unitPostcode + " )");
                    // Put the first and second parts together.
                    r += pp[0];
                    r += pp[1];
                    if (r.length() < 3) {
                        return "";
                    }
                    return r;
            }
        }
    }

    public UKP_Data(UKP_Environment e) {
        super(e);
        files = e.files;
        checker = new UKPC_Checker();
    }

    @Deprecated
    public String getDefaultLookupFilename() {
        String selection = "LS";
        selection += "BD";
        selection += "HG";
        //selection += "HD";
        selection += "CR";
        selection += "W";
        selection += "NP";
        selection += "BL";
        selection += "HX";
        selection += "HD";
        return "PostcodeLookUp_" + selection
                //+ "_" + YM3
                + "_TreeMap_String_Point.dat";
    }

    /**
     * @param ignorePointsAtOrigin If {@code true} then those postcodes with
     * eastings and northings at the origin are ignored (these are most likely
     * not at that location).
     * @param oFs ONSPDFiles
     * @param pf processedFilename
     * @return TreeMap with keys that are {@link UKP_YM3} and values which are
     * {@code TreeMap<String, ONSPD_Point>} where the keys are Postcode Units
     * and the values are ONSPD_Points.
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    public TreeMap<UKP_YM3, TreeMap<String, ONSPD_Point>> getPostcodeUnitPointLookups(
            boolean ignorePointsAtOrigin, TreeMap<UKP_YM3, Path> oFs,
            String pf) throws IOException, ClassNotFoundException {
        TreeMap<UKP_YM3, TreeMap<String, ONSPD_Point>> r = new TreeMap<>();
        Iterator<UKP_YM3> ite = oFs.keySet().iterator();
        while (ite.hasNext()) {
            UKP_YM3 YM3 = ite.next();
            Path outDir = Paths.get(files.getGeneratedONSPDDir().toString(), YM3.toString());
            Path outFile = Paths.get(outDir.toString(), pf);
            TreeMap<String, ONSPD_Point> postcodeUnitPointLookup;
            if (Files.exists(outFile)) {
                oe.env.log("Load " + outFile, true);
                postcodeUnitPointLookup = (TreeMap<String, ONSPD_Point>) Generic_IO.readObject(outFile);
            } else {
                Path f;
                f = oFs.get(YM3);
                oe.env.log("Format " + f, true);
                postcodeUnitPointLookup = initPostcodeUnitPointLookup(
                        f, ignorePointsAtOrigin);
                Files.createDirectories(outDir);
                Generic_IO.writeObject(postcodeUnitPointLookup, outFile);
            }
            r.put(YM3, postcodeUnitPointLookup);
        }
        return r;
    }

    @Deprecated
    public TreeMap<String, String[]> getPostcodeUnitCensusCodesLookup() {
        // Read NPD into a lookup
        TreeMap<String, String[]> lookup;
        lookup = null;
//        lookup = readONSPDIntoTreeMapPostcodeStrings(inputFile);
//        // Test some postcodes
//        String postcode = "LS2 9JT";
//        printTest2(lookup, postcode);
//        Generic_IO.writeObject(lookup, outputFile);
//        //lookup = (TreeMap<String, ONSPD_Point>) Generic_IO.readObject(lookupFile);
        return lookup;
    }

    /**
     * @param level "OA", "LSOA", "MSOA"
     * @param ym3 YM3NearestFormat
     * @param infile The path to the input file.
     * @param y The CensusYear 2001 or 2011.
     * @param outFile The path to the to store the result in.
     * @return Map with keys as postcode units and values as census codes given
     * by {@code level} and {@code y}.
     * @throws java.io.IOException If encountered.
     */
    public TreeMap<String, String> getPostcodeUnitCensusCodeLookup(
            Path infile, Path outFile, int level, int y,
            UKP_YM3 ym3) throws IOException {
        // Read NPD into a lookup
        TreeMap<String, String> lookup = readONSPDIntoTreeMapPostcodeString(
                infile, level, y, ym3);
        Generic_IO.writeObject(lookup, outFile);
        //lookup = (TreeMap<String, ONSPD_Point>) Generic_IO.readObject(outFile);
        return lookup;
    }

    /**
     * @param s Normally expecting an uppercase unit, sector, district or area
     * postcode, but can be anything.
     * @return
     * <ul>
     * <li>{@link #TYPE_UNIT} if {@code s} is a valid postcode unit.</li>
     * <li>{@link #TYPE_UNIT} if {@code s} is a valid postcode unit.</li>
     * <li>{@link #TYPE_SECTOR} if {@code s} is a valid postcode sector.</li>
     * <li>{@link #TYPE_DISTRICT} if {@code s} is a valid postcode
     * district.</li>
     * <li>{@link #TYPE_AREA} if {@code s} is a valid postcode area.</li>
     * <li>{@code -1} Otherwise.</li>
     * </ul>
     */
    public int getPostcodeLevel(String s) {
        if (checker.isValidPostcodeUnit(s)) {
            return TYPE_UNIT;
        } else if (checker.isValidPostcodeSector(s)) {
            return TYPE_SECTOR;
        } else if (checker.isValidPostcodeDistrict(s)) {
            return TYPE_DISTRICT;
        } else if (checker.isValidPostcodeArea(s)) {
            return TYPE_AREA;
        } else {
            return -1;
        }
    }

    public void run(int logID) throws IOException, ClassNotFoundException {
        String processedFilename = getDefaultLookupFilename();
        boolean ignorePointsAtOrigin = true;
        TreeMap<UKP_YM3, Path> InputONSPDFiles = files.getInputONSPDFiles();
        TreeMap<UKP_YM3, TreeMap<String, ONSPD_Point>> l;
        l = getPostcodeUnitPointLookups(ignorePointsAtOrigin, InputONSPDFiles,
                processedFilename);
    }

    public void run3() throws IOException {
        // Read NPD OutputArea code mapping
        // There are two OA codes and this is a lookup from one to another.
        HashSet<String> numerals_HashSet = getNumeralsHashSet();
        String filename = "oacode_new_to_old.txt";
        Path NPDDocumentsDirectory = Paths.get("/scratch01/Work/Projects/NewEnclosures/ONSPD/Documents/");
        Path NPDDocumentsLookUpsDirectory = Paths.get(
                NPDDocumentsDirectory.toString(), "Look-ups");
        Path file = Paths.get(NPDDocumentsLookUpsDirectory.toString(),
                filename);
        HashMap<String, String> oaCodeLookUp = readONSPD_OACodeLookup(file,
                numerals_HashSet);
//        Path oaCodeLookUpFile = Paths.get(
//                directory,
//                "oaCodeLookUp_HashmapStringString" + strings.sBinaryFileExtension);
//        Generic_IO.writeObject(oaCodeLookUp, oaCodeLookUpFile);
//        Generic_IO.writeObject(oaCodeLookUp, outputFile);
        //oaCodeLookUp = (HashMap<String, String>) Generic_IO.readObject(lookupFile);
    }

//    public TreeMap<String, String[]> run4() {
//        // Read NPD into a lookup
//        TreeMap<String, String[]> lookup;
//        lookup = readONSPDIntoTreeMapPostcodeStrings2(
//                inputFile);
//        // Test some postcodes
//        String postcode;
//        postcode = "LS7 2EU";
//        printTest2(
//                lookup,
//                postcode);
//        postcode = "LS2 9JT";
//        printTest2(
//                lookup,
//                postcode);
//        Generic_IO.writeObject(lookup, outputFile);
//        //lookup = (TreeMap<String, ONSPD_Point>) Generic_IO.readObject(lookupFile);
//        return lookup;
//    }
    public HashSet<String> getNumeralsHashSet() {
        HashSet<String> numerals_HashSet = new HashSet<>();
        numerals_HashSet.add("0");
        numerals_HashSet.add("1");
        numerals_HashSet.add("2");
        numerals_HashSet.add("3");
        numerals_HashSet.add("4");
        numerals_HashSet.add("5");
        numerals_HashSet.add("6");
        numerals_HashSet.add("7");
        numerals_HashSet.add("8");
        numerals_HashSet.add("9");
        return numerals_HashSet;
    }

    public HashMap<String, String> readONSPD_OACodeLookup(Path file,
            HashSet<String> numerals_HashSet) throws FileNotFoundException,
            IOException {
        HashMap<String, String> r = new HashMap<>();
        int lineCounter = 0;
        try (BufferedReader br = Generic_IO.getBufferedReader(file)) {
            StreamTokenizer st = getStreamTokeniser(br);
            String line = "";
            int tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line + " " + line);
                        String[] fields = line.split("  ");
                        //System.out.println("fields.length " + fields.length);
                        if (fields.length != 2) {
                            System.out.println(lineCounter + " " + line);
                            System.out.println("fields.length " + fields.length);
                        }
                        r.put(fields[0], fields[1]);
                        lineCounter++;
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
        }
        return r;
    }

    public TreeMap<String, ONSPD_Point> getStringToONSPD_PointLookup(Path file)
            throws IOException, ClassNotFoundException {
        return (TreeMap<String, ONSPD_Point>) Generic_IO.readObject(file);
    }

    public TreeMap<String, String[]> getStringToStringArrayLookup(Path file)
            throws IOException, ClassNotFoundException {
        return (TreeMap<String, String[]>) Generic_IO.readObject(file);
    }

    public HashMap<String, String> getStringToStringLookup(Path file)
            throws IOException, ClassNotFoundException {
        return (HashMap<String, String>) Generic_IO.readObject(file);
    }

    /**
     * Once all Unit Postcodes are read, then postcode sectors and postcode area
     * centroids are also added.
     *
     * http://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom Postcode
     * area is part of the outward code. The postcode area is between one and
     * two characters long and is all letters. Examples of postcode areas
     * include "L" for Liverpool, "RH" for Redhill and "EH" Edinburgh. A postal
     * area may cover a wide area, for example "RH" covers north Sussex, (which
     * has little to do with Redhill historically apart from the railway links),
     * and "BT" (Belfast) covers the whole of Northern Ireland.
     *
     * Postcode district is part of the outward code. It is one or two digits
     * (and sometimes a final letter) that are added to the end of the postcode
     * area. the outward code is between two and four characters long. Examples
     * of postcode districts include "W1A", "RH1", "RH10" or "SE1P".
     *
     * Postcode sector is made up of the postcode district, the single space,
     * and the first character of the inward code. It is between four and six
     * characters long (including the single space). Examples of postcode
     * sectors include "SW1W 0", "PO16 7", "GU16 7", or "L1 8".
     *
     * Postcode unit is two characters added to the end of the postcode sector.
     * Each postcode unit generally represents a street, part of a street, a
     * single address, a group of properties, a single property, a sub-section
     * of the property, an individual organisation or (for instance Driver and
     * Vehicle Licensing Agency) a subsection of the organisation. The level of
     * discrimination is often based on the amount of mail received by the
     * premises or business. Examples of postcode units include "SW1W 0NY",
     * "PO16 7GZ", "GU16 7HF", or "L1 8JQ".
     *
     * @param file
     * @param postcodeUnitPointLookup
     * @return
     */
    private TreeMap<String, ONSPD_Point> initPostcodeSectorPointLookup(
            TreeMap<String, ONSPD_Point> postcodeUnitPointLookup,
            boolean ignorePointsAtOrigin) {
        TreeMap<String, ONSPD_Point> result;
        result = new TreeMap<>();
        // Aggregate by postcode
        // Create postcodeSector to unitPostcodes look up
        TreeMap<String, HashSet<String>> postcodeSectorsAndUnitPostcodes;
        postcodeSectorsAndUnitPostcodes = new TreeMap<>();
        Iterator<String> ite;
        ite = postcodeUnitPointLookup.keySet().iterator();
        while (ite.hasNext()) {
            String unitPostcode = ite.next();
            String postcodeSector = getPostcodeSector(unitPostcode);
            if (postcodeSectorsAndUnitPostcodes.containsKey(postcodeSector)) {
                postcodeSectorsAndUnitPostcodes.get(postcodeSector).add(
                        unitPostcode);
            } else {
                postcodeSectorsAndUnitPostcodes.put(
                        postcodeSector, new HashSet<>());
            }
        }
        // Average points to get postcode sector centroids
        ite = postcodeSectorsAndUnitPostcodes.keySet().iterator();
        while (ite.hasNext()) {
            String postcodeSector = ite.next();
            HashSet<String> postcodes = postcodeSectorsAndUnitPostcodes.get(
                    postcodeSector);
            double sumx = 0;
            double sumy = 0;
            double n = postcodes.size();
            Iterator<String> ite2;
            ite2 = postcodes.iterator();
            while (ite2.hasNext()) {
                String unitPostcode = ite2.next();
                ONSPD_Point p = postcodeUnitPointLookup.get(unitPostcode);
                sumx += p.getX();
                sumy += p.getY();
            }
            int x = (int) (sumx / n);
            int y = (int) (sumy / n);
            ONSPD_Point postcodeSectorPoint = new ONSPD_Point(x, y);
            if (ignorePointsAtOrigin) {
                if (!(x < 1 || y < 1)) {
                    result.put(postcodeSector, postcodeSectorPoint);
                }
            } else {
                result.put(postcodeSector, postcodeSectorPoint);
            }
        }
        return result;
    }

    /**
     * Once all Unit Postcodes are read, then postcode sectors and postcode area
     * centroids are also added.
     *
     * http://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom Postcode
     * area is part of the outward code. The postcode area is between one and
     * two characters long and is all letters. Examples of postcode areas
     * include "L" for Liverpool, "RH" for Redhill and "EH" Edinburgh. A postal
     * area may cover a wide area, for example "RH" covers north Sussex, (which
     * has little to do with Redhill historically apart from the railway links),
     * and "BT" (Belfast) covers the whole of Northern Ireland.
     *
     * Postcode district is part of the outward code. It is one or two digits
     * (and sometimes a final letter) that are added to the end of the postcode
     * area. the outward code is between two and four characters long. Examples
     * of postcode districts include "W1A", "RH1", "RH10" or "SE1P".
     *
     * Postcode sector is made up of the postcode district, the single space,
     * and the first character of the inward code. It is between four and six
     * characters long (including the single space). Examples of postcode
     * sectors include "SW1W 0", "PO16 7", "GU16 7", or "L1 8".
     *
     * Postcode unit is two characters added to the end of the postcode sector.
     * Each postcode unit generally represents a street, part of a street, a
     * single address, a group of properties, a single property, a sub-section
     * of the property, an individual organisation or (for instance Driver and
     * Vehicle Licensing Agency) a subsection of the organisation. The level of
     * discrimination is often based on the amount of mail received by the
     * premises or business. Examples of postcode units include "SW1W 0NY",
     * "PO16 7GZ", "GU16 7HF", or "L1 8JQ".
     *
     * @param file
     * @param postcodeUnitPointLookup
     * @return
     */
    private TreeMap<String, ONSPD_Point> initPostcodeAreaPointLookup(
            TreeMap<String, ONSPD_Point> postcodeUnitPointLookup,
            boolean ignorePointsAtOrigin) {
        TreeMap<String, ONSPD_Point> result;
        result = new TreeMap<>();
        // Aggregate by postcode
        // Create postcodeSector to unitPostcodes look up
        TreeMap<String, HashSet<String>> postcodeAreasAndUnitPostcodes;
        postcodeAreasAndUnitPostcodes = new TreeMap<>();
        Iterator<String> ite;
        ite = postcodeUnitPointLookup.keySet().iterator();
        while (ite.hasNext()) {
            String unitPostcode = ite.next();
            String postcodeArea = getPostcodeArea(unitPostcode);
            if (postcodeAreasAndUnitPostcodes.containsKey(postcodeArea)) {
                postcodeAreasAndUnitPostcodes.get(postcodeArea).add(
                        unitPostcode);
            } else {
                postcodeAreasAndUnitPostcodes.put(
                        postcodeArea, new HashSet<>());
            }
        }
        // Average points to get postcode sector centroids
        ite = postcodeAreasAndUnitPostcodes.keySet().iterator();
        while (ite.hasNext()) {
            String postcodeSector = ite.next();
            HashSet<String> postcodes = postcodeAreasAndUnitPostcodes.get(
                    postcodeSector);
            double sumx = 0;
            double sumy = 0;
            double n = postcodes.size();
            Iterator<String> ite2;
            ite2 = postcodes.iterator();
            while (ite2.hasNext()) {
                String unitPostcode = ite2.next();
                ONSPD_Point p = postcodeUnitPointLookup.get(unitPostcode);
                sumx += p.getX();
                sumy += p.getY();
            }
            int x = (int) (sumx / n);
            int y = (int) (sumy / n);
            ONSPD_Point postcodeAreaPoint = new ONSPD_Point(x, y);
            if (ignorePointsAtOrigin) {
                if (!(x < 1 || y < 1)) {
                    result.put(postcodeSector, postcodeAreaPoint);
                }
            } else {
                result.put(postcodeSector, postcodeAreaPoint);
            }
        }
        return result;
    }

//    public String formatPostcodeForMapping(String postcode) {
//        String[] split = postcode.split(" ");
//        if (split.length == 2) {
//            return postcode;
//        }
//        int length = postcode.length();
//        String firstPartPostcode;
//        firstPartPostcode = postcode.substring(0,length - 3);
//        String secondPartPostcode;
//        secondPartPostcode = postcode.substring(length - 3);
//        return firstPartPostcode + " " + secondPartPostcode;        
//    }
    public String formatPostcodeForMapping(String postcode) {
        String[] split = postcode.split(" ");
        if (split.length == 2) {
            if (split[0].length() == 3) {
                return postcode;
            } else {
                return split[0] + split[1];
            }
        } else {
            int debug = 1;
        }
        return postcode;
    }

    /**
     * @param ym3 NearestYM3ForONSPDLookup
     * @param p PostcodeF
     * @return True iff Postcode is a valid Postcode.
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    public boolean isMappablePostcode(UKP_YM3 ym3, String p) throws IOException,
            ClassNotFoundException {
        if (p == null) {
            return false;
        }
        if (p.length() > 5) {
            TreeMap<String, ONSPD_Point> l = oe.getONSPDlookups().get(TYPE_UNIT)
                    .get(ym3);
            if (l == null) {
                System.err.println("yM3UnitPostcodeONSPDLookupsONS == null for "
                        + "NearestYM3ForONSPDLookup " + ym3);
            }
            return l.containsKey(p);
        }
        return false;
    }

    /**
     * For Unit Postcode "LS2 9JT": Postcode Sector = "LS2 9". There is no test
     * done that {@code p} is a valid unit postcode.
     *
     * @param p The unit postcode.
     * @return The postcode sector part of the postcode {@code p}.
     */
    public static String getPostcodeSector(String p) {
        if (p == null) {
            return null;
        }
        String r = "";
        String pt = p.trim();
        if (pt.length() < 5) {
            return r;
        } else {
            String[] pp = pt.split(" ");
            switch (pp.length) {
                case 2:
                    r = pp[0] + " " + pp[1].substring(0, 1);
                    return r;
                case 1:
                    int length = pt.length();
                    r = pt.substring(0, length - 2);
                    r = r.trim();
                    if (r.length() < 5) {
                        return "";
                    }
                    return r;
                default:
                    //throw new Exception("Postcode format exception 2 in getPostcodeSector(" + unitPostcode + " )");
                    // Put the first and second parts together and add the first part of the third
                    r += pp[0];
                    r += pp[1] + " ";
                    r += pp[2].substring(0, 1);
                    if (r.length() < 5) {
                        return "";
                    }
                    return r;
            }
        }
        //return result;
    }

    /**
     * For Unit Postcode "LS2 9JT": Postcode Area = "LS". There is no test done
     * that {@code p} is a valid unit postcode.
     * https://www.getthedata.com/postcode
     *
     * @param p The unit postcode.
     * @return The postcode sector part of the postcode {@code p}.
     */
    public static String getPostcodeArea(String p) {
        return p.split("[0-9]", 2)[0];
    }

    /**
     * This is currently only including postcodes beginning with LS, BD, HG, CR,
     * W, NP, BL, HX, HD. This almost certainly should be superseded with a
     * method that returns a map with keys that are faster to look up.
     *
     * @param f The file.
     * @param ignorePointsAtOrigin If true then postcodes that are at the origin
     * are ignored. These tend to be postcodes that are not geolocated.
     * @return A map of lookups from a postcode to a point.
     * @throws FileNotFoundException If encountered.
     * @throws IOException If encountered.
     */
    public TreeMap<String, ONSPD_Point> initPostcodeUnitPointLookup(Path f,
            boolean ignorePointsAtOrigin) throws FileNotFoundException,
            IOException {
        TreeMap<String, ONSPD_Point> r = new TreeMap<>();
        int lineCounter = 0;
        long rID = 0;
        try (BufferedReader br = Generic_IO.getBufferedReader(f)) {
            StreamTokenizer st = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            br.readLine();
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPD_Record rec = new ONSPD_Record(oe,
                                new Data_RecordID(rID), line);
                        rID++;
                        int easting = rec.getOseast1m();
                        int northing = rec.getOsnrth1m();
                        ONSPD_Point point = new ONSPD_Point(easting, northing);
                        String PostcodeF = rec.getPostcodeF();
                        if (ignorePointsAtOrigin) {
                            if (easting < 1 || northing < 1) {
                                /**
                                 * Postcodes ending ZZ are usually at origin,
                                 * but some others are too.
                                 */
                                int debug = 1;
                            } else if (PostcodeF.startsWith("LS")
                                    || PostcodeF.startsWith("BD")
                                    || PostcodeF.startsWith("HG")
                                    || PostcodeF.startsWith("CR")
                                    || PostcodeF.startsWith("W")
                                    || PostcodeF.startsWith("NP")
                                    || PostcodeF.startsWith("BL")
                                    || PostcodeF.startsWith("HX")
                                    || PostcodeF.startsWith("HD")) {
                                r.put(PostcodeF, point);
                            }
                        } else if (PostcodeF.startsWith("LS")
                                || PostcodeF.startsWith("BD")
                                || PostcodeF.startsWith("HG")
                                || PostcodeF.startsWith("CR")
                                || PostcodeF.startsWith("W")
                                || PostcodeF.startsWith("NP")
                                || PostcodeF.startsWith("BL")
                                || PostcodeF.startsWith("HX")
                                || PostcodeF.startsWith("HD")) {
                            r.put(PostcodeF, point);
                        }
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter
                                    + " lines out of something like 2560000");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
        }
        return r;
    }

//    public TreeMap<String, ONSPD_Record_2013_08Aug> readONSPDIntoTreeMapPostcodeONSPDRecord(
//            Path file) {
//        TreeMap<String, ONSPD_Record_2013_08Aug> result = new TreeMap<String, ONSPD_Record_2013_08Aug>();
//        try {
//            int lineCounter = 0;
//            int recordCounter = 0;
//            BufferedReader br;
//            br = Generic_IO.getBufferedReader(file);
//            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
//            String line = "";
//            //Skip the first line
//            int tokenType;
//            Generic_IO.skipline(aStreamTokenizer);
//            tokenType = aStreamTokenizer.nextToken();
//            while (tokenType != StreamTokenizer.TT_EOF) {
//                switch (tokenType) {
//                    case StreamTokenizer.TT_EOL:
//                        ONSPD_Record_2013_08Aug rec = new ONSPD_Record_2013_08Aug(line);
//                        result.put(rec.getPcd(), rec);
//                        lineCounter++;
//                        if (lineCounter % 100000 == 0) {
//                            System.out.println("Read " + lineCounter + " lines out of something like 2560000");
//                        }
//                        break;
//                    case StreamTokenizer.TT_WORD:
//                        line = aStreamTokenizer.sval;
//                        break;
//                }
//                tokenType = aStreamTokenizer.nextToken();
//            }
//            br.close();
//        } catch (IOException aIOException) {
//            System.err.println(aIOException.getMessage() + " in "
//                    + this.getClass().getName()
//                    + ".readONSPD(File)");
//            System.exit(2);
//        }
//        return result;
//    }
//    /**
//     * MSOA
//     *
//     * @param file
//     * @return
//     */
//    public TreeMap<String, String[]> readONSPDIntoTreeMapPostcodeStrings(
//            Path file) {
//        TreeMap<String, String[]> result = new TreeMap<String, String[]>();
//        try {
//            int lineCounter = 0;
//            int recordCounter = 0;
//            BufferedReader br;
//            br = Generic_IO.getBufferedReader(file);
//            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
//            String line = "";
//            //Skip the first line
//            int tokenType;
//            Generic_IO.skipline(aStreamTokenizer);
//            tokenType = aStreamTokenizer.nextToken();
//            while (tokenType != StreamTokenizer.TT_EOF) {
//                switch (tokenType) {
//                    case StreamTokenizer.TT_EOL:
//                        ONSPD_Record_2013_08Aug rec = new ONSPD_Record_2013_08Aug(line);
//                        String[] values = new String[4];
//                        values[0] = rec.getOa01();
//                        values[1] = rec.getMsoa01();
//                        values[2] = rec.getOa11();
//                        values[3] = rec.getMsoa11();
//                        result.put(rec.getPcd(), values);
//                        lineCounter++;
//                        if (lineCounter % 100000 == 0) {
//                            System.out.println("Read " + lineCounter + " lines out of something like 2560000");
//                        }
//                        break;
//                    case StreamTokenizer.TT_WORD:
//                        line = aStreamTokenizer.sval;
//                        break;
//                }
//                tokenType = aStreamTokenizer.nextToken();
//            }
//            br.close();
//        } catch (IOException aIOException) {
//            System.err.println(aIOException.getMessage() + " in "
//                    + this.getClass().getName()
//                    + ".readONSPD(File)");
//            System.exit(2);
//        }
//        return result;
//    }
    /**
     * @param file The file.
     * @param level The level
     * @param censusYear Either 2001 or 2011 or ignored if level is not a census
     * level.
     * @param YM3NearestFormat YM3NearestFormat
     * @return Map with postcode keys and level related values.
     * @throws java.io.FileNotFoundException If encountered.
     */
    public TreeMap<String, String> readONSPDIntoTreeMapPostcodeString(
            Path file, int level, int censusYear, UKP_YM3 YM3NearestFormat)
            throws FileNotFoundException, IOException {
        int year = YM3NearestFormat.getYear();
        int month = YM3NearestFormat.getMonth();
        oe.env.log("year " + year + " month " + month, true);
        TreeMap<String, String> result = new TreeMap<>();
        int lineCounter = 0;
        long rID = 0;
        try (BufferedReader br = Generic_IO.getBufferedReader(file)) {
            StreamTokenizer st = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            br.readLine();
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPD_Record0 rec;
                        Data_RecordID ID = new Data_RecordID(rID);
                        rID++;
                        if (year < 2011 || (year == 2011 && month < 4)) {
                            rec = new ONSPD_Record_2008_02Feb(oe, ID, line);
                        } else if (year < 2012 || (year == 2012 && month < 8)) {
                            rec = new ONSPD_Record_2011_05May(oe, ID, line);
                        } else if (year == 2012 && month < 11) {
                            rec = new ONSPD_Record_2011_05May(oe, ID, line);
                            //rec = new ONSPD_Record_2012_08Nov(oe, ID, line);
                        } else if (year < 2013 || (year == 2013 && month < 2)) {
                            rec = new ONSPD_Record_2012_11Nov(oe, ID, line);
                        } else if (year == 2013 && month < 5) {
                            rec = new ONSPD_Record_2013_02Feb(oe, ID, line);
                        } else if (year == 2013 && month < 8) {
                            rec = new ONSPD_Record_2013_05May(oe, ID, line);
                        } else if (year < 2014 || (year == 2014 && month < 11)) {
                            rec = new ONSPD_Record_2013_08Aug(oe, ID, line);
                        } else if (year < 2015 || (year == 2015 && month < 5)) {
                            rec = new ONSPD_Record_2014_11Nov(oe, ID, line);
                        } else if (year == 2015 && month < 8) {
                            rec = new ONSPD_Record_2015_05May(oe, ID, line);
                        } else if (year < 2016 || (year == 2016 && month < 2)) {
                            rec = new ONSPD_Record_2015_08Aug(oe, ID, line);
                        } else {
                            rec = new ONSPD_Record_2016_02Feb(oe, ID, line);
                        }
//                        if (YM3NearestFormat.equalsIgnoreCase("2016_Feb")) {
//                            rec = new ONSPD_Record_2016_02Feb(oe, ID, line);
//                        } else if (YM3NearestFormat.equalsIgnoreCase("2015_Aug")) {
//                            rec = new ONSPD_Record_2015_08Aug(oe, ID, line);
//                        } else if (YM3NearestFormat.equalsIgnoreCase("2015_May")) {
//                            rec = new ONSPD_Record_2015_05May(oe, ID, line);
//                        } else if (YM3NearestFormat.equalsIgnoreCase("2014_Nov")) {
//                            rec = new ONSPD_Record_2014_11Nov(oe, ID, line);
//                        } else {
//                            rec = new ONSPD_Record_2013_08Aug(oe, ID, line);
//                        }
                        String value = "";
                        if (level == TYPE_OA) {
                            if (censusYear == 2001) {
                                if (rec instanceof ONSPD_Record1) {
                                    value = ((ONSPD_Record1) rec).getOa01();
                                } else {
                                    value = null;
                                }
                            }
                            if (censusYear == 2011) {
                                if (rec instanceof ONSPD_Record1) {
                                    value = ((ONSPD_Record1) rec).getOa11();
                                } else if (rec instanceof ONSPD_Record_2011_05May) {
                                    value = ((ONSPD_Record_2011_05May) rec).getOacode();
                                } else {
                                    value = null;
                                }
                            }
                        }
                        if (level == TYPE_LSOA) {
                            if (censusYear == 2001) {
                                if (rec instanceof ONSPD_Record1) {
                                    value = ((ONSPD_Record1) rec).getLsoa01();
                                } else {
                                    value = null;
                                }
                            }
                            if (censusYear == 2011) {
                                if (rec instanceof ONSPD_Record1) {
                                    value = ((ONSPD_Record1) rec).getLsoa11();
                                } else if (rec instanceof ONSPD_Record_2011_05May) {
                                    value = null;
                                }
                            }
                        }
                        if (level == TYPE_MSOA) {
                            if (censusYear == 2001) {
                                if (rec instanceof ONSPD_Record1) {
                                    value = ((ONSPD_Record1) rec).getMsoa01();
                                } else {
                                    value = null;
                                }
                            }
                            if (censusYear == 2011) {
                                if (rec instanceof ONSPD_Record1) {
                                    value = ((ONSPD_Record1) rec).getMsoa11();
                                } else if (rec instanceof ONSPD_Record_2011_05May) {
                                    value = null;
                                }
                            }
                        }
                        String postcode = rec.getPcd();
                        //String PostcodeF = rec.getPostcodeF();
                        if (level == TYPE_UNIT) {
                            value = postcode;
                        }
                        if (level == TYPE_SECTOR) {
                            value = getPostcodeSector(postcode);
                        }
                        if (level == TYPE_DISTRICT) {
                            value = getPostcodeDistrict(postcode);
                        }
                        if (level == TYPE_ParliamentaryConstituency) {
                            value = rec.getPcon();
                        }
                        if (level == TYPE_StatisticalWard) {
                            value = rec.getStatsward();
                        }
                        result.put(rec.getPostcodeF(), value);
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines "
                                    + "out of something like 2560000");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
        }
        return result;
    }

    /**
     * OA01 LSOA01 OA11 LSOA11
     *
     * @param file The ONSPD file to read.
     * @return Map with postcode keys and values v where: v[1] =
     * rec.getLsoa01(); v[2] = rec.getOa11(); v[3] = rec.getLsoa11().
     * @throws java.io.IOException If encountered.
     */
    public TreeMap<String, String[]> readONSPDIntoTreeMapPostcodeStrings2(
            Path file) throws IOException {
        TreeMap<String, String[]> r = new TreeMap<>();
        int lineCounter = 0;
        long rID = 0;
        try (BufferedReader br = Generic_IO.getBufferedReader(file)) {
            StreamTokenizer st = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            br.readLine();
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPD_Record_2013_08Aug rec
                                = new ONSPD_Record_2013_08Aug(oe,
                                        new Data_RecordID(rID), line);
                        rID++;
                        String[] values = new String[4];
                        values[0] = rec.getOa01();
                        values[1] = rec.getLsoa01();
                        values[2] = rec.getOa11();
                        values[3] = rec.getLsoa11();
                        r.put(rec.getPcd(), values);
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines "
                                    + "out of something like 2560000");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
        }
        return r;
    }

    private StreamTokenizer getStreamTokeniser(BufferedReader br) {
        StreamTokenizer r = new StreamTokenizer(br);
        r.resetSyntax();
        r.wordChars(',', ',');
        r.wordChars('"', '"');
        r.wordChars('\'', '\'');
        r.wordChars('&', '&');
        r.wordChars(';', ';');
        r.wordChars('(', '(');
        r.wordChars(')', ')');
        r.wordChars('0', '0');
        r.wordChars('1', '1');
        r.wordChars('2', '2');
        r.wordChars('3', '3');
        r.wordChars('4', '4');
        r.wordChars('5', '5');
        r.wordChars('6', '6');
        r.wordChars('7', '7');
        r.wordChars('8', '8');
        r.wordChars('9', '9');
        r.wordChars('.', '.');
        r.wordChars('-', '-');
        r.wordChars('+', '+');
        r.wordChars('a', 'z');
        r.wordChars('A', 'Z');
        r.wordChars('\t', '\t');
        r.wordChars(' ', ' ');
        r.wordChars('#', '#');
        r.wordChars('*', '*');
        r.wordChars(':', ':');
        String s = "/";
        char c = s.charAt(0);
        int c_int = (int) c;
        //System.out.println("s " + s + " c " + c + " c_int " + c_int) ;
        r.wordChars(c_int, c_int);
        r.eolIsSignificant(true);
        return r;
    }

}
