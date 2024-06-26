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

package org.apache.axis2.rest;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.AxisBinding;
import org.apache.axis2.description.AxisBindingOperation;
import org.apache.axis2.description.AxisEndpoint;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.integration.UtilServer;
import org.apache.axis2.integration.UtilServerBasedTestCase;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.util.TimeValue;

import javax.xml.namespace.QName;
import java.io.InterruptedIOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class RESTfulServiceTest extends UtilServerBasedTestCase {

    ConfigurationContext configContext;
    ConfigurationContext clinetConfigurationctx;

    public static Test suite() {
        return getTestSetup(new TestSuite(RESTfulServiceTest.class));
    }

    protected void setUp() throws Exception {
        configContext = UtilServer.getConfigurationContext();
        clinetConfigurationctx = ConfigurationContextFactory.
                createConfigurationContextFromFileSystem(null, null);
    }

    public void test() throws Exception {
        AxisConfiguration axisConfig = configContext.getAxisConfiguration();
        AxisService axisService =
                AxisService.createService("org.apache.axis2.rest.StockService", axisConfig);
        assertNotNull(axisService);

        assertNotNull(axisService.getOperation(new QName("addStock")));
        assertNotNull(axisService.getOperation(new QName("getStockValue")));

        axisService.setTargetNamespace("http://rest.axis2.apache.org");

        Map httpLocationTable = new TreeMap(new Comparator() {
            public int compare(Object o1, Object o2) {
                return (-1 * ((Comparable) o1).compareTo(o2));
            }
        });
        // StockServiceHttpBinding for StockService
        AxisBinding binding = new AxisBinding();
        binding.setName(new QName("StockServiceHttpBinding"));
        binding.setType("http://www.w3.org/ns/wsdl/http");
        binding.setProperty(WSDL2Constants.ATTR_WHTTP_METHOD_DEFAULT,
                            Constants.Configuration.HTTP_METHOD_GET);

        // AxisBindingOperation for addStock
        AxisBindingOperation bindingOperation1 = new AxisBindingOperation();
        bindingOperation1.setAxisOperation(axisService.getOperation(new QName("addStock")));
        bindingOperation1.setName(axisService.getOperation(new QName("addStock")).getName());
        bindingOperation1.setParent(binding);
        bindingOperation1.setProperty(WSDL2Constants.ATTR_WHTTP_METHOD,
                                      Constants.Configuration.HTTP_METHOD_GET);
        bindingOperation1.setProperty(WSDL2Constants.ATTR_WHTTP_LOCATION,
                                      "add/{name}/value/{value}");
        httpLocationTable.put(Constants.Configuration.HTTP_METHOD_GET + "/add/",
                              axisService.getOperation(new QName("addStock")));
        bindingOperation1.setProperty(WSDL2Constants.ATTR_WHTTP_INPUT_SERIALIZATION,
                                      Constants.MIME_CT_APPLICATION_URL_ENCODED);
        bindingOperation1.setProperty(WSDL2Constants.ATTR_WHTTP_OUTPUT_SERIALIZATION,
                                      Constants.MIME_CT_APPLICATION_XML);
        binding.addChild(bindingOperation1);

        assertNotNull(binding.getChild(bindingOperation1.getName()));

        // AxisBindingOperation for getStockValue
        AxisBindingOperation bindingOperation2 = new AxisBindingOperation();
        bindingOperation2.setAxisOperation(axisService.getOperation(new QName("getStockValue")));
        bindingOperation2.setName(axisService.getOperation(new QName("getStockValue")).getName());
        bindingOperation2.setParent(binding);
        bindingOperation2.setProperty(WSDL2Constants.ATTR_WHTTP_METHOD,
                                      Constants.Configuration.HTTP_METHOD_GET);
        bindingOperation2.setProperty(WSDL2Constants.ATTR_WHTTP_LOCATION, "get/{name}");
        httpLocationTable.put(Constants.Configuration.HTTP_METHOD_GET + "/get/",
                              axisService.getOperation(new QName("getStockValue")));
        bindingOperation2.setProperty(WSDL2Constants.ATTR_WHTTP_INPUT_SERIALIZATION,
                                      Constants.MIME_CT_APPLICATION_URL_ENCODED);
        bindingOperation2.setProperty(WSDL2Constants.ATTR_WHTTP_OUTPUT_SERIALIZATION,
                                      Constants.MIME_CT_APPLICATION_XML);
        binding.addChild(bindingOperation2);

        assertNotNull(binding.getChild(bindingOperation2.getName()));
        binding.setProperty(WSDL2Constants.HTTP_LOCATION_TABLE, httpLocationTable);

        // adding Http AxisEndpoint, HttpBinding to service
        AxisEndpoint axisEndpoint = new AxisEndpoint();
        axisEndpoint.setBinding(binding);
        axisEndpoint.setName("StockServiceHttpEndpoint");
        axisService.addEndpoint("StockServiceHttpEndpoint", axisEndpoint);
        axisService.setEndpointName("StockServiceHttpEndpoint");
        axisService.setBindingName("StockServiceHttpBinding");
        axisService.setEndpointURL("http://127.0.0.1:" + (UtilServer.TESTING_PORT) +
                                   "/axis2/services/StockService.StockServiceHttpEndpoint/");
        assertNotNull(axisService.getEndpoint("StockServiceHttpEndpoint"));
        axisConfig.addService(axisService);
        assertEquals("StockService", axisService.getName());

        String url1 = "http://127.0.0.1:" + (UtilServer.TESTING_PORT)
                      + "/axis2/services/StockService/add/IBM/value/34.7";

	HttpGet httpGet = new HttpGet(url1);

	CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            CloseableHttpResponse hcResponse = httpclient.execute(httpGet);
	    int status = hcResponse.getCode();
            if (status != HttpStatus.SC_OK) {
                throw new ClientProtocolException("url request failed: " + new StatusLine(hcResponse));
            }
	    HttpEntity responseEntity = hcResponse.getEntity();
            if(responseEntity==null) {
                throw new ClientProtocolException("url request returned null entity: " + new StatusLine(hcResponse));
            }
            String responseStr = EntityUtils.toString(responseEntity);
            OMElement axisResponse = AXIOMUtil.stringToOM(responseStr);
            OMElement returnElem = axisResponse.getFirstChildWithName(new QName("return"));
            assertEquals("IBM stock added with value : 34.7", returnElem.getText());

        }finally {
            httpclient.close();
        }

        String url2 = "http://127.0.0.1:" + (UtilServer.TESTING_PORT) + "/axis2/services/StockService/get/IBM";

	httpGet = null;
	httpclient = null;

	httpGet = new HttpGet(url2);

	httpclient = HttpClients.createDefault();

        try {
            CloseableHttpResponse hcResponse = httpclient.execute(httpGet);
	    int status = hcResponse.getCode();
            if (status != HttpStatus.SC_OK) {
                throw new ClientProtocolException("url request failed: " + new StatusLine(hcResponse));
            }
	    HttpEntity responseEntity = hcResponse.getEntity();
            if(responseEntity==null) {
                throw new ClientProtocolException("url request returned null entity: " + new StatusLine(hcResponse));
            }

            String responseStr = EntityUtils.toString(responseEntity);
            OMElement axisResponse = AXIOMUtil.stringToOM(responseStr);
            OMElement returnElem = axisResponse.getFirstChildWithName(new QName("return"));
            assertEquals("34.7", returnElem.getText());

        }finally {
            httpclient.close();
        }

    }
}
