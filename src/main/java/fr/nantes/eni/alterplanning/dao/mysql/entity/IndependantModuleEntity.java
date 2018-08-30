package fr.nantes.eni.alterplanning.dao.mysql.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Entity
@Table(name = "independant_modules", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"shortName"})
})
public class IndependantModuleEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "shortName")
    private String shortName;

    @Column(name = "longName")
    private String longName;

    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "hours")
    private int hours;

    @Column(name = "codeLieu")
    private int codeLieu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getCodeLieu() {
        return codeLieu;
    }

    public void setCodeLieu(int codeLieu) {
        this.codeLieu = codeLieu;
    }
}
