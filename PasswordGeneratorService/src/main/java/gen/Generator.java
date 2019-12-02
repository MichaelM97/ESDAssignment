package gen;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import helper.Password;

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

    /**
     * Web service operation
     *
     * @param name (String) - Users Name
     * @return String - Password
     */
    @WebMethod(operationName = "generate_username")
    public String generate_username(@WebParam(name = "name") String name) {
        String toRet = "";
        String[] temp_name = name.split(" ");
        toRet += temp_name[0].charAt(0);
        toRet += "-";
        if (temp_name.length > 1) {
            toRet += temp_name[temp_name.length - 1];
        } else {
            toRet += temp_name[0];
        }
        return toRet;
    }
}
