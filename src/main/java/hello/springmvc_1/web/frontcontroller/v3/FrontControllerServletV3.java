package hello.springmvc_1.web.frontcontroller.v3;

import hello.springmvc_1.web.frontcontroller.ModelView;
import hello.springmvc_1.web.frontcontroller.MyView;
import hello.springmvc_1.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.springmvc_1.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.springmvc_1.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> contllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        contllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        contllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        contllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV3.service");

        String requestURI = request.getRequestURI();

        ControllerV3 controller = contllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        ModelView modelView = controller.process(paramMap);

        MyView myView = viewResolver(modelView.getViewName());

        myView.render(modelView.getModel(), request, response);
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }

}
