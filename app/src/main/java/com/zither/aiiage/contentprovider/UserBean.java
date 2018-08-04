package com.zither.aiiage.contentprovider;

/**
 * @author wangyanqin
 * @date 2018/08/04
 */
public class UserBean {
    private String uid;
    private String name;
    private String intro;


    public UserBean() {
    }

    public UserBean(String uid, String name, String intro) {
        this.uid = uid;
        this.name = name;
        this.intro = intro;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
