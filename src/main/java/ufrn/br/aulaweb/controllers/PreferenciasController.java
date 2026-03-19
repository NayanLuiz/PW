package ufrn.br.aulaweb.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PreferenciasController {

    @RequestMapping("/preferencias/tema")
    public void alternarTema(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        String temaAtual = "light";

        if(cookies != null){
            for(Cookie c : cookies) {
                if("app_tema".equals(c.getName())) {
                    temaAtual = c.getValue();
                }
            }
        }


        String novoTema = temaAtual.equals("light") ? "dark" : "light";

        Cookie cookieTema = new Cookie("app_tema", novoTema);

            cookieTema.setMaxAge(60 * 60 * 24 * 7);

            cookieTema.setHttpOnly(true);
            cookieTema.setPath("/");

            response.addCookie(cookieTema);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String corFundo = novoTema.equals("dark") ? "#1e1e1e" : "#ffffff";
        String corTexto = novoTema.equals("dark") ? "#ffffff" : "#000000";

        response.getWriter().println("<html style='background-color: " + corFundo + "; color: " + corTexto + ";'>");

        response.getWriter().println("<body>");

        response.getWriter().println("<h1>Sistema de Gestão de Tarefas</h1>");

        response.getWriter().println("<p>O tema foi gravado com sucesso no seu navegador.</p>");

        response.getWriter().println("<p>Tema atual: <strong>" + novoTema.toUpperCase() + "</strong></p>");

        response.getWriter().println("<a href='/preferencias/tema'><button>Alternar Tema</button></a>");

        response.getWriter().println("</body></html>");

        response.getWriter().flush();
    }
}
