package cn.com.infocloud.client;

public enum DomainEnum {
    DEFAULT("https://opassapi.infocloud.cc/"),
    IP_PORT("http://39.102.205.32:35172/");

    private final String domain;

    private DomainEnum(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return this.domain;
    }

    @Override
    public String toString() {
        return this.domain;
    }
}
