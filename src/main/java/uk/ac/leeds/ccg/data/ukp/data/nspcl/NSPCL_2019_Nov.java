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
package uk.ac.leeds.ccg.data.ukp.data.nspcl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Environment;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Object;
import uk.ac.leeds.ccg.data.ukp.data.id.UKP_UnitID;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.format.Data_ReadCSV;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.ukpc.UKPC_Checker;

/**
 * A class for loading and pre-processing the NSPCL.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class NSPCL_2019_Nov extends UKP_Object {

    protected final HashMap<UKP_UnitID, String> PID2P;
    protected final HashMap<String, UKP_UnitID> P2PID;

    /**
     * Set from the UKP_Environment, provided for convenience.
     */
    private transient final Generic_Environment env;

    /**
     * Set from the UKP_Environment, provided for convenience.
     */
    private transient final Generic_IO io;

    public NSPCL_2019_Nov(UKP_Environment e) throws FileNotFoundException, IOException, Exception {
        super(e);
        env = e.env;
        io = env.io;
        String m = this.getClass().getSimpleName() + "(UKP_Environment)";
        env.logStartTag(m);
        // If cached data exists then load it otherwise, load from source.
        UKP_YM3 ym3 = new UKP_YM3(2019, 11);
        Path cache = e.files.getNSPCLCacheFile();
        if (Files.exists(cache)) {
            NSPCL_2019_Nov n = (NSPCL_2019_Nov) Generic_IO.readObject(cache);
            PID2P = n.PID2P;
            P2PID = n.P2PID;
        } else {
            Path f = e.files.getNSPCLInputFile(ym3);
            PID2P = new HashMap<>();
            P2PID = new HashMap<>();
            UKPC_Checker checker = new UKPC_Checker();
            Data_ReadCSV reader = new Data_ReadCSV(e.de);
            try (BufferedReader br = Generic_IO.getBufferedReader(f)) {
                StreamTokenizer st = new StreamTokenizer(br);
                Generic_IO.setStreamTokenizerSyntax1(st);
                String line = reader.readLine();  // skip header...
                env.log(line);                      // ... but log it.
                line = reader.readLine();
                while (line != null) {
                    ArrayList<String> fields = reader.parseLine(line);
                    String s = fields.get(0);
                    if (checker.isValidPostcodeUnit(s)) {
                        //pcd7,pcd8,pcds,dointr,doterm,usertype,oseast1m,osnrth1m,oa11cd,oac11cd,oac11nm,wz11cd,wzc11cd,wzc11nm,lsoa11cd,lsoa11nm,msoa11cd,msoa11nm,soac11cd,soac11nm,ladcd,ladnm,ladnmw,laccd,lacnm
                        //"AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","0","385386","0801193","S00090303","1C3","Detached rural retirement","S34002990","D1","Non-metropolitan suburban areas","S01006514","Cults, Bieldside and Milltimber West - 02","S02001237","Cults, Bieldside and Milltimber Wes","8A","Affluent communities","S12000033","Aberdeen City","","2A1","Larger Towns and Cities"
                    } else {
                        throw new Exception("Unexpected postcode " + s + " in "
                                + f);
                    }
                    line = reader.readLine();
                }
            }
            cache(cache);
        }
        env.logEndTag(m);
    }

    /**
     * Caches this to cache.
     *
     * @param cache The file to cache this into.
     * @throws java.io.IOException If encountered.
     */
    public final void cache(Path cache) throws IOException {
        Generic_IO.writeObject(this, cache);
    }

}
