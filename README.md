
## 初始化项目

### 初始化数据库

```sql
CREATE DATABASE IF NOT EXISTS oauth2_server DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
create user 'oauth2_server'@'localhost' identified by 'password_dev';
grant all privileges on oauth2_server.* to 'oauth2_server'@'localhost';
```