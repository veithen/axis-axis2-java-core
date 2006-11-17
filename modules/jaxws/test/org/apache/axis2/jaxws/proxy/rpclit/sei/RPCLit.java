
package org.apache.axis2.jaxws.proxy.rpclit.sei;

import java.math.BigInteger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlList;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.test.proxy.rpclit.ComplexAll;
import org.test.proxy.rpclit.Enum;

/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0_01-b15-fcs
 * Generated source version: 2.0
 * 
 */
@WebService(name = "RPCLit", targetNamespace = "http://org/apache/axis2/jaxws/proxy/rpclit")
@SOAPBinding(style = Style.RPC)
public interface RPCLit {


    /**
     * 
     * @param simpleIn
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "simpleOut", partName = "simpleOut")
    public String testSimple(
        @WebParam(name = "simpleIn", partName = "simpleIn")
        String simpleIn);

    /**
     * 
     * @param arg70
     * @param arg00
     * @param arg10
     * @param arg20
     * @param arg30
     * @param arg40
     * @param arg50
     * @param arg60
     * @return
     *     returns javax.xml.namespace.QName[]
     */
    @XmlList
    @WebMethod
    @WebResult(name = "testListsReturn", partName = "testListsReturn")
    public QName[] testLists(
        @XmlList
        @WebParam(name = "arg_0_0", partName = "arg_0_0")
        QName[] arg00,
        @XmlList
        @WebParam(name = "arg_1_0", partName = "arg_1_0")
        XMLGregorianCalendar[] arg10,
        @XmlList
        @WebParam(name = "arg_2_0", partName = "arg_2_0")
        String[] arg20,
        @XmlList
        @WebParam(name = "arg_3_0", partName = "arg_3_0")
        BigInteger[] arg30,
        @XmlList
        @WebParam(name = "arg_4_0", partName = "arg_4_0")
        Long[] arg40,
        @XmlList
        @WebParam(name = "arg_5_0", partName = "arg_5_0")
        Enum[] arg50,
        @XmlList
        @WebParam(name = "arg_7_0", partName = "arg_7_0")
        String[] arg70,
        @WebParam(name = "arg_6_0", partName = "arg_6_0")
        ComplexAll arg60);

    /**
     * 
     * @param arg10
     * @return
     *     returns javax.xml.datatype.XMLGregorianCalendar[]
     */
    @XmlList
    @WebMethod
    @WebResult(name = "testCalendarList1Return", partName = "testCalendarList1Return")
    public XMLGregorianCalendar[] testCalendarList1(
        @XmlList
        @WebParam(name = "arg_1_0", partName = "arg_1_0")
        XMLGregorianCalendar[] arg10);

    /**
     * 
     * @param arg20
     * @return
     *     returns java.lang.String[]
     */
    @XmlList
    @WebMethod
    @WebResult(name = "testStringList2Return", partName = "testStringList2Return")
    public String[] testStringList2(
        @XmlList
        @WebParam(name = "arg_2_0", partName = "arg_2_0")
        String[] arg20);

    /**
     * 
     * @param arg30
     * @return
     *     returns java.math.BigInteger[]
     */
    @XmlList
    @WebMethod
    @WebResult(name = "testBigIntegerList3Return", partName = "testBigIntegerList3Return")
    public BigInteger[] testBigIntegerList3(
        @XmlList
        @WebParam(name = "arg_3_0", partName = "arg_3_0")
        BigInteger[] arg30);

    /**
     * 
     * @param arg40
     * @return
     *     returns java.lang.Long[]
     */
    @XmlList
    @WebMethod
    @WebResult(name = "testLongList4Return", partName = "testLongList4Return")
    public Long[] testLongList4(
        @XmlList
        @WebParam(name = "arg_4_0", partName = "arg_4_0")
        Long[] arg40);

    /**
     * 
     * @param arg50
     * @return
     *     returns org.apache.axis2.jaxws.proxy.rpclit.Enum[]
     */
    @XmlList
    @WebMethod
    @WebResult(name = "testEnumList5Return", partName = "testEnumList5Return")
    public Enum[] testEnumList5(
        @XmlList
        @WebParam(name = "arg_5_0", partName = "arg_5_0")
        Enum[] arg50);

    /**
     * 
     * @param arg60
     * @return
     *     returns org.apache.axis2.jaxws.proxy.rpclit.ComplexAll
     */
    @WebMethod
    @WebResult(name = "testComplexAll6Return", partName = "testComplexAll6Return")
    public ComplexAll testComplexAll6(
        @WebParam(name = "arg_6_0", partName = "arg_6_0")
        ComplexAll arg60);
    
    /**
     * 
     * @param arg70
     * @return
     *     returns java.lang.String[]
     */
    @XmlList
    @WebMethod
    @WebResult(name = "testEnumList7Return", partName = "testEnumList7Return")
    public String[] testEnumList7(
        @XmlList
        @WebParam(name = "arg_7_0", partName = "arg_7_0")
        String[] arg70);

  

}
