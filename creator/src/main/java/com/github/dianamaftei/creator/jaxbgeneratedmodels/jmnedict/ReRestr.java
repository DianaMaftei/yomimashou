package com.github.dianamaftei.creator.jaxbgeneratedmodels.jmnedict;

import javax.xml.bind.annotation.*;


/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "value"
})
@XmlRootElement(name = "re_restr")
public class ReRestr {

    @XmlValue
    protected String value;

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
