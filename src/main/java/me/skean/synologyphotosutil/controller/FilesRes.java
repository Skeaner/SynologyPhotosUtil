package me.skean.synologyphotosutil.controller;


import java.util.List;

public final class FilesRes {
   private boolean success;
   private List<String> msg;

   public FilesRes() {
   }

   public FilesRes(boolean success, List<String> msg) {
      this.success = success;
      this.msg = msg;
   }

   public boolean isSuccess() {
      return success;
   }

   public void setSuccess(boolean success) {
      this.success = success;
   }

   public List<String> getMsg() {
      return msg;
   }

   public void setMsg(List<String> msg) {
      this.msg = msg;
   }
}
