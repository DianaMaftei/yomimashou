package com.yomimashou.creator.jaxbgeneratedmodels.kanjidic;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{}rmgroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}nanori" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "rmgroup",
        "nanori"
})
@XmlRootElement(name = "reading_meaning")
public class ReadingMeaning {

    protected List<Rmgroup> rmgroup;
    protected List<String> nanori;

    /**
     * Gets the value of the rmgroup property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rmgroup property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRmgroup().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Rmgroup }
     */
    public List<Rmgroup> getRmgroup() {
        if (rmgroup == null) {
            rmgroup = new ArrayList<>();
        }
        return this.rmgroup;
    }

    /**
     * Gets the value of the nanori property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nanori property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNanori().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getNanori() {
        if (nanori == null) {
            nanori = new ArrayList<>();
        }
        return this.nanori;
    }

}
