
package com.github.dianamaftei.yomimashou.model.xmlOriginalModels.Kanjidic;

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
 *         &lt;element ref="{}grade" minOccurs="0"/>
 *         &lt;element ref="{}stroke_count" maxOccurs="unbounded"/>
 *         &lt;element ref="{}variant" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}freq" minOccurs="0"/>
 *         &lt;element ref="{}rad_name" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}jlpt" minOccurs="0"/>
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
    "grade",
    "strokeCount",
    "variant",
    "freq",
    "radName",
    "jlpt"
})
@XmlRootElement(name = "misc")
public class Misc {

    protected String grade;
    @XmlElement(name = "stroke_count", required = true)
    protected List<String> strokeCount;
    protected List<Variant> variant;
    protected String freq;
    @XmlElement(name = "rad_name")
    protected List<String> radName;
    protected String jlpt;

    /**
     * Gets the value of the grade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Sets the value of the grade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrade(String value) {
        this.grade = value;
    }

    /**
     * Gets the value of the strokeCount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the strokeCount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStrokeCount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getStrokeCount() {
        if (strokeCount == null) {
            strokeCount = new ArrayList<String>();
        }
        return this.strokeCount;
    }

    /**
     * Gets the value of the variant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the variant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVariant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Variant }
     * 
     * 
     */
    public List<Variant> getVariant() {
        if (variant == null) {
            variant = new ArrayList<Variant>();
        }
        return this.variant;
    }

    /**
     * Gets the value of the freq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreq() {
        return freq;
    }

    /**
     * Sets the value of the freq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreq(String value) {
        this.freq = value;
    }

    /**
     * Gets the value of the radName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the radName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRadName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRadName() {
        if (radName == null) {
            radName = new ArrayList<String>();
        }
        return this.radName;
    }

    /**
     * Gets the value of the jlpt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJlpt() {
        return jlpt;
    }

    /**
     * Sets the value of the jlpt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJlpt(String value) {
        this.jlpt = value;
    }

}
