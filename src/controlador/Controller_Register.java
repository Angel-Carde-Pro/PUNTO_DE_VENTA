package controlador;

import Vistas.Login_View;
import Vistas.Register_View;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import ws.Peticiones;
import ws.Peticiones_Service;
import ws.Rol;

/**
 *
 * @author angel
 */
public class Controller_Register {

    private Validaciones validaciones;

    Peticiones_Service servicio = new Peticiones_Service();
    Peticiones app = servicio.getPeticionesPort();
    private Register_View register_View;
    int idRol = app.listarroles().size() + 1;
    int idUsuario = app.listarUsuarios().size();
    ; // Debes implementar tu propia lógica para obtener el próximo ID
    int idPersona = app.listarPersonas().size();

    private ImageIcon showPassword = new ImageIcon("src\\Imagenes\\Login\\eye-open1.png"),
            dissmisPassword = new ImageIcon("src\\Imagenes\\Login\\eye-close.png");

    public Controller_Register(Register_View register_View) {
        this.register_View = register_View;
    }

    public void IniciarControl() {
        register_View.setTitle("Registro");
        register_View.setVisible(true);
        register_View.setLocationRelativeTo(null);

        register_View.getBtnVolver().addActionListener(l -> VolverBtn());
        register_View.getBtnRegistrar().addActionListener(l -> RegistrarseBtn());

        register_View.getJlabelShowPassword().setIcon(EscalarImagen(showPassword, register_View.getJlabelShowPassword()));
        register_View.getJlabelDismissPassword().setIcon(EscalarImagen(dissmisPassword, register_View.getJlabelDismissPassword()));

        register_View.getJlabelDismissPassword().setVisible(false);
        ValidarTextField();

        llenarComboBoxRoles();
    }

    private void llenarComboBoxRoles() {
        List<Rol> roles = app.listarroles();

        register_View.getjCBoxRolPersonal().removeAllItems();

        for (Rol rol : roles) {
            register_View.getjCBoxRolPersonal().addItem(rol.getRol());
        }
    }

    public ImageIcon EscalarImagen(ImageIcon imagenATransformar, JLabel tamañoPanel) {
        ImageIcon imagenEscalada = new ImageIcon(imagenATransformar.getImage().getScaledInstance(tamañoPanel.getWidth(),
                tamañoPanel.getHeight(), Image.SCALE_AREA_AVERAGING));
        return imagenEscalada;
    }

