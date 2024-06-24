package com.jiangnan.utils;

import com.jiangnan.domain.entity.Article;
import com.jiangnan.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {

    }

    //public static <V> Object copyBean(Object source, Class<V> cls) {
    public static <V> V copyBean(Object source, Class<V> cls) {

        //创建目标对象
        V result = null;
        try {
            //创建目标对象
            result = cls.newInstance();
            //实现属性拷贝
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> cls) {
//        for (Object lists : list) {
//            V v = copyBean(list, cls);
//            list.add(v);
//        }
//        return lists;

        //stream流
        return list.stream()
                .map(o -> copyBean(o, cls))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Andy");

        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
        System.out.println(hotArticleVo);
    }
}
