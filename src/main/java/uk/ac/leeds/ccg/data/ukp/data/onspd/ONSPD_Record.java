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

import uk.ac.leeds.ccg.data.Data_Record;
import uk.ac.leeds.ccg.data.id.Data_RecordID;
import uk.ac.leeds.ccg.data.id.Data_ID;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Environment;

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class ONSPD_Record extends Data_Record {

    public final UKP_Environment oe;
    
    protected String PostcodeF;
    private final int oseast1m;
    private final int osnrth1m;

    
    public ONSPD_Record(UKP_Environment e, Data_RecordID i){
        this(e, i, "");
    }
    
    /**
     * In 2016 there are:
     * <ul>
     * <li>5 Westminster Parliamentary Constituencies in Leeds:
     * <ul>
     * <li>E14000777 Leeds Central</li>
     * <li>E14000778 Leeds East</li>
     * <li>E14000779 Leeds North East</li>
     * <li>E14000780 Leeds North West</li>
     * <li>E14000781 Leeds West</li>
     * </ul></li>
     * <li>33 Statistical Wards in Leeds:
     * <ul>
     * <li>E05001411 00DAGL Adel and Wharfedale</li>
     * <li>E05001412 00DAGM Alwoodley</li>
     * <li>E05001413 00DAGN Ardsley and Robin Hood</li>
     * <li>E05001414 00DAGP Armley</li>
     * <li>E05001415 00DAGQ Beeston and Holbeck</li>
     * <li>E05001416 00DAGR Bramley and Stanningley</li>
     * <li>E05001417 00DAGS Burmantofts and Richmond Hill</li>
     * <li>E05001418 00DAGT Calverley and Farsley</li>
     * <li>E05001419 00DAGU Chapel Allerton</li>
     * <li>E05001420 00DAGW City and Hunslet</li>
     * <li>E05001421 00DAGX Cross Gates and Whinmoor</li>
     * <li>E05001422 00DAGY Farnley and Wortley</li>
     * <li>E05001423 00DAGZ Garforth and Swillington</li>
     * <li>E05001424 00DAHA Gipton and Harehills</li>
     * <li>E05001425 00DAHB Guiseley and Rawdon</li>
     * <li>E05001426 00DAHC Harewood</li>
     * <li>E05001427 00DAHD Headingley</li>
     * <li>E05001428 00DAHE Horsforth</li>
     * <li>E05001429 00DAHF Hyde Park and Woodhouse</li>
     * <li>E05001430 00DAHG Killingbeck and Seacroft</li>
     * <li>E05001431 00DAHH Kippax and Methley</li>
     * <li>E05001432 00DAHJ Kirkstall</li>
     * <li>E05001433 00DAHK Middleton Park</li>
     * <li>E05001434 00DAHL Moortown</li>
     * <li>E05001435 00DAHM Morley North</li>
     * <li>E05001436 00DAHN Morley South</li>
     * <li>E05001437 00DAHP Otley and Yeadon</li>
     * <li>E05001438 00DAHQ Pudsey</li>
     * <li>E05001439 00DAHR Rothwell</li>
     * <li>E05001440 00DAHS Roundhay</li>
     * <li>E05001441 00DAHT Temple Newsam</li>
     * <li>E05001442 00DAHU Weetwood</li>
     * <li>E05001443 00DAHW Wetherby</li>
     * </ul></li>
     * </ul>
     * @param e UKP_Environment
     * @param i Data_RecordID
     * @param line The record as a String.
     */
    public ONSPD_Record(UKP_Environment e, Data_RecordID i, String line) {
        super(i);
        this.oe = e;
        String[] fields = line.split("\",\"");
        //pcd = fields[0].substring(1);
        PostcodeF = e.getHandler().formatPostcode(fields[0]);
        if (fields[9].isEmpty()) {
            oseast1m = -1;
        } else {
            oseast1m = Integer.valueOf(fields[9]);
        }
        if (fields[10].isEmpty()) {
            osnrth1m = -1;
        } else {
            osnrth1m = Integer.valueOf(fields[10]);
        }
    }

    public String getPostcodeF() {
        return PostcodeF;
    }

    public int getOseast1m() {
        return oseast1m;
    }

    public int getOsnrth1m() {
        return osnrth1m;
    }

    @Override
    public Data_ID getID() {
        return id;
    }
}
