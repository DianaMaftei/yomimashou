
package com.github.dianamaftei.yomimashou.model.xmlOriginalModels.JMnedict;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entSeqOrKEleOrREleOrTrans"
})
@XmlRootElement(name = "entry")
public class Entry {

    @XmlElements({
        @XmlElement(name = "ent_seq", type = EntSeq.class),
        @XmlElement(name = "k_ele", type = KEle.class),
        @XmlElement(name = "r_ele", type = REle.class),
        @XmlElement(name = "trans", type = Trans.class)
    })
    protected List<Object> entSeqOrKEleOrREleOrTrans;

    /**
     * Gets the value of the entSeqOrKEleOrREleOrTrans property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entSeqOrKEleOrREleOrTrans property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntSeqOrKEleOrREleOrTrans().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntSeq }
     * {@link KEle }
     * {@link REle }
     * {@link Trans }
     * 
     * 
     */
    public List<Object> getEntSeqOrKEleOrREleOrTrans() {
        if (entSeqOrKEleOrREleOrTrans == null) {
            entSeqOrKEleOrREleOrTrans = new ArrayList<Object>();
        }
        return this.entSeqOrKEleOrREleOrTrans;
    }

}
