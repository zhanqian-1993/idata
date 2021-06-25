package cn.zhengcaiyun.idata.label.compute.query.dto;


import cn.zhengcaiyun.idata.label.compute.query.enums.ConnectionTypeEnum;

/**
 * @author shiyin(沐泽)
 * @date 2020/6/15 15:34
 */
public class ConnectionDto {
    private ConnectionTypeEnum type;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String dbCatalog;
    private String dbSchema;

    public ConnectionTypeEnum getType() {
        return type;
    }

    public void setType(ConnectionTypeEnum type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbCatalog() {
        return dbCatalog;
    }

    public void setDbCatalog(String dbCatalog) {
        this.dbCatalog = dbCatalog;
    }

    public String getDbSchema() {
        return dbSchema;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }
}
