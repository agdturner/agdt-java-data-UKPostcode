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
import java.util.logging.Level;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
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

    public ONSPD_Main_Process(ONSPD_Environment env) {
        super(env);
//        data = env.data;
        Strings = env.Strings;
        Files = env.Files;
    }

    public static void main(String[] args) {
        File dataDir = new File(System.getProperty("user.dir"), "data");
        Generic_Environment ge;
        ge = new Generic_Environment(dataDir, Level.FINE, 100);
        ONSPD_Environment env;
        env = new ONSPD_Environment(ge);
        ONSPD_Main_Process p;
        p = new ONSPD_Main_Process(env);
        // Main switches
        p.run();
    }

    public void run() {
    }

}
