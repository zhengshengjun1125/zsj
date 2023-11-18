package com.zsj.sms.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zsj.sms.entity.NoteEntity;
import com.zsj.sms.service.NoteService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;



/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-17 15:37:42
 */
@RestController
@RequestMapping("sms/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = noteService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		NoteEntity note = noteService.getById(id);

        return R.ok().put("note", note);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody NoteEntity note){
		noteService.save(note);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody NoteEntity note){
		noteService.updateById(note);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids){
		noteService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
