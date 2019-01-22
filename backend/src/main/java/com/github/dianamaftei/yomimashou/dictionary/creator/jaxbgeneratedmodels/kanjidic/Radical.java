
package com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic;

import javax.xml.bind.annotation.*;
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
 *         &lt;element ref="{}rad_value" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "radValue"
})
@XmlRootElement(name = "radical")
public class Radical {

    @XmlElement(name = "rad_value", required = true)
    protected List<RadValue> radValue;

    /**
     * Gets the value of the radValue property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the radValue property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRadValue().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RadValue }
     */
    public List<RadValue> getRadValue() {
        if (radValue == null) {
            radValue = new ArrayList<RadValue>();
        }
        return this.radValue;
    }

}
