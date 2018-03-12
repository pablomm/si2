package ssii2;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.annotation.Resource;
import javax.jms.QueueBrowser;
import java.util.Enumeration;
import javax.naming.InitialContext;

public class VisaQueueMessageProducer {

    @Resource(mappedName = "jms/VisaConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(mappedName = "jms/VisaPagosQueue")
    private static Queue queue;

    // Método de prueba
    public static void browseMessages(Session session)
    {
      try
      {
        Enumeration messageEnumeration;
        TextMessage textMessage;
        QueueBrowser browser = session.createBrowser(queue);
        messageEnumeration = browser.getEnumeration();
        if (messageEnumeration != null)
        {
          if (!messageEnumeration.hasMoreElements())
          {
            System.out.println("Cola de mensajes vacía!");
          }
          else
          {
            System.out.println("Mensajes en cola:");
            while (messageEnumeration.hasMoreElements())
            {
              textMessage =
                (TextMessage) messageEnumeration.nextElement();
              System.out.println(textMessage.getText());
            }
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }

    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;
        TextMessage message = null;
        // Descomentar para acceso jndi
        // ConnectionFactory connectionFactory = null;
        // Queue queue = null;

        if (args.length != 1) {
          System.err.println("Uso: VisaQueueMessageProducer [-browse | <msg>]");
          return;
        }

        try {

          // Inicializar connectionFactory
          // y queue mediante JNDI
          // Version comentada, el acceso se realiza a traves de ConnectionFactory
          // y Queue de la clase anotadas como @resource

          // InitialContext jndi = new InitialContext();
          // connectionFactory = (ConnectionFactory)jndi.lookup("jms/VisaConnectionFactory");
          // queue = (Queue)jndi.lookup("jms/VisaPagosQueue");

          connection = connectionFactory.createConnection();
          session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
          if (args[0].equals("-browse")) {
            browseMessages(session);
          } else {
            // Creamos el message producer
            messageProducer = session.createProducer(queue);

            // Creamos y enviamos el mensaje recibido en args[0]
            message = session.createTextMessage();
            message.setText(args[0]);
            messageProducer.send(message);
          }
        } catch (Exception e) {
            System.out.println("Excepcion : " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {}
            } //if

            if(messageProducer != null) {
              try {
                  messageProducer.close();
              } catch (JMSException e) {}
            } //if

            if(session != null) {
              try {
                  session.close();
              } catch (JMSException e) {}
            } //if
            System.exit(0);
        } // finally
    } // main
} // class
