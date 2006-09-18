/*
 * Copyright 2004,2005 The Apache Software Foundation.
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

package org.apache.rampart;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.modules.Module;
import org.apache.axis2.modules.ModulePolicyExtension;
import org.apache.axis2.modules.PolicyExtension;
import org.apache.ws.security.policy1.extension.WSSCodegenPolicyExtension;

public class Rampart implements Module, ModulePolicyExtension  {
    private AxisModule module;

    public void init(ConfigurationContext configContext, AxisModule module)
            throws AxisFault {
        this.module = module;
    }

    public void engageNotify(AxisDescription axisDescription) throws AxisFault {
        //Nothing to do here, since RampartMessageData will pick up the 
        //effective policy from the message context 
    }

    public void shutdown(ConfigurationContext configurationContext) throws AxisFault {
        // Do nothing
    }

    public PolicyExtension getPolicyExtension() {
        return new WSSCodegenPolicyExtension();
    }
}
