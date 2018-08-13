
package com.github.dianamaftei.yomimashou.dictionary.creator.jaxbGeneratedModels.Kanjidic;

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
 *       &lt;attribute name="var_type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "content"
})
@XmlRootElement(name = "variant")
public class Variant {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "var_type", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String varType;

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
     * Gets the value of the varType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVarType() {
        return varType;
    }

    /**
     * Sets the value of the varType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVarType(String value) {
        this.varType = value;
    }

}
