/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;
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
@Table(name = "canciones")
@NamedQueries({
    @NamedQuery(name = "Canciones.findAll", query = "SELECT c FROM Canciones c")})
public class Canciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "clave")
    private String clave;
    @Column(name = "interprete")
    private String interprete;
    @Column(name = "autor")
    private String autor;

    @ManyToMany
    private List<Albums> albumsList;

    public Canciones() {
    }

    public Canciones(String clave) {
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getInterprete() {
        return interprete;
    }

    public void setInterprete(String interprete) {
        this.interprete = interprete;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<Albums> getAlbumsList() {
        return albumsList;
    }

    public void setAlbumsList(List<Albums> albumsList) {
        this.albumsList = albumsList;
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
        if (!(object instanceof Canciones)) {
            return false;
        }
        Canciones other = (Canciones) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Canciones[ clave=" + clave + " ]";
    }
    
}
