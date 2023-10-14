package com.zsj.article.vo;

import com.zsj.article.entity.ClassEntity;
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
public class ClassVoForTreeForElement {

    //当前对象自己的id
    private String value;

    //当前对象自己的名称
    private String label;

    //当前对象的创建者
    private String creater;

    //当前对象的孩子
    //且这个孩子也有自己的孩子
    private List<ClassVoForTreeForElement> children;

    public ClassVoForTreeForElement(String value, String label,String creater, List<ClassVoForTreeForElement> child) {
        this.creater =creater;
        this.value = value;
        this.label = label;
        this.children = child;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ClassVoForTreeForElement> getChild() {
        return children;
    }

    public void setChild(List<ClassVoForTreeForElement> child) {
        this.children = child;
    }
}
