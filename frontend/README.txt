🚀 Guia de acceso y pruebas.

🔐 Credenciales de acceso

Como el sistema utiliza localStorage para la persistencia (almacenamiento local en el navegador), los datos no se transfieren en este archivo .zip. Para acceder al sistema, puedes seguir estos pasos:

1. Regístrate con un nuevo usuario en la página de registro. Esto creará una nueva entrada en el localStorage de tu navegador.

2. Luego, inicia sesión con las credenciales que acabas de crear. Esto te permitirá acceder a la funcionalidad del sistema.

(Nota: Actualmente solo se pueden registrar usuarios con el rol de "client". El rol de "admin" no está disponible para registro, pero puedes modificar el localStorage manualmente para agregar un usuario con ese rol si deseas probar la funcionalidad de administrador, aun que no esta desarrollada la vista de administrador, por ahora).

Pruebas

Para probar el sistema, puedes realizar las siguientes acciones:

- Agregar productos al carrito de compras desde la página de inicio.

- Ver el carrito de compras para verificar que los productos se hayan agregado correctamente.

- Eliminar y limpiar productos del carrito de compras para asegurarte de que la funcionalidad de eliminación funcione correctamente.

- Actualizar las cantidades de los productos en el carrito para verificar que el sistema maneje correctamente los cambios en las cantidades.

- Calcular el total de la compra en el carrito para asegurarte de que los precios se sumen correctamente.

- Realizar el proceso de compra para asegurarte de que el sistema funcione correctamente. (boton Finalizar compra aun no implementado)

- Probar la funcionalidad de búsqueda de productos en la página de inicio.

- Probar la funcionalidad de filtrado por categorías en la página de inicio.

Recuerda que, dado que el sistema utiliza localStorage, los datos se almacenarán solo en tu navegador y no se compartirán entre diferentes usuarios o dispositivos.

