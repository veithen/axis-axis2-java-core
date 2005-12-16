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


package org.apache.axis2.description;

/**
 * Interface FlowInclude
 */
public interface FlowInclude {

    /**
     * Method getFaultInFlow
     *
     * @return
     */
    public Flow getFaultInFlow();

    public Flow getFaultOutFlow();

    /**
     * Method getInFlow
     *
     * @return
     */
    public Flow getInFlow();

    /**
     * Method getOutFlow
     *
     * @return
     */
    public Flow getOutFlow();

    /**
     * Method setFaultInFlow
     *
     * @param faultFlow
     */
    public void setFaultInFlow(Flow faultFlow);

    /**
     * Method setFaultInFlow
     *
     * @param faultFlow
     */
    public void setFaultOutFlow(Flow faultFlow);

    /**
     * Method setInFlow
     *
     * @param inFlow
     */
    public void setInFlow(Flow inFlow);

    /**
     * Method setOutFlow
     *
     * @param outFlow
     */
    public void setOutFlow(Flow outFlow);
}
