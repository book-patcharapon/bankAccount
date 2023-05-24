package bankkingapplication;

import java.sql.*;



public class Bank {
    private String name;

    public Bank(String name) {
        this.name = name;
    }
    public Account getAccount(int number) {
        Connection con = BankConnection.connect();
        Account account = null;
        String sql = "SELECT * from account where accID='"+number+"'";
        Statement statement;
        try {
            String accountName = "";
            double balance = 0;
            statement = con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()){
                accountName = result.getString(2);
                balance = result.getDouble(3);
            }
            account = new Account(number,accountName,balance);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }
    public void listAccount() {
        Connection con = BankConnection.connect();
        Statement statement = null;
        try {
            statement = con.createStatement();
            String sql = "select * from account";
            ResultSet results = statement.executeQuery(sql);
            while(results.next()) {
                System.out.println(results.getString(1)+" "+results.getString(2)
                        +" "+results.getString(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public void openAccount(Account account){
        Connection con = BankConnection.connect();
        String sql = "insert into account" +
                "(accID,accName,accBalance)"
                + "values(?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, account.getNumber());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setDouble(3, account.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void closeAccount(int number){
        Connection con = BankConnection.connect();
        String sql = "delete from account where accID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, number);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void depositMoney(Account account,double amount){
        account.deposit(amount);
        System.out.println(account.getBalance());
        Connection con = BankConnection.connect();
        String sql = "UPDATE account set accBalance = ? where accID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setInt(2, account.getNumber());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void withdrawMoney(Account account,double amount){
        account.withdraw(amount);
    }
}
