package com.yz.enums;

/**
 * es分词
 * 更多请查询官网
 *
 */
public enum AnalyzerEnum {

    /**
     * 不分词
     * 结果同 STANDARD
     */
    DEFUALT(0, ""),

    /**
     * 标准分词,按词切分,字母小写,无下划线
     * heLLo World  2018 _   ----> hello, world ,2018
     */
    STANDARD(1, "standard"),

    /**
     * 按照非字符切分,全部小写,数字和下划线等不会分词
     * heLLo World  2018 _   ----> hello, world ,2018
     */
    SIMPLE(2, "simple"),

    /**
     * 按照空格切分,不区分大小写,有数字和下划线等
     * heLLo World  2018 _   ----> heLLo, World ,2018,_
     */
    WHITESPACE(3, "whitespace"),

    /**
     * stop word指语气助词等修饰性的词语,比如the,an,的,这等等
     * 在simple的基础上多了stop word的使用
     */
    STOP(4, "stop"),

    /**
     * 不分词,不想对文本进行分词的时候使用
     * 对整个词当做一个关键字处理
     */
    KEYWORD(5, "keyword"),

    /**
     * 通过正则表达式区分
     * 默认是\w+,即非字词的符号作为分隔符
     * <p>
     * the heLLo 'World  -2018  ---> the,hello,world,2018
     */
    PATTERN(6, "pattern"),

    /**
     * ik分词器
     */
    IK(7, "ik_max_word");


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

    AnalyzerEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取性别枚举对象
     *
     * @param code
     * @return
     */
    public static AnalyzerEnum getAnalyzerEnum(int code) {
        for (AnalyzerEnum analyzerEnum : AnalyzerEnum.values()) {
            if (code == analyzerEnum.getCode()) {
                return analyzerEnum;
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
        for (AnalyzerEnum analyzerEnum : AnalyzerEnum.values()) {
            if (code == analyzerEnum.getCode()) {
                return analyzerEnum.getName();
            }
        }
        return "";
    }
}
