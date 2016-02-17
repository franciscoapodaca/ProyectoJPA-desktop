/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name = "albums")
@NamedQueries({
    @NamedQuery(name = "Albums.findAll", query = "SELECT a FROM Albums a")})
public class Albums implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cveAlbum")
    private String cveAlbum;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "disquera")
    private String disquera;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @ManyToMany(mappedBy = "albumsList")
    private List<Canciones> cancionesList;

    public Albums() {
    }

    public Albums(String cveAlbum) {
        this.cveAlbum = cveAlbum;
    }

    public Albums(String cveAlbum, String titulo) {
        this.cveAlbum = cveAlbum;
        this.titulo = titulo;
    }

    public String getCveAlbum() {
        return cveAlbum;
    }

    public void setCveAlbum(String cveAlbum) {
        this.cveAlbum = cveAlbum;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDisquera() {
        return disquera;
    }

    public void setDisquera(String disquera) {
        this.disquera = disquera;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Canciones> getCancionesList() {
        return cancionesList;
    }

    public void setCancionesList(List<Canciones> cancionesList) {
        this.cancionesList = cancionesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveAlbum != null ? cveAlbum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Albums)) {
            return false;
        }
        Albums other = (Albums) object;
        if ((this.cveAlbum == null && other.cveAlbum != null) || (this.cveAlbum != null && !this.cveAlbum.equals(other.cveAlbum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Albums[ cveAlbum=" + cveAlbum + " ]";
    }
    
}
