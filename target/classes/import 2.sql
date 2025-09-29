insert into usuarios (id,apellido,email,enabled,estado,nombre,password) values (1,'Perez','estebanjpc@gmail.com',1,'1','Esteban','$2a$10$h5zWx.w.hq.a2ekp0S.BNe.cgoG4321.XcZYtJnPxANwJvw0xU2uq');
insert into usuarios (id,apellido,email,enabled,estado,nombre,password) values (2,'1','ejperezcardenas@gmail.com',1,'1','Contador','$2a$10$h5zWx.w.hq.a2ekp0S.BNe.cgoG4321.XcZYtJnPxANwJvw0xU2uq');
insert into usuarios (id,apellido,email,enabled,estado,nombre,password,rut) values (3,'1','ircd.chile@gmail.com',1,'1','Trabajador','$2a$10$h5zWx.w.hq.a2ekp0S.BNe.cgoG4321.XcZYtJnPxANwJvw0xU2uq','1122');
insert into usuarios (id,apellido,email,enabled,estado,nombre,password,rut) values (4,'2','1asd3@ail.com',1,'1','Trabajador','$2a$10$h5zWx.w.hq.a2ekp0S.BNe.cgoG4321.XcZYtJnPxANwJvw0xU2uq','12');
insert into usuarios (id,apellido,email,enabled,estado,nombre,password) values (5,'2','cont2@gmail.com',1,'1','Contador','$2a$10$h5zWx.w.hq.a2ekp0S.BNe.cgoG4321.XcZYtJnPxANwJvw0xU2uq');

INSERT INTO usuarios_rol (id,user_id,authority) values (1,1,'ROLE_ADMIN');
INSERT INTO usuarios_rol (id,user_id,authority) values (2,2,'ROLE_CONTADOR');
INSERT INTO usuarios_rol (id,user_id,authority) values (3,3,'ROLE_USER');
INSERT INTO usuarios_rol (id,user_id,authority) values (4,4,'ROLE_USER');
INSERT INTO usuarios_rol (id,user_id,authority) values (5,5,'ROLE_CONTADOR');

INSERT INTO empresas (id,estado,nombre,razon_social,rut,usuario_id) VALUES (1,1,'Empresa 1','Empresa 1','12332',2);
INSERT INTO empresas (id,estado,nombre,razon_social,rut,usuario_id) VALUES (2,1,'Empresa 2','Empresa 2','12345',2);
INSERT INTO empresas (id,estado,nombre,razon_social,rut,usuario_id) VALUES (3,1,'Empresa 3','Empresa 3','12345',5);


INSERT INTO empresa_usuarios (id,empresa_id,usuario_id) values (1,1,3);
INSERT INTO empresa_usuarios (id,empresa_id,usuario_id) values (2,2,4);
INSERT INTO empresa_usuarios (id,empresa_id,usuario_id) values (3,3,3);
