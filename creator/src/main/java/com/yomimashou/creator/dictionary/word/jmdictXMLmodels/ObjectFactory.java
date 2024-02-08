package com.yomimashou.creator.dictionary.word.jmdictXMLmodels;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.yomimashou.model.jaxbgeneratedmodels package.
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

    private static final QName _Stagr_QNAME = new QName("", "stagr");
    private static final QName _Xref_QNAME = new QName("", "xref");
    private static final QName _ReNokanji_QNAME = new QName("", "re_nokanji");
    private static final QName _Ant_QNAME = new QName("", "ant");
    private static final QName _Pri_QNAME = new QName("", "pri");
    private static final QName _Keb_QNAME = new QName("", "keb");
    private static final QName _KeInf_QNAME = new QName("", "ke_inf");
    private static final QName _Reb_QNAME = new QName("", "reb");
    private static final QName _Stagk_QNAME = new QName("", "stagk");
    private static final QName _KePri_QNAME = new QName("", "ke_pri");
    private static final QName _EntSeq_QNAME = new QName("", "ent_seq");
    private static final QName _ReInf_QNAME = new QName("", "re_inf");
    private static final QName _Field_QNAME = new QName("", "field");
    private static final QName _SInf_QNAME = new QName("", "s_inf");
    private static final QName _Pos_QNAME = new QName("", "pos");
    private static final QName _ReRestr_QNAME = new QName("", "re_restr");
    private static final QName _RePri_QNAME = new QName("", "re_pri");
    private static final QName _Misc_QNAME = new QName("", "misc");
    private static final QName _Dial_QNAME = new QName("", "dial");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.yomimashou.model.jaxbgeneratedmodels
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
        return new JAXBElement<>(_Stagr_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "xref")
    public JAXBElement<String> createXref(String value) {
        return new JAXBElement<>(_Xref_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "re_nokanji")
    public JAXBElement<String> createReNokanji(String value) {
        return new JAXBElement<>(_ReNokanji_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "ant")
    public JAXBElement<String> createAnt(String value) {
        return new JAXBElement<>(_Ant_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "pri")
    public JAXBElement<String> createPri(String value) {
        return new JAXBElement<>(_Pri_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "keb")
    public JAXBElement<String> createKeb(String value) {
        return new JAXBElement<>(_Keb_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "ke_inf")
    public JAXBElement<String> createKeInf(String value) {
        return new JAXBElement<>(_KeInf_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "reb")
    public JAXBElement<String> createReb(String value) {
        return new JAXBElement<>(_Reb_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "stagk")
    public JAXBElement<String> createStagk(String value) {
        return new JAXBElement<>(_Stagk_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "ke_pri")
    public JAXBElement<String> createKePri(String value) {
        return new JAXBElement<>(_KePri_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "ent_seq")
    public JAXBElement<String> createEntSeq(String value) {
        return new JAXBElement<>(_EntSeq_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "re_inf")
    public JAXBElement<String> createReInf(String value) {
        return new JAXBElement<>(_ReInf_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "field")
    public JAXBElement<String> createField(String value) {
        return new JAXBElement<>(_Field_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "s_inf")
    public JAXBElement<String> createSInf(String value) {
        return new JAXBElement<>(_SInf_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "pos")
    public JAXBElement<String> createPos(String value) {
        return new JAXBElement<>(_Pos_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "re_restr")
    public JAXBElement<String> createReRestr(String value) {
        return new JAXBElement<>(_ReRestr_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "re_pri")
    public JAXBElement<String> createRePri(String value) {
        return new JAXBElement<>(_RePri_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "misc")
    public JAXBElement<String> createMisc(String value) {
        return new JAXBElement<>(_Misc_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "dial")
    public JAXBElement<String> createDial(String value) {
        return new JAXBElement<>(_Dial_QNAME, String.class, null, value);
    }

}
