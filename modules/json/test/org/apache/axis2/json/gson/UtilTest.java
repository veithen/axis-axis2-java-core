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

package org.apache.axis2.json.gson;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;

public class UtilTest {

    public static String post(String jsonString, String strURL)
            throws IOException {
        HttpEntity stringEntity = new StringEntity(jsonString,ContentType.APPLICATION_JSON);
	HttpPost httpPost = new HttpPost(strURL);
        httpPost.setEntity(stringEntity);
	CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            CloseableHttpResponse response = httpclient.execute(httpPost);
	    int status = response.getCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity,"UTF-8") : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
	} catch (final Exception ex) {
            throw new ClientProtocolException("Unexpected Exception: " + ex.getMessage() + " , on URL: " + strURL);
        }finally {
            httpclient.close();
        }
    }
}
