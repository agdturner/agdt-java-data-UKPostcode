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
package uk.ac.leeds.ccg.data.ukp.data.id;

import java.util.Objects;
import uk.ac.leeds.ccg.data.id.Data_RecordID;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Point;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class ONSPD_PostcodeID extends Data_RecordID {

    protected final ONSPD_Point Point;

    public ONSPD_PostcodeID(long l, ONSPD_Point p) {
        super(l);
        this.Point = p;
    }

    /**
     * @return the point
     */
    public ONSPD_Point getPoint() {
        return Point;
    }

    /**
     * @param obj The Object to test for equality with this.
     * @return {@code true} if this and {@code obj} are equal.
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
            if (ID == o.ID) {
                if (this.Point.equals(o.Point)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.Point);
        return hash;
    }
}
