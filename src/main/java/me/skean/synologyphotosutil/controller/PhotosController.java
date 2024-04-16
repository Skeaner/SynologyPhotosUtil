package me.skean.synologyphotosutil.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

@Api(tags = "图片管理工具")
@RestController
@RequestMapping("/photos")
public class PhotosController {

    private List<String> IMG_TYPES = Arrays.asList("jpg", "jpeg", "heic", "png");
    private List<String> MOV_TYPES = Arrays.asList("mov", "mpg", "mkv", "mp4");

    @GetMapping(value = "/queryDuplicate", produces = "application/json")
    @ApiOperation(value = "查询重复", produces = "application/json")
    public ResponseEntity<PhotosRes> queryDuplicate(
            @ApiParam("路径")
            @RequestParam
                    String path) {
        return ResponseEntity.ok(operation(path, false));
    }

    @GetMapping(value = "/deleteDuplicate", produces = "application/json")
    @ApiOperation(value = "删除重复", produces = "application/json")
    public ResponseEntity<PhotosRes> deleteDuplicate(
            @ApiParam("路径")
            @RequestParam
                    String path) {
        return ResponseEntity.ok(operation(path, true));
    }

    private PhotosRes operation(String path, boolean actualDelete) {
        PhotosRes res = new PhotosRes("", null, null);
        List<String> keepList = new ArrayList<>();
        List<String> deleteList = new ArrayList<>();
        File dir = new File(path);
        if (dir.exists()) {
            res.setMsg("执行成功");
            List<File> files = (List<File>) FileUtils.listFiles(dir, null, false);
            files.sort(Comparator.comparing(File::getName));
            String groupName = UUID.randomUUID().toString();
            List<File> picGroups = new ArrayList<>();
            List<File> movGroups = new ArrayList<>();
            List<File> otherGroups = new ArrayList<>();
            for (File file : files) {
                String fileName = file.getName();
                String ext = FilenameUtils.getExtension(fileName).toLowerCase();
                String baseName = FilenameUtils.getBaseName(file.getName());
                //以点开头的文件或者名字太短的文件排除
                if (fileName.startsWith(".") || fileName.length() < 12) {
                    continue;
                }
                if (baseName.startsWith(groupName)) { //同一组的, 进行判断分类
                    putInGroup(file, ext, picGroups, movGroups, otherGroups);
                } else {
                    //  当切换组的时候, 进行操作
                    handle(deleteList, keepList, picGroups, movGroups, otherGroups, actualDelete);
                    //清空缓存
                    picGroups.clear();
                    movGroups.clear();
                    otherGroups.clear();
                    //加进新的组
                    groupName = baseName;
                    putInGroup(file, ext, picGroups, movGroups, otherGroups);
                }
            }
            //最后一组的操作
            handle(deleteList, keepList, picGroups, movGroups, otherGroups, actualDelete);
            res.setDeleteList(deleteList);
            res.setKeepList(keepList);
        } else {
            res.setMsg("路径不存在");
        }
        return res;
    }

    private void putInGroup(File file, String ext, List<File> picGroups, List<File> movGroups, List<File> otherGroups) {
        if (IMG_TYPES.contains(ext)) {
            picGroups.add(file);
        } else if (MOV_TYPES.contains(ext)) {
            movGroups.add(file);
        } else {
            otherGroups.add(file);
        }
    }

    private void handle(List<String> deleteList, List<String> keepList, List<File> picGroups, List<File> movGroups,
                        List<File> otherGroups, boolean actualDelete) {
        if (picGroups.size() > 1 || movGroups.size() > 1) {
            if (picGroups.size() > 1) {
                for (int i = 0; i < picGroups.size(); i++) {
                    if (i == 0) keepList.add(picGroups.get(i).getName());
                    else {
                        File file = picGroups.get(i);
                        if (actualDelete) {
                            FileUtils.deleteQuietly(file);
                        }
                        deleteList.add(file.getName());
                    }
                }
            }
            if (movGroups.size() > 1) {
                for (int i = 0; i < movGroups.size(); i++) {
                    if (i == 0) keepList.add(movGroups.get(i).getName());
                    else {
                        File file = movGroups.get(i);
                        if (actualDelete) {
                            FileUtils.deleteQuietly(file);
                        }
                        deleteList.add(file.getName());
                    }
                }
            }
            for (File other : otherGroups) {
                keepList.add(other.getName());
            }
        }
    }
}
