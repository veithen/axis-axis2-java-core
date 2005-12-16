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


package org.apache.axis2.phaseresolver;

/**
 * Class PhaseMetadata
 */
public class PhaseMetadata {
    public static final int IN_FLOW = 1;
    public static final int OUT_FLOW = 2;
    public static final int FAULT_OUT_FLOW = 4;
    public static final int FAULT_IN_FLOW = 3;

    // INFLOW
    public static final String PHASE_TRANSPORTIN = "TransportIn";
    public static final String PHASE_PRE_DISPATCH = "PreDispatch";
    public static final String PHASE_POST_DISPATCH = "PostDispatch";
    public static final String PHASE_POLICY_DETERMINATION = "PolicyDetermination";
    public static final String PHASE_MESSAGE_PROCESSING = "MessageProcessing";

    // OUTFLOW
    public static final String PHASE_MESSAGE_OUT = "MessageOut";
    public static final String PHASE_DISPATCH = "Dispatch";
    public static final String PHASE_TRANSPORT_OUT = "MessageOut";

    /**
     * todo  I think thi shas to be change
     * All the handlers inside transportsender and TranportRecievre in axis2.xml gose
     * to this phase
     */
    public static final String TRANSPORT_PHASE = "TRANSPORT";
}
