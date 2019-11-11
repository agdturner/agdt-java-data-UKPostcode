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
package uk.ac.leeds.ccg.andyt.generic.data.onspd.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.data.id.Data_RecordID;
import uk.ac.leeds.ccg.andyt.data.postcode.Data_UKPostcodeHandler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Strings;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.io.ONSPD_Files;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;

/**
 * A class for adding coordinate data and area codes for UK postcodes.
 * https://geoportal.statistics.gov.uk/Docs/PostCodes/ONSPD_AUG_2013_csv.zip
 */
public class ONSPD_Handler extends Data_UKPostcodeHandler implements Serializable {

    protected final transient ONSPD_Environment env;
    protected final transient ONSPD_Files files;
    public final String TYPE_UNIT = "Unit";
    public final String TYPE_SECTOR = "Sector";
    public final String TYPE_DISTRICT = "District";
    public final String TYPE_AREA = "Area";

    //TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> ONSPDLookups;
    public double getDistanceBetweenPostcodes(ONSPD_Point aPoint,
            ONSPD_Point bPoint, ONSPD_Point cPoint, ONSPD_Point dPoint,
            ONSPD_YM3 yM30v, ONSPD_YM3 yM31v, Data_RecordID PostcodeID0,
            Data_RecordID PostcodeID1) {
        double r = 0.0d;
        //ONSPD_Point aPoint;
        //aPoint = env.getSHBE_Data().getPostcodeIDToPointLookup(yM30v).get(PostcodeID0);
        //ONSPD_Point bPoint;
        //bPoint = env.getSHBE_Data().getPostcodeIDToPointLookup(yM31v).get(PostcodeID1);
        if (aPoint != null && bPoint != null) {
            r = aPoint.getDistance(bPoint);
        } else {
            env.env.log("<Issue calculating distance between PostcodeID0 "
                    + PostcodeID0 + " and PostcodeID1 " + PostcodeID1 + "/>",
                    true);
            if (aPoint == null) {
                env.env.log("No point look up for PostcodeID0 " + PostcodeID0
                        + " in " + yM30v, true);
                if (cPoint != null) {
                    env.env.log("However there is a look up for PostcodeID0 "
                            + PostcodeID0 + " in " + yM31v
                            + "! Maybe use this instead?", true);
                }
            }
            if (bPoint == null) {
                env.env.log("No point look up for PostcodeID1 " + PostcodeID1
                        + " in " + yM31v, true);
                if (dPoint != null) {
                    env.env.log("However there is a look up for PostcodeID1 "
                            + PostcodeID1 + " in " + yM30v
                            + "! Maybe use this instead?", true);
                }
            }
            env.env.log("</Issue calculating distance between PostcodeID0 "
                    + PostcodeID0 + " and PostcodeID1 " + PostcodeID1 + ">",
                    true);
        }
        return r;
    }

    public double getDistanceBetweenPostcodes(ONSPD_YM3 yM30v, ONSPD_YM3 yM31v,
            String postcode0, String postcode1) {
        double result = 0.0d;
        ONSPD_Point aPoint;
        aPoint = getPointFromPostcode(yM30v, TYPE_UNIT, postcode0);
        ONSPD_Point bPoint;
        bPoint = getPointFromPostcode(yM31v, TYPE_UNIT, postcode1);
        if (aPoint != null && bPoint != null) {
            result = aPoint.getDistance(bPoint);
        } else {
            env.env.log("<Issue calculating distance between PostcodeID0 "
                    + postcode0 + " and PostcodeID1 " + postcode1 + "/>",
                    true);
            if (aPoint == null) {
                env.env.log("No point look up for PostcodeID0 " + postcode0
                        + " in " + yM30v, true);
                aPoint = getPointFromPostcode(yM31v, TYPE_UNIT, postcode0);
                if (aPoint != null) {
                    env.env.log("However there is a look up for PostcodeID0 "
                            + postcode0 + " in " + yM31v
                            + "! Maybe use this instead?", true);
                }
            }
            if (bPoint == null) {
                env.env.log("No point look up for PostcodeID1 " + postcode1
                        + " in " + yM31v, true);
                bPoint = getPointFromPostcode(yM30v, TYPE_UNIT, postcode1);
                if (bPoint != null) {
                    env.env.log("However there is a look up for PostcodeID1 "
                            + postcode1 + " in " + yM30v
                            + "! Maybe use this instead?", true);
                }
            }
            env.env.log("</Issue calculating distance between PostcodeID0 "
                    + postcode0 + " and PostcodeID1 " + postcode1 + ">",
                    true);
        }
        return result;
    }

