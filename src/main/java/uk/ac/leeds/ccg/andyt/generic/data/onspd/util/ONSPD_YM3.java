/*
 * Copyright (C) 2017 geoagdt.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.ac.leeds.ccg.andyt.generic.data.onspd.util;

import java.io.Serializable;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;

/**
 *
 * @author geoagdt
 */
public class ONSPD_YM3 implements Comparable, Serializable {

    private final int Year;
    private final int Month;
    private static transient final String UNDERSCORE = "_";

    /**
     * Creates a new instance.
     *
     * @param YM3 Expected to be of the form YYYY_MMM (e.g. "2018_Dec") where:
     * YYYY is a year (e.g. "2018"); and, MMM is the first three letters of the
     * month name (e.g. Jan). The case of MMM is ignored, so DEC is treated the
     * same as dec etc.).
     */
    public ONSPD_YM3(String YM3) {
        String[] split;
        split = YM3.split(UNDERSCORE);
        Year = Integer.valueOf(split[0]);
        Month = Generic_Time.getMonthInt(split[1]);
    }

    /**
     * Creates a new instance.
     *
     * @param y The year e.g. "2018".
     * @param m The first 3 letters of the month name e.g. "Jan". The case of
     * MMM is ignored, so DEC is treated the same as dec etc.).
     */
    public ONSPD_YM3(String y, String m) {
        Year = Integer.valueOf(y);
        Month = Generic_Time.getMonthInt(m);
    }

    /**
     * Creates a new instance.
     *
     * @param Year The year e.g. 2018.
     * @param Month January is 1, February is 2,... December is 12.
     * {@link Generic_Time.getMonthInt(String)}
     */
    public ONSPD_YM3(int Year, int Month) {
        this.Year = Year;
        this.Month = Month;
    }

    /**
     * Creates a new instance that is effectively a clone of i.
     * @param i The instance to duplicate.
     */
    public ONSPD_YM3(ONSPD_YM3 i) {
        this(i.getYear(), i.getMonth());
    }

    @Override
    public String toString() {
        return Integer.toString(getYear()) + UNDERSCORE
                + Generic_Time.getMonth3Letters(getMonth());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ONSPD_YM3) {
            ONSPD_YM3 o2;
            o2 = (ONSPD_YM3) o;
            if (this.hashCode() == o2.hashCode()) {
                if (this.getMonth() == o2.getMonth()) {
                    if (this.getYear() == o2.getYear()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.getYear();
        hash = 67 * hash + this.getMonth();
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof ONSPD_YM3) {
            ONSPD_YM3 o2;
            o2 = (ONSPD_YM3) o;
            if (this.getYear() > o2.getYear()) {
                return 1;
            } else if (this.getYear() < o2.getYear()) {
                return -1;
            } else {
                if (this.getMonth() > o2.getMonth()) {
                    return 1;
                } else if (this.getMonth() < o2.getMonth()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        return -1;
    }

    /**
     * @return the Year
     */
    public int getYear() {
        return Year;
    }

    /**
     * @return the Month
     */
    public int getMonth() {
        return Month;
    }

}