    public void RegistrarseBtn() {
        int idUsuario = obtenerProximoIdUsuario();
        int idPersona = obtenerProximoIdPersona();
        String nombreUsuario = register_View.getjTextFieldUsuario().getText();
        String contraseña = (String.valueOf(register_View.getjPasswordField().getPassword()));
        String nombrePersona = register_View.getjTextFieldNombreRegistro().getText();
        String apellidoPersona = register_View.getjTextFieldApellido().getText();
        String dniPersona = register_View.getjTextFieldCedula().getText();
        String celularPersona = register_View.getjTextFieldtelefono().getText();
        String correoPersona = register_View.getjTextFieldCorreo().getText();

        if (!esCadenaValida(nombrePersona) || !esCadenaValida(apellidoPersona)) {
            JOptionPane.showMessageDialog(null, "Nombre y apellido deben contener solo letras.");
            return;
        }

        // Validar cédula (solo números)
        if (!esNumeroValido(dniPersona)) {
            JOptionPane.showMessageDialog(null, "La cédula debe contener solo números.");
            return;
        }

        // Validar teléfono (10 dígitos)
        if (!esTelefonoValido(celularPersona)) {
            JOptionPane.showMessageDialog(null, "El teléfono debe contener 10 dígitos.");
            return;
        }

        // Validar correo electrónico
        if (!esCorreoValido(correoPersona)) {
            JOptionPane.showMessageDialog(null, "Correo electrónico no válido.");
            return;
        }

        int idRol = obtenerIdRolSeleccionado();
        String nombreRol = register_View.getjCBoxRolPersonal().getSelectedItem().toString();
        boolean estadoRol = true;
        System.out.println(idUsuario + " idUsuario");
        System.out.println(idPersona + " idPersona");

        try {
            boolean registroExitoso = app.registrarUsuarioPersonaRol(idUsuario, idPersona, nombreUsuario, contraseña, nombrePersona, apellidoPersona, dniPersona, celularPersona, correoPersona, idRol, nombreRol, estadoRol);

            if (registroExitoso) {
                JOptionPane.showMessageDialog(null, "Registro exitoso.");
                register_View.dispose();
                Login_View login_View = new Login_View();
                Controller_Login login = new Controller_Login(login_View);
                login.IniciarControl();
            } else {
                System.out.println("error");
                // Limpiar campos de texto
                limpiarCamposTexto();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Puedes imprimir el seguimiento de la pila para depurar
            JOptionPane.showMessageDialog(null, "Error durante el registro: " + e.getMessage());
        }
    }

    public void ValidarTextField() {
//        register_View.getjTextFieldNombreRegistro().addKeyListener(new java.awt.event.KeyAdapter() {
//            public void keyTyped(java.awt.event.KeyEvent evt) {
//                validaciones.IngresarSoloLetras(evt);
//            }
//        });
//        register_View.getjTextFieldCedula().addKeyListener(new java.awt.event.KeyEvent() {
//            public void keyTyped(java.awt.event.KeyEvent evt) {
//                validaciones.IngresarSoloNumeros(evt);
//            }
//        });
//        register_View.getjTextFieldtelefono().addKeyListener(new java.awt.event.KeyAdapter() {
//            public void keyTyped(java.awt.event.KeyEvent evt) {
//                validaciones.IngresarSoloNumeros(evt);
//            }
//        });
//        register_View.getjTextFieldApellido().addKeyListener(new java.awt.event.KeyAdapter() {
//            public void keyTyped(java.awt.event.KeyEvent evt) {
//                validaciones.IngresarSoloLetras(evt);
//            }
//        });

        register_View.getjTextFieldNombreRegistro().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char caracter = evt.getKeyChar();

                if (!Character.isLetter(caracter) && caracter != KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });
        register_View.getjTextFieldApellido().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char caracter = evt.getKeyChar();

                if (!Character.isLetter(caracter) && caracter != KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });
        register_View.getjTextFieldCedula().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char caracter = evt.getKeyChar();
                // Solo permitir números y el backspace
                if (!Character.isDigit(caracter) && caracter != KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });

        register_View.getBtnVolver().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VolverBtn();
            }
        });
        register_View.getBtnRegistrar().addActionListener(l -> RegistrarseBtn());

        register_View.getJlabelShowPassword().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                register_View.getjPasswordField().setEchoChar((char) 0);
                register_View.getJlabelShowPassword().setVisible(false);
                register_View.getJlabelDismissPassword().setVisible(true);
            }
        });
        register_View.getJlabelDismissPassword().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                register_View.getjPasswordField().setEchoChar('*');
                register_View.getJlabelShowPassword().setVisible(true);
                register_View.getJlabelDismissPassword().setVisible(false);
            }
        });
    }

    private int obtenerIdRolSeleccionado() {
        // Obtener el ID del rol seleccionado en el JComboBox
        String nombreRol = register_View.getjCBoxRolPersonal().getSelectedItem().toString();
        for (Rol rol : app.listarroles()) {
            if (rol.getRol().equals(nombreRol)) {
                return rol.getIdRol();
            }
        }
        return -1; // Si no se encuentra el rol, devolver -1
    }

    public int obtenerProximoIdUsuario() {
        idUsuario = idUsuario + 1;

        return idUsuario;
    }

    public int obtenerProximoIdPersona() {
        idPersona = idPersona + 1;

        return idPersona;
    }

    public int obtenerProximoIdRol() {
        idRol = idRol + 1;
        return idRol;
    }

    public void VolverBtn() {
        register_View.dispose();
        Login_View vl = new Login_View();
        Controller_Login ctl = new Controller_Login(vl);
        ctl.IniciarControl();
    }

    public static boolean esCorreoValido(String correo) {
        // Utiliza una expresión regular para validar el formato del correo electrónico
        String regexCorreo = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return correo.matches(regexCorreo);
    }

    public static boolean esTelefonoValido(String telefono) {
        // Utiliza una expresión regular para validar que el teléfono tenga 10 dígitos
        String regexTelefono = "\\d{10}";
        return telefono.matches(regexTelefono);
    }

    public static boolean esNumeroValido(String numero) {
        // Utiliza una expresión regular para validar que la cadena contenga solo números
        String regexNumero = "\\d+";
        return numero.matches(regexNumero);
    }

    public static boolean esCadenaValida(String cadena) {
        // Utiliza una expresión regular para validar que la cadena contenga solo letras
        String regexLetras = "^[a-zA-Z]+$";
        return cadena.matches(regexLetras);
    }

    private void limpiarCamposTexto() {
        register_View.getjTextFieldNombreRegistro().setText("");
        register_View.getjPasswordField().setText("");
        register_View.getjLabelUsuario().setText("");
        register_View.getjTextFieldApellido().setText("");
        register_View.getjTextFieldCedula().setText("");
        register_View.getjTextFieldtelefono().setText("");
        register_View.getjTextFieldCorreo().setText("");
    }
}
