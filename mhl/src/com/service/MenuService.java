package com.service;

import com.dao.MenuDAO;
import com.daomain.Menu;

import java.util.List;

/**
 * 完成对menu表的各种操作
 */
public class MenuService {

    private MenuDAO menuDAO=new MenuDAO();

    public List<Menu> getMenuList(){
        return menuDAO.queryMulti("select * from menu",Menu.class);
    }

    public Menu getMenuById(int id){
        return menuDAO.querySingle("select * from menu where id=?",Menu.class ,id);
    }

}
