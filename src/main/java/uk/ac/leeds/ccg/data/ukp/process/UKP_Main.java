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
package uk.ac.leeds.ccg.data.ukp.process;

import java.nio.file.Path;
import java.nio.file.Paths;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Environment;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Object;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Strings;
import uk.ac.leeds.ccg.data.ukp.data.nspcl.NSPCL_2019_Nov;
import uk.ac.leeds.ccg.data.ukp.io.UKP_Files;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
//import uk.ac.leeds.ccg.generic.data.onspd.data.ONSPD_Collection;
//import uk.ac.leeds.ccg.generic.data.onspd.data.ONSPD_Combined_Record;
//import uk.ac.leeds.ccg.generic.data.onspd.data.ONSPD_Data;
/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class UKP_Main extends UKP_Object {

    // For convenience
    protected final UKP_Files Files;

    public UKP_Main(UKP_Environment env) {
        super(env);
        Files = env.files;
    }

    public static void main(String[] args) {
        try {
            Data_Environment de = new Data_Environment(new Generic_Environment(
                    new Generic_Defaults()));
            Path dataDir = Paths.get(de.files.getDir().toString(), UKP_Strings.s_data);
            dataDir = Paths.get(dataDir.toString(), UKP_Strings.s_ONSPD);
            UKP_Environment oe = new UKP_Environment(de, dataDir);
            UKP_Main p = new UKP_Main(oe);
            // Main switches
            p.run();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void run() throws Exception {
        NSPCL_2019_Nov d = new NSPCL_2019_Nov(oe);
    }

}
