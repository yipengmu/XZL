CREATE TABLE IF NOT EXISTS item(_id INTEGER PRIMARY KEY, refer_type INTEGER,refer_id INTEGER,date TIMESTAMP);
CREATE TABLE IF NOT EXISTS note(_id INTEGER PRIMARY KEY,topic TEXT,contact TEXT,content TEXT,started INTEGER,ended INTEGER,befored INTEGER,iscancel INTEGER,repeat_type INTEGER);
CREATE TABLE IF NOT EXISTS gua(_id INTEGER PRIMARY KEY, type INTEGER,title TEXT,date TEXT,time TEXT,doubleNumID INTEGER,body TEXT,yao TEXT,result TEXT,inference TEXT);
CREATE TABLE IF NOT EXISTS gua_tj(_id INTEGER PRIMARY KEY, type INTEGER,cnt INTEGER,all_cnt INTEGER, next_time INTEGER,xz_id INTEGER);
insert into gua_tj values(1,1,0,1,0,1);
insert into gua_tj values(2,2,0,1,0,1);
insert into gua_tj values(3,3,0,1,0,1);
insert into gua_tj values(4,4,0,1,0,1);
insert into gua_tj values(5,5,0,1,0,1);
insert into gua_tj values(6,6,0,1,0,2);
insert into gua_tj values(7,7,0,1,0,1);
insert into gua_tj values(8,8,0,1,0,1);
insert into gua_tj values(9,9,0,1,0,1);
insert into gua_tj values(10,10,0,1,0,2);
insert into gua_tj values(11,11,0,1,0,1);
insert into gua_tj values(12,12,0,1,0,1);
insert into gua_tj values(13,13,0,1,0,1);
insert into gua_tj values(14,14,0,1,0,1);
insert into gua_tj values(15,15,0,1,0,1);
insert into gua_tj values(16,16,0,1,0,1);
insert into gua_tj values(17,17,0,1,0,1);
insert into gua_tj values(18,18,0,1,0,1);
insert into gua_tj values(19,19,0,1,0,1);
insert into gua_tj values(20,20,0,1,0,1);
insert into gua_tj values(21,21,0,1,0,1);
insert into gua_tj values(22,22,0,1,0,2);
insert into gua_tj values(23,23,0,1,0,3);
insert into gua_tj values(24,24,0,1,0,1);
insert into gua_tj values(25,25,0,1,0,1);
insert into gua_tj values(26,26,0,1,0,1);
insert into gua_tj values(27,27,0,1,0,1);
insert into gua_tj values(28,28,0,1,0,1);
insert into gua_tj values(29,29,0,1,0,1);
insert into gua_tj values(30,30,0,1,0,1);
insert into gua_tj values(31,31,0,1,0,1);
insert into gua_tj values(32,32,0,1,0,1);
insert into gua_tj values(33,33,0,1,0,1);
insert into gua_tj values(34,34,0,1,0,1);
insert into gua_tj values(35,35,0,1,0,1);
insert into gua_tj values(36,36,0,1,0,1);
insert into gua_tj values(37,37,0,1,0,1);
insert into gua_tj values(38,38,0,1,0,1);
insert into gua_tj values(39,39,0,1,0,1);
insert into gua_tj values(40,40,0,1,0,1);
insert into gua_tj values(41,41,0,1,0,2);
insert into gua_tj values(42,42,0,1,0,1);
insert into gua_tj values(43,43,0,1,0,1);
insert into gua_tj values(44,44,0,1,0,1);
insert into gua_tj values(45,45,0,1,0,1);
insert into gua_tj values(46,46,0,1,0,1);
insert into gua_tj values(47,47,0,1,0,1);
