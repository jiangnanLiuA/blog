package com.jiangnan.controller;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.dto.AddTagDto;
import com.jiangnan.domain.dto.EditTagDto;
import com.jiangnan.domain.dto.TagListDto;
import com.jiangnan.domain.entity.Tag;
import com.jiangnan.domain.vo.PageVo;
import com.jiangnan.domain.vo.TagVo;
import com.jiangnan.service.TagService;
import com.jiangnan.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 测试接口
     *
     * @return
     */
//    @GetMapping("/list")
//    public ResponseResult list() {
//        return ResponseResult.okResult(tagService.list());
//    }
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {

        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    /**
     * 新增标签
     *
     * @param tagDto
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody AddTagDto tagDto) {
        tagService.save(BeanCopyUtils.copyBean(tagDto, Tag.class));
        return ResponseResult.okResult();
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
//    @DeleteMapping
//    public ResponseResult remove(@RequestParam(value = "ids") String ids) {
//        if (!ids.contains(",")) {
//            tagService.removeById(ids);
//        } else {
//            String[] idArr = ids.split(",");
//            for (String id : idArr) {
//                tagService.removeById(id);
//            }
//        }
//        return ResponseResult.okResult();
//    }


    /**
     * 修改标签
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    //根据标签的id来查询标签
    public ResponseResult getInfo(@PathVariable(value = "id") Long id) {
        return ResponseResult.okResult(tagService.getById(id));
    }

    @PutMapping
    //根据标签的id来修改标签
    public ResponseResult edit(@RequestBody EditTagDto editTagDto) {
        tagService.updateById(BeanCopyUtils.copyBean(editTagDto, Tag.class));
        return ResponseResult.okResult();
    }

    /**
     * 写博文-查询文章标签的接口
     *
     * @return
     */
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag() {
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}
