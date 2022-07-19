package com.xuanwu.ipaas.plugin.mail.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
    public String fromAdd;

    public String toAdd;

    public String title;

    public String content;

    public String img;

    public String prop;
}
