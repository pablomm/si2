/**
 * Pr&aacute;ctricas de Sistemas Inform&aacute;ticos II
 *
 * Esta servlet se encarga de eliminar los pagos para un determinado comercio.
 * Es necesario que en la llamada se incluya un valor correcto del par&aacute;metros:
 * <dl>
 *    <dt>Identificador del comercio</dt>
 *    <dd>Este identificador es &uacute;nico para cada comercio. Se provee al comercio
 * tras la firma del contrato de utilizaci&oacute;n del sistema de pago. </dd>
 * </dl>
 */

package ssii2.controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ssii2.visa.PagoBean;

import javax.ejb.EJB;
import ssii2.visa.VisaDAOLocal;

//import javax.xml.ws.BindingProvider;
/**
 *
 * @author phaya
 */
public class DelPagos extends ServletRaiz {

    /**
     * Par&aacute;metro que indica el identificador de comercio
     */
    public final static String PARAM_ID_COMERCIO = "idComercio";

    /**
     * Par&aacute;metro que indica la ruta de retorno
     */
    public final static String PARAM_RUTA_RETORNO = "ruta";

    /**
     * Atribute que hace referencia a la lista de pagos
     */
    public final static String ATTR_BORRADOS = "borrados";

    /**
    * Procesa una petici&oacute;n HTTP tanto <code>GET</code> como <code>POST</code>.
    * @param request objeto de petici&oacute;n
    * @param response objeto de respuesta
    */
     @EJB(name="VisaDAOBean", beanInterface=VisaDAOLocal.class)
    private VisaDAOLocal dao;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

      /*
      VisaDAOBean dao=null;
      try{
          // Realiza una instanciacion del servicio VisaDAOBean
          VisaDAOBeanService service = new VisaDAOBeanService();
          // Obtenemos el dao a partir de la llamada al servicio
          dao = service.getVisaDAOBeanPort();

          // Obtenemos la direccion del xml
          String direccion =getServletContext().getInitParameter("service-url");

          BindingProvider bp = (BindingProvider) dao;

          bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, direccion);
      } catch (Exception e){
          enviaError(new Exception("Error. Server unreacheable"), request, response);
          return;
      }
      /*

		/* Se recoge de la petici&oacute;n el par&aacute;metro idComercio*/
		String idComercio = request.getParameter(PARAM_ID_COMERCIO);

		/* Petici&oacute;n de los pagos para el comercio */
		int ret = dao.delPagos(idComercio);

		if (ret != 0) {
			request.setAttribute(ATTR_BORRADOS, ret);
			reenvia("/borradook.jsp", request, response);
		}
		else {
			reenvia("/borradoerror.jsp", request, response);
		}
        return;
    }

   /**
    * Procesa una petici&oacute;n HTTP <code>GET</code>.
    * @param request objeto de petici&oacute;n
    * @param response objeto de respuesta
    */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
    * Procesa una petici&oacute;n HTTP <code>POST</code>.
    * @param request objeto de petici&oacute;n
    * @param response objeto de respuesta
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
    * Devuelve una descripici&oacute;n abreviada del servlet
    */
    @Override
    public String getServletInfo() {
        return "Servlet Get Pagos";
    }

}
