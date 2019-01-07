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
package uk.ac.leeds.ccg.andyt.generic.data.onspd.process;

import java.io.File;
import java.io.PrintWriter;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Strings;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.io.ONSPD_Files;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Object;
//import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Collection;
//import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Combined_Record;
//import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Data;

/**
 *
 * @author geoagdt
 */
public class ONSPD_Main_Process extends ONSPD_Object {

    // For convenience
//    protected final ONSPD_Data data;
    protected final ONSPD_Strings Strings;
    protected final ONSPD_Files Files;

    // For logging.
    File logF;
    public static transient PrintWriter logPW;
    File logF0;
    public static transient PrintWriter logPW0;

    public ONSPD_Main_Process(ONSPD_Environment env) {
        super(env);
//        data = env.data;
        Strings = env.Strings;
        Files = env.Files;
    }

    public static void main(String[] args) {
        ONSPD_Main_Process p;
        ONSPD_Environment env;
        env = new ONSPD_Environment();
        p = new ONSPD_Main_Process(env);
        p.Files.setDataDirectory(new File(System.getProperty("user.dir"), "data"));
        // Main switches
        p.doLoadDataIntoCaches = true; // rename/reuse just left here for convenience...
        p.run();
    }

    public void run() {
        logF0 = new File(Files.getOutputDataDir(Strings), "log0.txt");
        logPW0 = Generic_IO.getPrintWriter(logF0, false); // Overwrite log file.

        File indir;
        File outdir;
        File generateddir;
//        ONSPD_HHOLD_Handler hholdHandler;
//
//        indir = Files.getONSPDInputDir();
//        generateddir = Files.getGeneratedONSPDDir();
//        outdir = new File(generateddir, "Subsets");
//        outdir.mkdirs();
//        hholdHandler = new ONSPD_HHOLD_Handler(Env.Files, Env.Strings, indir);
//
//        int chunkSize;
//        chunkSize = 256; //1024; 512; 256;
//        doDataProcessingStep1New(indir, outdir, hholdHandler);
//        doDataProcessingStep2(indir, outdir, hholdHandler, chunkSize);

        logPW.close();
    }

    public static void log0(String s) {
        logPW.println(s);
    }

    public static void log1(String s) {
        System.out.println(s);
    }

    public static void log(String s) {
        logPW.println(s);
        System.out.println(s);
    }

    public static void logStart(String s) {
        s = "<" + s + ">";
        logPW.println(s);
        System.out.println(s);
    }

    public static void logEnd(String s) {
        s = "</" + s + ">";
        logPW.println(s);
        System.out.println(s);
    }

    boolean doJavaCodeGeneration = false;
    boolean doLoadDataIntoCaches = false;

}
