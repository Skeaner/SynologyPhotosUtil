package me.skean.synologyphotosutil.controller;

import java.util.List;

public final class PhotosRes {
    private String msg;
    private List<String> deleteList;
    private List<String> keepList;

    public PhotosRes() {
    }

    public PhotosRes(String msg, List<String> deleteList, List<String> keepList) {
        this.msg = msg;
        this.deleteList = deleteList;
        this.keepList = keepList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getDeleteList() {
        return deleteList;
    }

    public void setDeleteList(List<String> deleteList) {
        this.deleteList = deleteList;
    }

    public List<String> getKeepList() {
        return keepList;
    }

    public void setKeepList(List<String> keepList) {
        this.keepList = keepList;
    }
}