    /**
     *
     * @param nearestYM3ForONSPDLookup
     * @param level Expects either "Unit", "Sector" or "Area"
     * @param postcode
     * @return
     */
    public ONSPD_Point getPointFromPostcode(ONSPD_YM3 nearestYM3ForONSPDLookup,
            String level, String postcode) {
        ONSPD_Point r;
        String formattedPostcode;
        formattedPostcode = formatPostcode(postcode);
        r = env.getONSPDlookups().get(level).get(nearestYM3ForONSPDLookup)
                .get(formattedPostcode);
        return r;
    }

    /**
     *
     * @param NearestYM3ForONSPDLookup
     * @param level Expects either "Unit", "Sector" or "Area"
     * @param PostcodeF
     * @return
     */
    public ONSPD_Point getPointFromPostcodeNew(
            ONSPD_YM3 NearestYM3ForONSPDLookup, String level,
            String PostcodeF) {
        ONSPD_Point r;
        TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>> ONSPDlookupsLevel;
        ONSPDlookupsLevel = env.getONSPDlookups().get(level);
        TreeMap<String, ONSPD_Point> l;
        l = ONSPDlookupsLevel.get(NearestYM3ForONSPDLookup);
        r = l.get(PostcodeF);
        return r;
    }

    /**
     * 2008_FEB 2008_MAY 2008_AUG 2008_NOV 2009_FEB 2009_MAY 2009_AUG 2009_NOV
     * 2010_FEB 2010_MAY 2010_AUG 2010_NOV
     *
     * 2011_MAY 2011_AUG 2011_NOV 2012_FEB 2012_MAY 2012_AUG 2012_NOV 2013_FEB
     * 2013_MAY 2013_AUG 2013_NOV 2014_FEB 2014_MAY 2014_AUG 2014_NOV 2015_FEB
     * 2015_MAY 2015_AUG 2015_NOV 2016_FEB 2016_MAY 2016_AUG 2016_NOV
     *
     * @param YM3
     * @return
     */
    public ONSPD_YM3 getNearestYM3ForONSPDLookup(ONSPD_YM3 YM3) {
        ONSPD_YM3 defaultLatest = new ONSPD_YM3(2017, 5);
        int year = YM3.getYear();
        int month = YM3.getMonth();
        if (year > 2016) {
            if (month == 1 || month == 2) {
                return new ONSPD_YM3(year, 2);
            }
            return defaultLatest;
        } else if (year < 2008) {
            return new ONSPD_YM3(2017, 2);
        } else if (year == 2011) {
            // There was no realease in February!
            if (month < 6) {
                return new ONSPD_YM3(2011, 5);
            } else if (month < 9) {
                return new ONSPD_YM3(2011, 8);
            } else if (month < 12) {
                return new ONSPD_YM3(2011, 11);
            } else {
                return new ONSPD_YM3(2012, 2);
            }
        } else {
            if (month < 3) {
                return new ONSPD_YM3(year, 2);
            } else if (month < 6) {
                return new ONSPD_YM3(year, 5);
            } else if (month < 9) {
                return new ONSPD_YM3(year, 8);
            } else if (month < 12) {
                return new ONSPD_YM3(year, 11);
            } else {
                if (year == 2010) {
                    return new ONSPD_YM3(2011, 5);
                } else {
                    if (year == 2016) {
                        return defaultLatest;
                    } else {
                        return new ONSPD_YM3(year + 1, 2);
                    }
                }
            }
        }
    }

    public ONSPD_YM3 getDefaultYM3() {
        return new ONSPD_YM3(2013, 8);
    }

