
package com.github.dianamaftei.yomimashou.model.xmlOriginalModels.JMdict;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}reb"/>
 *         &lt;element ref="{}re_nokanji" minOccurs="0"/>
 *         &lt;element ref="{}re_restr" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}re_inf" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}re_pri" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "reb",
    "reNokanji",
    "reRestr",
    "reInf",
    "rePri"
})
@XmlRootElement(name = "r_ele")
public class REle {

    @XmlElement(required = true)
    protected String reb;
    @XmlElement(name = "re_nokanji")
    protected String reNokanji;
    @XmlElement(name = "re_restr")
    protected List<String> reRestr;
    @XmlElement(name = "re_inf")
    protected List<String> reInf;
    @XmlElement(name = "re_pri")
    protected List<String> rePri;

    /**
     * Gets the value of the reb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReb() {
        return reb;
    }

    /**
     * Sets the value of the reb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReb(String value) {
        this.reb = value;
    }

    /**
     * Gets the value of the reNokanji property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReNokanji() {
        return reNokanji;
    }

    /**
     * Sets the value of the reNokanji property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReNokanji(String value) {
        this.reNokanji = value;
    }

    /**
     * Gets the value of the reRestr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reRestr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReRestr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReRestr() {
        if (reRestr == null) {
            reRestr = new ArrayList<String>();
        }
        return this.reRestr;
    }

    /**
     * Gets the value of the reInf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reInf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReInf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReInf() {
        if (reInf == null) {
            reInf = new ArrayList<String>();
        }
        return this.reInf;
    }

    /**
     * Gets the value of the rePri property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rePri property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRePri().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRePri() {
        if (rePri == null) {
            rePri = new ArrayList<String>();
        }
        return this.rePri;
    }

}
