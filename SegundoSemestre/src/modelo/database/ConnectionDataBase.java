package modelo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConnectionDataBase {

    /* INFORMACION_VENTA, GASTOS y USUARIO son sentencias 
    diseñadas para poder insertar registro.  
     */
    public final String INFORMACION_VENTA = "INSERT INTO informacionventa "
            + "(fecha, bollo_angelito_traido, bollo_angelito_quedado, "
            + "bollo_angelito_total, bollo_limpias_traido, bollo_limpias_quedado, bollo_limpias_total, "
            + "bollo_mazorca_traido, bollo_mazorca_quedado, bollo_mazorca_total, bollo_yuca_traido, "
            + "bollo_yuca_quedado, bollo_yuca_total, bollo_yuca_dulce_traido, bollo_yuca_dulce_quedado, "
            + "bollo_yuca_dulce_total, usuario_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
// 17
    public final String GASTOS = "INSERT INTO gastos "
            + "(fecha, servilletas, bolsas, pasajes, usuario_idGasto) VALUES (?, ?, ?, ?, ?)";
    public final String USUARIO = "INSERT INTO Usuario "
            + "(id, nombre, usuario, password) VALUES (?, ?, ?, ?)";
    public final String LOGIN = "SELECT * FROM usuario WHERE usuario = ? AND password = ?";

    // TERMINA
    private static final ConnectionDataBase DATA_BASE = new ConnectionDataBase();
    private final String URL = "jdbc:mysql://localhost:3306/proyectofinal";
    private Connection connection;

    /**
     * Devuelve un objeto de tipo ConnectionDataBase
     * @return instancia de ConnectionDataBase
     */
    public static ConnectionDataBase getInstancia() {
        return DATA_BASE;
    }

    private ConnectionDataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, "root", "123456789**");
            System.out.println("Nice");
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
