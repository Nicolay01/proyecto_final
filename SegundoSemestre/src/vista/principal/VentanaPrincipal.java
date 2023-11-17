/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.principal;

import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;
import modelo.database.ConnectionDataBase;
import java.sql.PreparedStatement;
import modelo.usuario.Usuario;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import modelo.bollo.BolloAngelito;
import modelo.bollo.BolloLimpio;
import modelo.bollo.BolloMazorca;
import modelo.bollo.BolloYuca;
import modelo.bollo.BolloYucaDulce;
import modelo.gasto.Gastos;

/**
 *
 * @author decar
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    private int id = -1;
    private final Usuario usuario;
    private BolloAngelito angelito;
    private BolloLimpio limpio;
    private BolloMazorca mazorca;
    private BolloYuca yuca;
    private BolloYucaDulce yucaDulce;
    private Gastos gastos;

    private Object[] informacionVenta = {
        "id",
        "fecha",
        "bollo_angelito_traido",
        "bollo_angelito_quedado",
        "bollo_angelito_total",
        "bollo_limpias_traido",
        "bollo_limpias_quedado",
        "bollo_limpias_total",
        "bollo_mazorca_traido",
        "bollo_mazorca_quedado",
        "bollo_mazorca_total",
        "bollo_yuca_traido",
        "bollo_yuca_quedado",
        "bollo_yuca_total",
        "bollo_yuca_dulce_traido",
        "bollo_yuca_dulce_quedado",
        "bollo_yuca_dulce_total"
    };

    private Object[] infogastos = {
        "fecha",
        "servilletas",
        "bolsas",
        "pasajes",};

    private DefaultTableModel modelInformacion = new DefaultTableModel(informacionVenta, informacionVenta.length);
    private DefaultTableModel modelGastos = new DefaultTableModel(infogastos, infogastos.length);

    /**
     * Creates new form VentanaPrincipal
     *
     * @param usuario
     */
    public VentanaPrincipal(Usuario usuario) {
        initComponents();
        this.usuario = usuario;
        this.id = getID(usuario);

        // Iniciarlizar
        jTable1.setModel(modelInformacion);
        jTable2.setModel(modelGastos);

        System.out.println(java.sql.Date.valueOf(LocalDate.now()).getDay());

        if (java.sql.Date.valueOf(LocalDate.now()).getDay() != 5) {
            Date date = new Date();
            int diferencia = date.getDay() - 5;
            diferencia = diferencia < 0 ? diferencia * (-1) : diferencia;
            jLabel3.setText("Faltan " + diferencia + " dia para bollo de yuca dulce.");
            textTraidoYucaDulce.setEnabled(false);
            textQuedadoYucaDulce.setEnabled(false);
        } else {
            jLabel3.setText("Dia vierne bollo yuca dulce disponible.");
        }

        jLabel4.setText("Usuario: " + usuario.getUsuario());

        cargarRegistroInformacionVenta();
        cargarRegistroGastos();
//        System.out.println(LocalDate.now());
    }

    public void cargarRegistroGastos() {
        try (PreparedStatement ps = ConnectionDataBase
                .getInstancia()
                .getConnection()
                .prepareStatement("SELECT * FROM gastos WHERE usuario_idGasto = ?")) {

            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();

            modelGastos.setRowCount(0);

            while (resultSet.next()) {
                Object[] rowData = {
                    resultSet.getDate("fecha"), // Cambiado a getDate si la columna es de tipo fecha
                    resultSet.getInt("servilletas"),
                    resultSet.getInt("bolsas"),
                    resultSet.getInt("pasajes")
                };
                modelGastos.addRow(rowData);
            }

            // Asignar el modelo de datos a la jTable (si es necesario)
            jTable2.setModel(modelGastos);

        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.getMessage());
        }

    }

    public void cargarRegistroInformacionVenta() {
        try (PreparedStatement ps = ConnectionDataBase
                .getInstancia()
                .getConnection()
                .prepareStatement("SELECT * FROM informacionventa WHERE usuario_id = ?");) {
            ps.setInt(1, id);

            // Ejecutar la consulta
            ResultSet resultSet = ps.executeQuery();
            // Limpiar el modelo de la tabla antes de agregar nuevos datos
            modelInformacion.setRowCount(0);

            while (resultSet.next()) {
                Object[] rowData = {
                    resultSet.getInt("id"),
                    resultSet.getDate("fecha"),
                    resultSet.getInt("bollo_angelito_traido"),
                    resultSet.getInt("bollo_angelito_quedado"),
                    resultSet.getInt("bollo_angelito_total"),
                    resultSet.getInt("bollo_limpias_traido"),
                    resultSet.getInt("bollo_limpias_quedado"),
                    resultSet.getInt("bollo_limpias_total"),
                    resultSet.getInt("bollo_mazorca_traido"),
                    resultSet.getInt("bollo_mazorca_quedado"),
                    resultSet.getInt("bollo_mazorca_total"),
                    resultSet.getInt("bollo_yuca_traido"),
                    resultSet.getInt("bollo_yuca_quedado"),
                    resultSet.getInt("bollo_yuca_total"),
                    resultSet.getInt("bollo_yuca_dulce_traido"),
                    resultSet.getInt("bollo_yuca_dulce_quedado"),
                    resultSet.getInt("bollo_yuca_dulce_total"), // ... (otros campos)
                };
                modelInformacion.addRow(rowData);
//                jTable1.setModel(modelInformacion);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Devuelve el ID del usuario, atravez de su usuario registrado.
     *
     * @param usuario
     * @return
     */
    private int getID(Usuario usuario) {
        String literal = "SELECT id FROM usuario WHERE usuario = ?";
        try (PreparedStatement pt = ConnectionDataBase.
                getInstancia().getConnection().prepareStatement(literal);) {
            pt.setString(1, usuario.getUsuario());

            try (ResultSet resultSet = pt.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("ID del usuario '" + usuario.getUsuario() + "': " + resultSet.getInt("id"));
                    jLabel7.setText("ID: " + this.id);
                    return resultSet.getInt("id");
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    // DOCUMENTAR TODO LO QUE SIGUE ABAJO
    private void updateBolloAngelito() {
        try {
            angelito.setBolloTraido(Integer.parseInt(textTraidoAngelito.getText()));
            angelito.setBolloQuedado(Integer.parseInt(textQuedadoAngelito.getText()));
            textTotalAngelito.setText(Integer.toString(angelito.total()));
        } catch (NumberFormatException ex) {
            // Manejar la excepción si los valores no son números válidos
            textTotalAngelito.setText("0");
        }
    }

    private void updateBolloLimpio() {
        try {
            limpio.setBolloTraido(Integer.parseInt(textTraidoLimpio.getText()));
            limpio.setBolloQuedado(Integer.parseInt(textQuedadoLimpio.getText()));
            textTotalLimpio.setText(Integer.toString(limpio.total()));
        } catch (NumberFormatException ex) {
            textTotalLimpio.setText("0");
        }
    }

    private void updateBolloMazorca() {
        try {
            mazorca.setBolloTraido(Integer.parseInt(textTraidoMazorca.getText()));
            mazorca.setBolloQuedado(Integer.parseInt(textQuedadoMazorca.getText()));
            textTotalMazorca.setText(Integer.toString(mazorca.total()));
        } catch (NumberFormatException ex) {
            textTotalMazorca.setText("0");
        }
    }

    private void updateBolloYuca() {
        try {
            yuca.setBolloTraido(Integer.parseInt(textTraidoYuca.getText()));
            yuca.setBolloQuedado(Integer.parseInt(textQuedadoYuca.getText()));
            textTotalYuca.setText(Integer.toString(yuca.total()));
        } catch (NumberFormatException ex) {
            textTotalYuca.setText("0");
        }
    }

    private void updateBolloYucaDulce() {
        try {
            yucaDulce.setBolloTraido(Integer.parseInt(textTraidoYucaDulce.getText()));
            yucaDulce.setBolloQuedado(Integer.parseInt(textQuedadoYucaDulce.getText()));
            textTotalYucaDulce.setText(Integer.toString(yucaDulce.total()));
        } catch (NumberFormatException ex) {
            textTotalYucaDulce.setText("0");
        }
    }

    private void updateGastos() {
        try {
            gastos.setServilletas(Integer.parseInt(textServilleta.getText()));
            gastos.setBolsas(Integer.parseInt(textBolsas.getText()));
            gastos.setPasajes(Integer.parseInt(textPasajes.getText()));
        } catch (NumberFormatException ex) {
            textServilleta.setText("0");
            textBolsas.setText("0");
            textPasajes.setText("0");
        }
    }

    // Metodo para registro de datos en sql
    public void registroInformacionBolloSQL() {
        try (PreparedStatement ps = ConnectionDataBase.getInstancia()
                .getConnection().prepareStatement(ConnectionDataBase.
                        getInstancia().INFORMACION_VENTA);) {
            // fecha
            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            // bollo angelito
            ps.setInt(2, angelito.getBolloTraido());
            ps.setInt(3, angelito.getBolloQuedado());
            ps.setInt(4, angelito.total());
            // bollo limpio
            ps.setInt(5, limpio.getBolloTraido());
            ps.setInt(6, limpio.getBolloQuedado());
            ps.setInt(7, limpio.total());
            // bollo mazorca
            ps.setInt(8, mazorca.getBolloTraido());
            ps.setInt(9, mazorca.getBolloQuedado());
            ps.setInt(10, mazorca.total());
            // bollo yuca
            ps.setInt(11, yuca.getBolloTraido());
            ps.setInt(12, yuca.getBolloQuedado());
            ps.setInt(13, yuca.total());
            // bollo yuca dulce
            ps.setInt(14, yucaDulce.getBolloTraido());
            ps.setInt(15, yucaDulce.getBolloQuedado());
            ps.setInt(16, yucaDulce.total());
            // id user
            ps.setInt(17, id);

            int filasAfectadas = ps.executeUpdate();
            System.out.println("Filas Afectadas: " + filasAfectadas);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void registroGastosSQL() {
        // cargar los ultimos datos ingresados en gastos
        updateGastos();
        try (PreparedStatement ps = ConnectionDataBase.getInstancia()
                .getConnection().prepareStatement(ConnectionDataBase.
                        getInstancia().GASTOS);) {
            // fecha actual (devuelve la fecha de hoy cuando estas utilziando el programa)
            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            // servilleta
            ps.setInt(2, gastos.getServilletas());
            // bolsas
            ps.setInt(3, gastos.getBolsas());
            // pasaje
            ps.setInt(4, gastos.getPasajes());
            // id del usuario
            ps.setInt(5, id);

            int filasAfectadas = ps.executeUpdate();
            System.out.println("Filas afectadas: " + filasAfectadas);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
        java.awt.GridBagConstraints gridBagConstraints;

        rootPanel = new javax.swing.JPanel();
        subPanelesTabbed = new javax.swing.JTabbedPane();
        Tabla = new javax.swing.JPanel();
        panelBolloInformacion = new javax.swing.JPanel();
        labelBolloAngelito = new javax.swing.JLabel();
        labelTraido = new javax.swing.JLabel();
        labelQuedado = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        textTraidoAngelito = new javax.swing.JTextField();
        textQuedadoAngelito = new javax.swing.JTextField();
        textTotalAngelito = new javax.swing.JTextField();
        labelBolloLimpio = new javax.swing.JLabel();
        textTraidoLimpio = new javax.swing.JTextField();
        textQuedadoLimpio = new javax.swing.JTextField();
        textTotalLimpio = new javax.swing.JTextField();
        labelBolloMarzorca = new javax.swing.JLabel();
        textTraidoMazorca = new javax.swing.JTextField();
        textQuedadoMazorca = new javax.swing.JTextField();
        textTotalMazorca = new javax.swing.JTextField();
        labelBolloYuca = new javax.swing.JLabel();
        textTraidoYuca = new javax.swing.JTextField();
        textQuedadoYuca = new javax.swing.JTextField();
        textTotalYuca = new javax.swing.JTextField();
        labelBolloYucaDulce = new javax.swing.JLabel();
        textTraidoYucaDulce = new javax.swing.JTextField();
        textQuedadoYucaDulce = new javax.swing.JTextField();
        textTotalYucaDulce = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        labelServilletas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        labelBolsas = new javax.swing.JLabel();
        labelPasajes = new javax.swing.JLabel();
        textServilleta = new javax.swing.JTextField();
        textBolsas = new javax.swing.JTextField();
        textPasajes = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        buttonRegistrar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        grafico = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ventana Principal");

        rootPanel.setBackground(new java.awt.Color(255, 255, 255));

        subPanelesTabbed.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        Tabla.setBackground(new java.awt.Color(255, 255, 255));

        panelBolloInformacion.setBackground(new java.awt.Color(255, 255, 255));
        panelBolloInformacion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bollo informacion", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        panelBolloInformacion.setToolTipText("");
        panelBolloInformacion.setLayout(new java.awt.GridBagLayout());

        labelBolloAngelito.setText("Bollo angelito");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 11, 0, 0);
        panelBolloInformacion.add(labelBolloAngelito, gridBagConstraints);

        labelTraido.setText("Traidos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 30, 0, 0);
        panelBolloInformacion.add(labelTraido, gridBagConstraints);

        labelQuedado.setText("Quedados");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 35, 0, 0);
        panelBolloInformacion.add(labelQuedado, gridBagConstraints);

        labelTotal.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 34, 0, 0);
        panelBolloInformacion.add(labelTotal, gridBagConstraints);

        textTraidoAngelito.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(25, 12, 0, 0);
        panelBolloInformacion.add(textTraidoAngelito, gridBagConstraints);
        textTraidoAngelito.getDocument().addDocumentListener(
            new DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateBolloAngelito();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateBolloAngelito();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateBolloAngelito();
                }
            });

            textQuedadoAngelito.setText("0");
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.ipadx = 7;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(25, 18, 0, 0);
            panelBolloInformacion.add(textQuedadoAngelito, gridBagConstraints);
            textQuedadoAngelito.getDocument().addDocumentListener(
                new DocumentListener(){
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        updateBolloAngelito();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        updateBolloAngelito();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        updateBolloAngelito();
                    }
                });

                textTotalAngelito.setEnabled(false);
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 7;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.ipadx = 7;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(25, 18, 0, 22);
                panelBolloInformacion.add(textTotalAngelito, gridBagConstraints);

                labelBolloLimpio.setText("Bollo limpio");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.ipadx = 9;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(21, 11, 0, 0);
                panelBolloInformacion.add(labelBolloLimpio, gridBagConstraints);

                textTraidoLimpio.setText("0");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 4;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.gridheight = 2;
                gridBagConstraints.ipadx = 9;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                gridBagConstraints.insets = new java.awt.Insets(18, 12, 0, 0);
                panelBolloInformacion.add(textTraidoLimpio, gridBagConstraints);
                textTraidoLimpio.getDocument().addDocumentListener(
                    new DocumentListener(){
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            updateBolloLimpio();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            updateBolloLimpio();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            updateBolloLimpio();
                        }
                    });

                    textQuedadoLimpio.setText("0");
                    gridBagConstraints = new java.awt.GridBagConstraints();
                    gridBagConstraints.gridx = 6;
                    gridBagConstraints.gridy = 3;
                    gridBagConstraints.gridheight = 2;
                    gridBagConstraints.ipadx = 7;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                    gridBagConstraints.insets = new java.awt.Insets(18, 18, 0, 0);
                    panelBolloInformacion.add(textQuedadoLimpio, gridBagConstraints);
                    textQuedadoLimpio.getDocument().addDocumentListener(
                        new DocumentListener(){
                            @Override
                            public void insertUpdate(DocumentEvent e) {
                                updateBolloLimpio();
                            }

                            @Override
                            public void removeUpdate(DocumentEvent e) {
                                updateBolloLimpio();
                            }

                            @Override
                            public void changedUpdate(DocumentEvent e) {
                                updateBolloLimpio();
                            }
                        });

                        textTotalLimpio.setEnabled(false);
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 7;
                        gridBagConstraints.gridy = 3;
                        gridBagConstraints.gridwidth = 2;
                        gridBagConstraints.gridheight = 2;
                        gridBagConstraints.ipadx = 7;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.insets = new java.awt.Insets(18, 18, 0, 22);
                        panelBolloInformacion.add(textTotalLimpio, gridBagConstraints);

                        labelBolloMarzorca.setText("Bollo Marzorca");
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 5;
                        gridBagConstraints.gridwidth = 3;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.insets = new java.awt.Insets(21, 11, 0, 0);
                        panelBolloInformacion.add(labelBolloMarzorca, gridBagConstraints);

                        textTraidoMazorca.setText("0");
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 4;
                        gridBagConstraints.gridy = 5;
                        gridBagConstraints.gridwidth = 2;
                        gridBagConstraints.gridheight = 2;
                        gridBagConstraints.ipadx = 9;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        gridBagConstraints.insets = new java.awt.Insets(18, 12, 0, 0);
                        panelBolloInformacion.add(textTraidoMazorca, gridBagConstraints);
                        textTraidoMazorca.getDocument().addDocumentListener(
                            new DocumentListener(){
                                @Override
                                public void insertUpdate(DocumentEvent e) {
                                    updateBolloMazorca();
                                }

                                @Override
                                public void removeUpdate(DocumentEvent e) {
                                    updateBolloMazorca();
                                }

                                @Override
                                public void changedUpdate(DocumentEvent e) {
                                    updateBolloMazorca();
                                }
                            });

                            textQuedadoMazorca.setText("0");
                            gridBagConstraints = new java.awt.GridBagConstraints();
                            gridBagConstraints.gridx = 6;
                            gridBagConstraints.gridy = 5;
                            gridBagConstraints.gridheight = 2;
                            gridBagConstraints.ipadx = 7;
                            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                            gridBagConstraints.insets = new java.awt.Insets(18, 18, 0, 0);
                            panelBolloInformacion.add(textQuedadoMazorca, gridBagConstraints);
                            textQuedadoMazorca.getDocument().addDocumentListener(
                                new DocumentListener(){
                                    @Override
                                    public void insertUpdate(DocumentEvent e) {
                                        updateBolloMazorca();
                                    }

                                    @Override
                                    public void removeUpdate(DocumentEvent e) {
                                        updateBolloMazorca();
                                    }

                                    @Override
                                    public void changedUpdate(DocumentEvent e) {
                                        updateBolloMazorca();
                                    }
                                });

                                textTotalMazorca.setEnabled(false);
                                gridBagConstraints = new java.awt.GridBagConstraints();
                                gridBagConstraints.gridx = 7;
                                gridBagConstraints.gridy = 5;
                                gridBagConstraints.gridwidth = 2;
                                gridBagConstraints.gridheight = 2;
                                gridBagConstraints.ipadx = 7;
                                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                gridBagConstraints.insets = new java.awt.Insets(18, 18, 0, 22);
                                panelBolloInformacion.add(textTotalMazorca, gridBagConstraints);

                                labelBolloYuca.setText("Bollo yuca");
                                gridBagConstraints = new java.awt.GridBagConstraints();
                                gridBagConstraints.gridx = 0;
                                gridBagConstraints.gridy = 7;
                                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                gridBagConstraints.insets = new java.awt.Insets(21, 11, 0, 0);
                                panelBolloInformacion.add(labelBolloYuca, gridBagConstraints);

                                textTraidoYuca.setText("0");
                                gridBagConstraints = new java.awt.GridBagConstraints();
                                gridBagConstraints.gridx = 4;
                                gridBagConstraints.gridy = 7;
                                gridBagConstraints.gridwidth = 2;
                                gridBagConstraints.gridheight = 2;
                                gridBagConstraints.ipadx = 9;
                                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                gridBagConstraints.insets = new java.awt.Insets(18, 12, 0, 0);
                                panelBolloInformacion.add(textTraidoYuca, gridBagConstraints);
                                textTraidoYuca.getDocument().addDocumentListener(
                                    new DocumentListener(){
                                        @Override
                                        public void insertUpdate(DocumentEvent e) {
                                            updateBolloYuca();
                                        }

                                        @Override
                                        public void removeUpdate(DocumentEvent e) {
                                            updateBolloYuca();
                                        }

                                        @Override
                                        public void changedUpdate(DocumentEvent e) {
                                            updateBolloYuca();
                                        }
                                    });

                                    textQuedadoYuca.setText("0");
                                    gridBagConstraints = new java.awt.GridBagConstraints();
                                    gridBagConstraints.gridx = 6;
                                    gridBagConstraints.gridy = 7;
                                    gridBagConstraints.gridheight = 2;
                                    gridBagConstraints.ipadx = 7;
                                    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                    gridBagConstraints.insets = new java.awt.Insets(18, 18, 0, 0);
                                    panelBolloInformacion.add(textQuedadoYuca, gridBagConstraints);
                                    textQuedadoYuca.getDocument().addDocumentListener(
                                        new DocumentListener(){
                                            @Override
                                            public void insertUpdate(DocumentEvent e) {
                                                updateBolloYuca();
                                            }

                                            @Override
                                            public void removeUpdate(DocumentEvent e) {
                                                updateBolloYuca();
                                            }

                                            @Override
                                            public void changedUpdate(DocumentEvent e) {
                                                updateBolloYuca();
                                            }
                                        });

                                        textTotalYuca.setEnabled(false);
                                        gridBagConstraints = new java.awt.GridBagConstraints();
                                        gridBagConstraints.gridx = 7;
                                        gridBagConstraints.gridy = 7;
                                        gridBagConstraints.gridwidth = 2;
                                        gridBagConstraints.gridheight = 2;
                                        gridBagConstraints.ipadx = 7;
                                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                        gridBagConstraints.insets = new java.awt.Insets(18, 18, 0, 22);
                                        panelBolloInformacion.add(textTotalYuca, gridBagConstraints);

                                        labelBolloYucaDulce.setText("Bollo yuca dulce");
                                        gridBagConstraints = new java.awt.GridBagConstraints();
                                        gridBagConstraints.gridx = 0;
                                        gridBagConstraints.gridy = 9;
                                        gridBagConstraints.gridwidth = 4;
                                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                        gridBagConstraints.insets = new java.awt.Insets(21, 11, 0, 0);
                                        panelBolloInformacion.add(labelBolloYucaDulce, gridBagConstraints);

                                        textTraidoYucaDulce.setText("0");
                                        gridBagConstraints = new java.awt.GridBagConstraints();
                                        gridBagConstraints.gridx = 4;
                                        gridBagConstraints.gridy = 9;
                                        gridBagConstraints.gridwidth = 2;
                                        gridBagConstraints.gridheight = 2;
                                        gridBagConstraints.ipadx = 9;
                                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                        gridBagConstraints.insets = new java.awt.Insets(18, 12, 25, 0);
                                        panelBolloInformacion.add(textTraidoYucaDulce, gridBagConstraints);
                                        textTraidoYucaDulce.getDocument().addDocumentListener(
                                            new DocumentListener(){
                                                @Override
                                                public void insertUpdate(DocumentEvent e) {
                                                    updateBolloYucaDulce();
                                                }

                                                @Override
                                                public void removeUpdate(DocumentEvent e) {
                                                    updateBolloYucaDulce();
                                                }

                                                @Override
                                                public void changedUpdate(DocumentEvent e) {
                                                    updateBolloYucaDulce();
                                                }
                                            });

                                            textQuedadoYucaDulce.setText("0");
                                            gridBagConstraints = new java.awt.GridBagConstraints();
                                            gridBagConstraints.gridx = 6;
                                            gridBagConstraints.gridy = 9;
                                            gridBagConstraints.gridheight = 2;
                                            gridBagConstraints.ipadx = 7;
                                            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                            gridBagConstraints.insets = new java.awt.Insets(18, 18, 25, 0);
                                            panelBolloInformacion.add(textQuedadoYucaDulce, gridBagConstraints);
                                            textQuedadoYucaDulce.getDocument().addDocumentListener(
                                                new DocumentListener(){
                                                    @Override
                                                    public void insertUpdate(DocumentEvent e) {
                                                        updateBolloYucaDulce();
                                                    }

                                                    @Override
                                                    public void removeUpdate(DocumentEvent e) {
                                                        updateBolloYucaDulce();
                                                    }

                                                    @Override
                                                    public void changedUpdate(DocumentEvent e) {
                                                        updateBolloYucaDulce();
                                                    }
                                                });

                                                textTotalYucaDulce.setEnabled(false);
                                                gridBagConstraints = new java.awt.GridBagConstraints();
                                                gridBagConstraints.gridx = 7;
                                                gridBagConstraints.gridy = 9;
                                                gridBagConstraints.gridwidth = 2;
                                                gridBagConstraints.gridheight = 2;
                                                gridBagConstraints.ipadx = 7;
                                                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                                gridBagConstraints.insets = new java.awt.Insets(18, 18, 25, 22);
                                                panelBolloInformacion.add(textTotalYucaDulce, gridBagConstraints);

                                                jPanel3.setBackground(new java.awt.Color(255, 255, 255));
                                                jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tabla", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

                                                jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
                                                jScrollPane1.setViewportView(jTable1);

                                                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                                                jPanel3.setLayout(jPanel3Layout);
                                                jPanel3Layout.setHorizontalGroup(
                                                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(jScrollPane1)
                                                        .addContainerGap())
                                                );
                                                jPanel3Layout.setVerticalGroup(
                                                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                );

                                                labelServilletas.setBackground(new java.awt.Color(255, 255, 255));
                                                labelServilletas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Gastos adicionales", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

                                                jLabel1.setText("Servilletas");

                                                labelBolsas.setText("Bolsas");

                                                labelPasajes.setText("Pasajes");

                                                textServilleta.setText("0");

                                                textBolsas.setText("0");

                                                textPasajes.setText("0");

                                                jLabel2.setText("Total");

                                                jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
                                                jScrollPane2.setViewportView(jTable2);

                                                javax.swing.GroupLayout labelServilletasLayout = new javax.swing.GroupLayout(labelServilletas);
                                                labelServilletas.setLayout(labelServilletasLayout);
                                                labelServilletasLayout.setHorizontalGroup(
                                                    labelServilletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(labelServilletasLayout.createSequentialGroup()
                                                        .addGap(26, 26, 26)
                                                        .addGroup(labelServilletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(labelServilletasLayout.createSequentialGroup()
                                                                .addComponent(labelBolsas, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(textBolsas, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(labelServilletasLayout.createSequentialGroup()
                                                                .addComponent(labelPasajes, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(26, 26, 26)
                                                                .addComponent(textPasajes, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(labelServilletasLayout.createSequentialGroup()
                                                                .addComponent(jLabel1)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(labelServilletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(textServilleta, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, labelServilletasLayout.createSequentialGroup()
                                                                        .addComponent(jLabel2)
                                                                        .addGap(29, 29, 29)))))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                                                        .addContainerGap())
                                                );
                                                labelServilletasLayout.setVerticalGroup(
                                                    labelServilletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(labelServilletasLayout.createSequentialGroup()
                                                        .addGap(29, 29, 29)
                                                        .addComponent(jLabel2)
                                                        .addGap(18, 18, 18)
                                                        .addGroup(labelServilletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel1)
                                                            .addComponent(textServilleta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(labelServilletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(textBolsas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(labelBolsas))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(labelServilletasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(textPasajes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(labelPasajes))
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, labelServilletasLayout.createSequentialGroup()
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap())
                                                );

                                                jPanel1.setBackground(new java.awt.Color(255, 255, 255));
                                                jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Acciones", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

                                                buttonRegistrar.setText("Registrar");
                                                buttonRegistrar.addActionListener(new java.awt.event.ActionListener() {
                                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                        buttonRegistrarActionPerformed(evt);
                                                    }
                                                });

                                                jLabel3.setText("dia");

                                                jLabel4.setText("jLabel4");

                                                jLabel5.setText("Filtro fecha");

                                                jRadioButton1.setText("Gastos");

                                                jRadioButton2.setText("Tabla");

                                                jLabel7.setText("ID: " + this.id);

                                                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                                                jPanel1.setLayout(jPanel1Layout);
                                                jPanel1Layout.setHorizontalGroup(
                                                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel5)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jRadioButton1)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(61, Short.MAX_VALUE))
                                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(buttonRegistrar))
                                                                .addGap(0, 0, Short.MAX_VALUE))))
                                                );
                                                jPanel1Layout.setVerticalGroup(
                                                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(jLabel3)
                                                        .addGap(14, 14, 14)
                                                        .addComponent(jLabel4)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel7)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(buttonRegistrar)
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel5)
                                                            .addComponent(jRadioButton1)
                                                            .addComponent(jRadioButton2))
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                );

                                                javax.swing.GroupLayout TablaLayout = new javax.swing.GroupLayout(Tabla);
                                                Tabla.setLayout(TablaLayout);
                                                TablaLayout.setHorizontalGroup(
                                                    TablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(TablaLayout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addGroup(TablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, TablaLayout.createSequentialGroup()
                                                                .addComponent(panelBolloInformacion, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(labelServilletas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                );
                                                TablaLayout.setVerticalGroup(
                                                    TablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(TablaLayout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addGroup(TablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(labelServilletas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(panelBolloInformacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                );

                                                subPanelesTabbed.addTab("registro", Tabla);

                                                javax.swing.GroupLayout graficoLayout = new javax.swing.GroupLayout(grafico);
                                                grafico.setLayout(graficoLayout);
                                                graficoLayout.setHorizontalGroup(
                                                    graficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGap(0, 1463, Short.MAX_VALUE)
                                                );
                                                graficoLayout.setVerticalGroup(
                                                    graficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGap(0, 783, Short.MAX_VALUE)
                                                );

                                                subPanelesTabbed.addTab("grafico", grafico);

                                                javax.swing.GroupLayout rootPanelLayout = new javax.swing.GroupLayout(rootPanel);
                                                rootPanel.setLayout(rootPanelLayout);
                                                rootPanelLayout.setHorizontalGroup(
                                                    rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(subPanelesTabbed, javax.swing.GroupLayout.Alignment.TRAILING)
                                                );
                                                rootPanelLayout.setVerticalGroup(
                                                    rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(subPanelesTabbed, javax.swing.GroupLayout.Alignment.TRAILING)
                                                );

                                                subPanelesTabbed.getAccessibleContext().setAccessibleName("informacion");
                                                subPanelesTabbed.getAccessibleContext().setAccessibleDescription("");

                                                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                                                getContentPane().setLayout(layout);
                                                layout.setHorizontalGroup(
                                                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                );
                                                layout.setVerticalGroup(
                                                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(rootPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                );

                                                pack();
                                            }// </editor-fold>//GEN-END:initComponents

    private void buttonRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRegistrarActionPerformed
        // TODO add your handling code here:
        System.out.println("ID: " + id);
        registroInformacionBolloSQL();
        registroGastosSQL();

        cargarRegistroGastos();
        cargarRegistroInformacionVenta();
    }//GEN-LAST:event_buttonRegistrarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new VentanaPrincipal(new Usuario("Juan Pérez", "clave123", "juanperez"))
                    .setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Tabla;
    private javax.swing.JButton buttonRegistrar;
    private javax.swing.JPanel grafico;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel labelBolloAngelito;
    private javax.swing.JLabel labelBolloLimpio;
    private javax.swing.JLabel labelBolloMarzorca;
    private javax.swing.JLabel labelBolloYuca;
    private javax.swing.JLabel labelBolloYucaDulce;
    private javax.swing.JLabel labelBolsas;
    private javax.swing.JLabel labelPasajes;
    private javax.swing.JLabel labelQuedado;
    private javax.swing.JPanel labelServilletas;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JLabel labelTraido;
    private javax.swing.JPanel panelBolloInformacion;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JTabbedPane subPanelesTabbed;
    private javax.swing.JTextField textBolsas;
    private javax.swing.JTextField textPasajes;
    private javax.swing.JTextField textQuedadoAngelito;
    private javax.swing.JTextField textQuedadoLimpio;
    private javax.swing.JTextField textQuedadoMazorca;
    private javax.swing.JTextField textQuedadoYuca;
    private javax.swing.JTextField textQuedadoYucaDulce;
    private javax.swing.JTextField textServilleta;
    private javax.swing.JTextField textTotalAngelito;
    private javax.swing.JTextField textTotalLimpio;
    private javax.swing.JTextField textTotalMazorca;
    private javax.swing.JTextField textTotalYuca;
    private javax.swing.JTextField textTotalYucaDulce;
    private javax.swing.JTextField textTraidoAngelito;
    private javax.swing.JTextField textTraidoLimpio;
    private javax.swing.JTextField textTraidoMazorca;
    private javax.swing.JTextField textTraidoYuca;
    private javax.swing.JTextField textTraidoYucaDulce;
    // End of variables declaration//GEN-END:variables
}
