delete from permanent_operation;
delete from user_roles;
delete from role;
delete from operation;
delete from type_operation;
delete from user;

insert into user(id, nom, prenom, email, password) values(1, 'Marchive', 'Cyril', 'cyril.marchive@gmail.com', '{bcrypt}$2a$10$F/kEGUBGtokJwiIWjTmtg.JLdeFqiftMeJ/MpmDkRU3Rt.NrWwlSq');
insert into user(id, nom, prenom, email, password) values(2, 'Boussat', 'Melanie', 'melanie.boussat@gmail.com', '{bcrypt}$2a$10$F/kEGUBGtokJwiIWjTmtg.JLdeFqiftMeJ/MpmDkRU3Rt.NrWwlSq');

insert into type_operation(id, value_type) values(1, 'CREDIT');
insert into type_operation(id, value_type) values(2, 'DEBIT');

insert into role(id, role) values(1, 'ROLE_ADMIN');
insert into role(id, role) values(2, 'ROLE_USER');

insert into user_roles(user_id, roles_id) values(1, 2);
insert into user_roles(user_id, roles_id) values(1, 1);
insert into user_roles(user_id, roles_id) values(2, 2);

insert into permanent_operation(id,  intitule, jour, prix, type_operation_id, user_id) values(1, 'HydroQuebec', 2, 100, 2, 1);
insert into permanent_operation(id,  intitule, jour, prix, type_operation_id, user_id) values(2, 'Virgin Modile', 10, 49, 2, 1);
insert into permanent_operation(id,  intitule, jour, prix, type_operation_id, user_id) values(3, 'Salaire', 1, 1000, 1, 1);
insert into permanent_operation(id,  intitule, jour, prix, type_operation_id, user_id) values(4, 'Bell', 1, 69, 2, 2);