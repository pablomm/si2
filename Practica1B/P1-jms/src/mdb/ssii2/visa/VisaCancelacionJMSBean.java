/**
 * Pr&aacute;ctricas de Sistemas Inform&aacute;ticos II
 * VisaCancelacionJMSBean.java
 */

package ssii2.visa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.ActivationConfigProperty;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * @author jaime
 */
@MessageDriven(mappedName = "jms/VisaPagosQueue")
public class VisaCancelacionJMSBean extends DBTester implements MessageListener {
  static final Logger logger = Logger.getLogger("VisaCancelacionJMSBean");
  @Resource
  private MessageDrivenContext mdc;

  private static final String UPDATE_CANCELA_QRY = "UPDATE pago SET codRespuesta=999 WHERE idAutorizacion=?";

  private static final String RECTIFICA_PAGO_QRY = "UPDATE tarjeta AS t1 " +
                                                   "SET saldo = saldo + importe " +
                                                   "FROM pago WHERE pago.idAutorizacion=? " +
                                                   "AND pago.numeroTarjeta=t1.numeroTarjeta";

  public VisaCancelacionJMSBean() {
  }

  public void onMessage(Message inMessage) {
      TextMessage msg = null;
      Connection con = null;
      PreparedStatement pstmt = null;
      int idAutorizacion;

      try {
          if (inMessage instanceof TextMessage) {
              msg = (TextMessage) inMessage;
              logger.info("MESSAGE BEAN: Message received: " + msg.getText());
              // Obtenemos el id del pago definido por el servidor
              idAutorizacion =  Integer.parseInt(msg.getText());

              // Conexion con la base de datos
              con = getConnection();

              // Actualizamos el pago con el codigo de respuesta a 999
              pstmt = con.prepareStatement(UPDATE_CANCELA_QRY);
              pstmt.setInt(1,idAutorizacion);
              pstmt.execute();
              logger.info("Query1: " + UPDATE_CANCELA_QRY);

              // Restauramos el pago de la tarjeta
              pstmt = con.prepareStatement(RECTIFICA_PAGO_QRY);
              pstmt.setInt(1,idAutorizacion);
              pstmt.execute();
              logger.info("Query2: " + RECTIFICA_PAGO_QRY);

          } else {
              logger.warning(
                      "Message of wrong type: "
                      + inMessage.getClass().getName());
          }
      } catch (JMSException e) {
          e.printStackTrace();
          mdc.setRollbackOnly();
      } catch (Throwable te) {
          te.printStackTrace();
      }
  }

}
