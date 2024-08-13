package me.skean.synologyphotosutil.controller;

import java.util.List;

public final class FilesReq {
   private  String dir;
   private  List<String> originals;
   private  List<String> replacements;

   public FilesReq() {
   }

   public FilesReq(String dir, List<String> originals, List<String> replacements) {
      this.dir = dir;
      this.originals = originals;
      this.replacements = replacements;
   }

   public String getDir() {
      return dir;
   }

   public void setDir(String dir) {
      this.dir = dir;
   }

   public List<String> getOriginals() {
      return originals;
   }

   public void setOriginals(List<String> originals) {
      this.originals = originals;
   }

   public List<String> getReplacements() {
      return replacements;
   }

   public void setReplacements(List<String> replacements) {
      this.replacements = replacements;
   }
}