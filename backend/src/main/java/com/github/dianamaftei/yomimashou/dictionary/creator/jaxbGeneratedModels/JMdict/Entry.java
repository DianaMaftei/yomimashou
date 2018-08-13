
package com.github.dianamaftei.yomimashou.dictionary.creator.jaxbGeneratedModels.JMdict;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbGeneratedModels.DictionaryEntry;

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
 *         &lt;element ref="{}ent_seq"/>
 *         &lt;element ref="{}k_ele" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}r_ele" maxOccurs="unbounded"/>
 *         &lt;element ref="{}sense" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "entSeq",
        "kEle",
        "rEle",
        "sense"
})
@XmlRootElement(name = "entry")
public class Entry implements DictionaryEntry {

    @XmlElement(name = "ent_seq", required = true)
    protected String entSeq;
    @XmlElement(name = "k_ele")
    protected List<KEle> kEle;
    @XmlElement(name = "r_ele", required = true)
    protected List<REle> rEle;
    @XmlElement(required = true)
    protected List<Sense> sense;

    /**
     * Gets the value of the entSeq property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEntSeq() {
        return entSeq;
    }

    /**
     * Sets the value of the entSeq property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEntSeq(String value) {
        this.entSeq = value;
    }

    /**
     * Gets the value of the kEle property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kEle property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKEle().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KEle }
     */
    public List<KEle> getKEle() {
        if (kEle == null) {
            kEle = new ArrayList<KEle>();
        }
        return this.kEle;
    }

    /**
     * Gets the value of the rEle property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rEle property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getREle().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REle }
     */
    public List<REle> getREle() {
        if (rEle == null) {
            rEle = new ArrayList<REle>();
        }
        return this.rEle;
    }

    /**
     * Gets the value of the sense property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sense property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSense().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Sense }
     */
    public List<Sense> getSense() {
        if (sense == null) {
            sense = new ArrayList<Sense>();
        }
        return this.sense;
    }

}
