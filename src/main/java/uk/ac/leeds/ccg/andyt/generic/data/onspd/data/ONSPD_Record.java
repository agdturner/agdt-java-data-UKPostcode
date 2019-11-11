/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.generic.data.onspd.data;

import uk.ac.leeds.ccg.andyt.data.Data_Record;
import uk.ac.leeds.ccg.andyt.data.id.Data_RecordID;
import uk.ac.leeds.ccg.andyt.data.id.Data_ID;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Environment;

/**
 *
 * @author geoagdt
 */
public class ONSPD_Record extends Data_Record {

    public final ONSPD_Environment oe;
    
    private final String PostcodeF;
    private final int oseast1m;
    private final int osnrth1m;

    public ONSPD_Record(ONSPD_Environment e, Data_RecordID i){
        this(e, i, "");
    }
    
    public ONSPD_Record(ONSPD_Environment e, Data_RecordID i, String line) {
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
        return ID;
    }
}
