@echo off
:: MySQL账户的用户名和密码，根据实际情况修改
chcp 65001
echo 请输入账号:
set /p userName=
echo 请输入密码:
set /p password=
set time=%date:~3,4%-%date:~8,2%-%date:~11,2%
:: mysqldump.exe的路径，根据实际情况修改
set dumpPath57="C:\Program Files\MySQL\MySQL Server 5.7\bin\mysqldump.exe"
set dumpPath80="F:\DevFramework\mysql8.0\bin\mysqldump.exe"
:: 要备份的数据库名，根据实际情况修改
set Nacos="nacos"
set System="zsj_system"
set article="zsj_article"
set relations="zsj_relations"
set sms="zsj_sms"
:: 备份文件的保存路径，根据实际情况修改
set baseDir="H:\zsj\zsj\sqlback\"
echo create dir %baseDir%%time%.....
md %baseDir%%time%
set filePath1="H:\zsj\zsj\sqlback\%time%\"%Nacos%".sql"
set filePath2="H:\zsj\zsj\sqlback\%time%\"%System%".sql"
set filePath3="H:\zsj\zsj\sqlback\%time%\"%article%".sql"
set filePath4="H:\zsj\zsj\sqlback\%time%\"%relations%".sql"
set filePath5="H:\zsj\zsj\sqlback\%time%\"%sms%".sql"

:: 执行备份命令
%dumpPath57% -u%userName% -p%password% %Nacos%  > %filePath1%
%dumpPath80% -P3307 -u%userName% -p%password% %System%  > %filePath2%
%dumpPath80% -P3307 -u%userName% -p%password% %article%  > %filePath3%
%dumpPath80% -P3307 -u%userName% -p%password% %relations%  > %filePath4%
%dumpPath80% -P3307 -u%userName% -p%password% %sms%  > %filePath5%
:: 提示备份完成
echo MySQL database "%Nacos%" has been backed up successfully to "%filePath1%"
echo MySQL database "%System%" has been backed up successfully to "%filePath2%"
echo MySQL database "%article%" has been backed up successfully to "%filePath3%"
echo MySQL database "%relations%" has been backed up successfully to "%filePath4%"
echo MySQL database "%sms%" has been backed up successfully to "%filePath5%"
pause