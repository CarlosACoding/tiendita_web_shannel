package com.TiendaWeb.Channel.Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios", schema = "tienda_vestimenta_accesorios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // o GenerationType.AUTO dependiendo de tu base de datos
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;


    @Column(name = "email", nullable = false, length = 65)
    private String email;

    @Column(name = "clave", nullable = false, length = 200)
    private String clave;

    @Column(name = "telefono", length = 10)
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id")
    private Role rol;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "cedula")
    private Integer cedula;

    @Column(name = "apellidos", length = 200)
    private String apellidos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getCedula() {
        return cedula;
    }

    public void setCedula(Integer cedula) {
        this.cedula = cedula;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Usuario(Integer id, String apellidos, Integer cedula, String direccion, Role rol, String telefono, String clave, String email, String nombre) {
        this.id = id;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.direccion = direccion;
        this.rol = rol;
        this.telefono = telefono;
        this.clave = clave;
        this.email = email;
        this.nombre = nombre;
    }

    public Usuario() {
    }
}