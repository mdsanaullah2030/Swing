package view;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.DbUtil;

public class ProductView extends javax.swing.JFrame {

    DbUtil db = new DbUtil();

    PreparedStatement ps;
    ResultSet rs;

    LocalDate currentDate = LocalDate.now();
    java.sql.Date sqlCurrentDate = java.sql.Date.valueOf(currentDate);

    public ProductView() {
        initComponents();
        showProductOnTable();
        showProductToCombo();
        showStockOnTable();

        comProductName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                getProductSalesPrice(e);
            }

        });

    }

    public void getPurchaseReport() {
        String[] productViewTableColumn = {"SL", "Name", "Unit Price", "Qunatity", "Total Price", "Sales Price", "Date"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(productViewTableColumn);
        tblReport.setModel(model);

        java.util.Date toDate = dateToReport.getDate();
        java.util.Date fromDate = dateFromReport.getDate();

        String sql = "select * from product where purchaseDate between ? and ?";
        String sql2 = "select sum(totalPrice)from product where purchaseDate between ? and ?";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            int sl = 1;

            while (rs.next()) {

                String name = rs.getString("name");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalPrice");
                float salesPrice = rs.getFloat("salesPrice");
                Date purcahseDate = rs.getDate("purchaseDate");

                model.addRow(new Object[]{sl, name, unitPrice, quantity, totalPrice, salesPrice, purcahseDate});

                sl += 1;
            }

            ps.close();
            rs.close();
            db.getCon().close();

            ps = db.getCon().prepareStatement(sql2);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            //Sum total
            while (rs.next()) {

                float totalPrice = rs.getFloat("sum(totalPrice)");
                model.addRow(new Object[]{"", "", "", "Total Amount ", totalPrice});
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getSalesReport() {
        String[] salesViewTableColumn = {"SL", "Name", "Unit Price", "Qunatity", "Total Price", "Date"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(salesViewTableColumn);
        tblReport.setModel(model);

        java.util.Date toDate = dateToReport.getDate();
        java.util.Date fromDate = dateFromReport.getDate();

        String sql = "select * from sales where salesDate between ? and ?";
        String sql2 = "select sum(salesTotalPrice)from sales where salesDate between ? and ?";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            int sl = 1;

            while (rs.next()) {

                String name = rs.getString("name");
                float unitPrice = rs.getFloat("salesUnitPrice");
                float quantity = rs.getFloat("salesQuantity");
                float totalPrice = rs.getFloat("salesTotalPrice");
                Date salesDate = rs.getDate("salesDate");

                model.addRow(new Object[]{sl, name, unitPrice, quantity, totalPrice, salesDate});

                sl += 1;
            }

            ps.close();
            rs.close();
            db.getCon().close();

            // Sum Total
            ps = db.getCon().prepareStatement(sql2);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            while (rs.next()) {
                float totalPrice = rs.getFloat("sum(salesTotalPrice)");
                model.addRow(new Object[]{"", "", "", "Total Amount ", totalPrice});

            }
            ps.close();
            rs.close();
            db.getCon().close();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getGrossProfit() {

        java.util.Date toDate = dateToReport.getDate();
        java.util.Date fromDate = dateFromReport.getDate();

        String sql2 = "select sum(totalPrice)from product where purchaseDate between ? and ?";
        String sql = "select sum(salesTotalPrice)from sales where salesDate between ? and ?";

        try {
            ps = db.getCon().prepareStatement(sql2);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            float totalPurchasePrice = 0;
            rs = ps.executeQuery();

            while (rs.next()) {
                totalPurchasePrice = rs.getFloat("sum(totalPrice)");
            }

            rs.close();
            ps.close();
            db.getCon().close();

            ps = db.getCon().prepareStatement(sql);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            float salesTotalPrice = 0;
            while (rs.next()) {
                salesTotalPrice = rs.getFloat("sum(salesTotalPrice)");

            }
            ps.close();
            rs.close();
            db.getCon().close();

            float getGrossProfit = salesTotalPrice - totalPurchasePrice;
            lblProfit.setText("Profit : " + getGrossProfit);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean getStockProductList() {
        String sql = "select name from stock";
        boolean status = false;
        String purchaseProductName = txtName.getText().trim();

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name");
                if (productName.equalsIgnoreCase(purchaseProductName)) {
                    status = true;
                    break;
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

        return status;
    }

    public void addProductToStock() {
        boolean status = getStockProductList();

        if (status) {
            String sql = "update stock set quantity=quantity+? where name=?";
            try {
                ps = db.getCon().prepareStatement(sql);

                ps.setFloat(1, Float.parseFloat(txtQuantity.getText().trim()));
                ps.setString(2, txtName.getText().trim());

                ps.executeUpdate();

                ps.close();
                db.getCon().close();

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            String sql = "insert into stock(name,purcahsePrice,quantity,purchaseDate) values (?,?,?,?)";

            try {
                ps = db.getCon().prepareStatement(sql);

                ps.setString(1, txtName.getText().trim());
                ps.setFloat(2, Float.parseFloat(txtUnitPrice.getText().trim()));
                ps.setFloat(3, Float.parseFloat(txtQuantity.getText().trim()));
                ps.setDate(4, sqlCurrentDate);

                ps.executeUpdate();

                ps.close();
                db.getCon().close();

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void addProduct() {

        String sql = "insert into product(name,unitPrice,quantity,totalPrice, salesPrice,purchaseDate) values (?,?,?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setString(1, txtName.getText().trim());
            ps.setFloat(2, Float.parseFloat(txtUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtTotalPrice.getText().trim()));
            ps.setFloat(5, Float.parseFloat(txtSalesPrice.getText().trim()));
            ps.setDate(6, sqlCurrentDate);
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

            JOptionPane.showMessageDialog(this, "Add Product Successfully");
            addProductToStock();
            showStockOnTable();
            clear();
            showProductOnTable();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Add Product Unsuccessfully");
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Add Product Unsuccessfully");
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    public void getTotalPrice() {

        float unitPrice = Float.parseFloat(txtUnitPrice.getText().trim());
        float quantity = Float.parseFloat(txtQuantity.getText().trim());

        float totalPrice = unitPrice * quantity;

        txtTotalPrice.setText(totalPrice + "");

    }

    private void clear() {
        txtId.setText("");
        txtName.setText("");
        txtUnitPrice.setText("");
        txtQuantity.setText("");
        txtTotalPrice.setText("");
        txtSalesPrice.setText("");
    }

    String[] productViewTableColumn = {"Id", "Name", "Unit Price", "Qunatity", "Total Price", "Sales Price"};
    String[] stockViewTableColumn = {"id", "Name", "Qunatity", "Unit Price"};

    public void showProductOnTable() {

        String sql = "select * from product";

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(productViewTableColumn);

        tblProductView.setModel(model);

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalPrice");
                float salesPrice = rs.getFloat("salesPrice");

                model.addRow(new Object[]{id, name, unitPrice, quantity, totalPrice, salesPrice});
            }

            rs.close();
            ps.close();
            db.getCon().close();

        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    public void showStockOnTable() {

        String sql = "select * from stock";

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(stockViewTableColumn);

        tblStock.setModel(model);

        try {
            ps = db.getCon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                float quantity = rs.getFloat("quantity");
                float unitPrice = rs.getFloat("purcahsePrice");
//                Date purchaseDate=rs.getDate("purchaseDate");

                model.addRow(new Object[]{id, name, quantity, unitPrice});

            }

            rs.close();
            ps.close();
            db.getCon();

        } catch (ClassNotFoundException | SQLException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

        }
    }

    public void deleteProduct() {
        String sql = "delete from product where id=?";
        PreparedStatement ps;

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

            JOptionPane.showMessageDialog(this, "Delete Product Successfully");
            clear();
            showProductOnTable();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Delete Product Unsuccessfully");
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Delete Product Unsuccessfully");

            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    public void editProduct() {
        String sql = "update product set name=?, unitPrice=?, quantity=?, totalPrice=?, salesPrice=? where id=?";
        PreparedStatement ps;
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, txtName.getText().trim());
            ps.setFloat(2, Float.parseFloat(txtUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtTotalPrice.getText().trim()));
            ps.setFloat(5, Float.parseFloat(txtSalesPrice.getText().trim()));
            ps.setInt(6, Integer.parseInt(txtId.getText()));

            ps.executeUpdate();

            ps.close();
            db.getCon();
            JOptionPane.showMessageDialog(this, "Update Product Successfully");
            clear();
            showProductOnTable();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Update Product Unsuccessfully");
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Update Product Unsuccessfully");
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    public void showProductToCombo() {

        String sql = "select name from product";
        PreparedStatement ps;
        ResultSet rs;

        comProductName.removeAllItems();

        try {
            ps = db.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name");
                comProductName.addItem(productName);
            }

            ps.close();
            db.getCon().close();
            rs.close();

        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public void getProductSalesPrice(ItemEvent e) {
        String selectProductName = "";

        if (e.getStateChange() == ItemEvent.SELECTED) {
            selectProductName = (String) e.getItem();

            extractSalesPrice(selectProductName);
        }

        String sql = "select quantity from stock where name=?";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, selectProductName);

            rs = ps.executeQuery();

            while (rs.next()) {
                float quantity = rs.getFloat("quantity");
                txtStock.setText(quantity + "");
            }

            ps.close();
            db.getCon().close();
            rs.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void extractSalesPrice(String productName) {

        String sql = "select salesPrice from product where name=?";

        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, productName);

            rs = ps.executeQuery();

            while (rs.next()) {
                String salesPrice = rs.getString("salesPrice");
                txtSalesUnitPrice.setText(salesPrice);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public void getTotalSalesPrice() {

        float quantity = Float.parseFloat(txtSalesQunatity.getText().toString().trim());
        float unitPrice = Float.parseFloat(txtSalesUnitPrice.getText().toString().trim());
        float salesTotalPrice = quantity * unitPrice;
        txtSalesTotalPrice.setText(salesTotalPrice + "");

    }

    public String formatDateToDDMMYYYY(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public static java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }
        return null;
    }

    public static java.sql.Date convertStringToSqlDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy");
        try {
            java.util.Date utilDate = inputFormat.parse(dateString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = outputFormat.format(utilDate);

            return java.sql.Date.valueOf(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stockUpdateOnSales() {
        String sql = "update stock set quantity = quantity -? where name =?";

        try {
            ps = db.getCon().prepareStatement(sql);

            ps.setFloat(1, Float.parseFloat(txtSalesQunatity.getText().trim()));
            ps.setString(2, comProductName.getSelectedItem().toString());
            ps.executeUpdate();

            ps.close();
            db.getCon().close();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addSales() {
        Date date = convertUtilDateToSqlDate(salesDate.getDate());

        PreparedStatement ps;
        String sql = "insert into sales(name, salesUnitPrice,salesQuantity,salesTotalPrice,salesDate) "
                + "values(?,?,?,?,?)";
        try {
            ps = db.getCon().prepareStatement(sql);
            ps.setString(1, comProductName.getSelectedItem().toString());
            ps.setFloat(2, Float.parseFloat(txtSalesUnitPrice.getText()));
            ps.setFloat(3, Float.parseFloat(txtSalesQunatity.getText()));
            ps.setFloat(4, Float.parseFloat(txtSalesTotalPrice.getText()));
            ps.setDate(5, convertUtilDateToSqlDate(date));

            ps.executeUpdate();

            ps.close();
            db.getCon().close();

            JOptionPane.showMessageDialog(this, "Add Sales Successfully ! ");
            stockUpdateOnSales();
            showStockOnTable();

        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Add Sales Unsuccessfully ");
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnaddProduct = new javax.swing.JButton();
        btnsales = new javax.swing.JButton();
        btnstock = new javax.swing.JButton();
        btnreport = new javax.swing.JButton();
        mainView = new javax.swing.JTabbedPane();
        add = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtUnitPrice = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        txtTotalPrice = new javax.swing.JTextField();
        txtSalesPrice = new javax.swing.JTextField();
        btnAddProduct = new javax.swing.JButton();
        btnProductDelete = new javax.swing.JButton();
        btnProductUpdate = new javax.swing.JButton();
        btnProductReset = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductView = new javax.swing.JTable();
        sales = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comProductName = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtSalesQunatity = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSalesUnitPrice = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSalesTotalPrice = new javax.swing.JTextField();
        btnSalesSave = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        salesDate = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        stock = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        report = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        dateToReport = new com.toedter.calendar.JDateChooser();
        dateFromReport = new com.toedter.calendar.JDateChooser();
        btnReportPurchase = new javax.swing.JButton();
        btnReportSales = new javax.swing.JButton();
        btnGrossProfit = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();
        btnReportReset = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lblProfit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Cambria", 3, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("JEE  STOR");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 70));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 720, 70));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        btnaddProduct.setBackground(new java.awt.Color(0, 0, 51));
        btnaddProduct.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnaddProduct.setForeground(new java.awt.Color(255, 255, 255));
        btnaddProduct.setText("Add Product");
        btnaddProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnaddProductMouseClicked(evt);
            }
        });

        btnsales.setBackground(new java.awt.Color(0, 0, 51));
        btnsales.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnsales.setForeground(new java.awt.Color(255, 255, 255));
        btnsales.setText("Sales");
        btnsales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnsalesMouseClicked(evt);
            }
        });

        btnstock.setBackground(new java.awt.Color(0, 0, 51));
        btnstock.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnstock.setForeground(new java.awt.Color(255, 255, 255));
        btnstock.setText("Stock");
        btnstock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnstockMouseClicked(evt);
            }
        });

        btnreport.setBackground(new java.awt.Color(0, 0, 51));
        btnreport.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnreport.setForeground(new java.awt.Color(255, 255, 255));
        btnreport.setText("Report");
        btnreport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnreportMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnaddProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnsales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnstock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnreport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnaddProduct)
                .addGap(18, 18, 18)
                .addComponent(btnsales)
                .addGap(18, 18, 18)
                .addComponent(btnstock)
                .addGap(18, 18, 18)
                .addComponent(btnreport)
                .addContainerGap(248, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 110, 430));

        jPanel3.setBackground(new java.awt.Color(0, 102, 51));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Add Product");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("ID");

        txtId.setEditable(false);
        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Name");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Unit Price");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Quantity");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Total Price");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Sales Price");

        txtQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuantityFocusLost(evt);
            }
        });

        txtTotalPrice.setEditable(false);
        txtTotalPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalPriceActionPerformed(evt);
            }
        });

        txtSalesPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalesPriceActionPerformed(evt);
            }
        });

        btnAddProduct.setText("Add");
        btnAddProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddProductMouseClicked(evt);
            }
        });

        btnProductDelete.setText("Delete");
        btnProductDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductDeleteMouseClicked(evt);
            }
        });

        btnProductUpdate.setText("Edit");
        btnProductUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductUpdateMouseClicked(evt);
            }
        });

        btnProductReset.setText("Reset");
        btnProductReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductResetMouseClicked(evt);
            }
        });

        tblProductView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblProductView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProductView);

        javax.swing.GroupLayout addLayout = new javax.swing.GroupLayout(add);
        add.setLayout(addLayout);
        addLayout.setHorizontalGroup(
            addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addLayout.createSequentialGroup()
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(addLayout.createSequentialGroup()
                                    .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtName)
                                        .addComponent(txtId)))
                                .addGroup(addLayout.createSequentialGroup()
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(addLayout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnProductUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(btnAddProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnProductDelete, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnProductReset, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(41, 41, 41))
                    .addGroup(addLayout.createSequentialGroup()
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addLayout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addLayout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtSalesPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(addLayout.createSequentialGroup()
                .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        addLayout.setVerticalGroup(
            addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSalesPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)))
                    .addGroup(addLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddProduct)
                            .addComponent(btnProductDelete))
                        .addGap(34, 34, 34)
                        .addGroup(addLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProductUpdate)
                            .addComponent(btnProductReset))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainView.addTab("add", add);

        jPanel4.setBackground(new java.awt.Color(0, 255, 204));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Calibri", 3, 20)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Product Sales");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Name");

        comProductName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Quantity");

        txtSalesQunatity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesQunatityFocusLost(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Date");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Unit Price");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Total  Price");

        txtSalesTotalPrice.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N

        btnSalesSave.setBackground(new java.awt.Color(204, 204, 255));
        btnSalesSave.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        btnSalesSave.setText("Save");
        btnSalesSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesSaveMouseClicked(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 204, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton2.setText("Edit");

        jButton3.setBackground(new java.awt.Color(153, 153, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton3.setText("Reset");

        jButton4.setBackground(new java.awt.Color(255, 0, 51));
        jButton4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton4.setText("Delete");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel17.setBackground(new java.awt.Color(0, 51, 51));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Stock");

        txtStock.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout salesLayout = new javax.swing.GroupLayout(sales);
        sales.setLayout(salesLayout);
        salesLayout.setHorizontalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(salesLayout.createSequentialGroup()
                .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnSalesSave, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comProductName, 0, 104, Short.MAX_VALUE)
                    .addComponent(txtSalesUnitPrice)
                    .addGroup(salesLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jButton2)))
                .addGap(18, 18, 18)
                .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(salesLayout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addContainerGap())
                    .addGroup(salesLayout.createSequentialGroup()
                        .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSalesQunatity)
                            .addComponent(txtSalesTotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(salesLayout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(salesDate, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
                            .addGroup(salesLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 21, Short.MAX_VALUE))))))
        );
        salesLayout.setVerticalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(salesDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(salesLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(comProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(txtSalesQunatity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))))
                .addGap(27, 27, 27)
                .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtSalesUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtSalesTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalesSave)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(0, 220, Short.MAX_VALUE))
        );

        mainView.addTab("sales", sales);

        stock.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(51, 0, 51));

        jLabel4.setBackground(new java.awt.Color(51, 0, 51));
        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Stock");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        stock.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, 70));

        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblStock);

        stock.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 610, 340));

        mainView.addTab("stock", stock);

        report.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(0, 51, 102));

        jButton5.setFont(new java.awt.Font("Algerian", 1, 18)); // NOI18N
        jButton5.setText("Report");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(256, 256, 256)
                .addComponent(jButton5)
                .addContainerGap(256, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jButton5)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("From");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("To");

        btnReportPurchase.setText("Purchase");
        btnReportPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportPurchaseMouseClicked(evt);
            }
        });

        btnReportSales.setText("Sales");
        btnReportSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportSalesMouseClicked(evt);
            }
        });

        btnGrossProfit.setText("Gross Profit");
        btnGrossProfit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGrossProfitMouseClicked(evt);
            }
        });

        tblReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblReport);

        btnReportReset.setText("Reset");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        lblProfit.setBackground(new java.awt.Color(0, 0, 0));
        lblProfit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblProfit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProfit.setText("Profit : ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblProfit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblProfit, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout reportLayout = new javax.swing.GroupLayout(report);
        report.setLayout(reportLayout);
        reportLayout.setHorizontalGroup(
            reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportLayout.createSequentialGroup()
                .addGroup(reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, reportLayout.createSequentialGroup()
                            .addComponent(btnReportPurchase)
                            .addGap(18, 18, 18)
                            .addComponent(btnReportSales, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnGrossProfit)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnReportReset)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(reportLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addGap(35, 35, 35)
                        .addComponent(dateFromReport, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateToReport, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        reportLayout.setVerticalGroup(
            reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel18)
                    .addComponent(dateToReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateFromReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reportLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReportPurchase)
                            .addComponent(btnReportSales)
                            .addComponent(btnGrossProfit)
                            .addComponent(btnReportReset))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainView.addTab("report", report);

        getContentPane().add(mainView, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 610, 470));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnaddProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnaddProductMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(0);
    }//GEN-LAST:event_btnaddProductMouseClicked

    private void btnsalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnsalesMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(1);
        showProductToCombo();
    }//GEN-LAST:event_btnsalesMouseClicked

    private void btnstockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnstockMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(2);
    }//GEN-LAST:event_btnstockMouseClicked

    private void btnreportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnreportMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(3);
    }//GEN-LAST:event_btnreportMouseClicked

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void txtTotalPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalPriceActionPerformed

    private void txtSalesPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalesPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalesPriceActionPerformed

    private void btnAddProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddProductMouseClicked
        // TODO add your handling code here:
        addProduct();

    }//GEN-LAST:event_btnAddProductMouseClicked

    private void txtQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuantityFocusLost
        // TODO add your handling code here:
        getTotalPrice();
    }//GEN-LAST:event_txtQuantityFocusLost

    private void btnProductResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductResetMouseClicked
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnProductResetMouseClicked

    private void tblProductViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductViewMouseClicked
        // TODO add your handling code here:
        int rowIndex = tblProductView.getSelectedRow();

        String id = tblProductView.getModel().getValueAt(rowIndex, 0).toString();
        String name = tblProductView.getModel().getValueAt(rowIndex, 1).toString();
        String unitPrice = tblProductView.getModel().getValueAt(rowIndex, 2).toString();
        String quantity = tblProductView.getModel().getValueAt(rowIndex, 3).toString();
        String totalPrice = tblProductView.getModel().getValueAt(rowIndex, 4).toString();
        String salesPrice = tblProductView.getModel().getValueAt(rowIndex, 5).toString();

        txtId.setText(id);
        txtName.setText(name);
        txtUnitPrice.setText(unitPrice);
        txtQuantity.setText(quantity);
        txtTotalPrice.setText(totalPrice);
        txtSalesPrice.setText(salesPrice);


    }//GEN-LAST:event_tblProductViewMouseClicked

    private void btnProductDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductDeleteMouseClicked
        // TODO add your handling code here:
        deleteProduct();

    }//GEN-LAST:event_btnProductDeleteMouseClicked

    private void btnProductUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductUpdateMouseClicked
        // TODO add your handling code here:
        editProduct();


    }//GEN-LAST:event_btnProductUpdateMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtSalesQunatityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesQunatityFocusLost
        // TODO add your handling code here:
        getTotalSalesPrice();

    }//GEN-LAST:event_txtSalesQunatityFocusLost

    private void btnSalesSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesSaveMouseClicked
        // TODO add your handling code here:
        addSales();


    }//GEN-LAST:event_btnSalesSaveMouseClicked

    private void btnReportPurchaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportPurchaseMouseClicked
        // TODO add your handling code here:
        getPurchaseReport();
    }//GEN-LAST:event_btnReportPurchaseMouseClicked

    private void btnReportSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportSalesMouseClicked
        // TODO add your handling code here:
        getSalesReport();
    }//GEN-LAST:event_btnReportSalesMouseClicked

    private void btnGrossProfitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGrossProfitMouseClicked
        // TODO add your handling code here:
        getGrossProfit();
    }//GEN-LAST:event_btnGrossProfitMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProductView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProductView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel add;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnGrossProfit;
    private javax.swing.JButton btnProductDelete;
    private javax.swing.JButton btnProductReset;
    private javax.swing.JButton btnProductUpdate;
    private javax.swing.JButton btnReportPurchase;
    private javax.swing.JButton btnReportReset;
    private javax.swing.JButton btnReportSales;
    private javax.swing.JButton btnSalesSave;
    private javax.swing.JButton btnaddProduct;
    private javax.swing.JButton btnreport;
    private javax.swing.JButton btnsales;
    private javax.swing.JButton btnstock;
    private javax.swing.JComboBox<String> comProductName;
    private com.toedter.calendar.JDateChooser dateFromReport;
    private com.toedter.calendar.JDateChooser dateToReport;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblProfit;
    private javax.swing.JTabbedPane mainView;
    private javax.swing.JPanel report;
    private javax.swing.JPanel sales;
    private com.toedter.calendar.JDateChooser salesDate;
    private javax.swing.JPanel stock;
    private javax.swing.JTable tblProductView;
    private javax.swing.JTable tblReport;
    private javax.swing.JTable tblStock;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSalesPrice;
    private javax.swing.JTextField txtSalesQunatity;
    private javax.swing.JTextField txtSalesTotalPrice;
    private javax.swing.JTextField txtSalesUnitPrice;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtTotalPrice;
    private javax.swing.JTextField txtUnitPrice;
    // End of variables declaration//GEN-END:variables

}
