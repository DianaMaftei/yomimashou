
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
 *       &lt;sequence>
 *         &lt;element ref="{}file_version"/>
 *         &lt;element ref="{}database_version"/>
 *         &lt;element ref="{}date_of_creation"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "fileVersion",
        "databaseVersion",
        "dateOfCreation"
})
@XmlRootElement(name = "header")
public class Header {

    @XmlElement(name = "file_version", required = true)
    protected String fileVersion;
    @XmlElement(name = "database_version", required = true)
    protected String databaseVersion;
    @XmlElement(name = "date_of_creation", required = true)
    protected String dateOfCreation;

    /**
     * Gets the value of the fileVersion property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFileVersion() {
        return fileVersion;
    }

    /**
     * Sets the value of the fileVersion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFileVersion(String value) {
        this.fileVersion = value;
    }

    /**
     * Gets the value of the databaseVersion property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDatabaseVersion() {
        return databaseVersion;
    }

    /**
     * Sets the value of the databaseVersion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDatabaseVersion(String value) {
        this.databaseVersion = value;
    }

    /**
     * Gets the value of the dateOfCreation property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * Sets the value of the dateOfCreation property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDateOfCreation(String value) {
        this.dateOfCreation = value;
    }

}
