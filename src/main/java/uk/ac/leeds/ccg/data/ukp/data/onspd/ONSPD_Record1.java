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
package uk.ac.leeds.ccg.data.ukp.data.onspd;

import uk.ac.leeds.ccg.data.id.Data_RecordID;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Environment;

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public abstract class ONSPD_Record1 extends ONSPD_Record0 {

    protected String oa01;
    protected String lsoa01;
    protected String msoa01;
    protected String ur01ind;
    protected String oac01;
    protected String oldpct;
    protected String oa11;
    protected String lsoa11;
    protected String msoa11;
    
    // 2012_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11
    // 2013_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11
    // 2013_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg
    // 2013_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11
    // 2015_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long
    // 2015_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2
    // 2016_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    
    // 2012_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","","",""
    // 2013_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","","","","S99999999","S99999999"
    // 2013_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg
    // 2013_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","","","","S99999999","S99999999","S03000012"
    // 2013_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2013_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","","","","S99999999","S99999999","S03000012","S99999999","S99999999",""
    // 2013_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2013_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","","","S99999999","S99999999","S03000012","S99999999","S99999999","3"
    // 2014_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","","","S99999999","S99999999","S03000012","S99999999","S99999999","3"
    // 2014_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","","","S99999999","S99999999","S03000012","S99999999","S99999999","3"
    // 2014_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","","","S99999999","S99999999","S03000012","S99999999","S99999999","3"
    // 2014_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11
    // 2014_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3"
    // 2015_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11
    // 2015_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3"
    // 2015_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long
    // 2015_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851
    // 2015_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2
    // 2015_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999"
    // 2015_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2
    // 2015_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999"
    // 2016_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    // 2016_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999","S23000009",6002
    // 2016_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    // 2016_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999","S23000009",6002
    // 2016_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    // 2016_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999","S23000009",6002
    // 2016_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    // 2016_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S34002990","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999","S23000009",6808
    public ONSPD_Record1(UKP_Environment e, Data_RecordID i) {
        super(e, i);
        oa01 = "";
        casward = "";
        park = "";
        lsoa01 = "";
        msoa01 = "";
        ur01ind = "";
        oac01 = "";
        oldpct = "";
        oa11 = "";
        lsoa11 = "";
        msoa11 = "";
    }

    public String getOa01() {
        return oa01;
    }

    public String getLsoa01() {
        return lsoa01;
    }

    public String getMsoa01() {
        return msoa01;
    }

    public String getUr01ind() {
        return ur01ind;
    }

    public String getOac01() {
        return oac01;
    }

    public String getOldpct() {
        return oldpct;
    }

    public String getOa11() {
        return oa11;
    }

    public String getLsoa11() {
        return lsoa11;
    }

    public String getMsoa11() {
        return msoa11;
    }

}
