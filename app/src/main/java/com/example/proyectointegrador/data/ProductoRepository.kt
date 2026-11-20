package com.example.proyectointegrador.data

class ProductoRepository(private val dao: ProductoDao) {
    val productos = dao.obtenerTodos()

    suspend fun guardar(producto: Producto) = dao.insertar(producto)

    suspend fun actualizar(producto: Producto) = dao.actualizar(producto)

    suspend fun eliminar(producto: Producto) = dao.eliminar(producto)
}