    /**
     * Return postcodef with a space added between the first and second parts if
     * it is long enough.
     *
     * @param postcodef
     * @return
     */
    public String getPostcodePrintFormat(String postcodef) {
        int length;
        length = postcodef.length();
        if (length < 5) {
            return postcodef;
        } else {
            String firstPartPostcode;
            firstPartPostcode = postcodef.substring(0, length - 3);
            String secondPartPostcode;
            secondPartPostcode = postcodef.substring(length - 3, length);
            return firstPartPostcode + " " + secondPartPostcode;
        }
    }

    /**
     * @see also
     * @param unformattedUnitPostcode
     * @return A better format of the unformattedUnitPostcode
     */
    public String formatPostcode(String unformattedUnitPostcode) {
        if (unformattedUnitPostcode == null) {
            return "";
        } else {
            return unformattedUnitPostcode.replaceAll("[^A-Za-z0-9]", "");
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
     * @param input TreeMap<String, String> where values are postcodes for which
     * the coordinates are to be returned as a ONSPD_Point.
     * @param yM3v
     * @return TreeMap<String, ONSPD_Point> with the keys as in input and values
     * calculated using getPointFromPostcode(value). If no look up is found for
     * a postcode its key does not get put into the result.
     */
    public TreeMap<String, ONSPD_Point> postcodeToPoints(
            TreeMap<String, String> input, ONSPD_YM3 yM3v) {
        TreeMap<String, ONSPD_Point> r;
        r = new TreeMap<>();
        Iterator<String> ite_String = input.keySet().iterator();
        while (ite_String.hasNext()) {
            String key = ite_String.next();
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
     *
     * @param unitPostcode
     * @return
     */
    public String getPostcodeDistrict(String unitPostcode) {
        String result = "";
        String p;
        p = unitPostcode.trim();
        if (p.length() < 3) {
            //throw new Exception("Postcode format exception 1 in getPostcodeSector(" + unitPostcode + " )");
            return result;
        } else {
            String[] pp = p.split(" ");
            switch (pp.length) {
                case 2:
                    result = pp[0];
                    return result;
                case 1:
                    int length = p.length();
                    result = p.substring(0, length - 2);
                    result = result.trim();
                    if (result.length() < 3) {
                        return "";
                    }
                    return result;
                default:
                    //throw new Exception("Postcode format exception 2 in getPostcodeSector(" + unitPostcode + " )");
                    // Put the first and second parts together.
                    result += pp[0];
                    result += pp[1];
                    if (result.length() < 3) {
                        return "";
                    }
                    return result;
            }
        }
    }

//    public ONSPD_Handler() {
//    }
    public ONSPD_Handler(ONSPD_Environment env) {
        this.env = env;
        this.files = env.files;
    }

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
     *
     * @param ignorePointsAtOrigin
     * @param ONSPDFiles
     * @param processedFilename
     * @return TreeMap with keys that are {@Link ONSPD_YM3} and values which are
     * {@code TreeMap<String, ONSPD_Point>} where the keys are Postcode Units
     * and the values are ONSPD_Points.
     */
    public TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>> getPostcodeUnitPointLookups(
            boolean ignorePointsAtOrigin, TreeMap<ONSPD_YM3, File> ONSPDFiles,
            String processedFilename) {
        TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>> result;
        result = new TreeMap<>();
        Iterator<ONSPD_YM3> ite;
        ite = ONSPDFiles.keySet().iterator();
        while (ite.hasNext()) {
            ONSPD_YM3 YM3;
            YM3 = ite.next();
            File outDir;
            outDir = new File(files.getGeneratedONSPDDir(), YM3.toString());
            File outFile = new File(outDir, processedFilename);
            TreeMap<String, ONSPD_Point> postcodeUnitPointLookup;
            if (outFile.exists()) {
                env.env.log("Load " + outFile, true);
                postcodeUnitPointLookup = (TreeMap<String, ONSPD_Point>) env.env.io.readObject(outFile);
            } else {
                File f;
                f = ONSPDFiles.get(YM3);
                env.env.log("Format " + f, true);
                postcodeUnitPointLookup = initPostcodeUnitPointLookup(
                        f, ignorePointsAtOrigin);
                outDir.mkdirs();
                env.env.io.writeObject(postcodeUnitPointLookup, outFile);
            }
            result.put(YM3, postcodeUnitPointLookup);
        }
        return result;
    }

    public TreeMap<String, String[]> getPostcodeUnitCensusCodesLookup() {
        // Read NPD into a lookup
        TreeMap<String, String[]> lookup;
        lookup = null;
//        lookup = readONSPDIntoTreeMapPostcodeStrings(
//                inputFile);
//        // Test some postcodes
//        String postcode;
//        postcode = "LS2 9JT";
//        printTest2(
//                lookup,
//                postcode);
//        Generic_IO.writeObject(lookup, outputFile);
//        //lookup = (TreeMap<String, ONSPD_Point>) Generic_IO.readObject(lookupFile);
        return lookup;
    }

    /**
     *
     * @param level "OA", "LSOA", "MSOA"
     * @param YM3NearestFormat
     * @param infile
     * @param CensusYear Has to be either 2001 or 2011.
     * @param outFile
     * @return
     */
    public TreeMap<String, String> getPostcodeUnitCensusCodeLookup(
            File infile,
            File outFile,
            String level,
            int CensusYear,
            ONSPD_YM3 YM3NearestFormat
    ) {
        // Read NPD into a lookup
        TreeMap<String, String> lookup;
        lookup = readONSPDIntoTreeMapPostcodeString(infile, level, CensusYear, YM3NearestFormat);
        env.env.io.writeObject(lookup, outFile);
//        //lookup = (TreeMap<String, ONSPD_Point>) Generic_IO.readObject(outFile);
        return lookup;
    }

    /**
     *
     * @param postcode
     * @return
     */
    public String getPostcodeLevel(String postcode) {
        if (postcode == null) {
            return null;
        }
        String p;
        p = postcode.trim();
//        if (p.length() < 2) {
//            return null;
//        }
        while (p.contains("  ")) {
            p = p.replaceAll("  ", " ");
        }
        String[] pa;
        pa = p.split(" ");
        if (pa.length == 1) {
            String pType;
            pType = getFirstPartPostcodeType(p);
            if (!pType.isEmpty()) {
                return TYPE_AREA;
            } else {
                return null;
            }
        }
        if (pa.length == 2) {
            String pa1;
            pa1 = pa[1];
            if (pa1.length() == 3) {
                return TYPE_UNIT;
            }
            if (pa1.length() == 1) {
                return TYPE_SECTOR;
            }
        }
        if (pa.length > 2) {
            // Assume the first two parts should be joined together as the 
            // outward part of the postcode and the third part is the inward 
            // part.
            String pa1;
            pa1 = pa[2];
            if (pa1.length() == 3) {
                return TYPE_UNIT;
            }
            if (pa1.length() == 1) {
                return TYPE_SECTOR;
            }
        }
        return null;
    }

    public void run(int logID) {
        String processedFilename = getDefaultLookupFilename();
        boolean ignorePointsAtOrigin = true;
        TreeMap<ONSPD_YM3, File> InputONSPDFiles;
        InputONSPDFiles = files.getInputONSPDFiles();
        TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>> l;
        l = getPostcodeUnitPointLookups(ignorePointsAtOrigin, InputONSPDFiles,
                processedFilename);
    }

    public void run3() {
        // Read NPD OutputArea code mapping
        // There are two OA codes and this is a lookup from one to another.
        HashSet<String> numerals_HashSet = getNumeralsHashSet();
        String filename = "oacode_new_to_old.txt";
        File NPDDocumentsDirectory = new File("/scratch01/Work/Projects/NewEnclosures/ONSPD/Documents/");
        File NPDDocumentsLookUpsDirectory = new File(
                NPDDocumentsDirectory,
                "Look-ups");
        File file = new File(
                NPDDocumentsLookUpsDirectory,
                filename);
        HashMap<String, String> oaCodeLookUp = readONSPD_OACodeLookup(
                file,
                numerals_HashSet);
//        File oaCodeLookUpFile = new File(
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

    public HashMap<String, String> readONSPD_OACodeLookup(
            File file,
            HashSet<String> numerals_HashSet) {
        HashMap<String, String> result = new HashMap<>();
        try {
            int lineCounter = 0;
            //int recordCounter = 0;
            BufferedReader br;
            br = env.env.io.getBufferedReader(file);
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            //skipline(aStreamTokenizer);
            tokenType = aStreamTokenizer.nextToken();
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
                        result.put(fields[0], fields[1]);
                        lineCounter++;
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = aStreamTokenizer.sval;
                        break;
                }
                tokenType = aStreamTokenizer.nextToken();
            }
            br.close();
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD2(File)");
            System.exit(2);
        }
        return result;
    }

    public TreeMap<String, ONSPD_Point> getStringToONSPD_PointLookup(File file) {
        return (TreeMap<String, ONSPD_Point>) env.env.io.readObject(file);
    }

    public TreeMap<String, String[]> getStringToStringArrayLookup(File file) {
        return (TreeMap<String, String[]>) env.env.io.readObject(file);
    }

    public HashMap<String, String> getStringToStringLookup(File file) {
        return (HashMap<String, String>) env.env.io.readObject(file);
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
     *
     * @param NearestYM3ForONSPDLookup
     * @param PostcodeF
     * @return True iff Postcode is a valid Postcode.
     */
    public boolean isMappablePostcode(
            ONSPD_YM3 NearestYM3ForONSPDLookup,
            String PostcodeF) {
        if (PostcodeF == null) {
            return false;
        }
        if (PostcodeF.length() > 5) {
            boolean isMappablePostcode;
//            TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> ONSPDLookups;
//            ONSPDLookups = Maps.getONSPDlookups();
            TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>> ONSPDLookupUnitPostcode;
            ONSPDLookupUnitPostcode = env.getONSPDlookups().get(TYPE_UNIT);
            TreeMap<String, ONSPD_Point> ONSPDLookupUnitPostcodeNearestYM3;
            ONSPDLookupUnitPostcodeNearestYM3 = ONSPDLookupUnitPostcode.get(NearestYM3ForONSPDLookup);
            if (ONSPDLookupUnitPostcodeNearestYM3 == null) {
                System.err.println("yM3UnitPostcodeONSPDLookupsONS == null for NearestYM3ForONSPDLookup " + NearestYM3ForONSPDLookup);
            }
            isMappablePostcode = ONSPDLookupUnitPostcodeNearestYM3.containsKey(PostcodeF);
            return isMappablePostcode;
        }
        return false;
    }

    /**
     * For Unit Postcode "LS2 9JT": Postcode Sector = "LS2 9"
     *
     * @param unitPostcode
     * @return
     */
    public String getPostcodeSector(String unitPostcode) {
        if (unitPostcode == null) {
            return null;
        }
        String result = "";
        String p;
        p = unitPostcode.trim();
        if (p.length() < 5) {
            //throw new Exception("Postcode format exception 1 in getPostcodeSector(" + unitPostcode + " )");
            return result;
        } else {
            String[] pp = p.split(" ");
            switch (pp.length) {
                case 2:
                    result = pp[0] + " " + pp[1].substring(0, 1);
                    return result;
                case 1:
                    int length = p.length();
                    result = p.substring(0, length - 2);
                    result = result.trim();
                    if (result.length() < 5) {
                        return "";
                    }
                    return result;
                default:
                    //throw new Exception("Postcode format exception 2 in getPostcodeSector(" + unitPostcode + " )");
                    // Put the first and second parts together and add the first part of the third
                    result += pp[0];
                    result += pp[1] + " ";
                    result += pp[2].substring(0, 1);
                    if (result.length() < 5) {
                        return "";
                    }
                    return result;
            }
        }
        //return result;
    }

    public String getPostcodeArea(String ONSPDPostcodeUnit) {
        String result;
        int length = ONSPDPostcodeUnit.length();
        result = ONSPDPostcodeUnit.substring(0, length - 3);
        result = result.trim();
//        // With a Space
//        String outward = ONSPDPostcodeUnit.substring(length - 3);
//        String inward = ONSPDPostcodeUnit.substring(0,length - 4).trim();
//        result = outward + " " + inward.substring(0, 1);
        return result;
    }

    public TreeMap<String, ONSPD_Point> initPostcodeUnitPointLookup(
            File file,
            boolean ignorePointsAtOrigin) {
        TreeMap<String, ONSPD_Point> result = new TreeMap<>();
        try {
            int lineCounter = 0;
            long rID = 0;
            BufferedReader br = env.env.io.getBufferedReader(file);
            StreamTokenizer st = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            env.env.io.skipline(st);
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPD_Record rec;
                        rec = new ONSPD_Record(env, new Data_RecordID(rID), line);
                        rID++;
                        int easting = rec.getOseast1m();
                        int northing = rec.getOsnrth1m();
                        ONSPD_Point point;
                        point = new ONSPD_Point(easting, northing);
                        String PostcodeF = rec.getPostcodeF();
                        if (ignorePointsAtOrigin) {
                            // Test for orgin point (postcodes ending ZZ are usually at origin, but some others are too.)
//                            if (!(easting == 0 && northing == 0)) {
//                                result.put(rec.getPcd(), point);
//                            } else {
//                                int debug = 1;
//                            }
                            if (easting < 1 || northing < 1) {
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
                                result.put(PostcodeF, point);
                            }
//                            if (postcode.endsWith("ZZ")) {
//                                int debug = 1;
//                            }
                        } else if (PostcodeF.startsWith("LS")
                                || PostcodeF.startsWith("BD")
                                || PostcodeF.startsWith("HG")
                                || PostcodeF.startsWith("CR")
                                || PostcodeF.startsWith("W")
                                || PostcodeF.startsWith("NP")
                                || PostcodeF.startsWith("BL")
                                || PostcodeF.startsWith("HX")
                                || PostcodeF.startsWith("HD")) {
                            result.put(PostcodeF, point);
                        }
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines out of something like 2560000");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage() + " in "
                    + "ONSPD_Postcode_Handler.initPostcodeUnitPointLookup(File,boolean)");
            System.exit(2);
        }
        return result;
    }

//    public TreeMap<String, ONSPD_Record_2013_08Aug> readONSPDIntoTreeMapPostcodeONSPDRecord(
//            File file) {
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
//            File file) {
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
     * @param file
     * @param level
     * @param censusYear Either 2001 or 2011 or ignored if level is not a census
     * level.
     * @param YM3NearestFormat
     * @return
     */
    public TreeMap<String, String> readONSPDIntoTreeMapPostcodeString(
            File file, String level, int censusYear, ONSPD_YM3 YM3NearestFormat) {
        int year = YM3NearestFormat.getYear();
        int month = YM3NearestFormat.getMonth();
        env.env.log("year " + year + " month " + month, true);
        TreeMap<String, String> result = new TreeMap<>();
        try {
            int lineCounter = 0;
            long rID = 0;
            BufferedReader br = env.env.io.getBufferedReader(file);
            StreamTokenizer st = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            env.env.io.skipline(st);
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPD_Record0 rec;
                        Data_RecordID ID = new Data_RecordID(rID);
                        rID++;
                        if (year < 2011 || (year == 2011 && month < 4)) {
                            rec = new ONSPD_Record_2008_02Feb(env, ID, line);
                        } else if (year < 2012 || (year == 2012 && month < 8)) {
                            rec = new ONSPD_Record_2011_05May(env, ID, line);
                        } else if (year == 2012 && month < 11) {
                            rec = new ONSPD_Record_2011_05May(env, ID, line);
                            //rec = new ONSPD_Record_2012_08Nov(env, ID, line);
                        } else if (year < 2013 || (year == 2013 && month < 2)) {
                            rec = new ONSPD_Record_2012_11Nov(env, ID, line);
                        } else if (year == 2013 && month < 5) {
                            rec = new ONSPD_Record_2013_02Feb(env, ID, line);
                        } else if (year == 2013 && month < 8) {
                            rec = new ONSPD_Record_2013_05May(env, ID, line);
                        } else if (year < 2014 || (year == 2014 && month < 11)) {
                            rec = new ONSPD_Record_2013_08Aug(env, ID, line);
                        } else if (year < 2015 || (year == 2015 && month < 5)) {
                            rec = new ONSPD_Record_2014_11Nov(env, ID, line);
                        } else if (year == 2015 && month < 8) {
                            rec = new ONSPD_Record_2015_05May(env, ID, line);
                        } else if (year < 2016 || (year == 2016 && month < 2)) {
                            rec = new ONSPD_Record_2015_08Aug(env, ID, line);
                        } else {
                            rec = new ONSPD_Record_2016_02Feb(env, ID, line);
                        }
//                        if (YM3NearestFormat.equalsIgnoreCase("2016_Feb")) {
//                            rec = new ONSPD_Record_2016_02Feb(env, ID, line);
//                        } else if (YM3NearestFormat.equalsIgnoreCase("2015_Aug")) {
//                            rec = new ONSPD_Record_2015_08Aug(env, ID, line);
//                        } else if (YM3NearestFormat.equalsIgnoreCase("2015_May")) {
//                            rec = new ONSPD_Record_2015_05May(env, ID, line);
//                        } else if (YM3NearestFormat.equalsIgnoreCase("2014_Nov")) {
//                            rec = new ONSPD_Record_2014_11Nov(env, ID, line);
//                        } else {
//                            rec = new ONSPD_Record_2013_08Aug(env, ID, line);
//                        }
                        String value = "";
                        if (level.equalsIgnoreCase(ONSPD_Strings.s_OA)) {
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
                        if (level.equalsIgnoreCase(ONSPD_Strings.s_LSOA)) {
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
                        if (level.equalsIgnoreCase(ONSPD_Strings.s_MSOA)) {
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
                        if (level.equalsIgnoreCase(ONSPD_Strings.s_PostcodeUnit)) {
                            value = postcode;
                        }
                        if (level.equalsIgnoreCase(ONSPD_Strings.s_PostcodeSector)) {
                            value = getPostcodeSector(postcode);
                        }
                        if (level.equalsIgnoreCase(ONSPD_Strings.s_PostcodeDistrict)) {
                            value = getPostcodeDistrict(postcode);
                        }
                        if (level.equalsIgnoreCase(ONSPD_Strings.s_ParliamentaryConstituency)) {
                            value = rec.getPcon();
                        }
                        if (level.equalsIgnoreCase(ONSPD_Strings.s_StatisticalWard)) {
                            value = rec.getStatsward();
                        }
                        result.put(rec.getPostcodeF(), value);
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines out of something like 2560000");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD(File)");
            System.exit(2);
        }
        return result;
    }

    /**
     * OA01 LSOA01 OA11 LSOA11
     *
     * @param file
     * @return LSOA
     */
    public TreeMap<String, String[]> readONSPDIntoTreeMapPostcodeStrings2(
            File file) {
        TreeMap<String, String[]> result = new TreeMap<>();
        try {
            int lineCounter = 0;
            long rID = 0;
            BufferedReader br;
            br = env.env.io.getBufferedReader(file);
            StreamTokenizer st = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            env.env.io.skipline(st);
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPD_Record_2013_08Aug rec = new ONSPD_Record_2013_08Aug(env, new Data_RecordID(rID), line);
                        rID ++;
                        String[] values = new String[4];
                        values[0] = rec.getOa01();
                        values[1] = rec.getLsoa01();
                        values[2] = rec.getOa11();
                        values[3] = rec.getLsoa11();
                        result.put(rec.getPcd(), values);
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines out of something like 2560000");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD(File)");
            System.exit(2);
        }
        return result;
    }

    private StreamTokenizer getStreamTokeniser(BufferedReader br) {
        StreamTokenizer r;
        r = new StreamTokenizer(br);
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
