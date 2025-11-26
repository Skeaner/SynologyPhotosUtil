package me.skean.synologyphotosutil.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class FilesRes {
   private String msg;
   private List<String> data;


}
