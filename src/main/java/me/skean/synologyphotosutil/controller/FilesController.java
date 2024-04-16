package me.skean.synologyphotosutil.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Api(tags = "文件管理工具")
@RestController
@RequestMapping("/photos")
public class FilesController {

    @PostMapping(value = "/testRename", produces = "application/json", consumes = "application/json")
    @ApiOperation(value = "测试重命名", produces = "application/json", consumes = "application/json")
    public ResponseEntity<FilesRes> testRename(
            @ApiParam("路径")
            @RequestBody
                    FilesReq req) throws IOException {
        return ResponseEntity.ok(operation(req, false));
    }

    @PostMapping(value = "/doRename", produces = "application/json", consumes = "application/json")
    @ApiOperation(value = "重命名", produces = "application/json", consumes = "application/json")
    public ResponseEntity<FilesRes> doRename(
            @ApiParam("路径")
            @RequestBody
                    FilesReq req) throws IOException {
        return ResponseEntity.ok(operation(req, true));
    }

    private FilesRes operation(FilesReq req, boolean actualRename) throws IOException {
        List<String> msg = new ArrayList<>();
        if (StringUtils.isEmpty(req.getDir())) {
            msg.add("没有指定父路径");
            return new FilesRes(false, msg);
        }
        File dir = new File(req.getDir());
        if (!dir.exists() || !dir.isDirectory()) {
            msg.add("父路径不存在/父路径非文件夹");
            return new FilesRes(false, msg);
        }
        if (req.getOriginals() == null || req.getOriginals().size() == 0 || req.getReplacements() == null || req.getReplacements()
                                                                                                                .size() == 0) {
            msg.add("没有指定替换内容");
            return new FilesRes(false, msg);
        }
        if (req.getOriginals().size() != req.getReplacements().size()) {
            msg.add("替换内容不匹配");
            return new FilesRes(false, msg);
        }
        List<File> files = (List<File>) FileUtils.listFiles(dir, null, false);
        for (File file : files) {
            String destFilePath = file.getAbsolutePath();
            for (int i = 0; i < req.getOriginals().size(); i++) {
                String original = req.getOriginals().get(i);
                String replacement = req.getReplacements().get(i);
                destFilePath = destFilePath.replace(original, replacement);
            }
            File destFile = new File(destFilePath);
            msg.add(file.getName() + "->" + destFile.getName());
            if (actualRename) {
                FileUtils.moveFile(file, destFile);
            }
        }
        return new FilesRes(true, msg);
    }

}
