package org.boyce.enums;

/**
 * @Author: Yuan Baiyu
 * @Date: Created in 15:49 2018/3/30
 */
public enum MessageEnum {

    SUCCESS(1, "登录成功");




    private int state;
    private String info;

    MessageEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }

    public String getInfo() {
        return info;
    }

    public static MessageEnum stateOf(int index) {
        for (MessageEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
