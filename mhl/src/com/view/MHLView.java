package com.view;

import com.daomain.Bill;
import com.daomain.DiningTable;
import com.daomain.Employee;
import com.daomain.Menu;
import com.service.BillService;
import com.service.DiningTableService;
import com.service.EmployeeService;
import com.service.MenuService;
import com.utils.Utility;

import java.util.List;

public class MHLView {
    public static void main(String[] args) {
        new MHLView().mainMenu();
    }

    private boolean loop=true;//控制是否退出菜单
    private String key="";//接受用户的选择
    private EmployeeService employeeService=new EmployeeService();//定义EmployeeService对象
    private DiningTableService diningTableService=new DiningTableService();//定义DiningTableService对象
    private MenuService menuService=new MenuService();
    private BillService billService=new BillService();

    public void mainMenu(){
        while (loop){
            System.out.println("=======满汉楼=======");
            System.out.println("\t1  登录满汉楼");
            System.out.println("\t2  退出满汉楼");
            System.out.println("请输入你的选择：");
            key= Utility.readString(1);
            switch (key){
                case "1":
                    System.out.println("请输入账号：");
                    String empId = Utility.readString(50);
                    System.out.println("请输入密码：");
                    String empPwd = Utility.readString(50);
                    //数据库，人员登录
                    Employee employee = employeeService.getEmployeeByIdAndPwd(empId, empPwd);
                    if (employee!=null){
                        System.out.println("登录成功，欢迎"+employee.getJob()+"---"+employee.getName());
                        //显示二级菜单,循环操作，所以也是while操作
                        while (loop){
                            System.out.println("=======满汉楼=======");
                            System.out.println("\t1  显示餐桌状态");
                            System.out.println("\t2  预定餐桌");
                            System.out.println("\t3  显示菜品");
                            System.out.println("\t4  点餐服务");
                            System.out.println("\t5  查看账单");
                            System.out.println("\t6  结账");
                            System.out.println("\t7  退出满汉楼");
                            key=Utility.readString(1);
                            switch (key){
                                case "1":
                                    System.out.println("\t-------餐桌状态-------");
                                    listDiningTable();
                                    break;
                                case "2":
                                    System.out.println("\t-------预定餐桌-------");
                                    orderDiningTable();
                                    break;
                                case "3":
                                    System.out.println("\t-------菜品-------");
                                    listMenu();
                                    break;
                                case "4":
                                    System.out.println("\t-------点餐服务-------");
                                    orderMenu();
                                    break;
                                case "5":
                                    System.out.println("\t-------查看账单-------");
                                    listBill();
                                    break;
                                case "6":
                                    System.out.println("\t-------结账-------");
                                    payBill();
                                    break;
                                case "7":
                                    loop=false;
                                    break;
                                default:
                                    System.out.println("输入有误，请重新输入");
                            }
                        }
                    }else {
                        System.out.println("登录失败，请检查登录信息！");
                    }
                    break;
                case "2":
                    loop=false;
                    break;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        }

        System.out.println("退出");

    }

    //显示餐桌状态
    public void listDiningTable(){
        List<DiningTable> list = diningTableService.list();
        System.out.println("\n餐桌编号\t餐桌状态");
        for (DiningTable diningTable : list) {
            System.out.println("座位"+diningTable.getId()+"\t状态："+diningTable.getState());
        }
    }

    //预定餐桌
    public void orderDiningTable(){
        System.out.println("\n请选择要预定的餐桌编号（-1退出）");
        int orderId = Utility.readInt();
        if (orderId==-1){
            System.out.println("取消预定");
            return;
        }
        char key = Utility.readConfirmSelection();//该方法返回的只有 Y 或者 N
        if (key== 'Y'){
            //根据orderId返回对应的DiningTable对象，若为null 说明该对象不存在
            DiningTable diningTable = diningTableService.getDiningTableById(orderId);
            if (diningTable==null){
                System.out.println("您预定的餐桌不存在！");
                return;
            }
            if (!("空".equals(diningTable.getState()))){
                System.out.println("该餐桌已被预定或者就餐中！");
                return;
            }
            System.out.println("请输入预定人姓名：");
            String orderName = Utility.readString(50);
            System.out.println("请输入预定人电话：");
            String orderTel = Utility.readString(50);
            if (diningTableService.orderDiningTable(orderId,orderName,orderTel)){
                System.out.println("预定成功！");
            }else {
                System.out.println("预定失败！");
            }
        }else {
            System.out.println("取消预定");
        }
    }

    //获取菜品
    public void listMenu(){
        List<Menu> menuList = menuService.getMenuList();
        System.out.println("编号\t菜品名称\t菜品分类\t菜品价格");
        for (Menu menu : menuList) {
            System.out.println(menu.getId()+"\t"+menu.getName()+"\t"+menu.getType()+"\t\t"+menu.getPrice());
        }
    }

    //点餐服务
    public void orderMenu(){
        System.out.println("请输入点餐的座位（-1退出）：");
        int orderDiningTableId = Utility.readInt();
        if (orderDiningTableId==-1){
            System.out.println("取消点餐！");
            return;
        }
        System.out.println("请输入点餐的菜品号（-1退出）：");
        int orderMenuId = Utility.readInt();
        if (orderMenuId==-1){
            System.out.println("取消点餐！");
            return;
        }
        System.out.println("请输入点餐的菜品量（-1退出）：");
        int orderMenuNums = Utility.readInt();
        if (orderMenuNums==-1){
            System.out.println("取消点餐！");
            return;
        }

        //验证餐桌号是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(orderDiningTableId);
        if (diningTable==null){
            System.out.println("餐桌号不存在！");
            return;
        }
        //验证菜品标号是否存在
        Menu menu = menuService.getMenuById(orderMenuId);
        if (menu==null){
            System.out.println("菜品不存在！");
            return;
        }
        //点餐
        if (billService.oderMenu(orderMenuId,orderMenuNums,orderDiningTableId)){
            System.out.println("点餐成功！");
        }else{
            System.out.println("点餐失败！");
        }

    }

    //查看所有账单
    public void listBill(){
        List<Bill> billList = billService.getBillList();
        System.out.println("\n编号\t菜品号\t菜品量\t金额\t\t桌位号\t日期\t\t\t\t\t\t状态");
        for (Bill bill : billList) {
            System.out.println(bill.getId()+"\t"+bill.getMenuId()+"\t\t"+bill.getNums()+"\t\t"+bill.getMoney()+"\t"+bill.getDiningTableId()+"\t\t"+bill.getBillDate()+"\t"+bill.getState());
        }
    }

    //结账
    public void payBill(){
        System.out.println("请输入要结账的餐桌编号（-1退出）：");
        int diningTableId = Utility.readInt();
        if (diningTableId==-1){
            System.out.println("退出");
            return;
        }
        //验证餐桌是否存在
        DiningTable diningTableById = diningTableService.getDiningTableById(diningTableId);
        if (diningTableById==null){
            System.out.println("餐桌不存在");
            return;
        }
        //验证餐桌是否有要结账的账单
        if (!billService.hasPayBillByDiningTableId(diningTableId)){
            System.out.println("没有要结账的账单");
            return;
        }
        System.out.println("请输入结账方式（现金/支付宝/微信）回车退出：");
        String payMode = Utility.readString(20, "");//若回车，则返回""
        if (payMode!="现金" || payMode!="支付宝" || payMode!="微信"){
            System.out.println("你输入有误");
            return;
        }
        if ("".equals(payMode)){
            System.out.println("取消结账");
            return;
        }
        char key = Utility.readConfirmSelection();
        if (key=='Y'){
            //调用相关方法结账
            if (billService.payBill(diningTableId,payMode)){
                System.out.println("结账成功");
            }else {
                System.out.println("结账失败");
            }
        }else {
            System.out.println("取消结账");
            return;
        }
    }

}
