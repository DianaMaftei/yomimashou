package com.yomimashou.creator.dictionary.name.jmnedictXMLmodels;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "reb",
        "reRestr",
        "reInf",
        "rePri"
})
@XmlRootElement(name = "r_ele")
public class REle {

    @XmlElement(required = true)
    protected String reb;
    @XmlElement(name = "re_restr")
    protected List<ReRestr> reRestr;
    @XmlElement(name = "re_inf")
    protected List<ReInf> reInf;
    @XmlElement(name = "re_pri")
    protected List<RePri> rePri;

    /**
     * Gets the value of the reb property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getReb() {
        return reb;
    }

    /**
     * Sets the value of the reb property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setReb(String value) {
        this.reb = value;
    }

    /**
     * Gets the value of the reRestr property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reRestr property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReRestr().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReRestr }
     */
    public List<ReRestr> getReRestr() {
        if (reRestr == null) {
          reRestr = new ArrayList<>();
        }
        return this.reRestr;
    }

    /**
     * Gets the value of the reInf property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reInf property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReInf().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReInf }
     */
    public List<ReInf> getReInf() {
        if (reInf == null) {
          reInf = new ArrayList<>();
        }
        return this.reInf;
    }

    /**
     * Gets the value of the rePri property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rePri property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRePri().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RePri }
     */
    public List<RePri> getRePri() {
        if (rePri == null) {
          rePri = new ArrayList<>();
        }
        return this.rePri;
    }

}
