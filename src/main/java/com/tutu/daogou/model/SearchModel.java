package com.tutu.daogou.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchModel {

    private String inputSearchKey;

    private List<String> searcKey;
}
