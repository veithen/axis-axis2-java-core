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


package org.apache.axis2.engine;

import org.apache.axis2.AxisFault;
import org.apache.axis2.deployment.DeploymentEngine;
import org.apache.axis2.deployment.repository.util.ArchiveReader;
import org.apache.axis2.deployment.util.PhasesInfo;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.description.HandlerDescription;
import org.apache.axis2.description.ModuleConfiguration;
import org.apache.axis2.description.ModuleDescription;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.ParameterInclude;
import org.apache.axis2.description.ParameterIncludeImpl;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.om.OMElement;
import org.apache.axis2.util.HostConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Class AxisConfigurationImpl
 */
public class AxisConfiguration implements ParameterInclude {
    private Log log = LogFactory.getLog(getClass());

    /**
     * Field modules
     */
    private final HashMap modules = new HashMap();
    private final HashMap serviceGroups = new HashMap();
    private final HashMap transportsIn = new HashMap();
    private final HashMap transportsOut = new HashMap();

    // to store AxisObserver Objects
    private ArrayList observersList = null;
    private String axis2Repository = null;
    private HashMap allservices = new HashMap();

    /**
     * Field engagedModules
     */
    protected final List engagedModules;
    private Hashtable faultyModules;

    /**
     * To store faulty services
     */
    private Hashtable faultyServices;

    // to store hots configuration if any
    HostConfiguration hostConfiguration;
    private ArrayList inFaultPhases;
    private ArrayList inPhasesUptoAndIncludingPostDispatch;
    protected HashMap messagReceivers;

    // ///////////////////// From AxisGlobal /////////////////////////////////////

    private ClassLoader moduleClassLoader;
    private HashMap moduleConfigmap;
    private ArrayList outFaultPhases;

    /**
     * Field phases
     */

    // private ArrayList inPhases;
    private ArrayList outPhases;

    // ///////////////////// From AxisGlobal /////////////////////////////////////

    /**
     * Field paramInclude
     */
    protected final ParameterInclude paramInclude;
    protected PhasesInfo phasesinfo;
    private ClassLoader serviceClassLoader;
    private ClassLoader systemClassLoader;

    /**
     * Constructor AxisConfigurationImpl
     */
    public AxisConfiguration() {
        moduleConfigmap = new HashMap();
        paramInclude = new ParameterIncludeImpl();
        engagedModules = new ArrayList();
        messagReceivers = new HashMap();
        outPhases = new ArrayList();
        inFaultPhases = new ArrayList();
        outFaultPhases = new ArrayList();
        faultyServices = new Hashtable();
        faultyModules = new Hashtable();
        observersList = new ArrayList();
        inPhasesUptoAndIncludingPostDispatch = new ArrayList();
        systemClassLoader = Thread.currentThread().getContextClassLoader();
        serviceClassLoader = Thread.currentThread().getContextClassLoader();
        moduleClassLoader = Thread.currentThread().getContextClassLoader();

        // setting the dafualt flow , if some one creat AxisConfig programatically
        // most requird handles will be there in the flow.

        // todo we need to fix this , we know that we are doing wrong thing here
        createDefaultChain();
    }

    // //////////////////////// Form Axis Global
    public void addMessageReceiver(String key, MessageReceiver messageReceiver) {
        messagReceivers.put(key, messageReceiver);
    }

    /**
     * Method addModule.
     *
     * @param module
     * @throws AxisFault
     */
    public synchronized void addModule(ModuleDescription module) throws AxisFault {
        module.setParent(this);
        modules.put(module.getName(), module);
    }

    /**
     * Adds module configuration, if there is moduleConfig tag in service.
     *
     * @param moduleConfiguration
     */
    public void addModuleConfig(ModuleConfiguration moduleConfiguration) {
        moduleConfigmap.put(moduleConfiguration.getModuleName(), moduleConfiguration);
    }

    public void addObservers(AxisObserver axisObserver) {
        observersList.add(axisObserver);
    }

    /**
     * Method addParameter.
     *
     * @param param
     */
    public void addParameter(Parameter param) throws AxisFault {
        if (isParameterLocked(param.getName())) {
            throw new AxisFault("Parmter is locked can not overide: " + param.getName());
        } else {
            paramInclude.addParameter(param);
        }
    }

    /**
     * Method addService.
     *
     * @param service
     * @throws AxisFault
     */
    public synchronized void addService(AxisService service) throws AxisFault {
        AxisServiceGroup axisServiceGroup = new AxisServiceGroup();

        axisServiceGroup.setServiceGroupName(service.getName());
        axisServiceGroup.setParent(this);
        axisServiceGroup.addService(service);
        addServiceGroup(axisServiceGroup);
    }

