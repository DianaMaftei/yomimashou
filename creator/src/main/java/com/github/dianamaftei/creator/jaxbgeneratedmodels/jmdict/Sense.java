package com.github.dianamaftei.creator.jaxbgeneratedmodels.jmdict;

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
 *         &lt;element ref="{}stagk" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}stagr" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}pos" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}xref" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}ant" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}field" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}misc" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}s_inf" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}lsource" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}dial" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}gloss" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "stagk",
        "stagr",
        "pos",
        "xref",
        "ant",
        "field",
        "misc",
        "sInf",
        "lsource",
        "dial",
        "gloss"
})
@XmlRootElement(name = "sense")
public class Sense {

    protected List<String> stagk;
    protected List<String> stagr;
    protected List<String> pos;
    protected List<String> xref;
    protected List<String> ant;
    protected List<String> field;
    protected List<String> misc;
    @XmlElement(name = "s_inf")
    protected List<String> sInf;
    protected List<Lsource> lsource;
    protected List<String> dial;
    protected List<Gloss> gloss;

    /**
     * Gets the value of the stagk property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stagk property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStagk().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getStagk() {
        if (stagk == null) {
            stagk = new ArrayList<>();
        }
        return this.stagk;
    }

    /**
     * Gets the value of the stagr property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stagr property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStagr().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getStagr() {
        if (stagr == null) {
            stagr = new ArrayList<>();
        }
        return this.stagr;
    }

    /**
     * Gets the value of the pos property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pos property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPos().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getPos() {
        if (pos == null) {
            pos = new ArrayList<>();
        }
        return this.pos;
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
     * {@link String }
     */
    public List<String> getXref() {
        if (xref == null) {
            xref = new ArrayList<>();
        }
        return this.xref;
    }

    /**
     * Gets the value of the ant property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ant property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnt().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getAnt() {
        if (ant == null) {
            ant = new ArrayList<>();
        }
        return this.ant;
    }

    /**
     * Gets the value of the field property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the field property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getField().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getField() {
        if (field == null) {
            field = new ArrayList<>();
        }
        return this.field;
    }

    /**
     * Gets the value of the misc property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the misc property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMisc().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getMisc() {
        if (misc == null) {
            misc = new ArrayList<>();
        }
        return this.misc;
    }

    /**
     * Gets the value of the sInf property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sInf property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSInf().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getSInf() {
        if (sInf == null) {
            sInf = new ArrayList<>();
        }
        return this.sInf;
    }

    /**
     * Gets the value of the lsource property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lsource property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLsource().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lsource }
     */
    public List<Lsource> getLsource() {
        if (lsource == null) {
            lsource = new ArrayList<>();
        }
        return this.lsource;
    }

    /**
     * Gets the value of the dial property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dial property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDial().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getDial() {
        if (dial == null) {
            dial = new ArrayList<>();
        }
        return this.dial;
    }

    /**
     * Gets the value of the gloss property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gloss property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGloss().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Gloss }
     */
    public List<Gloss> getGloss() {
        if (gloss == null) {
            gloss = new ArrayList<>();
        }
        return this.gloss;
    }

}
