/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name = "generos")
@NamedQueries({
    @NamedQuery(name = "Generos.findAll", query = "SELECT g FROM Generos g")})
public class Generos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cveGenero")
    private String cveGenero;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "tipoMedio")
    private String tipoMedio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cveGenero")
    private List<Medios> mediosList;

    public Generos() {
    }

    public Generos(String cveGenero) {
        this.cveGenero = cveGenero;
    }

    public Generos(String cveGenero, String nombre, String tipoMedio) {
        this.cveGenero = cveGenero;
        this.nombre = nombre;
        this.tipoMedio = tipoMedio;
    }

    public String getCveGenero() {
        return cveGenero;
    }

    public void setCveGenero(String cveGenero) {
        this.cveGenero = cveGenero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoMedio() {
        return tipoMedio;
    }

    public void setTipoMedio(String tipoMedio) {
        this.tipoMedio = tipoMedio;
    }

    public List<Medios> getMediosList() {
        return mediosList;
    }

    public void setMediosList(List<Medios> mediosList) {
        this.mediosList = mediosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveGenero != null ? cveGenero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Generos)) {
            return false;
        }
        Generos other = (Generos) object;
        if ((this.cveGenero == null && other.cveGenero != null) || (this.cveGenero != null && !this.cveGenero.equals(other.cveGenero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return cveGenero ;
    }
    
}
