/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name = "actores")
@NamedQueries({
    @NamedQuery(name = "Actores.findAll", query = "SELECT a FROM Actores a")})
public class Actores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cveActor")
    private String cveActor;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;

    @ManyToMany(mappedBy = "actoresList")
    private List<Peliculas> peliculasList;

    public Actores() {
    }

    public Actores(String cveActor) {
        this.cveActor = cveActor;
    }

    public Actores(String cveActor, String nombre) {
        this.cveActor = cveActor;
        this.nombre = nombre;
    }

    public String getCveActor() {
        return cveActor;
    }

    public void setCveActor(String cveActor) {
        this.cveActor = cveActor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Peliculas> getPeliculasList() {
        return peliculasList;
    }

    public void setPeliculasList(List<Peliculas> peliculasList) {
        this.peliculasList = peliculasList;

    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveActor != null ? cveActor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actores)) {
            return false;
        }
        Actores other = (Actores) object;
        if ((this.cveActor == null && other.cveActor != null) || (this.cveActor != null && !this.cveActor.equals(other.cveActor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Actores[ cveActor=" + cveActor + " ]";
    }

}
