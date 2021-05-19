--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

insert into question(value, answer_structure_id)
values ('Інформація про предмет закупівлі', 3)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Порушення на суму', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Ревізійний період', 4)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('В тому числі кошти Фонду COVID-19, грн', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Витрати матеріальних ресурсів відповідають нормативним витратам, передбаченим державною ресурсно-елементною кошторисною нормою',
        7)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Інформація про оприлюднення (ID номер КОНТРАКТУ)', 6)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Територіальний орган, що здійснює заходи ДФК', 2)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Усунені порушення, грн', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Об''єкт контролю (ЄДРПОУ)', 5)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('В тому числі що призвели до втрат, грн', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Період проведення заходу ДФК', 4)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Начальник ревізії', 2)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Захід державного фінансового контролю', 2)
on conflict do nothing;

insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (1, 12)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (2, 3)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (3, 2)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (4, 3)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (5, 4)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (5, 5)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (5, 6)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (6, 9)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (7, 7)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (8, 3)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (9, 10)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (9, 11)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (10, 3)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (11, 2)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (12, 1)
on conflict do nothing;
insert into question_knowledge_class(question_id, knowledge_class_outer_id)
values (13, 8)
on conflict do nothing;

-- ukr-avto-dor
insert into question(value, answer_structure_id)
values ('Перевіряючий', 10)
on conflict do nothing;

insert into question(value, answer_structure_id)
values ('Загальна вартість договору з урахуванням змін (Кошти Державного бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Загальна вартість договору за ревізійний період (Кошти Державного бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг отриманих бюджетних коштів за ревізійний період (Кошти Державного бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг виконаних підрядником робіт (послуг) за ревізійний період (Кошти Державного бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг перевірених робіт (послуг) (Кошти Державного бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Загальна вартість договору з урахуванням змін (З яких кошти із фонду COVID-19, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Загальна вартість договору за ревізійний період (З яких кошти із фонду COVID-19, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг отриманих бюджетних коштів за ревізійний період (З яких кошти із фонду COVID-19, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг виконаних підрядником робіт (послуг) за ревізійний період (З яких кошти із фонду COVID-19, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг перевірених робіт (послуг) (З яких кошти із фонду COVID-19, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Загальна вартість договору з урахуванням змін (Кошти місцевого бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Загальна вартість договору за ревізійний період (Кошти місцевого бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг отриманих бюджетних коштів за ревізійний період (Кошти місцевого бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг виконаних підрядником робіт (послуг) за ревізійний період (Кошти місцевого бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг перевірених робіт (послуг) (Кошти місцевого бюджету, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Загальна вартість договору з урахуванням змін (Інші джерела фінансування, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Загальна вартість договору за ревізійний період (Інші джерела фінансування, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг отриманих бюджетних коштів за ревізійний період (Інші джерела фінансування, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг виконаних підрядником робіт (послуг) за ревізійний період (Інші джерела фінансування, грн)', 8)
on conflict do nothing;
insert into question(value, answer_structure_id)
values ('Обсяг перевірених робіт (послуг) (Інші джерела фінансування, грн)', 8)
on conflict do nothing;

insert into question(value, answer_structure_id)
values ('Короткий коментар з приводу спірних питань. Загальне резюме огляду', 1)
on conflict do nothing;

insert into question(value, answer_structure_id)
values ('Дата створення', 9)
on conflict do nothing;

insert into question(value, answer_structure_id) values ('Вартість матеріальних ресурсів в актах форми КБ-2в відповідає фактичній вартості матеріальних ресурсів, списаній за даними бухгалтерського обліку.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Вартість матеріальних ресурсів калькуляції (власне виробництво) у актах форми КБ-2в відповідає фактичній вартості матеріальних ресурсів, списаній за даними бухгалтерського обліку.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Витрати матеріальних ресурсів відповідають нормативним витратам, передбаченим державною ресурсно-елементною кошторисною нормою.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Відстань транспортування матеріальних ресурсів в актах форми КБ-2в відповідає фактичній відстані транспортування матеріальних ресурсів.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Вартість машино-годин експлуатації будівельних машин та механізмів у актах форми КБ-2в фактично підтверджено вартістю за даними бухгалтерського обліку.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Вартість експлуатації машин та механізмів у актах форми КБ-2в документально підтверджено.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Зазначені у актах форми КБ-2в норми витрат труда робітників, зайнятих на ремонтно-будівельних роботах, а також робітників, зайнятих на керуванні та обслуговуванні машин та механізмів, відповідають нормам, визначеним державними ресурсно-елементними кошторисними нормами.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Обґрунтовано включення до актів форми КБ-2в коефіцієнтів для урахування впливу умов виконання робіт до норм витрат труда робітників, зайнятих на ремонтно-будівельних роботах; робітників, зайнятих на керуванні та обслуговуванні машин та механізмів.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('В актах форми КБ-2в правильно визначено показники загальновиробничих, адміністративних витрат та кошторисного прибутку при обчисленні норм витрат труда робітників, зайнятих на керуванні та обслуговуванні орендованих машин та механізмів.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Застосований в актах форми КБ-2в коефіцієнт кошторисного прибутку відповідає класу наслідків (відповідальності), який фактично відповідає об’єкту будівництва.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Документально підтверджено зазначені в актах форми КБ-2в витрати на перебазування підрядником будівельних машин та механізмів.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Списано паливно-мастильних матеріалів у обсягах, що не перевищують нормативні.1', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Документально підтверджено зазначені в актах форми КБ-2в додаткові витрати, пов’язані з виконанням робіт у зимовий та літній період.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Документально підтверджено зазначені в актах форми КБ-2в витрати на перевезення будівельників на об’єкт будівництва.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Документально підтверджено зазначені в актах форми КБ-2в витрати на відрядження та проживання робітників.', 7) on conflict do nothing;

insert into question(value, answer_structure_id) values ('Товщини укладеного асфальтобетонного шару відповідають проектним, нормативним даним.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Ширина покриття відповідає проектним даним', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Площа укладеного шару відповідає обсягу укладеного асфальтобетону визначеному проектною документацією (умовам договору).', 7) on conflict do nothing;

insert into question(value, answer_structure_id) values ('Зазначені в актах форми КБ-2в види, обсяги робіт відповідають проектно-кошторисній документації та договірній ціні.', 7) on conflict do nothing;

insert into question(value, answer_structure_id) values ('Товщина вирівнюючого шару із асфальтобетону (при влаштуванні окремим шаром) дорівнює або більше ніж два з половиною діаметри максимального розміру зерна щебеню.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Фізико-механічні властивості асфальтобетону відповідають вимогам нормативних документів (ДСТУ Б В. 2.7-119 «Суміші асфальтобетонні і асфальтобетон дорожній та аеродромний. Технічні умови», ДСТУ Б В.2.7-127 «Суміші асфальтобетонні і асфальтобетон щебенево-мастикові. Технічні умови»).', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Марка бітуму відповідає кліматичному району і вмісту бітуму затвердженому складу (Додаток А, Б ДСТУ Б В. 2.7-119 «Суміші асфальтобетонні і асфальтобетон дорожній та аеродромний. Технічні умови», затверджений склад асфальтобетону).', 7) on conflict do nothing;