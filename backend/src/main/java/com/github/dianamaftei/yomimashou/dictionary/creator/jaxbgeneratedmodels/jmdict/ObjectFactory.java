
package com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.github.dianamaftei.yomimashou.model.jaxbGeneratedModels package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Stagr_QNAME = new QName("", "stagr");
    private final static QName _Xref_QNAME = new QName("", "xref");
    private final static QName _ReNokanji_QNAME = new QName("", "re_nokanji");
    private final static QName _Ant_QNAME = new QName("", "ant");
    private final static QName _Pri_QNAME = new QName("", "pri");
    private final static QName _Keb_QNAME = new QName("", "keb");
    private final static QName _KeInf_QNAME = new QName("", "ke_inf");
    private final static QName _Reb_QNAME = new QName("", "reb");
    private final static QName _Stagk_QNAME = new QName("", "stagk");
    private final static QName _KePri_QNAME = new QName("", "ke_pri");
    private final static QName _EntSeq_QNAME = new QName("", "ent_seq");
    private final static QName _ReInf_QNAME = new QName("", "re_inf");
    private final static QName _Field_QNAME = new QName("", "field");
    private final static QName _SInf_QNAME = new QName("", "s_inf");
    private final static QName _Pos_QNAME = new QName("", "pos");
    private final static QName _ReRestr_QNAME = new QName("", "re_restr");
    private final static QName _RePri_QNAME = new QName("", "re_pri");
    private final static QName _Misc_QNAME = new QName("", "misc");
    private final static QName _Dial_QNAME = new QName("", "dial");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.github.dianamaftei.yomimashou.model.jaxbGeneratedModels
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JMdict }
     */
    public JMdict createJMdict() {
        return new JMdict();
    }

    /**
     * Create an instance of {@link Entry }
     */
    public Entry createEntry() {
        return new Entry();
    }

    /**
     * Create an instance of {@link KEle }
     */
    public KEle createKEle() {
        return new KEle();
    }

    /**
     * Create an instance of {@link REle }
     */
    public REle createREle() {
        return new REle();
    }

    /**
     * Create an instance of {@link Sense }
     */
    public Sense createSense() {
        return new Sense();
    }

    /**
     * Create an instance of {@link Lsource }
     */
    public Lsource createLsource() {
        return new Lsource();
    }

    /**
     * Create an instance of {@link Gloss }
     */
    public Gloss createGloss() {
        return new Gloss();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "stagr")
    public JAXBElement<String> createStagr(String value) {
        return new JAXBElement<String>(_Stagr_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "xref")
    public JAXBElement<String> createXref(String value) {
        return new JAXBElement<String>(_Xref_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "re_nokanji")
    public JAXBElement<String> createReNokanji(String value) {
        return new JAXBElement<String>(_ReNokanji_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "ant")
    public JAXBElement<String> createAnt(String value) {
        return new JAXBElement<String>(_Ant_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "pri")
    public JAXBElement<String> createPri(String value) {
        return new JAXBElement<String>(_Pri_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "keb")
    public JAXBElement<String> createKeb(String value) {
        return new JAXBElement<String>(_Keb_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "ke_inf")
    public JAXBElement<String> createKeInf(String value) {
        return new JAXBElement<String>(_KeInf_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "reb")
    public JAXBElement<String> createReb(String value) {
        return new JAXBElement<String>(_Reb_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "stagk")
    public JAXBElement<String> createStagk(String value) {
        return new JAXBElement<String>(_Stagk_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "ke_pri")
    public JAXBElement<String> createKePri(String value) {
        return new JAXBElement<String>(_KePri_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "ent_seq")
    public JAXBElement<String> createEntSeq(String value) {
        return new JAXBElement<String>(_EntSeq_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "re_inf")
    public JAXBElement<String> createReInf(String value) {
        return new JAXBElement<String>(_ReInf_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "field")
    public JAXBElement<String> createField(String value) {
        return new JAXBElement<String>(_Field_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "s_inf")
    public JAXBElement<String> createSInf(String value) {
        return new JAXBElement<String>(_SInf_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "pos")
    public JAXBElement<String> createPos(String value) {
        return new JAXBElement<String>(_Pos_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "re_restr")
    public JAXBElement<String> createReRestr(String value) {
        return new JAXBElement<String>(_ReRestr_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "re_pri")
    public JAXBElement<String> createRePri(String value) {
        return new JAXBElement<String>(_RePri_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "misc")
    public JAXBElement<String> createMisc(String value) {
        return new JAXBElement<String>(_Misc_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "dial")
    public JAXBElement<String> createDial(String value) {
        return new JAXBElement<String>(_Dial_QNAME, String.class, null, value);
    }

}
