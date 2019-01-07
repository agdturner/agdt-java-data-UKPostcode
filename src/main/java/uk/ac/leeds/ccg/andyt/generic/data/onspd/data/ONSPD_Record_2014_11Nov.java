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
public class ONSPD_Record_2014_11Nov extends ONSPD_Record_2013_08Aug {

    protected final String oac11;
    
    public ONSPD_Record_2014_11Nov(ONSPD_Environment env, String line) {
        super(env, line);
        String[] fields = line.split("\",\"");
        oac11 = fields[fields.length - 1];
    }

    public String getOac11() {
        return oac11;
    }
}
