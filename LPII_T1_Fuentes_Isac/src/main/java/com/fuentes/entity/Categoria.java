package com.fuentes.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // ========== ENUM para frecuencia_compra ==========
    public enum FrecuenciaCompra {
        Diario, Interdiario, Semanal, Por_Stock
    }
    
    // ========== ATRIBUTOS ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cate")
    private int idCate;
    
    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia_compra", nullable = false)
    private FrecuenciaCompra frecuenciaCompra;
    
    // ========== RELACIONES ==========
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();
    
    // ========== CONSTRUCTORES ==========
    public Categoria() {
        // Constructor por defecto requerido por JPA
    }
    
    public Categoria(String descripcion, FrecuenciaCompra frecuenciaCompra) {
        this.descripcion = descripcion;
        this.frecuenciaCompra = frecuenciaCompra;
    }
    
    // ========== GETTERS Y SETTERS ==========
    public int getIdCate() {
        return idCate;
    }
    
    public void setIdCate(int idCate) {
        this.idCate = idCate;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public FrecuenciaCompra getFrecuenciaCompra() {
        return frecuenciaCompra;
    }
    
    public void setFrecuenciaCompra(FrecuenciaCompra frecuenciaCompra) {
        this.frecuenciaCompra = frecuenciaCompra;
    }
    
    public List<Producto> getProductos() {
        return productos;
    }
    
    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
    
    // ========== MÉTODOS DE CONVENIENCIA ==========
    public void agregarProducto(Producto producto) {
        productos.add(producto);
        producto.setCategoria(this);
    }
    
    public void removerProducto(Producto producto) {
        productos.remove(producto);
        producto.setCategoria(null);
    }
    
    // ========== MÉTODOS OVERRIDE ==========
    @Override
    public String toString() {
        return "Categoria [idCate=" + idCate + 
               ", descripcion=" + descripcion + 
               ", frecuenciaCompra=" + frecuenciaCompra + "]";
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.idCate;
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
        final Categoria other = (Categoria) obj;
        return this.idCate == other.idCate;
    }
}
