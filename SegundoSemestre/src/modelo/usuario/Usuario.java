package modelo.usuario;

/**
 *
 * @author Nicolay, David, Joshep, Sebastian.
 */
public class Usuario {

    private final String usuario;
    private final String nombre;
    private final String password;

    public Usuario(String nombre, String password, String usuario) {
        this.nombre = nombre;
        this.password = password;
        this.usuario = usuario;
    }

    /**
     * El usuario registrado por la persona sera devuelto a partir de getUsuario()
     * @return devuelve el usuario creado por la persona.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * El nombre ingresado por la persona sera devuelto a partir de getNombre()
     * @return devuelve el nombre de la persona.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * La contraseña ingresado por el usuario sera devuelto a partir de getPassword()
     * @return devuelve la contraseña de la persona.
     */
    public String getPassword() {
        return password;
    }
}
