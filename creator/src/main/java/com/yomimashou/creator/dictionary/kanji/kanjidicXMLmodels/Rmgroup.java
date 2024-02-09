package com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element ref="{}reading" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}meaning" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "reading",
        "meaning"
})
@XmlRootElement(name = "rmgroup")
public class Rmgroup {

    protected List<Reading> reading;
    protected List<Meaning> meaning;

    /**
     * Gets the value of the reading property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reading property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReading().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reading }
     */
    public List<Reading> getReading() {
        if (reading == null) {
            reading = new ArrayList<>();
        }
        return this.reading;
    }

    /**
     * Gets the value of the meaning property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the meaning property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMeaning().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Meaning }
     */
    public List<Meaning> getMeaning() {
        if (meaning == null) {
            meaning = new ArrayList<>();
        }
        return this.meaning;
    }

}
