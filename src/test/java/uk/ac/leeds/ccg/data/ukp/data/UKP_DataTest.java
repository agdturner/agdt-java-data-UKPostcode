/*
 * Copyright 2020 Andy Turner, University of Leeds.
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
package uk.ac.leeds.ccg.data.ukp.data;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.data.id.Data_RecordID;
import uk.ac.leeds.ccg.data.ukp.core.UKP_Environment;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Point;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;

/**
 *
 * @author geoagdt
 */
public class UKP_DataTest {
    
    public UKP_DataTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getPostcodeDistrict method, of class UKP_Data.
     */
    @Test
    public void testGetPostcodeDistrict() {
        System.out.println("getPostcodeDistrict");
        String unitPostcode = "LS2 9JT";
        String expResult = "LS2";
        String result = UKP_Data.getPostcodeDistrict(unitPostcode);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPostcodeLevel method, of class UKP_Data.
     * @throws java.lang.Exception If encountered.
     */
    @Test
    public void testGetPostcodeLevel() throws Exception {
        System.out.println("getPostcodeLevel");
        String s;
        UKP_Data instance = new UKP_Data(new UKP_Environment(
                new Data_Environment(new Generic_Environment(
                        new Generic_Defaults()))));
        int expResult;
        int result;
        // Test 1
        s = "LS";
        expResult = UKP_Data.TYPE_AREA;
        result = instance.getPostcodeLevel(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPostcodeSector method, of class UKP_Data.
     */
    @Test
    public void testGetPostcodeSector() {
        System.out.println("getPostcodeSector");
        String p = "LS2 9JT";
        String expResult = "LS2 9";
        String result = UKP_Data.getPostcodeSector(p);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPostcodeArea method, of class UKP_Data.
     */
    @Test
    public void testGetPostcodeArea() {
        System.out.println("getPostcodeArea");
        String p = "LS2 9JT";
        String expResult = "LS";
        String result = UKP_Data.getPostcodeArea(p);
        assertEquals(expResult, result);
    }

}
