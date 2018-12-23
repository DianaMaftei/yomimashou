
package com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic;

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
 *       &lt;attribute name="dr_type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="m_vol" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="m_page" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "content"
})
@XmlRootElement(name = "dic_ref")
public class DicRef {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "dr_type", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String drType;
    @XmlAttribute(name = "m_vol")
    @XmlSchemaType(name = "anySimpleType")
    protected String mVol;
    @XmlAttribute(name = "m_page")
    @XmlSchemaType(name = "anySimpleType")
    protected String mPage;

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
     * Gets the value of the drType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDrType() {
        return drType;
    }

    /**
     * Sets the value of the drType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDrType(String value) {
        this.drType = value;
    }

    /**
     * Gets the value of the mVol property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMVol() {
        return mVol;
    }

    /**
     * Sets the value of the mVol property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMVol(String value) {
        this.mVol = value;
    }

    /**
     * Gets the value of the mPage property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMPage() {
        return mPage;
    }

    /**
     * Sets the value of the mPage property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMPage(String value) {
        this.mPage = value;
    }

}
