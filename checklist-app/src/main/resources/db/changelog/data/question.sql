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

-- dasu checklist
insert into question(value, answer_structure_id)
values ('Аудитор', 10)
on conflict do nothing;

insert into question(value, answer_structure_id)
values ('Короткий коментар з приводу спірних питань. Загальне резюме огляду', 1)
on conflict do nothing;

insert into question(value, answer_structure_id)
values ('Дата створення', 9)
on conflict do nothing;

insert into question(value, answer_structure_id) values ('Тип шаблону контрольного листа', 2) on conflict do nothing;

-- ukr-avto-dor
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
insert into question(value, answer_structure_id) values ('Випадки завищення вартості виконаних робіт з інших причин відсутні', 7) on conflict do nothing;



-- simple checklist
insert into question(value, answer_structure_id)
values ('Використанi кошти', 8)
on conflict do nothing;

insert into question(value, answer_structure_id)
values ('в т.ч. сума використаних коштів, виділених з субвенції на подолання COVID 19', 8)
on conflict do nothing;

insert into question(value, answer_structure_id) values ('Моніторинг закупівлі було здійснено без встановлених порушень', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Наявність документів, що свідчать про обґрунтування визначення потреби закупівлі предмета закупівлі', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Наявність документів, що свідчать про обґрунтування технічних та якісних характеристик предмета закупівлі, розміру бюджетного призначення, очікуваної вартості предмета закупівлі;', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримано вимог законодавства у сфері публічних закупівель при складанні тендерної документації у випадках, визначених законодавством', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримання вимог законодавства у сфері публічних закупівель при застосуванні процедури закупівлі / спрощеної закупівлі', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримання законодавства у сфері публічних закупівель при публікації звіту про договір про закупівлю, укладеного без використання електронної системи закупівель', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримання законодавства у сфері публічних закупівель щодо повернення/ не повернення забезпечення тендерної пропозиції / пропозиції (у випадках передбачених частиною третьою, четвертою статті 25 Закону України "Про публічні закупівлі" (далі - Закон)', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Відповідність умов договору умовам тендерної пропозиції переможця', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Відповідність технічних та якісних характеристик предмета закупівлі вимогам тендерної документації (у разі проведення закупівлі за переговорною процедурою питання досліджується за умови застосування пункту першого частини другої статті 40 Закону)', 7) on conflict do nothing;

insert into question(value, answer_structure_id) values ('Дотримання законодавства щодо внесення змін до договору (наявність підстав)', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Замовник дотримувався законодавства при внесені змін до істотних умов договору (предмер закупівлі, ціна та срок дії договору).', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Замовник дотримувався законодавства при внесені змін до інших умов договору.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Замовник дотримувався законодавства при збільшені ціни за одиницю товару', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Замовник дотримувався вимог Закону 922 при оприлюдненні інформації про внесення змін до договору', 7) on conflict do nothing;

insert into question(value, answer_structure_id) values ('Дотримання сторонами умов договору при його виконанні', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Документальне підтвердження кількості (обсягу) та вартості поставлених товарів, виконаних робіт або послуг у відповідності до умов договору', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримання переможцем гарантійних зобовязань за договором', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Документальне підтвердження якості поставлених товарів, виконаних робіт у відповідності до умов договору', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Поставлені товари, виконані роботи або послуги відповідають якісним та технічним характеристикам запропонованим переможцем в його тендерній пропозиції (наприклад наявність сертифікатів якості, свідоцтв відповідності, тощо)', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Умови договору виконуються/виконані Замовником за відсутності претензійно-позовної роботи та без застосування штрафних санкцій з боку Постачальника', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Умови договору виконуються/виконані Постачальником за відсутності претензійно-позовної роботи та без застосування штрафних санкцій з боку Замовника', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Постачання товару, виконання робіт та послуг здійснено до проведення замовником повної (100 %) оплати', 7) on conflict do nothing;

insert into question(value, answer_structure_id) values ('Фактичне залучення субпідрядних організацій під час виконання договору в обсязі більше 20% від вартості договору про закупівлі і у відповідності до умов ТД та тендерної пропозиції про заплановане залучення субпідрядної організації під час виконання договору про закупівлю.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Фактичне залучення субпідрядних організацій під час виконання договору в обсязі менше 20% від вартості договору про закупівлі і у відповідності до умов ТД та тендерної пропозиції про заплановане залучення субпідрядної організації під час виконання договору про закупівлю.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Відсутня необхідність у проведенні зустрічної звірки', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('До проведення зустрічної звірки посадових осіб Держаудитслужби допущено', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Обєкт зустірчної звірки знаходиться за фактичною та/або юридичною адресою, зазначених у договорі', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Обєктом зустрічної звірки надіслано Держаудитслужбі запитувану інформацію та документи у повному обсязі', 7) on conflict do nothing;

insert into question(value, answer_structure_id) values ('Не виявлено лишків при здійснення інвентаризації', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Не виявлено нестачі при здійснення інвентаризації', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Проведені контрольні обміри не встановили завищення виконаних робіт', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Замовник сприяв здійсненню контрольних обмірів та не застосовував перешкоди', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('При проведеному обстеженні встановлено відповідність якості поставлених товарів, наданих послуг, виконаних робіт умовам договору та/або тендерної пропозиції', 7) on conflict do nothing;

insert into question(value, answer_structure_id) values ('Вартість виконаних робіт справедлива, та завищення відсутні', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Роботи передбачені договором виконані', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Технологічні процеси при виконанні ремонтно-будівельних робіт дотримані', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримано законодавства у сфері публічних закупівель при оприлюдненні інформації про виконання договору (звіт про виконання договору)', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримано законодавства при відображенні в бухгалтерському обліку замовника розрахунків за договором', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримано законодавства з питання своєчасності, повноти та правильності оприбуткування придбаних товарно-матеріальних цінностей, робіт та послуг', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Умовами тендерної документації/оголошення про проведення спрощеної закупівлі передбачено надання забезпечення виконання договору', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримано законодавства та умов договору при внесенні переможцем процедури закупівлі/ спрощеної закупівлі забезпечення виконання договору про закупівлю', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Дотримано законодавства та умов договору при поверненні/неповернення замовником забезпечення виконання договору та подальшого перерахування до відповідного бюджету (або на рахунок замовника)', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Відсутня необхідність у спрямуванні матеріалів до правоохоронних органів.', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Вартість придбаних товарів не перевищує середньоринкові ціни по регіону', 7) on conflict do nothing;
insert into question(value, answer_structure_id) values ('Придбаний товар Замовник не передав на відповідальне зберігання іншому суб’єкту господарювання', 7) on conflict do nothing;