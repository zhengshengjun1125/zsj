package com.zsj.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.article.vo.ClassVoForTree;
import com.zsj.article.vo.ClassVoForTreeForElement;
import com.zsj.common.utils.PageUtils;
import com.zsj.article.entity.ClassEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-07 20:47:17
 */
public interface ClassService extends IService<ClassEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ClassVoForTree> listTree();

    List<ClassVoForTreeForElement> listTree4ELE();

    boolean zccg(Long id);

    List<ClassEntity> getChildById(Long id);
}

