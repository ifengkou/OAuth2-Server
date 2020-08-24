
## 初始化项目

### 初始化数据库

```sql
CREATE DATABASE IF NOT EXISTS oauth2_server DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
create user 'oauth2_server'@'localhost' identified by 'password_dev';
grant all privileges on oauth2_server.* to 'oauth2_server'@'localhost';
```

### 生成RSA 密钥(更换过程)

1. 生成密钥

```bash
keytool -genkeypair -alias jwt -keyalg RSA -keypass key_password -keystore jwt.jks -storepass  key_password
```


2. 获取密钥的公钥 和 证书

```bash
keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey
#输入上面的密钥库口令
```
