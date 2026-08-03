# Descripción General de la Aplicación

## Nombre del Proyecto
**Sistema de Gestión de Cafetería**

## Tema Elegido
El proyecto consiste en un sistema integral de punto de venta (POS) y administración diseñado para una cafetería. El objetivo principal es digitalizar y centralizar el flujo de trabajo operativo: desde la toma de pedidos en el mostrador hasta el análisis de métricas de ventas.

## Entidades Principales del Sistema
El sistema se estructura alrededor de más de 3 entidades principales fuertemente interconectadas:

1. **User (Usuarios):** Representa tanto al personal interno (MANAGER, EMPLOYEE) con sistema de autenticación de credenciales, como a los clientes (CUSTOMER) para llevar registro de los compradores.
2. **Category & Product (Catálogo):** Gestión del inventario ofrecido. Los productos pertenecen a una categoría, y pueden o no requerir la especificación de un tamaño.
3. **Size (Tamaños):** Entidad que define variadores de precio (ej. multiplicador de x1.5 para tamaño "Grande").
4. **Order & OrderItem (Pedidos):** El núcleo transaccional. Una `Order` vincula al empleado que atiende, al cliente que compra, y mantiene un ciclo de vida de estado. Sus detalles desglosados (productos, cantidades, subtotales) se almacenan en `OrderItem`.

## Procesos de Ciclo de Vida Completo
El sistema implementa un ciclo de vida de apertura y cierre para las órdenes. Una vez que un pedido es registrado en el sistema, debe transicionar secuencialmente a través de los siguientes estados (gestionados internamente):
- `PENDING` (Pendiente de cobro/preparación)
- `PREPARING` (En preparación por los baristas)
- `READY` (Listo para entregar)
- `DELIVERED` (Entregado, ciclo cerrado) / `CANCELLED` (Anulado).

## Módulo de Reportes
El sistema recopila datos operativos suficientes para generar estadísticas críticas de negocio, incluyendo:
- Volumen de productos registrados.
- Ingresos (solo considerando órdenes en estado `DELIVERED`).
- Producto Top (más pedido) por cada categoría, calculado analíticamente desde los registros de ventas y no mediante estimaciones.
