
package com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element ref="{}keb"/>
 *         &lt;element ref="{}ke_inf" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}ke_pri" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "keb",
        "keInf",
        "kePri"
})
@XmlRootElement(name = "k_ele")
public class KEle {

    @XmlElement(required = true)
    protected String keb;
    @XmlElement(name = "ke_inf")
    protected List<String> keInf;
    @XmlElement(name = "ke_pri")
    protected List<String> kePri;

    /**
     * Gets the value of the keb property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getKeb() {
        return keb;
    }

    /**
     * Sets the value of the keb property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setKeb(String value) {
        this.keb = value;
    }

    /**
     * Gets the value of the keInf property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keInf property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeInf().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getKeInf() {
        if (keInf == null) {
            keInf = new ArrayList<String>();
        }
        return this.keInf;
    }

    /**
     * Gets the value of the kePri property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kePri property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKePri().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getKePri() {
        if (kePri == null) {
            kePri = new ArrayList<String>();
        }
        return this.kePri;
    }

}
