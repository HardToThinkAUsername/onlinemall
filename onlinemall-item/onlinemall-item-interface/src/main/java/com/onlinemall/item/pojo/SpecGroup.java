package com.onlinemall.item.pojo;

import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;

    @Transient
    private List<SpecParam> paramList = new ArrayList<>();

    public void setParamList(List<SpecParam> paramList) {
        this.paramList = paramList;
    }

    public List<SpecParam> getParamList() {
        return paramList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}