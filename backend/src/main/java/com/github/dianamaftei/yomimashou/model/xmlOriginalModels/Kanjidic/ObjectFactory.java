
package com.github.dianamaftei.yomimashou.model.xmlOriginalModels.Kanjidic;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the xmlOriginalModels package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FileVersion_QNAME = new QName("", "file_version");
    private final static QName _RadName_QNAME = new QName("", "rad_name");
    private final static QName _Nanori_QNAME = new QName("", "nanori");
    private final static QName _Grade_QNAME = new QName("", "grade");
    private final static QName _Jlpt_QNAME = new QName("", "jlpt");
    private final static QName _Freq_QNAME = new QName("", "freq");
    private final static QName _DateOfCreation_QNAME = new QName("", "date_of_creation");
    private final static QName _DatabaseVersion_QNAME = new QName("", "database_version");
    private final static QName _StrokeCount_QNAME = new QName("", "stroke_count");
    private final static QName _Literal_QNAME = new QName("", "literal");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: xmlOriginalModels
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Codepoint }
     * 
     */
    public Codepoint createCodepoint() {
        return new Codepoint();
    }

    /**
     * Create an instance of {@link CpValue }
     * 
     */
    public CpValue createCpValue() {
        return new CpValue();
    }

    /**
     * Create an instance of {@link Rmgroup }
     * 
     */
    public Rmgroup createRmgroup() {
        return new Rmgroup();
    }

    /**
     * Create an instance of {@link Reading }
     * 
     */
    public Reading createReading() {
        return new Reading();
    }

    /**
     * Create an instance of {@link Meaning }
     * 
     */
    public Meaning createMeaning() {
        return new Meaning();
    }

    /**
     * Create an instance of {@link RadValue }
     * 
     */
    public RadValue createRadValue() {
        return new RadValue();
    }

    /**
     * Create an instance of {@link QCode }
     * 
     */
    public QCode createQCode() {
        return new QCode();
    }

    /**
     * Create an instance of {@link Character }
     * 
     */
    public Character createCharacter() {
        return new Character();
    }

    /**
     * Create an instance of {@link Radical }
     * 
     */
    public Radical createRadical() {
        return new Radical();
    }

    /**
     * Create an instance of {@link Misc }
     * 
     */
    public Misc createMisc() {
        return new Misc();
    }

    /**
     * Create an instance of {@link Variant }
     * 
     */
    public Variant createVariant() {
        return new Variant();
    }

    /**
     * Create an instance of {@link DicNumber }
     * 
     */
    public DicNumber createDicNumber() {
        return new DicNumber();
    }

    /**
     * Create an instance of {@link DicRef }
     * 
     */
    public DicRef createDicRef() {
        return new DicRef();
    }

    /**
     * Create an instance of {@link QueryCode }
     * 
     */
    public QueryCode createQueryCode() {
        return new QueryCode();
    }

    /**
     * Create an instance of {@link ReadingMeaning }
     * 
     */
    public ReadingMeaning createReadingMeaning() {
        return new ReadingMeaning();
    }

    /**
     * Create an instance of {@link Kanjidic2 }
     * 
     */
    public Kanjidic2 createKanjidic2() {
        return new Kanjidic2();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "file_version")
    public JAXBElement<String> createFileVersion(String value) {
        return new JAXBElement<String>(_FileVersion_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "rad_name")
    public JAXBElement<String> createRadName(String value) {
        return new JAXBElement<String>(_RadName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nanori")
    public JAXBElement<String> createNanori(String value) {
        return new JAXBElement<String>(_Nanori_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "grade")
    public JAXBElement<String> createGrade(String value) {
        return new JAXBElement<String>(_Grade_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "jlpt")
    public JAXBElement<String> createJlpt(String value) {
        return new JAXBElement<String>(_Jlpt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "freq")
    public JAXBElement<String> createFreq(String value) {
        return new JAXBElement<String>(_Freq_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "date_of_creation")
    public JAXBElement<String> createDateOfCreation(String value) {
        return new JAXBElement<String>(_DateOfCreation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "database_version")
    public JAXBElement<String> createDatabaseVersion(String value) {
        return new JAXBElement<String>(_DatabaseVersion_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "stroke_count")
    public JAXBElement<String> createStrokeCount(String value) {
        return new JAXBElement<String>(_StrokeCount_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "literal")
    public JAXBElement<String> createLiteral(String value) {
        return new JAXBElement<String>(_Literal_QNAME, String.class, null, value);
    }

}
