package com.yjxxt.exceptions;

public class PermissionException extends RuntimeException{

    private Integer code=300;
    private String msg="权限不足!";


    public PermissionException() {
        super("权限不足!");
    }

    public PermissionException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public PermissionException(Integer code) {
        super("权限不足!");
        this.code = code;
    }

    public PermissionException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