    public void addServiceGroup(AxisServiceGroup axisServiceGroup) throws AxisFault {
        Iterator services = axisServiceGroup.getServices();

        axisServiceGroup.setParent(this);

        AxisService description;

        while (services.hasNext()) {
            description = (AxisService) services.next();

            if (allservices.get(description.getName()) != null) {
                throw new AxisFault("Two services can not have same name, a service with "
                        + description.getName() + " alredy exist in the system");
            }
        }

        services = axisServiceGroup.getServices();

        while (services.hasNext()) {
            description = (AxisService) services.next();
            allservices.put(description.getName(), description);
            notifyObservers(AxisEvent.SERVICE_DEPLOY, description);
        }

        Iterator enModule = engagedModules.iterator();

        while (enModule.hasNext()) {
            QName moduleDescription = (QName) enModule.next();

            axisServiceGroup.engageModuleToGroup(moduleDescription);
        }

        serviceGroups.put(axisServiceGroup.getServiceGroupName(), axisServiceGroup);
    }

    /**
     * Method addTransportIn.
     *
     * @param transport
     * @throws AxisFault
     */
    public synchronized void addTransportIn(TransportInDescription transport) throws AxisFault {
        transportsIn.put(transport.getName(), transport);
    }

    /**
     * Method addTransportOut.
     *
     * @param transport
     * @throws AxisFault
     */
    public synchronized void addTransportOut(TransportOutDescription transport) throws AxisFault {
        transportsOut.put(transport.getName(), transport);
    }

    private void createDefaultChain() {
        Phase transportIN = new Phase("TransportIn");
        Phase preDispatch = new Phase("PreDispatch");
        DispatchPhase dispatchPhase = new DispatchPhase();

        dispatchPhase.setName("Dispatch");

        AddressingBasedDispatcher abd = new AddressingBasedDispatcher();

        abd.initDispatcher();

        RequestURIBasedDispatcher rud = new RequestURIBasedDispatcher();

        rud.initDispatcher();

        SOAPActionBasedDispatcher sabd = new SOAPActionBasedDispatcher();

        sabd.initDispatcher();

        SOAPMessageBodyBasedDispatcher smbd = new SOAPMessageBodyBasedDispatcher();

        smbd.initDispatcher();

        InstanceDispatcher id = new InstanceDispatcher();

        id.init(new HandlerDescription(new QName("InstanceDispatcher")));
        dispatchPhase.addHandler(abd);
        dispatchPhase.addHandler(rud);
        dispatchPhase.addHandler(sabd);
        dispatchPhase.addHandler(smbd);
        dispatchPhase.addHandler(id);
        inPhasesUptoAndIncludingPostDispatch.add(transportIN);
        inPhasesUptoAndIncludingPostDispatch.add(preDispatch);
        inPhasesUptoAndIncludingPostDispatch.add(dispatchPhase);
    }

    public void deserializeParameters(OMElement parameters) throws AxisFault {
        this.paramInclude.deserializeParameters(parameters);
    }

    public void engageModule(QName moduleref) throws AxisFault {
        ModuleDescription module = getModule(moduleref);
        boolean isNewmodule = false;

        if (module == null) {
            File file =
                    new ArchiveReader().creatModuleArchivefromResource(moduleref.getLocalPart(),
                            getRepository());

            module = new DeploymentEngine().buildModule(file, this);
            isNewmodule = true;
        }

        if (module != null) {
            for (Iterator iterator = engagedModules.iterator(); iterator.hasNext();) {
                QName qName = (QName) iterator.next();

                if (moduleref.equals(qName)) {
                    log.info("Attempt to engage an already engaged module " + qName);

                    return;
                }
            }
        } else {
            throw new AxisFault(this + " Refer to invalid module " + moduleref.getLocalPart()
                    + " has not bean deployed yet !");
        }

        Iterator servicegroups = getServiceGroups();

        while (servicegroups.hasNext()) {
            AxisServiceGroup serviceGroup = (AxisServiceGroup) servicegroups.next();

            serviceGroup.engageModuleToGroup(module.getName());
        }

        if (isNewmodule) {
            addModule(module);
        }

        engagedModules.add(moduleref);
    }

    public void notifyObservers(int event_type, AxisService service) {
        AxisEvent event = new AxisEvent(service, event_type);

        for (int i = 0; i < observersList.size(); i++) {
            AxisObserver axisObserver = (AxisObserver) observersList.get(i);

            axisObserver.update(event);
        }
    }

    /**
     * Method removeService.
     *
     * @param name
     * @throws AxisFault
     */
    public synchronized void removeService(String name) throws AxisFault {
        AxisService service = (AxisService) allservices.remove(name);

        if (service != null) {
            log.info("Removed service " + name);
        }
    }

    /**
     * Method getEngagedModules.
     *
     * @return Collection
     */
    public Collection getEngagedModules() {
        return engagedModules;
    }

    public Hashtable getFaultyModules() {
        return faultyModules;
    }

    public Hashtable getFaultyServices() {
        return faultyServices;
    }

    // to get the out flow correpodning to the global out flow;
    public ArrayList getGlobalOutPhases() {
        return this.outPhases;
    }

    public HostConfiguration getHostConfiguration() {
        return this.hostConfiguration;
    }

