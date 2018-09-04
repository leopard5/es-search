package com.yz.enums;

/**
 *
 */
public enum TokenFilterEnum {
    /**
     * 去掉html中的符号
     */
    LOWERCASE(1, "lowercase"),
    /**
     * 删除stop words
     */
    STOP(2, "stop"),
    /**
     * NGram 和 Edge NGram连词分割 类似百度谷歌等搜索
     */
    NGRAM(3, "ngram"),

    /**
     * 会一次查询出来你每个字后面的词,edge_ngram只会查询出第一个词后面的词
     */
    EDGE_NGRAM(4, "edge_ngram"),
    /**
     * 添加近义词
     */
    SYNONYM(5, "synonym");


    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    TokenFilterEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取性别枚举对象
     *
     * @param code
     * @return
     */
    public static TokenFilterEnum getTokenFilterEnum(int code) {
        for (TokenFilterEnum tokenFilterEnum : TokenFilterEnum.values()) {
            if (code == tokenFilterEnum.getCode()) {
                return tokenFilterEnum;
            }
        }
        return null;
    }


    /**
     * 根据code获取名称
     *
     * @param code
     * @return
     */
    public static String getNameByCode(int code) {
        for (TokenFilterEnum tokenFilterEnum : TokenFilterEnum.values()) {
            if (code == tokenFilterEnum.getCode()) {
                return tokenFilterEnum.getName();
            }
        }
        return "";
    }

}
