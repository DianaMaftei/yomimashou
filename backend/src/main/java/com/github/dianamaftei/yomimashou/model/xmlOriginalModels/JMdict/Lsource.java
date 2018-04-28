
package com.github.dianamaftei.yomimashou.model.xmlOriginalModels.JMdict;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang default="eng""/>
 *       &lt;attribute name="ls_type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="ls_wasei" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
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
@XmlRootElement(name = "lsource")
public class Lsource {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anySimpleType")
    protected String lang;
    @XmlAttribute(name = "ls_type")
    @XmlSchemaType(name = "anySimpleType")
    protected String lsType;
    @XmlAttribute(name = "ls_wasei")
    @XmlSchemaType(name = "anySimpleType")
    protected String lsWasei;

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
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        if (lang == null) {
            return "eng";
        } else {
            return lang;
        }
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * Gets the value of the lsType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLsType() {
        return lsType;
    }

    /**
     * Sets the value of the lsType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLsType(String value) {
        this.lsType = value;
    }

    /**
     * Gets the value of the lsWasei property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLsWasei() {
        return lsWasei;
    }

    /**
     * Sets the value of the lsWasei property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLsWasei(String value) {
        this.lsWasei = value;
    }

}
