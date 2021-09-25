package com.service;

import com.dao.BillDAO;
import com.daomain.Bill;

import java.util.List;
import java.util.UUID;

/**
 *处理和账单相关的业务逻辑
 */
public class BillService {

    private BillDAO billDAO=new BillDAO();
    private MenuService menuService=new MenuService();
    private DiningTableService diningTableService=new DiningTableService();

    //编写点餐方法
    //1. 生成账单
    //2. 需要更新对应餐桌的状态
    public boolean oderMenu(int menuId,int nums,int diningTableId){
        //生成一个账单号, UUID
        String billId = UUID.randomUUID().toString();
        //将账单生成到bill表
        int update = billDAO.update("insert into bill values(null ,? ,? ,? ,? ,? ,now() ,'未结账')",
                billId, menuId, nums, menuService.getMenuById(menuId).getPrice() * nums, diningTableId);

        if (update<=0){
            return false;
        }

        return diningTableService.updateDiningTableState(diningTableId,"就餐中");

    }

    //返回账单
    public List<Bill> getBillList(){
        return billDAO.queryMulti("select * from bill", Bill.class);
    }

    //查看某个餐桌是否有未结账的账单
    public boolean hasPayBillByDiningTableId(int diningTableId){
        Bill bill = billDAO.querySingle("select * from bill where diningTableId=? and state='未结账' limit 0, 1", Bill.class, diningTableId);
        return bill!=null;
    }

    //修改bill的状态，完成结账（若餐桌存在，并且该餐桌有未结账的账单）
    public boolean payBill(int diningTableId , String payMode){
        //修改bill表
        int update = billDAO.update("update bill set state=? where diningTableId=? and state='未结账' ", payMode, diningTableId);
        if (update<=0){
            return false;
        }
        //修改diningTable表，不在此操作，调用DiningTableService中方法
        if (!diningTableService.updateDiningTableToFree(diningTableId,"空")){
            return false;
        }

        return true;
    }

}
