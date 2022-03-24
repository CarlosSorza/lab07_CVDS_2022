
package edu.eci.cvds.sampleprj.jdbc.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class JDBCExample {
    
    public static void main(String args[]){
        try {
            String url="jdbc:mysql://desarrollo.is.escuelaing.edu.co:3306/bdprueba";
            String driver="com.mysql.jdbc.Driver";
            String user="bdprueba";
            String pwd="prueba2019";
                        
            Class.forName(driver);
            Connection con=DriverManager.getConnection(url,user,pwd);
            con.setAutoCommit(false);
                 
            
            System.out.println("Valor total pedido 1:"+valorTotalPedido(con, 2168514));
            
            List<String> prodsPedido=nombresProductosPedido(con, 2168514);
            
            
            System.out.println("Productos del pedido 1:");
            System.out.println("-----------------------");
            for (String nomprod:prodsPedido){
                System.out.println(nomprod);
            }
            System.out.println("-----------------------");
            
            
            int suCodigoECI=2168;
            registrarNuevoProducto(con, suCodigoECI, "David leon", 20000);            
            con.commit();
            
            int suCodigoECI2=216;
            registrarNuevoProducto(con, suCodigoECI2, "Carlos Sorza", 99999999);            
            con.commit();
                        
            
            con.close();
                                   
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
     * Agregar un nuevo producto con los parámetros dados
     * @param con la conexión JDBC
     * @param codigo
     * @param nombre
     * @param precio
     * @throws SQLException 
     */
    public static void registrarNuevoProducto(Connection con, int codigo, String nombre,int precio) throws SQLException{
        //Crear preparedStatement
        PreparedStatement nuevoProducto = null;
        String insert = "INSERT INTO ORD_PRODUCTOS VALUES (?,?,?);";
        nuevoProducto = con.prepareStatement(insert);
        nuevoProducto.setInt(1, codigo);
        nuevoProducto.setString(2, nombre);
        nuevoProducto.setInt(3, precio);
        nuevoProducto.executeUpdate();

        //Asignar parámetros
        //usar 'execute'

        
        con.commit();
        
    }
    
    /**
     * Consultar los nombres de los productos asociados a un pedido
     * @param con la conexión JDBC
     * @param codigoPedido el código del pedido
     * @return 
     * @throws SQLException 
     */
    public static List<String> nombresProductosPedido(Connection con, int codigoPedido) throws SQLException{
        List<String> np=new LinkedList<>();
        
        //Crear prepared statement
        //asignar parámetros
        //usar executeQuery
        //Sacar resultados del ResultSet
        //Llenar la lista y retornarla
        PreparedStatement nombreProducto = null;
        String select = "SELECT nombre , pedido_fk " +
                        "FROM ORD_PRODUCTOS po ,ORD_DETALLE_PEDIDO pe " +
                        "WHERE po.codigo = pe.producto_fk " +
                        "ORDER BY pedido_fk;";
        nombreProducto = con.prepareStatement(select);
        ResultSet names = nombreProducto.executeQuery();
        while(names.next()){
            String nombre = names.getString("nombre");
            String pedido = names.getString("pedido_fk");
            np.add(nombre);
            np.add(pedido);
        }
        return np;
    }

    
    /**
     * Calcular el costo total de un pedido
     * @param con
     * @param codigoPedido código del pedido cuyo total se calculará
     * @return el costo total del pedido (suma de: cantidades*precios)
     * @throws SQLException 
     */
    public static int valorTotalPedido(Connection con, int codigoPedido) throws SQLException{
        
        //Crear prepared statement
        //asignar parámetros
        //usar executeQuery
        //Sacar resultado del ResultSet
        PreparedStatement costoPedido = null;
        String select = "SELECT sum(cantidad) as precio " +
                        "FROM ORD_DETALLE_PEDIDO " +
                        "WHERE pedido_fk ="+codigoPedido+" "+
                        "GROUP BY (pedido_fk);";
        costoPedido = con.prepareStatement(select);
        ResultSet costo = costoPedido.executeQuery();
        int precio = 0;
        while(costo.next()){
            precio = costo.getInt("precio");
        }
        return precio;

    }
    

    
    
    
}