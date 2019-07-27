
package com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict;

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
        "keb",
        "keInf",
        "kePri"
})
@XmlRootElement(name = "k_ele")
public class KEle {

    @XmlElement(required = true)
    protected String keb;
    @XmlElement(name = "ke_inf")
    protected List<KeInf> keInf;
    @XmlElement(name = "ke_pri")
    protected List<KePri> kePri;

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
     * {@link KeInf }
     */
    public List<KeInf> getKeInf() {
        if (keInf == null) {
          keInf = new ArrayList<>();
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
     * {@link KePri }
     */
    public List<KePri> getKePri() {
        if (kePri == null) {
          kePri = new ArrayList<>();
        }
        return this.kePri;
    }

}
