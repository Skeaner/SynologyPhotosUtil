package me.skean.synologyphotosutil.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.annotation.Resource;
import me.skean.synologyphotosutil.OperationService;
import me.skean.synologyphotosutil.bean.FilesRes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文件管理")
@RestController
@RequestMapping("/files")
public class FilesController {
    @Resource
    private OperationService service;

    @ApiOperationSupport(order = 11)
    @PostMapping(value = "/listFiles", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "列出文件", description = "列出指定目录下的文件")
    public ResponseEntity<FilesRes> listFiles(
            @Parameter(description = "目录路径")
            @RequestParam("path")
            String path,

            @Parameter(description = "关键字", required = false)
            @RequestParam(value = "key", required = false)
            String key) {
        return ResponseEntity.ok(service.fileList(path, key));
    }

    @ApiOperationSupport(order = 12)
    @PostMapping(value = "/testRename", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "模拟重命名", description = "模拟文件重命名操作，不实际执行")
    public ResponseEntity<FilesRes> testRename(
            @Parameter(description = "目录路径")
            @RequestParam("path")
            String path,
            @Parameter(description = "目标字符, 可以多个逗号分割, 支持正则表达式")
            @RequestParam("target")
            String target,
            @Parameter(description = "替换字符, 可以多个逗号分割, 要跟目标字符数量匹配")
            @RequestParam("replacement")
            String replacement) {
        return ResponseEntity.ok(service.fileOperation(path, target, replacement, false));
    }

    @ApiOperationSupport(order = 13)
    @PostMapping(value = "/doRename", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "重命名", description = "实际执行文件重命名操作")
    public ResponseEntity<FilesRes> doRename(
            @Parameter(description = "目录路径")
            @RequestParam("path")
            String path,
            @Parameter(description = "目标字符, 可以多个逗号分割, 支持正则表达式")
            @RequestParam("target")
            String target,
            @Parameter(description = "替换字符, 可以多个逗号分割, 要跟目标字符数量匹配")
            @RequestParam("replacement")
            String replacement) {
        return ResponseEntity.ok(service.fileOperation(path, target, replacement, true));
    }

}
