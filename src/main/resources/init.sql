insert into base_user (id, created_at, email, first_name, last_name, password, updated_at)
values  (109, '2021-11-16 14:19:46.988000', 'akp.an4f3i@yahoo.com', 'aniefiok', 'akpan', '$2a$10$zsElSSlQX8p3k/NlfTYwluU1EK8QvESSPzUd1ohVMIgMhgCQIj4kG', '2021-11-16 14:19:46.988000'),
        (220, '2021-11-16 14:19:59.767000', 'akp.an4f3mi@yahoo.com', 'aniefiok', 'akpan', '$2a$10$Tcrnj3XmoocBUX4tqrP7FOWIIFdXciSHDHq1ZbZqBLgiL29P9csyu', '2021-11-16 14:19:59.767000');

insert into answer(id,date_created,option_id,user_id) values(1000,now(),12,109);
insert into answer(id,date_created,option_id,user_id) values(1100,now(),11,109);
insert into answer(id,date_created,option_id,user_id) values(1200,now(),14,109);
insert into answer(id,date_created,option_id,user_id) values(1050,now(),18,109);
