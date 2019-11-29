package gen;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import helper.Password;

/**
 *
 * @author dominiclewis
 */
@WebService(serviceName = "Generator")
@Stateless()
public class Generator {

    /**
     * Web service operation
     *
     * @param length (int) - Length of Password to be generated
     * @return String - Password
     */
    @WebMethod(operationName = "generate_password")
    public String generate_password(@WebParam(name = "i") int length) {
        return new Password(length).getPassword();
    }
}
