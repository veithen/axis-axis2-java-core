/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package org.apache.axis2.wsdl.codegen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.description.AxisService;
import org.apache.ws.commons.schema.XmlSchema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CodeGenConfigurationTest extends XMLSchemaTest{
    
    protected AxisService service;
    private ArrayList<XmlSchema> schemas;
    
    @BeforeEach
    void setUp() throws Exception {
        service = new AxisService();
        schemas = new ArrayList<XmlSchema>();
        loadSampleSchemaFile(schemas);
        service.addSchema(schemas);
    }
    
    @AfterEach
    protected void tearDown() throws Exception {
        service=null;
        schemas=null;
    }
    
    @Test
    public void testGetSchemaListForAllServices(){
        CodeGenConfiguration configuration = new CodeGenConfiguration();
        configuration.addAxisService(service);
        List<XmlSchema> list=configuration.getSchemaListForAllServices();
        assertEquals(schemas.get(0), list.get(0));
    }
    
    
}
