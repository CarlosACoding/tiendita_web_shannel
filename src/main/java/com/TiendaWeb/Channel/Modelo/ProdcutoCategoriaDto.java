package com.TiendaWeb.Channel.Modelo;

public class ProdcutoCategoriaDto {
    private Long productoId;
    private Long categoriaId;

    public ProdcutoCategoriaDto() {
    }

    public ProdcutoCategoriaDto(Long productoId, Long categoriaId) {
        this.productoId = productoId;
        this.categoriaId = categoriaId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
