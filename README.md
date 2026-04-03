install XAMPP for database.



Open XAMPP and start Apache and MySQL then click on Admin beside MySQL the in the database
Run these commands in MySQL XAMPP shell:
CREATE DATABASE firs;
CREATE USER 'root'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON firs.* TO 'root'@'localhost';

FLUSH PRIVILEGES;
✅ Then update src/main/resources/application.properties
spring.datasource.username=root
spring.datasource.password=1234


Set password in phpMyAdmin
📍 Go to this file:
xampp/phpMyAdmin/config.inc.php
(or wherever phpMyAdmin is installed)

🔍 Find this line:
$cfg['Servers'][$i]['user'] = 'root';
$cfg['Servers'][$i]['password'] = '';

✏️ Change it to your real password:
$cfg['Servers'][$i]['user'] = 'root';
$cfg['Servers'][$i]['password'] = '1234';
(put your actual MySQL password)
🔄 Restart XAMPP (Apache + MySQL)


