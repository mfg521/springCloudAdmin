package com.mfg.auth.admin.vo;

import com.mfg.auth.common.vo.TreeNode;

/**
 * @author mengfanguang
 * @create 2018-12-12
 */
public class GroupTree extends TreeNode {
    String label;

    public String getLabel(){return label;}

    public void setLabel(String label){
        this.label=label;
    }

    String name;

    public String getName(){return name;}

    public void setName(String name){
        this.name=name;
    }
}
