
package com.github.dianamaftei.yomimashou.model.xmlOriginalModels.JMnedict;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nameType",
    "xref",
    "transDet"
})
@XmlRootElement(name = "trans")
public class Trans {

    @XmlElement(name = "name_type")
    protected List<NameType> nameType;
    protected List<Xref> xref;
    @XmlElement(name = "trans_det")
    protected List<TransDet> transDet;

    /**
     * Gets the value of the nameType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nameType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNameType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NameType }
     * 
     * 
     */
    public List<NameType> getNameType() {
        if (nameType == null) {
            nameType = new ArrayList<NameType>();
        }
        return this.nameType;
    }

    /**
     * Gets the value of the xref property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xref property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXref().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Xref }
     * 
     * 
     */
    public List<Xref> getXref() {
        if (xref == null) {
            xref = new ArrayList<Xref>();
        }
        return this.xref;
    }

    /**
     * Gets the value of the transDet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transDet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransDet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransDet }
     * 
     * 
     */
    public List<TransDet> getTransDet() {
        if (transDet == null) {
            transDet = new ArrayList<TransDet>();
        }
        return this.transDet;
    }

}
