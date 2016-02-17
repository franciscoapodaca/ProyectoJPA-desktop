/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name = "medios")
@NamedQueries({
    @NamedQuery(name = "Medios.findAll", query = "SELECT m FROM Medios m")})
public class Medios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "clave")
    private String clave;
    @Basic(optional = false)
    @Column(name = "titutlo")
    private String titutlo;
    @Basic(optional = false)
    @Column(name = "tipoMedio")
    private String tipoMedio;
    @Basic(optional = false)
    @Column(name = "duracion")
    private int duracion;
    @JoinColumn(name = "cveGenero", referencedColumnName = "cveGenero")
    @ManyToOne(optional = false)
    private Generos cveGenero;

    public Medios() {
    }

    public Medios(String clave) {
        this.clave = clave;
    }

    public Medios(String clave, String titutlo, String tipoMedio, int duracion) {
        this.clave = clave;
        this.titutlo = titutlo;
        this.tipoMedio = tipoMedio;
        this.duracion = duracion;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTitutlo() {
        return titutlo;
    }

    public void setTitutlo(String titutlo) {
        this.titutlo = titutlo;
    }

    public String getTipoMedio() {
        return tipoMedio;
    }

    public void setTipoMedio(String tipoMedio) {
        this.tipoMedio = tipoMedio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Generos getCveGenero() {
        return cveGenero;
    }

    public void setCveGenero(Generos cveGenero) {
        this.cveGenero = cveGenero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medios)) {
            return false;
        }
        Medios other = (Medios) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Medios[ clave=" + clave + " ]";
    }
    
}
