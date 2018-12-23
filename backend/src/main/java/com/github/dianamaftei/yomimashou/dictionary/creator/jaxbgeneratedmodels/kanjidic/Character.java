
package com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.DictionaryEntry;

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
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{}literal"/>
 *         &lt;element ref="{}codepoint"/>
 *         &lt;element ref="{}radical"/>
 *         &lt;element ref="{}misc"/>
 *         &lt;element ref="{}dic_number" minOccurs="0"/>
 *         &lt;element ref="{}query_code" minOccurs="0"/>
 *         &lt;element ref="{}reading_meaning" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "literalAndCodepointAndRadical"
})
@XmlRootElement(name = "character")
public class Character implements DictionaryEntry {

    @XmlElements({
            @XmlElement(name = "literal", type = String.class),
            @XmlElement(name = "codepoint", type = Codepoint.class),
            @XmlElement(name = "radical", type = Radical.class),
            @XmlElement(name = "misc", type = Misc.class),
            @XmlElement(name = "dic_number", type = DicNumber.class),
            @XmlElement(name = "query_code", type = QueryCode.class),
            @XmlElement(name = "reading_meaning", type = ReadingMeaning.class)
    })
    protected List<Object> literalAndCodepointAndRadical;

    /**
     * Gets the value of the literalAndCodepointAndRadical property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the literalAndCodepointAndRadical property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLiteralAndCodepointAndRadical().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * {@link Codepoint }
     * {@link Radical }
     * {@link Misc }
     * {@link DicNumber }
     * {@link QueryCode }
     * {@link ReadingMeaning }
     */
    public List<Object> getLiteralAndCodepointAndRadical() {
        if (literalAndCodepointAndRadical == null) {
            literalAndCodepointAndRadical = new ArrayList<Object>();
        }
        return this.literalAndCodepointAndRadical;
    }

}
