package com.company;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
//import java.awt.event.ActionListener;
public class Main {

    public static void main(String[] args) {
        String sql="create table if not exists  account(balance int,timee int)";
        String insert="insert into account values (100000,0)";
        Connection conn=null;
        Statement stmtp=null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
             conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ganesh", "root", "");
            System.out.println("Connected to database.");
            stmtp=conn.createStatement();
            stmtp.executeUpdate(sql);
            stmtp.executeUpdate(insert);
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            try {
                stmtp.close();
                conn.close();

            }catch (Exception f){
                System.out.println(f);
            }
        }
        JFrame f = new JFrame("Button Example");
       JTextField tf = new JTextField();
        tf.setBounds(50, 50, 150, 20);
        JButton b = new JButton("Fund transfer");
        DefaultTableModel model = new DefaultTableModel();
        JTable tabGrid = new JTable(model);
        JScrollPane scrlPane = new JScrollPane(tabGrid);
        b.setBounds(50, 100, 200, 30);
        scrlPane.setBounds(0,200,900,600);
        model.addColumn("transactions");
        f.add(scrlPane);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Connection con=null;
                Statement stmt=null;
                PreparedStatement ptmt=null;
                tabGrid.setFont(new Font("Times New Roman",0,15));

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ganesh", "root", "");
                    System.out.println("Connected to database.");
                     stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                     ResultSet rs=stmt.executeQuery("SELECT * from account");
                    rs.last();
                    System.out.println("Accessing the data "+rs.getRow());
                    int bal=rs.getInt("balance")-1000;
                    int trans=rs.getInt("timee")+1;
                    int count=0;
                    if (model.getRowCount() > 0) {
                        for (int i = model.getRowCount() - 1; i > -1; i--) {
                            model.removeRow(i);
                        }
                    }
                    while (rs.previous()){
                        if(count<5) {
                            model.addRow(new Object[]{
                                    rs.getInt("balance")
                            });
                            count++;
                        }
                    }
                    count=0;
                    System.out.println(bal+" "+trans);
                    ptmt=con.prepareStatement("insert into account values(?,?)");
                    ptmt.setInt(1,bal);
                    ptmt.setInt(2,trans);
                    ptmt.executeUpdate();

                } catch (Exception f) {
                    System.out.println(f);
                }
                finally {
                    try{
                        stmt.close();
                        ptmt.close();
                        con.close();
                    }
                    catch (Exception f){
                        System.out.println(f);
                    }
                }
            }
        });
        f.add(b);
        f.add(tf);
        f.setSize(900,700);
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
    }
}
