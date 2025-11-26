package me.skean.synologyphotosutil.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class PhotosRes {
    private String msg;
    private List<String> deleteList;
    private List<String> keepList;

}