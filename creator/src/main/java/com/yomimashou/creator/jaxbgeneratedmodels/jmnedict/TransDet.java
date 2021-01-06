package com.yomimashou.creator.jaxbgeneratedmodels.jmnedict;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "value"
})
@XmlRootElement(name = "trans_det")
public class TransDet {

    @XmlAttribute(name = "xml:lang")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String xmlLang;
    @XmlValue
    protected String value;

    /**
     * Gets the value of the xmlLang property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getXmlLang() {
        return xmlLang;
    }

    /**
     * Sets the value of the xmlLang property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setXmlLang(String value) {
        this.xmlLang = value;
    }

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getvalue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setvalue(String value) {
        this.value = value;
    }

}
