
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
 *       &lt;attribute name="qc_type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="skip_misclass" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
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
@XmlRootElement(name = "q_code")
public class QCode {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "qc_type", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String qcType;
    @XmlAttribute(name = "skip_misclass")
    @XmlSchemaType(name = "anySimpleType")
    protected String skipMisclass;

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
     * Gets the value of the qcType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQcType() {
        return qcType;
    }

    /**
     * Sets the value of the qcType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQcType(String value) {
        this.qcType = value;
    }

    /**
     * Gets the value of the skipMisclass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSkipMisclass() {
        return skipMisclass;
    }

    /**
     * Sets the value of the skipMisclass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkipMisclass(String value) {
        this.skipMisclass = value;
    }

}
