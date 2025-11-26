package me.skean.synologyphotosutil.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import me.skean.synologyphotosutil.OperationService;
import me.skean.synologyphotosutil.bean.PhotosRes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

@Tag(name = "图片管理")
@RestController
@RequestMapping("/photos")
public class PhotosController {
    @Resource
    private OperationService service;

    @ApiOperationSupport(order = 21)
    @PostMapping(value = "/queryDuplicate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "查询重复")
    public ResponseEntity<PhotosRes> queryDuplicate(
            @Parameter(description = "目录路径")
            @RequestParam
            String path) {
        return ResponseEntity.ok(service.photoOperation(path, false));
    }

    @ApiOperationSupport(order = 25)
    @PostMapping(value = "/deleteDuplicate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "删除重复")
    public ResponseEntity<PhotosRes> deleteDuplicate(
            @Parameter(description = "目录路径")
            @RequestParam
            String path) {
        return ResponseEntity.ok(service.photoOperation(path, true));
    }

}
