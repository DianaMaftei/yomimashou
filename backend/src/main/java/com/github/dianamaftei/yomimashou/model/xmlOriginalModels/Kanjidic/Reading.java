
package com.github.dianamaftei.yomimashou.model.xmlOriginalModels.Kanjidic;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="r_type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="on_type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="r_status" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "reading")
public class Reading {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "r_type", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String rType;
    @XmlAttribute(name = "on_type")
    @XmlSchemaType(name = "anySimpleType")
    protected String onType;
    @XmlAttribute(name = "r_status")
    @XmlSchemaType(name = "anySimpleType")
    protected String rStatus;

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the rType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRType() {
        return rType;
    }

    /**
     * Sets the value of the rType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRType(String value) {
        this.rType = value;
    }

    /**
     * Gets the value of the onType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnType() {
        return onType;
    }

    /**
     * Sets the value of the onType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnType(String value) {
        this.onType = value;
    }

    /**
     * Gets the value of the rStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRStatus() {
        return rStatus;
    }

    /**
     * Sets the value of the rStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRStatus(String value) {
        this.rStatus = value;
    }

}
