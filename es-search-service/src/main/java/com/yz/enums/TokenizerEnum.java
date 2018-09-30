package com.yz.enums;

/**
 * tokenizer
 * 更多请查询官网
 *
 */
public enum TokenizerEnum {

    /**
     * 按照标准单词进分割
     */
    STANDARD(1, "standard"),

    /**
     * keyword
     */
    KEYWORD(2, "keyword"),

    /**
     * 按照非字符进行分割
     */
    LETTER(3, "letter"),

    /**
     * 按照空格进行分割
     */
    WHITESPACE(4, "whitespace"),

    /**
     * 按照standard分割,但是不会分割邮箱和url
     */
    UAX_URL_Email(5, "uax_url_email"),

    /**
     * NGram 和 Edge NGram连词分割 类似百度谷歌等搜索
     */
    NGRAM(6, "ngram"),

    /**
     * 会一次查询出来你每个字后面的词,edge_ngram只会查询出第一个词后面的词
     */
    EDGE_NGRAM(7, "edge_ngram"),

    /**
     * 按照文件路径进行分割
     */
    PATH_HIERARCHY(8, "path_hierarchy"),;

    private int code;
    private String name;

    public int getCode( ) {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName( ) {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    TokenizerEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取性别枚举对象
     *
     * @param code
     * @return
     */
    public static TokenizerEnum getTokenizerEnum(int code) {
        for (TokenizerEnum tokenizerEnum : TokenizerEnum.values()) {
            if (code == tokenizerEnum.getCode()) {
                return tokenizerEnum;
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
        for (TokenizerEnum tokenizerEnum : TokenizerEnum.values()) {
            if (code == tokenizerEnum.getCode()) {
                return tokenizerEnum.getName();
            }
        }
        return "";
    }
}
