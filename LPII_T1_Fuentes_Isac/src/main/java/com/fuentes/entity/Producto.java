package com.fuentes.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // ========== ATRIBUTOS ==========
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prod")
    private int idProd;
    
    @Column(name = "nom_prod", nullable = false, length = 100)
    private String nomProd;
    
    @Column(name = "stock_actual")
    private int stockActual;
    
    // ========== RELACIONES ==========
    @ManyToOne
    @JoinColumn(name = "id_cate", nullable = false)
    private Categoria categoria;
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventario> inventarios = new ArrayList<>();
    
    // ========== CONSTRUCTORES ==========
    public Producto() {
        // Constructor por defecto requerido por JPA
    }
    
    public Producto(String nomProd, int stockActual, Categoria categoria) {
        this.nomProd = nomProd;
        this.stockActual = stockActual;
        this.categoria = categoria;
    }
    
    // ========== GETTERS Y SETTERS ==========
    public int getIdProd() {
        return idProd;
    }
    
    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }
    
    public String getNomProd() {
        return nomProd;
    }
    
    public void setNomProd(String nomProd) {
        this.nomProd = nomProd;
    }
    
    public int getStockActual() {
        return stockActual;
    }
    
    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    public List<Inventario> getInventarios() {
        return inventarios;
    }
    
    public void setInventarios(List<Inventario> inventarios) {
        this.inventarios = inventarios;
    }
    
    // ========== MÉTODOS DE CONVENIENCIA ==========
    public void agregarInventario(Inventario inventario) {
        inventarios.add(inventario);
        inventario.setProducto(this);
    }
    
    public void removerInventario(Inventario inventario) {
        inventarios.remove(inventario);
        inventario.setProducto(null);
    }
    
    // ========== MÉTODOS OVERRIDE ==========
    @Override
    public String toString() {
        return nomProd + " (Stock: " + stockActual + ")";
    }
    
    // Método para mostrar en combobox
    public String getDisplayText() {
        return nomProd + " - Stock: " + stockActual;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.idProd;
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
        final Producto other = (Producto) obj;
        return this.idProd == other.idProd;
    }
}
