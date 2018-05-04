/**
 * Pr&aacute;ctricas de Sistemas Inform&aacute;ticos II
 *
 * Esta servlet se encarga de visualizar los pagos para un determinado comercio.
 * Es necesario que en la llamada se incluya un valor correcto del par&aacute;metros:
 * <dl>
 *    <dt>Identificador del comercio</dt>
 *    <dd>Este identificador es &uacute;nico para cada comercio. Se provee al comercio
 * tras la firma del contrato de utilizaci&oacute;n del sistema de pago. </dd>
 * </dl>
 */

package ssii2.controlador;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ssii2.visa.PagoBean;

import ssii2.visa.VisaDAOWSService; // Stub generado automáticamente
import ssii2.visa.VisaDAOWS; // Stub generado automáticamente

import javax.xml.ws.BindingProvider;
/**
 *
 * @author phaya
 */
public class GetPagos extends ServletRaiz {

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
    public final static String ATTR_PAGOS = "pagos";

    /**
     * Atribute que hace referencia al importe maximo de la busqueda
     */
    public final static String PARAM_IMPORTE_MAXIMO = "importeMax";

    /**
     * Atribute que hace referencia al importe minimo de la busqueda
     */
    public final static String PARAM_IMPORTE_MINIMO = "importeMin";

    /**
    * Procesa una petici&oacute;n HTTP tanto <code>GET</code> como <code>POST</code>.
    * @param request objeto de petici&oacute;n
    * @param response objeto de respuesta
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

      VisaDAOWS dao=null;
      try{
          // Realiza una instanciacion del servicio VisaDaoWS
          VisaDAOWSService service = new VisaDAOWSService();
          // Obtenemos el dao a partir de la llamada al servicio
          dao = service. getVisaDAOWSPort ();

          // Obtenemos la direccion del xml
          String direccion =getServletContext().getInitParameter("service-url");

          BindingProvider bp = (BindingProvider) dao;
          bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, direccion);
      } catch (Exception e){
          enviaError(new Exception("Error. Server unreacheable"), request, response);
          return;
      }

		/* Se recoge de la petici&oacute;n el par&aacute;metro idComercio*/
		String idComercio = request.getParameter(PARAM_ID_COMERCIO);

    /* Obtenemos importe maximo y minimo y lo parseamos a double para comprobar que es correcto*/
    Double importeMaximo=-1.0;
    Double importeMinimo=-1.0;
    try {
        importeMaximo = Double.parseDouble(request.getParameter(PARAM_IMPORTE_MAXIMO));
        importeMinimo = Double.parseDouble(request.getParameter(PARAM_IMPORTE_MINIMO));
    } catch (NumberFormatException e) {
        importeMaximo = -1.0;
        importeMinimo = -1.0;
    } catch (NullPointerException e) {
        importeMaximo = -1.0;
        importeMinimo = -1.0;
    }

		/* Petici&oacute;n de los pagos para el comercio */
    /* Conversion para que funcione como anteriormente */
    List<PagoBean> responsePagos = dao.getPagos(idComercio, importeMinimo.toString(), importeMaximo.toString());
		PagoBean[] pagos = responsePagos.toArray(new PagoBean[responsePagos.size()]);

        request.setAttribute(ATTR_PAGOS, pagos);
        reenvia("/listapagos.jsp", request, response);
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
