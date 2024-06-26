/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.axis2.transport.jms;

import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;

import org.apache.axis2.transport.testkit.name.Name;

@Name("bytes")
public class JMSBytesMessageFactory implements JMSMessageFactory<byte[]> {
    public static final JMSBytesMessageFactory INSTANCE = new JMSBytesMessageFactory();
    
    private JMSBytesMessageFactory() {}

    public Message createMessage(Session session, byte[] data) throws JMSException {
        BytesMessage message = session.createBytesMessage();
        message.writeBytes(data);
        return message;
    }

    public byte[] parseMessage(Message message) throws JMSException {
        BytesMessage bytesMessage = (BytesMessage)message;
        byte[] data = new byte[(int)bytesMessage.getBodyLength()];
        bytesMessage.readBytes(data);
        return data;
    }
}
