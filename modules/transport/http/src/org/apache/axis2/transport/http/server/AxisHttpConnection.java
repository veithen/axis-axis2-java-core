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

package org.apache.axis2.transport.http.server;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.impl.io.SocketHolder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface AxisHttpConnection extends HttpConnection {

    InputStream getInputStream();
    
    void sendResponse(ClassicHttpResponse response) 
        throws HttpException, IOException;    
    
    OutputStream getOutputStream();
    
    void flush()
        throws IOException;
    
    void reset()
        throws IOException;

    ClassicHttpRequest receiveRequest() throws HttpException, IOException;

    SocketHolder getSocketHolder();
}
