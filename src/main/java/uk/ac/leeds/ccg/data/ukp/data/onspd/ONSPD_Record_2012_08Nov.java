/*
 * Copyright 2019 Centre for Computational Geography, University of Leeds.
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
public class ONSPD_Record_2012_08Nov extends ONSPD_Record_2011_05May {

    protected String oa01;
       
    /*
     * "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","7","01C30","S99999999","S99999999","S01000011","S99999999","9","6","Z","S02000007","99ZZ99Z9","3C2","X98"
     */
    /**
     * @param e UKP_Environment
     * @param i Data_RecordID 
     * @param line The record as a String.
     */
    public ONSPD_Record_2012_08Nov(UKP_Environment e, Data_RecordID i, String line) {
        super(e, i);
        String[] fields = line.split("\",\"");
        int n;
        n = initPart1(fields);
        n = initPart2(n, fields);
        genind = fields[n];
        n ++;
        pafind = fields[n];
        n ++;
        gor = fields[n];
        n ++;
        streg = fields[n];
        n ++;
        n = initPart3(n, fields);
        addrct = fields[n];
        n ++;
        dpct = fields[n];
        n ++;
        moct = fields[n];
        n ++;
        smlbusct = fields[n];
        n ++;
        n = initPart4(n, fields);
        oa01 = fields[n];
        n ++;
        initPart6(n, fields);
    }

}
