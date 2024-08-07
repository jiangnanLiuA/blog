package com.jiangnan.constants;

//字面值(代码中的固定值)处理，把字面值都在这里定义成常量
public class SystemConstants {

    /**
     * 文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;

    /**
     * 文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 文章列表当前查询页数
     */
    public static final int ARTICLE_STATUS_CURRENT = 1;

    /**
     * 文章列表每页显示的数据条数
     */
    public static final int ARTICLE_STATUS_SIZE = 10;

    /**
     * 文章状态正常
     */
    public static final String STATUS_NORMAL = "0";

    /**
     * 文章状态异常
     */
    public static final String STATUS_ERROR = "1";

    /**
     * 友链审核已通过
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     * 评论为根评论
     */
    public static final String ROOT_COMMENT = "-1";

    /**
     * 文章评论
     */
    public static final String ARTICLE_COMMENT = "0";

    /**
     * 友链评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * 文章浏览量
     */
    public static final String ARTICLE_VIEW_COUNT = "article:viewCount";

    /**
     * 权限类型  菜单
     */
    public static final String MENU = "C";

    /**
     * 权限类型  按钮
     */
    public static final String BUTTON = "F";

    /**
     * 正常状态
     */
    public static final String NORMAL = "0";

    /**
     * 判断为管理员用户
     */
    public static final String IS_ADMIN = "1";

}
