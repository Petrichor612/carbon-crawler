package com.carbon.entity;

/**
 * @author zhangbo $
 * @title $
 * @description $
 * @updateTime $ 11:54$ $
 * @throws $
 */
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Policy {
    private Integer id;
    private String title;
    private String source;
    private LocalDate publishTime;
    private String keyword;
    private String type;
    private String docNumber;

    // 新增两个字段
    private String url;      // 详情链接
    private String content;  // 详情内容

    // ------------------- 下面自动生成 getter/setter -------------------
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getSource() {return source;}
    public void setSource(String source) {this.source = source;}
    public LocalDate getPublishTime() {return publishTime;}
    public void setPublishTime(LocalDate publishTime) {this.publishTime = publishTime;}
    public String getKeyword() {return keyword;}
    public void setKeyword(String keyword) {this.keyword = keyword;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public String getDocNumber() {return docNumber;}
    public void setDocNumber(String docNumber) {this.docNumber = docNumber;}

    // 新增
    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
}
