package com.yz.enums;

/**
 * charFilter
 * 更多请查询官网
 *
 */
public enum CharFilterEnum {

    /**
     * 去掉html中的符号
     */
    HTML_STRIP(1, "html_strip");


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

    CharFilterEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取性别枚举对象
     *
     * @param code
     * @return
     */
    public static CharFilterEnum getCharFilterEnum(int code) {
        for (CharFilterEnum charFilterEnum : CharFilterEnum.values()) {
            if (code == charFilterEnum.getCode()) {
                return charFilterEnum;
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
        for (CharFilterEnum charFilterEnum : CharFilterEnum.values()) {
            if (code == charFilterEnum.getCode()) {
                return charFilterEnum.getName();
            }
        }
        return "";
    }
}
