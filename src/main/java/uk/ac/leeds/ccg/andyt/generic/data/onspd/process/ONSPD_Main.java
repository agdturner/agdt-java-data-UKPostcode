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
import java.io.IOException;
import uk.ac.leeds.ccg.andyt.data.core.Data_Environment;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Object;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Strings;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.io.ONSPD_Files;
//import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Collection;
//import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Combined_Record;
//import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Data;
/**
 *
 * @author geoagdt
 */
public class ONSPD_Main extends ONSPD_Object {

    // For convenience
    protected final ONSPD_Files Files;

    public ONSPD_Main(ONSPD_Environment env) {
        super(env);
        Files = env.files;
    }

    public static void main(String[] args) {
        try {
            Data_Environment de = new Data_Environment(
                    new Generic_Environment());
            File dataDir = new File(de.files.getDir(), ONSPD_Strings.s_data);
            dataDir = new File(dataDir, ONSPD_Strings.s_ONSPD);
            ONSPD_Environment oe = new ONSPD_Environment(de, dataDir);
            ONSPD_Main p = new ONSPD_Main(oe);
            // Main switches
            p.run();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void run() {
    }

}