    /**
     * @return Returns ArrayList
     */
    public ArrayList getInFaultFlow() {
        return inFaultPhases;
    }

    public ArrayList getInPhasesUptoAndIncludingPostDispatch() {
        return inPhasesUptoAndIncludingPostDispatch;
    }

    public MessageReceiver getMessageReceiver(String key) {
        return (MessageReceiver) messagReceivers.get(key);
    }

    /**
     * Method getModule.
     *
     * @param name
     * @return Returns ModuleDescription.
     */
    public ModuleDescription getModule(QName name) {
        return (ModuleDescription) modules.get(name);
    }

    // the class loder that become the paranet of all the moduels
    public ClassLoader getModuleClassLoader() {
        return this.moduleClassLoader;
    }

    public ModuleConfiguration getModuleConfig(QName moduleName) {
        return (ModuleConfiguration) moduleConfigmap.get(moduleName);
    }

    /**
     * @return Returns HashMap.
     */
    public HashMap getModules() {
        return modules;
    }

    /**
     * @return Returns ArrayList
     */
    public ArrayList getOutFaultFlow() {
        return outFaultPhases;
    }

    /**
     * Method getParameter.
     *
     * @param name
     * @return Returns Parameter
     */
    public Parameter getParameter(String name) {
        return paramInclude.getParameter(name);
    }

    public ArrayList getParameters() {
        return paramInclude.getParameters();
    }

    public PhasesInfo getPhasesInfo() {
        return phasesinfo;
    }

    public String getRepository() {
        return axis2Repository;
    }

    /**
     * Method getService.
     *
     * @param name
     * @return Returns AxisService.
     * @throws AxisFault
     */
    public AxisService getService(String name) throws AxisFault {
        return (AxisService) allservices.get(name);
    }

    // the class loder that become the paranet of all the services
    public ClassLoader getServiceClassLoader() {
        return this.serviceClassLoader;
    }

    public AxisServiceGroup getServiceGroup(String serviceNameAndGroupString) {
        return (AxisServiceGroup) serviceGroups.get(serviceNameAndGroupString);
    }

    public Iterator getServiceGroups() {
        return serviceGroups.values().iterator();
    }

    // to get all the services in the system
    public HashMap getServices() {
        Iterator sgs = getServiceGroups();

        while (sgs.hasNext()) {
            AxisServiceGroup axisServiceGroup = (AxisServiceGroup) sgs.next();
            Iterator servics = axisServiceGroup.getServices();

            while (servics.hasNext()) {
                AxisService axisService = (AxisService) servics.next();

                allservices.put(axisService.getName(), axisService);
            }
        }

        return allservices;
    }

    // the class loder which become the top most parent of all the modules and services
    public ClassLoader getSystemClassLoader() {
        return this.systemClassLoader;
    }

    public TransportInDescription getTransportIn(QName name) throws AxisFault {
        return (TransportInDescription) transportsIn.get(name);
    }

    public TransportOutDescription getTransportOut(QName name) throws AxisFault {
        return (TransportOutDescription) transportsOut.get(name);
    }

    public HashMap getTransportsIn() {
        return transportsIn;
    }

    public HashMap getTransportsOut() {
        return transportsOut;
    }

    public boolean isEngaged(QName moduleName) {
        return engagedModules.contains(moduleName);
    }

    /**
     * Checks whether a given parameter is locked.
     *
     * @param parameterName
     * @return Returns boolean
     */
    public boolean isParameterLocked(String parameterName) {
        Parameter parameter = getParameter(parameterName);

        return (parameter != null) && parameter.isLocked();
    }

    public void setGlobalOutPhase(ArrayList outPhases) {
        this.outPhases = outPhases;
    }

    // to set and get host configuration
    public void setHostConfiguration(HostConfiguration hostConfiguration) {
        this.hostConfiguration = hostConfiguration;
    }

    /**
     * @param list
     */
    public void setInFaultPhases(ArrayList list) {
        inFaultPhases = list;
    }

    public void setInPhasesUptoAndIncludingPostDispatch(
            ArrayList inPhasesUptoAndIncludingPostDispatch) {
        this.inPhasesUptoAndIncludingPostDispatch = inPhasesUptoAndIncludingPostDispatch;
    }

    public void setModuleClassLoader(ClassLoader classLoader) {
        this.moduleClassLoader = classLoader;
    }

    /**
     * @param list
     */
    public void setOutFaultPhases(ArrayList list) {
        outFaultPhases = list;
    }

    public void setPhasesinfo(PhasesInfo phasesInfo) {
        this.phasesinfo = phasesInfo;
    }

    public void setRepository(String axis2Repository) {
        this.axis2Repository = axis2Repository;
    }

    public void setServiceClassLoader(ClassLoader classLoader) {
        this.serviceClassLoader = classLoader;
    }

    public void setSystemClassLoader(ClassLoader classLoader) {
        this.systemClassLoader = classLoader;
    }
}
