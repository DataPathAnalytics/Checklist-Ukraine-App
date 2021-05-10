--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

insert into question(value, answer_structure_id) values ('Інформація про предмет закупівлі',  3) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Порушення на суму',  8) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Ревізійний період',  4) on conflict do nothing;
insert into question(value, answer_structure_id) values ('В тому числі кошти Фонду COVID-19, грн',  8) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Витрати матеріальних ресурсів відповідають нормативним витратам, передбаченим державною ресурсно-елементною кошторисною нормою',  7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Інформація про оприлюднення (ID номер КОНТРАКТУ)',  6) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Територіальний орган, що здійснює заходи ДФК',  2) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Усунені порушення, грн',  8) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Об''єкт контролю (ЄДРПОУ)',  5) on conflict do nothing;
insert into question(value, answer_structure_id) values ('В тому числі що призвели до втрат, грн',  8) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Період проведення заходу ДФК',  4) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Начальник ревізії',  2) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Захід державного фінансового контролю',  2) on conflict do nothing;

insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (1,  12) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (2,  3) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (3,  2) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (4,  3) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (5,  4) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (5,  5) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (5,  6) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (6,  9) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (7,  7) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (8,  3) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (9,  10) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (9,  11) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (10,  3) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (11,  2) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (12,  1) on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id) values (13,  8) on conflict do nothing;