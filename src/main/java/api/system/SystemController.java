package api.system;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;


@Controller
public class SystemController {
    /**
     * 页面跳转公用方法
     * @param request
     * @return
     */
    @RequestMapping(value = "/toPage")
    public String toPage(HttpServletRequest request, Model model){
        model.addAttribute("stcd",request.getParameter("stcd"));
        model.addAttribute("stnm",request.getParameter("stnm"));
        model.addAttribute("layerId",request.getParameter("layerId"));
        String url = request.getParameter("url");

        return url;
    }









}
