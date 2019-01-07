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
package uk.ac.leeds.ccg.andyt.generic.data.onspd.data;

import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_ID;

/**
 * @author Andy Turner.
 */
public class ONSPD_PostcodeID {

    protected final ONSPD_ID ID;
    protected final ONSPD_Point Point;

    public ONSPD_PostcodeID(
            ONSPD_ID ID,
            ONSPD_Point p) {
        this.ID = ID;
        this.Point = p;
    }

    /**
     * @return the point
     */
    public ONSPD_Point getPoint() {
        return Point;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof ONSPD_PostcodeID) {
            ONSPD_PostcodeID o;
            o = (ONSPD_PostcodeID) obj;
            if (ID.equals(o.ID)) {
                if (this.Point == o.Point) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.ID != null ? this.ID.hashCode() : 0);
        hash = 23 * hash + (this.Point != null ? this.Point.hashCode() : 0);
        return hash;
    }

    
}
