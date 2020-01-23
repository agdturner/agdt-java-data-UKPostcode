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
public class ONSPD_Record_2011_05May extends ONSPD_Record_2008_02Feb {

    public ONSPD_Record_2011_05May(UKP_Environment e, Data_RecordID i){
        super(e, i);
    }
    /*
     * pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,pafind,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
     * "AB1 0AB","AB1  0AB","AB1 0AB","198001","199606","S99999999","S12000033","S13002484","0","385177","0801314","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001270","7","01C31","S99999999","S99999999","S01000011","S99999999","9","6","Z","S02000007","99ZZ99Z9","4B3","X98" 
     */
    public ONSPD_Record_2011_05May(UKP_Environment e, Data_RecordID i, String line) {
        super(e, i);
        //System.out.println(line);
        String[] fields = line.split("\",\"");
        int n;
        n = initPart1(fields);
        n = initPart2(n, fields);
        /*
         * 3
         * pafind,gor,streg,
         * " ","S99999999","0",
         */
        //pafind = fields[n];
        //n++;
        gor = fields[n];
        n++;
        streg = fields[n];
        n++;
        n = initPart3(n, fields);
        n = initPart4(n, fields);
        n = initPart5(n, fields);
        n = initPart6(n, fields);
        initPart7(n, fields);
    }

}
