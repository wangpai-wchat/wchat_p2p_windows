package org.wangpai.wchat.universal.protocol.internal.persistence;

public enum OrderBy {
    DESC("DESC"),
    ASC("ASC");

    private String sqlStat;

    OrderBy(String sqlStatement) {
        this.sqlStat = sqlStatement;
    }

    @Override
    public String toString() {
        return this.sqlStat;
    }
}
