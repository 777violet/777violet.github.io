package com.service;

import com.dao.DiningTableDAO;
import com.daomain.DiningTable;

import java.util.List;

public class DiningTableService {

    private DiningTableDAO diningTableDAO=new DiningTableDAO();

    //获取餐桌状态集合
    public List<DiningTable> list(){
        List<DiningTable> diningTables = diningTableDAO.queryMulti("select id, state from diningTable", DiningTable.class);
        return diningTables;
    }

    //根据id 查询对应餐桌DiningTable 对象
    //若返回null， 则表示id对应的餐桌不存在
    public DiningTable getDiningTableById(int id){
        DiningTable diningTable = diningTableDAO.querySingle("select * from diningTable where id=?", DiningTable.class, id);
        return diningTable;
    }

    //若餐桌可预订，调用该方法对其状态更新（包括预定人名字和电话）
    public boolean orderDiningTable(int id,String orderName,String orderTel){
        int update = diningTableDAO.update("update diningTable set state='已经预定', orderName=?, orderTel=? where id=?", orderName, orderTel, id);
        return update > 0;
    }

    //更新餐桌状态
    public boolean updateDiningTableState(int id,String state){
        int update = diningTableDAO.update("update diningTable set state=? where id = ?", state, id);
        return update>0;
    }

    //提供方法，将指定的餐桌设定为初始状态
    public boolean updateDiningTableToFree(int diningTableId,String state){
        int update = diningTableDAO.update("update diningTable set state=?, orderName='', orderTel='' where id=? ", state, diningTableId);
        return update>0;
    }

}
