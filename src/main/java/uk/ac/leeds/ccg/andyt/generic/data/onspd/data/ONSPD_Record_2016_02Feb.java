/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.generic.data.onspd.data;

import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_Environment;

/**
 *
 * @author geoagdt
 */
public class ONSPD_Record_2016_02Feb extends ONSPD_Record_2015_08Aug {

    protected final String pfa;
    protected final String imd;

    public ONSPD_Record_2016_02Feb(ONSPD_Environment env, String line) {
        super(env, line);
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
