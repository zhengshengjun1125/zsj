package com.zsj.article.vo;

import com.zsj.article.entity.ClassEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/8.
 */
@Data
@NoArgsConstructor
@ToString
public class ClassVoForTree {

    //当前对象自己
    private ClassEntity self;

    //当前对象的孩子
    //且这个孩子也有自己的孩子
    private List<ClassVoForTree> child;

    public ClassVoForTree(ClassEntity self, List<ClassVoForTree> child) {
        this.self = self;
        this.child = child;
    }

    public ClassEntity getSelf() {
        return self;
    }

    public void setSelf(ClassEntity self) {
        this.self = self;
    }

    public List<ClassVoForTree> getChild() {
        return child;
    }

    public void setChild(List<ClassVoForTree> child) {
        this.child = child;
    }
}
