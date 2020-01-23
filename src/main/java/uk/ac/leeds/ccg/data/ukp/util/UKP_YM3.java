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
package uk.ac.leeds.ccg.data.ukp.util;

import java.io.Serializable;
import uk.ac.leeds.ccg.generic.util.Generic_Time;

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class UKP_YM3 implements Comparable, Serializable {

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
    public UKP_YM3(String YM3) {
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
    public UKP_YM3(String y, String m) {
        Year = Integer.valueOf(y);
        Month = Generic_Time.getMonthInt(m);
    }

    /**
     * Creates a new instance.
     *
     * @param Year The year e.g. 2018.
     * @param Month January is 1, February is 2,... December is 12.
     */
    public UKP_YM3(int Year, int Month) {
        this.Year = Year;
        this.Month = Month;
    }

    /**
     * Creates a new instance that is effectively a clone of i.
     * @param i The instance to duplicate.
     */
    public UKP_YM3(UKP_YM3 i) {
        this(i.getYear(), i.getMonth());
    }

    @Override
    public String toString() {
        return Integer.toString(getYear()) + UNDERSCORE
                + Generic_Time.getMonth3Letters(getMonth());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UKP_YM3) {
            UKP_YM3 o2;
            o2 = (UKP_YM3) o;
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
        if (o instanceof UKP_YM3) {
            UKP_YM3 o2;
            o2 = (UKP_YM3) o;
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
