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
package uk.ac.leeds.ccg.data.ukp.data.onspd;

import uk.ac.leeds.ccg.data.id.Data_RecordID;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Environment;

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class ONSPD_Record_2016_02Feb extends ONSPD_Record_2015_08Aug {

    protected final String pfa;
    protected final String imd;

    public ONSPD_Record_2016_02Feb(UKP_Environment e, Data_RecordID i, String line) {
        super(e, i, line);
        String[] fields = line.split("\",\"");
        pfa = fields[fields.length - 2];
        imd = fields[fields.length - 1];
    }

    public String getPfa() {
        return pfa;
    }

    public String getImd() {
        return imd;
    }
}
