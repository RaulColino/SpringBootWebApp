
@ResponseStatus annotated error classes, in combination with the server.error configuration properties, allows us to manipulate almost all the fields in our Spring-defined error response payload.
---
Usare herencia para distinguir usuarios con distintos roles.

Utilizare la siguiente estrategia de herencia: 
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

Por que?:
The single table strategy maps all entities of the inheritance structure to the same database table. This approach makes polymorphic queries very efficient and provides the best performance.

---
Uso Project Lombok Framework para que con anotaciones como @Data, @NoArgsConstructor y @AllArgsConstructor se creen automaticamente los getters, setters, constructores, etc. de las entidades.
---
@JsonView()
In order to avoid circular references when getting entity info with nested entities we can use something like jsoningnore() but when the relation gets complex and we have to show diferent data views depending on the request we cannot use jsoningnore().
Instead we have to use JsonView interfaces to adapt each response to its request.

---
Existen tres tipos de usuarios: customer, organizer, admin

---
Se ha implementado una mejora: Implementar una solución al problema de concurrencia en la reserva de tickets

---
A la hora de probar las requests se inicializa la base de datos con usuarios y eventos. Se puede consultar la info de las instancias en la clase DatabaseInitializer.java
