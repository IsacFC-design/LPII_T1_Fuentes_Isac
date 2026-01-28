package com.fuentes.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "inventario")
public class Inventario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // ========== ATRIBUTOS ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro_inventario")
    private int nroInventario;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", insertable = false, updatable = false)
    private Date fecha;
    
    @Column(name = "costo_ingreso", nullable = false)
    private double costoIngreso;
    
    @Column(name = "motivo_ingreso", nullable = false, length = 200)
    private String motivoIngreso;
    
    // ========== ATRIBUTO NO SERIALIZABLE (Transient) ==========
    @Transient
    private int estado;
    
    // ========== RELACIONES ==========
    @ManyToOne
    @JoinColumn(name = "id_prod", nullable = false)
    private Producto producto;
    
    // ========== CONSTRUCTORES ==========
    public Inventario() {
        // Constructor por defecto requerido por JPA
    }
    
    public Inventario(double costoIngreso, String motivoIngreso, Producto producto) {
        this.costoIngreso = costoIngreso;
        this.motivoIngreso = motivoIngreso;
        this.producto = producto;
    }
    
    // ========== GETTERS Y SETTERS ==========
    public int getNroInventario() {
        return nroInventario;
    }
    
    public void setNroInventario(int nroInventario) {
        this.nroInventario = nroInventario;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public double getCostoIngreso() {
        return costoIngreso;
    }
    
    public void setCostoIngreso(double costoIngreso) {
        this.costoIngreso = costoIngreso;
    }
    
    public String getMotivoIngreso() {
        return motivoIngreso;
    }
    
    public void setMotivoIngreso(String motivoIngreso) {
        this.motivoIngreso = motivoIngreso;
    }
    
    public int getEstado() {
        return estado;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    // ========== MÉTODOS OVERRIDE ==========
    @Override
    public String toString() {
        return "Inventario [nroInventario=" + nroInventario + 
               ", fecha=" + fecha + 
               ", producto=" + (producto != null ? producto.getNomProd() : "null") + 
               ", costoIngreso=" + costoIngreso + "]";
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.nroInventario;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Inventario other = (Inventario) obj;
        return this.nroInventario == other.nroInventario;
    }
}
