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
@RequestMapping("/admin")
public class AdminController {

    private List<String> IMG_TYPES = Arrays.asList("jpg", "jpeg", "heic", "png");
    private List<String> MOV_TYPES = Arrays.asList("mov", "mpg", "mkv", "mp4");

    @GetMapping(value = "/queryDuplicate", produces = "application/json")
    @ApiOperation(value = "查询重复", produces = "application/json")
    public ResponseEntity<Res> queryDuplicate(
            @ApiParam("路径")
            @RequestParam
                    String path) {
        return ResponseEntity.ok(operation(path, false));
    }

    @GetMapping(value = "/deleteDuplicate", produces = "application/json")
    @ApiOperation(value = "删除重复", produces = "application/json")
    public ResponseEntity<Res> deleteDuplicate(
            @ApiParam("路径")
            @RequestParam
                    String path) {
        return ResponseEntity.ok(operation(path, true));
    }

    private Res operation(String path, boolean doDelete) {
        Res res = new Res("", null, null);
        Set<String> keepSet = new HashSet<>();
        List<String> deleteList = new ArrayList<>();
        File dir = new File(path);
        if (dir.exists()) {
            res.setMsg("执行成功");
            List<File> files = (List<File>) FileUtils.listFiles(dir, null, false);
            files.sort(Comparator.comparing(File::getName));
            String tmpBaseName = UUID.randomUUID().toString();
            String tmpFileExt = null;
            for (File file : files) {
                String fileName = file.getName();
                String ext = FilenameUtils.getExtension(fileName).toLowerCase();
                String baseName = FilenameUtils.getBaseName(file.getName());
                if (fileName.startsWith(".") || fileName.length() < 10 || !(IMG_TYPES.contains(ext) || MOV_TYPES.contains(ext)))
                    continue;
                if (baseName.startsWith(tmpBaseName) && isSameType(ext, tmpFileExt)) {
                    if (doDelete) FileUtils.deleteQuietly(file);
                    deleteList.add(file.getName());
                    keepSet.add(tmpBaseName + "." + tmpFileExt);
                } else {
                    tmpBaseName = baseName;
                    tmpFileExt = ext;
                }
            }
            res.setDeleteList(deleteList);
            List<String> keepList = new ArrayList<>(keepSet);
            keepList.sort(Comparator.naturalOrder());
            res.setKeepList(keepList);
        } else {
            res.setMsg("路径不存在");
        }
        return res;
    }

    private boolean isSameType(String extA, String extB) {
        return (IMG_TYPES.contains(extA) && IMG_TYPES.contains(extB)) || (MOV_TYPES.contains(extA) && MOV_TYPES.contains(extB));
    }
}
