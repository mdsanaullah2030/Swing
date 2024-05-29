/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.DbUtil;

/**
 *
 * @author HP
 */
public class ProductView extends javax.swing.JFrame {

    DbUtil db = new DbUtil();

    /**
     * Creates new form ProductView
     */
    public ProductView() {
        initComponents();
        showProductonTable();
    }

    public void addProduct() {
        String sql = "insert into product(name,unitPrice,quantity,totalPrice,salesPrice)values(?,?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = db.getCon().prepareCall(sql);

            ps.setString(1, txtName.getText().trim());

            ps.setFloat(2, Float.parseFloat(txtUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtTotalPrice.getText().trim()));
            ps.setFloat(5, Float.parseFloat(txtSalesPrice.getText().trim()));

            ps.executeUpdate();
            ps.close();
            db.getCon().close();

            JOptionPane.showMessageDialog(this, "Add Product Successfully ");
            clear();
            showProductonTable();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Add Product unSuccessfully ");

            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Add Product unSuccessfully ");

            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getTotalPrice() {

        float unitPrice = Float.parseFloat(txtUnitPrice.getText().trim());
        float quantity = Float.parseFloat(txtQuantity.getText().trim());
        float totalPrice = unitPrice * quantity;
        txtTotalPrice.setText(totalPrice + "");

    }

    public void clear() {
        txtId.setText("");
        txtName.setText("");
        txtUnitPrice.setText("");
        txtQuantity.setText("");
        txtTotalPrice.setText("");
        txtSalesPrice.setText("");

    }

    String[] productViewTableColumn = {"id", "Name", "Unit Price", "Qunatity", "Total Price", "Sales Price"};

    public void showProductonTable() {

        String sql = "select * from product";
        PreparedStatement ps;
        ResultSet rs;

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(productViewTableColumn);
        tblProductView.setModel(model);

        try {
            ps = db.getCon().prepareCall(sql);
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
            ps.close();
            db.getCon();
            rs.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
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
            db.getCon();
            JOptionPane.showMessageDialog(this, "Delete Product Successfully ");
            clear();
            showProductonTable();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Delete Product unsuccessfully ");

            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Delete Product unsuccessfully ");

            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    
    public void editProduct(){
       String sql = "update  product set name=?,unitprice=?, qunatity=?, totalprice=?, salesprice=? where id=?";
        PreparedStatement ps;
        
        try {
            ps=db.getCon().prepareStatement(sql);
            

            ps.setString(1, txtName.getText().trim());

            ps.setFloat(2, Float.parseFloat(txtUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtTotalPrice.getText().trim()));
            ps.setFloat(5, Float.parseFloat(txtSalesPrice.getText().trim()));
            ps.setInt(6, Integer.parseInt(txtId.getText()));

            
              ps.executeUpdate();
            ps.close();
            db.getCon().close();

            JOptionPane.showMessageDialog(this, "Updet Product Successfully ");
            clear();
            showProductonTable();
            
            
        } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(this, "Updet Product unsuccessfully ");

            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Updet Product unsuccessfully ");

            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnAddProduct = new javax.swing.JButton();
        btnSalesProduct = new javax.swing.JButton();
        btnStock = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        mainView = new javax.swing.JTabbedPane();
        Add = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTotalPrice = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSalesPrice = new javax.swing.JTextField();
        btnProductAdd = new javax.swing.JButton();
        btnProductDelete = new javax.swing.JButton();
        btnProductEdit = new javax.swing.JButton();
        btnProductReset = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtUnitPrice = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductView = new javax.swing.JTable();
        Sales = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Stock = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        Report = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 121, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 531, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("IT shop");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 90));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        btnAddProduct.setText("Add Product");
        btnAddProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddProductMouseClicked(evt);
            }
        });

        btnSalesProduct.setText("Sales Product");
        btnSalesProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesProductMouseClicked(evt);
            }
        });

        btnStock.setText("Stock");
        btnStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStockMouseClicked(evt);
            }
        });

        btnReport.setText("Report");
        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAddProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSalesProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
            .addComponent(btnStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnReport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btnAddProduct)
                .addGap(36, 36, 36)
                .addComponent(btnSalesProduct)
                .addGap(38, 38, 38)
                .addComponent(btnStock)
                .addGap(41, 41, 41)
                .addComponent(btnReport)
                .addGap(0, 303, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 130, 510));

        jPanel3.setBackground(new java.awt.Color(0, 153, 153));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Add Product");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
        );

        jLabel7.setText("ID");

        txtId.setEditable(false);

        jLabel8.setText("Name");

        jLabel9.setText("Quantity");

        txtQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuantityFocusLost(evt);
            }
        });
        txtQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantityActionPerformed(evt);
            }
        });

        jLabel10.setText("Total Price");

        txtTotalPrice.setEditable(false);

        jLabel11.setText("Sales Price");

        btnProductAdd.setText("Add");
        btnProductAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductAddMouseClicked(evt);
            }
        });

        btnProductDelete.setBackground(new java.awt.Color(102, 0, 51));
        btnProductDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnProductDelete.setText("Delete");
        btnProductDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductDeleteMouseClicked(evt);
            }
        });

        btnProductEdit.setBackground(new java.awt.Color(0, 153, 51));
        btnProductEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnProductEdit.setText("Edit");
        btnProductEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductEditMouseClicked(evt);
            }
        });

        btnProductReset.setBackground(new java.awt.Color(51, 51, 51));
        btnProductReset.setForeground(new java.awt.Color(255, 255, 255));
        btnProductReset.setText("Reset");
        btnProductReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductResetMouseClicked(evt);
            }
        });

        jLabel12.setText("Unit Price");

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

        javax.swing.GroupLayout AddLayout = new javax.swing.GroupLayout(Add);
        Add.setLayout(AddLayout);
        AddLayout.setHorizontalGroup(
            AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(AddLayout.createSequentialGroup()
                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AddLayout.createSequentialGroup()
                        .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(AddLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(AddLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8))
                            .addGroup(AddLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12))
                            .addGroup(AddLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9))
                            .addGroup(AddLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10))
                            .addGroup(AddLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel11)))
                        .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(AddLayout.createSequentialGroup()
                                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(AddLayout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AddLayout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(106, 106, 106)
                                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(AddLayout.createSequentialGroup()
                                        .addComponent(btnProductEdit)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnProductReset))
                                    .addGroup(AddLayout.createSequentialGroup()
                                        .addComponent(btnProductAdd)
                                        .addGap(150, 150, 150)
                                        .addComponent(btnProductDelete))))
                            .addGroup(AddLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                                    .addComponent(txtTotalPrice)
                                    .addComponent(txtSalesPrice))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        AddLayout.setVerticalGroup(
            AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnProductAdd)
                        .addComponent(btnProductDelete)))
                .addGap(17, 17, 17)
                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AddLayout.createSequentialGroup()
                        .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProductEdit)
                            .addComponent(btnProductReset))
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AddLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AddLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addGroup(AddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtSalesPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainView.addTab("Add", Add);

        jLabel3.setText("Sales");

        javax.swing.GroupLayout SalesLayout = new javax.swing.GroupLayout(Sales);
        Sales.setLayout(SalesLayout);
        SalesLayout.setHorizontalGroup(
            SalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesLayout.createSequentialGroup()
                .addGap(296, 296, 296)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(502, Short.MAX_VALUE))
        );
        SalesLayout.setVerticalGroup(
            SalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesLayout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(245, Short.MAX_VALUE))
        );

        mainView.addTab("Sales", Sales);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel4.setText("Stock");

        javax.swing.GroupLayout StockLayout = new javax.swing.GroupLayout(Stock);
        Stock.setLayout(StockLayout);
        StockLayout.setHorizontalGroup(
            StockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StockLayout.createSequentialGroup()
                .addGroup(StockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(StockLayout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(StockLayout.createSequentialGroup()
                        .addGap(291, 291, 291)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(463, Short.MAX_VALUE))
        );
        StockLayout.setVerticalGroup(
            StockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StockLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(138, 138, 138)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(192, Short.MAX_VALUE))
        );

        mainView.addTab("Stock", Stock);

        jLabel5.setText("Report");

        javax.swing.GroupLayout ReportLayout = new javax.swing.GroupLayout(Report);
        Report.setLayout(ReportLayout);
        ReportLayout.setHorizontalGroup(
            ReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportLayout.createSequentialGroup()
                .addGap(235, 235, 235)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(433, Short.MAX_VALUE))
        );
        ReportLayout.setVerticalGroup(
            ReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportLayout.createSequentialGroup()
                .addContainerGap(185, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(198, 198, 198))
        );

        mainView.addTab("Report", Report);

        getContentPane().add(mainView, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 870, 580));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddProductMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(0);
    }//GEN-LAST:event_btnAddProductMouseClicked

    private void btnSalesProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesProductMouseClicked
        // TODO add your handling code here:

        mainView.setSelectedIndex(1);
    }//GEN-LAST:event_btnSalesProductMouseClicked

    private void btnStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStockMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(2);
    }//GEN-LAST:event_btnStockMouseClicked

    private void btnReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseClicked
        // TODO add your handling code here:
        mainView.setSelectedIndex(3);
    }//GEN-LAST:event_btnReportMouseClicked

    private void txtQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantityActionPerformed

    private void btnProductAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductAddMouseClicked
        // TODO add your handling code here:
        addProduct();

    }//GEN-LAST:event_btnProductAddMouseClicked

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

    private void btnProductEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductEditMouseClicked
        // TODO add your handling code here:
        editProduct();
    }//GEN-LAST:event_btnProductEditMouseClicked

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
    private javax.swing.JPanel Add;
    private javax.swing.JPanel Report;
    private javax.swing.JPanel Sales;
    private javax.swing.JPanel Stock;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnProductAdd;
    private javax.swing.JButton btnProductDelete;
    private javax.swing.JButton btnProductEdit;
    private javax.swing.JButton btnProductReset;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnSalesProduct;
    private javax.swing.JButton btnStock;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane mainView;
    private javax.swing.JTable tblProductView;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSalesPrice;
    private javax.swing.JTextField txtTotalPrice;
    private javax.swing.JTextField txtUnitPrice;
    // End of variables declaration//GEN-END:variables
}
