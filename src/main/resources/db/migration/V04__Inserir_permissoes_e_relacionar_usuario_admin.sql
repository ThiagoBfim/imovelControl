INSERT INTO usuario VALUES (1,'admin', 'root','thiagobomfim1995@gmail.com', '$2a$10$fC2sD8WrvtAfvbGGKgv51eQFU5q6BZJMnu1rshdQ0jacPOfD8PYoa', true, null);

--Senha = admin
INSERT INTO permissao VALUES (1, 'ROLE_LISTAR_USUARIO');

INSERT INTO grupo_permissao (codigo_grupo, codigo_permissao) VALUES (1, 1);
INSERT INTO usuario_grupo (codigo_usuario, codigo_grupo) VALUES (
	(SELECT codigo FROM usuario WHERE email = 'thiagobomfim1995@gmail.com'), 1);