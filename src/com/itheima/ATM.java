package com.itheima;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private Account loginAcc;
    public void start(){
        while (true) {
            System.out.println("===欢迎进入ATM系统===");
            System.out.println("1.用户登录");
            System.out.println("2.用户开户");
            System.out.println("请选择：");
            int command= sc.nextInt();
            switch(command){
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                default:
                    System.out.println("没有该选择~");
            }
        }
    }
    private void login(){
        System.out.println("===系统登录===");
        if(accounts.isEmpty()){
            System.out.println("没有账户捏");
            return;
        }
        while (true) {
            System.out.println("请输入您的登录卡号:");
            String cardId = sc.next();
            Account acc = getAccountByIdCard(cardId);
            if(acc == null){
                System.out.println("输入的卡号不存在");
            }else{
                while (true) {
                    System.out.println("请输入你的登录密码：");
                    String password = sc.next();
                    if(acc.getPassword().equals(password)){
                        loginAcc = acc;
                        System.out.println("恭喜你"+acc.getUserName()+"成功登录系统，欢迎卡号："+acc.getCardId()+"用户");
                        showUserCommand();
                        return;
                    }else {
                        System.out.println("您输入的密码错误~");
                    }
                }
            }
        }
    }
    private void showUserCommand(){
        while (true) {
            System.out.println(loginAcc.getUserName()+"您可以选择如下功能进行账户的处理======");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.密码修改");
            System.out.println("6.退出");
            System.out.println("7.注销当前账户");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch(command){
                case 1:
                    showLoginAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transforMoney();
                    break;
                case 5:
                    updatePassWord();
                    return;
                case 6:
                    System.out.println(loginAcc.getUserName()+"退出系统成功");
                    return;
                case 7:
                    if (deleteAccount()) {
                        return;
                    }
                    break;
                default:
                    System.out.println("操作不存在");
            }
        }
    }

    private void updatePassWord() {
        System.out.println("===密码修改操作===");
        while (true) {
            System.out.println("请输入之前的密码：");
            String password = sc.next();
            if(loginAcc.getPassword().equals(password)){
                while (true) {
                    System.out.println("请您输入新密码");
                    String newPassword = sc.next();
                    System.out.println("请您确认密码");
                    String okPassword = sc.next();
                    if(newPassword.equals(okPassword)){
                        loginAcc.setPassword(newPassword);
                        System.out.println("修改成功");
                        return;
                    }
                    else{
                        System.out.println("两次密码不一致，请重新输入~");
                    }
                }
            }else {
                System.out.println("当前输入密码不正确");
            }
        }

    }

    private boolean deleteAccount() {
        System.out.println("===进行销户操作===");
        System.out.println("是否确认销户：y/n");
        String command = sc.next();
        switch (command) {
            case "y":
                if(loginAcc.getMoney() == 0){
                    accounts.remove(loginAcc);
                    System.out.println("销户成功！");
                    return true;
                }else{
                    System.out.println("账户存在余额，不允许销户：");
                    return false;
                }
            default:
                System.out.println("退出成功");
                return false;
        }
    }

    private void transforMoney() {
        System.out.println("===用户转账===");
        if(accounts.size()<2){
            System.out.println("当前系统只有一个账户，无法为其他用户转账");
            return;
        }
        if(loginAcc.getMoney() == 0){
            System.out.println("你自己都没钱，转你妈");
            return;
        }
        while (true) {
            System.out.println("请输入对方卡号：");
            String cardId = sc.next();
            Account acc = getAccountByIdCard(cardId);
            if(acc == null){
                System.out.println("输入的卡号不存在");
            }else {
                String name = "*"+acc.getUserName().substring(1);
                System.out.println("请您输入【"+name+"】姓氏");
                String preName = sc.next();
                while (true) {
                    if(acc.getUserName().startsWith(preName)){
                        System.out.println("请您输入转账给对方的金额：");
                        double money = sc.nextDouble();
                        if(loginAcc.getMoney() >= money){
                            if(money> loginAcc.getLimit()){
                                System.out.println("超过限额，无法转账");
                            }else{
                                loginAcc.setMoney(loginAcc.getMoney() - money);
                                acc.setMoney(acc.getMoney() + money);
                                System.out.println("转账成功！");
                                return;
                            }
                        }else{
                            System.out.println("余额不足,余额为："+loginAcc.getMoney());
                        }
                    }else {
                        System.out.println("输入的姓氏有问题");
                    }
                }

            }
        }
    }

    private void drawMoney() {
        System.out.println("===取钱操作===");
        if(loginAcc.getMoney()<100){
            System.out.println("您的账户不足100，取钱失败");
            return;
        }
        while (true) {
            System.out.println("请输入你的取款金额");
            double money = sc.nextDouble();
            if(loginAcc.getMoney()>=money){
                if(money>loginAcc.getLimit()){
                    System.out.println("您取钱超过限额，最多可取："+loginAcc.getLimit());
                }else {
                    loginAcc.setMoney(loginAcc.getMoney()-money);
                    System.out.println("您取款："+money+"成功，取款后剩余"+loginAcc.getMoney());
                    break;
                }
            }else{
                System.out.println("您的账户余额不足,您的余额是："+loginAcc.getMoney());
            }
        }

    }

    private void depositMoney(){
        System.out.println("===存钱操作===");
        System.out.println("请您输入存款金额：");
        double money = sc.nextDouble();
        loginAcc.setMoney(loginAcc.getMoney()+money);
        System.out.println("恭喜你，存钱："+money + "成功，存钱后余额为："+loginAcc.getMoney());

    }

    private void showLoginAccount(){
        System.out.println("===当前展示信息如下===");
        System.out.println("卡号："+loginAcc.getCardId());
        System.out.println("户主："+loginAcc.getUserName());
        System.out.println("性别："+loginAcc.getSex());
        System.out.println("余额："+loginAcc.getMoney());
        System.out.println("每次取现额度："+loginAcc.getLimit());
    }

    private void createAccount(){
        System.out.println("===系统开户===");
        Account acc = new Account();
        System.out.println("请输入你的账户名称");
        String name = sc.next();
        acc.setUserName(name);
        while (true) {
            System.out.println("请输入你的性别：");
            char sex = sc.next().charAt(0);
            if(sex == '男' || sex == '女'){
                acc.setSex(sex);
                break;
            }
            else{
                System.out.println("BYD你到底是男是女");
            }
        }
        while (true) {
            System.out.println("请输入你的账户密码：");
            String password = sc.next();
            System.out.println("请确认密码：");
            String rightpassword = sc.next();
            if(password.equals(rightpassword)){
                acc.setPassword(password);
                break;
            }else{
                System.out.println("密码不相同，重新输入");
            }
        }
        System.out.println("请您输入您的取现额度");
        double limit = sc.nextDouble();
        acc.setLimit(limit);

        String newCardId = createCardId();
        acc.setCardId(newCardId);

        accounts.add(acc);
        System.out.println("恭喜您，"+acc.getUserName()+"开户成功，你的卡号是"+acc.getCardId());
    }
    private String createCardId(){
        while (true) {
            String cardId = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                int data = r.nextInt(10);
                cardId = cardId + data;
            }
            Account acc = getAccountByIdCard(cardId);
            if(acc == null){
                return cardId;
            }
        }
    }
    private Account getAccountByIdCard(String cardId){
        for(int i = 0;i<accounts.size();i++){
            Account acc = accounts.get(i);
            if(acc.getCardId().equals(cardId)){
                return acc;
            }
        }
        return null;

    }
}
