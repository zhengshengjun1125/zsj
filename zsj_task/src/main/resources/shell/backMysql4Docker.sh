#!/bin/bash
cur_date="`date +%Y-%m-%d`"
mkdir $cur_date
echo '当前时间为:' $cur_date
echo '开始备份数据库(zsj_system)·····'
docker exec -it 4c9c76e96c6f mysqldump -h47.108.177.31 -p3306 -uroot -pZsj@20001125 zsj_system > /opt/back/mysql/$cur_date/zsj_system.sql
echo '开始备份数据库(zsj_article)·····'
docker exec -it 4c9c76e96c6f mysqldump -h47.108.177.31 -p3306 -uroot -pZsj@20001125 zsj_article > /opt/back/mysql/$cur_date/zsj_article.sql
echo '开始备份数据库(zsj_relations)·····'
docker exec -it 4c9c76e96c6f mysqldump -h47.108.177.31 -p3306 -uroot -pZsj@20001125 zsj_relations > /opt/back/mysql/$cur_date/zsj_relations.sql
echo '开始备份数据库(nacos)·····'
docker exec -it 4c9c76e96c6f mysqldump -h47.108.177.31 -p3306 -uroot -pZsj@20001125 nacos > /opt/back/mysql/$cur_date/nacos.sql
echo '开始备份数据库(zsj_sms)·····'
docker exec -it 4c9c76e96c6f mysqldump -h47.108.177.31 -p3306 -uroot -pZsj@20001125 zsj_sms > /opt/back/mysql/$cur_date/zsj_sms.sql
echo '备份完成·····'
