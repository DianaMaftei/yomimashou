package com.yomimashou.creator.jaxbgeneratedmodels.kanjidic;

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
 *       &lt;attribute name="rad_type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "content"
})
@XmlRootElement(name = "rad_value")
public class RadValue {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "rad_type", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String radType;

    /**
     * Gets the value of the content property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the radType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRadType() {
        return radType;
    }

    /**
     * Sets the value of the radType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRadType(String value) {
        this.radType = value;
    }

}
