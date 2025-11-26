package me.skean.synologyphotosutil;

import lombok.SneakyThrows;
import me.skean.synologyphotosutil.bean.FilesRes;
import me.skean.synologyphotosutil.bean.PhotosRes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Service
public class OperationService {

    private final List<String> IMG_TYPES = Arrays.asList("jpg", "jpeg", "heic", "png");
    private final List<String> MOV_TYPES = Arrays.asList("mov", "mpg", "mkv", "mp4");

    public FilesRes fileList(String path, String key) {
        List<String> data = new ArrayList<>();
        if (StringUtils.isBlank(path)) {
            return new FilesRes("没有指定路径", data);
        }
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return new FilesRes("父路径不存在/父路径非文件夹", data);
        }
        List<File> files = (List<File>) FileUtils.listFiles(dir, new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return file != null && accept(file, file.getName());
            }

            @Override
            public boolean accept(File dir, String name) {
                return StringUtils.isBlank(key) || name.toLowerCase().contains(key.toLowerCase());
            }
        }, null);
        for (File file : files) {
            data.add(file.getName());
        }
        return new FilesRes("执行路径" + dir.getAbsolutePath(), data);
    }

    @SneakyThrows
    public FilesRes fileOperation(String path, String target, String replacement, boolean actualRename) {
        List<String> data = new ArrayList<>();
        if (StringUtils.isBlank(path)) {
            return new FilesRes("没有指定父路径", data);
        }
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            return new FilesRes("父路径不存在/父路径非文件夹", data);
        }
        if (StringUtils.isBlank(target) || StringUtils.isBlank(replacement)) {
            return new FilesRes("没有指定替换内容", data);
        }
        String[] originals = target.split(",");
        String[] replacements = replacement.split(",");
        if (originals.length != replacements.length) {
            return new FilesRes("替换内容数目不匹配", data);
        }
        List<File> files = (List<File>) FileUtils.listFiles(dir, null, false);
        for (File file : files) {
            String oldName = file.getName();
            String newName = oldName;
            for (int i = 0; i < originals.length; i++) {
                String original = originals[i];
                String replace = replacements[i];
                newName = oldName.replaceAll(original, replace);
            }
            data.add(file.getName() + " 改为 " + newName);
            if (actualRename) {
                File destFile = new File(file.getParent(), newName);
                FileUtils.moveFile(file, destFile);
            }
        }
        return new FilesRes("执行路径" + dir.getAbsolutePath(), data);
    }

    public PhotosRes photoOperation(String path, boolean actualDelete) {
        List<String> keepList = new ArrayList<>();
        List<String> deleteList = new ArrayList<>();
        PhotosRes res = new PhotosRes("", deleteList, keepList);
        if (StringUtils.isBlank(path)) {
            res.setMsg("没有指定父路径");
            return res;
        }
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            res.setMsg("父路径不存在/父路径非文件夹");
            return res;
        }
        List<File> files = (List<File>) FileUtils.listFiles(dir, null, false);
        files.sort(Comparator.comparing(File::getName));
        String groupName = UUID.randomUUID().toString();
        List<File> picGroups = new ArrayList<>();
        List<File> movGroups = new ArrayList<>();
        List<File> otherGroups = new ArrayList<>();
        //切换组操作
        Runnable switchGroupingAction = () -> {
            if (!picGroups.isEmpty() || !movGroups.isEmpty()) {
                if (!picGroups.isEmpty()) {
                    keepList.add(picGroups.get(0).getName());
                    deleteList.addAll(picGroups.stream().skip(1).map(f -> {
                        if (actualDelete) FileUtils.deleteQuietly(f);
                        return f.getName();
                    }).toList());
                }
                if (!movGroups.isEmpty()) {
                    keepList.add(movGroups.get(0).getName());
                    deleteList.addAll(movGroups.stream().skip(1).map(f -> {
                        if (actualDelete) FileUtils.deleteQuietly(f);
                        return f.getName();
                    }).toList());
                }
                keepList.addAll(otherGroups.stream().map(File::getName).toList());
            }
            //清空缓存
            picGroups.clear();
            movGroups.clear();
            otherGroups.clear();
        };//
        for (File file : files) {
            String fileName = file.getName();
            //以点开头的文件或者名字太短的文件排除
            if (fileName.startsWith(".") || fileName.length() < 12) {
                continue;
            }
            //不带后缀的文件名作为组名, 例如 IMG_20230303_12
            String baseName = FilenameUtils.getBaseName(file.getName());
            //分组操作, 以文件后缀进行分组操作
            BiConsumer<File, String> groupingAction = (targetFile, filename) -> {
                String ext = FilenameUtils.getExtension(filename).toLowerCase();
                if (IMG_TYPES.contains(ext)) {
                    picGroups.add(targetFile);
                } else if (MOV_TYPES.contains(ext)) {
                    movGroups.add(targetFile);
                } else {
                    otherGroups.add(targetFile);
                }
            };

            if (baseName.startsWith(groupName)) { //同一组的, 进行文件判断分类
                groupingAction.accept(file, fileName);
            } else {
                //  当切换组的时候, 进行操作
                switchGroupingAction.run();
                //加进新的组
                groupName = baseName;
                groupingAction.accept(file, fileName);
            }
        }
        //最后一组的操作
        switchGroupingAction.run();
        res.setMsg("执行路径" + dir.getAbsolutePath());
        return res;
    }
}
